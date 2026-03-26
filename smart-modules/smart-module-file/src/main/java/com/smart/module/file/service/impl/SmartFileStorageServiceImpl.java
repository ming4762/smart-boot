package com.smart.module.file.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.smart.framework.commons.core.utils.Base64Utils;
import com.smart.framework.commons.core.utils.auth.RsaUtils;
import com.smart.framework.crud.query.PageSortQuery;
import com.smart.framework.crud.service.BaseServiceImpl;
import com.smart.framework.crud.utils.CrudUtils;
import com.smart.framework.file.core.exception.SmartFileException;
import com.smart.framework.file.core.parameter.FileStorageInitProperties;
import com.smart.framework.file.core.service.FileStorageService;
import com.smart.module.api.file.constants.FileStorageTypeEnum;
import com.smart.module.file.mapper.SmartFileStorageMapper;
import com.smart.module.file.model.SmartFileStoragePO;
import com.smart.module.file.pojo.FileStorageServiceCacheData;
import com.smart.module.file.service.SmartFileStorageService;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.security.KeyPair;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
* smart_file_storage - 文件存储器配置 Service实现类
* @author SmartCodeGenerator
* 2023-2-14
*/
@Service
public class SmartFileStorageServiceImpl extends BaseServiceImpl<SmartFileStorageMapper, SmartFileStoragePO> implements SmartFileStorageService {

    private static final ConcurrentHashMap<FileStorageLockObject, ReentrantLock> LOCKS = new ConcurrentHashMap<>();

    private static final String DEFAULT_FILE_STORAGE_CODE = "default_%$_123";

    private static final Map<Long, FileStorageServiceCacheData> FILE_STORAGE_SERVICE_ID_MAP = new ConcurrentHashMap<>();
    private static final Map<String, FileStorageServiceCacheData> FILE_STORAGE_SERVICE_CODE_MAP = new ConcurrentHashMap<>();

    private final Map<FileStorageTypeEnum, FileStorageService> actualFileServiceMap;

    public SmartFileStorageServiceImpl(List<FileStorageService> fileStorageServiceList) {
        actualFileServiceMap = fileStorageServiceList.stream()
                .collect(Collectors.toMap(item -> item.getRegisterName().getStorageType(), item -> item));
    }

    /**
     * 查询函数
     *
     * @param queryWrapper 查询参数
     * @param parameter    原始参数
     * @param paging       是否分页
     * @return 查询结果
     */
    @Override
    public List<SmartFileStoragePO> list(@NonNull QueryWrapper<SmartFileStoragePO> queryWrapper, @NonNull PageSortQuery parameter, boolean paging) {
        queryWrapper.lambda()
                .select(SmartFileStoragePO.class, tableInfo ->
                        !tableInfo.getProperty().equals(CrudUtils.getJavaProperty(SmartFileStoragePO::getPrivateKey))
                       && !tableInfo.getProperty().equals(CrudUtils.getJavaProperty(SmartFileStoragePO::getPublicKey))
                );
        return super.list(queryWrapper, parameter, paging);
    }

    /**
     * 设置默认
     *
     * @param id ID
     * @return 修改结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setDefault(Serializable id) {
        this.update(
                new UpdateWrapper<SmartFileStoragePO>().lambda()
                        .set(SmartFileStoragePO::getDefaultStorage, false)
                        .notIn(SmartFileStoragePO::getId, List.of(id))
        );
        this.update(
                new UpdateWrapper<SmartFileStoragePO>().lambda()
                        .set(SmartFileStoragePO::getDefaultStorage, true)
                        .eq(SmartFileStoragePO::getId, id)
        );
        // 清除默认的存储器
        FILE_STORAGE_SERVICE_CODE_MAP.remove(DEFAULT_FILE_STORAGE_CODE);
        return true;
    }

    /**
     * 通过code查询
     *
     * @param code code
     * @return 文件存储器信息
     */
    @Override
    public SmartFileStoragePO getByCode(String code) {
        return this.getOne(
                new QueryWrapper<SmartFileStoragePO>().lambda()
                        .eq(SmartFileStoragePO::getStorageCode, code)
        );
    }

    /**
     * 获取默认的存储器
     *
     * @return 默认存储器
     */
    @Override
    public SmartFileStoragePO getDefault() {
        return this.getOne(
                new QueryWrapper<SmartFileStoragePO>().lambda()
                        .eq(SmartFileStoragePO::getDefaultStorage, true)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateBatch(Collection<SmartFileStoragePO> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return false;
        }
        boolean result = super.saveOrUpdateBatch(entityList);
        // 重置存储器
        List<Long> fileStorageIdList = entityList.stream()
                .map(SmartFileStoragePO::getId)
                .toList();
        this.destroyFileStorageById(fileStorageIdList);
        return result;
    }

    /**
     * 设置为加密存储器
     *
     * @param fileStorageIdList 文件存储ID列表
     * @return 是否设置成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setEncrypt(List<Long> fileStorageIdList) {
        if (CollectionUtils.isEmpty(fileStorageIdList)) {
            return false;
        }
        // 获取加密的存储器
        List<SmartFileStoragePO> fileStorageList = this.lambdaQuery()
                .select(SmartFileStoragePO::getId, SmartFileStoragePO::getEncryptedYn, SmartFileStoragePO::getStorageType, SmartFileStoragePO::getStorageCode)
                .in(SmartFileStoragePO::getId, fileStorageIdList)
                .list().stream()
                .filter(item -> !Boolean.TRUE.equals(item.getEncryptedYn()))
                .toList();
        List<Long> noEncryptedIdList = fileStorageList.stream()
                .map(SmartFileStoragePO::getId)
                .toList();
        if (CollectionUtils.isEmpty(noEncryptedIdList)) {
            return true;
        }
        noEncryptedIdList.forEach(item -> {
            KeyPair keyPair = RsaUtils.generateKeyPair();
            this.update(
                    Wrappers.lambdaUpdate(this.getEntityClass())
                            .set(SmartFileStoragePO::getEncryptedYn, Boolean.TRUE)
                            .set(SmartFileStoragePO::getPublicKey, Base64Utils.encode(keyPair.getPublic().getEncoded()))
                            .set(SmartFileStoragePO::getPrivateKey, Base64Utils.encode(keyPair.getPrivate().getEncoded()))
                            .eq(SmartFileStoragePO::getId, item)
            );
        });
        // 重置存储器
        this.destroyFileStorage(fileStorageList);
        return true;
    }

    /**
     * 获取文件存储服务
     * 优先通过ID获取,如果ID不存在,则通过code获取
     * 如果都不存在,则获取默认的文件存储器
     *
     * @param fileStorageId   文件存储ID
     * @param fileStorageCode 文件存储code
     * @return 文件存储服务
     */
    @Override
    public FileStorageServiceCacheData getFileStorageService(Long fileStorageId, String fileStorageCode) {
        // 根据ID或code获取
        if (fileStorageId != null && FILE_STORAGE_SERVICE_ID_MAP.containsKey(fileStorageId)) {
            return FILE_STORAGE_SERVICE_ID_MAP.get(fileStorageId);
        }
        if (StringUtils.hasText(fileStorageCode) && FILE_STORAGE_SERVICE_CODE_MAP.containsKey(fileStorageCode)) {
            return FILE_STORAGE_SERVICE_CODE_MAP.get(fileStorageCode);
        }
        // 默认值变更之后需要清除
        if (FILE_STORAGE_SERVICE_CODE_MAP.containsKey(DEFAULT_FILE_STORAGE_CODE)) {
            return FILE_STORAGE_SERVICE_CODE_MAP.get(DEFAULT_FILE_STORAGE_CODE);
        }
        // 加锁,防止重复初始化
        ReentrantLock lock = LOCKS.computeIfAbsent(new FileStorageLockObject(fileStorageId, fileStorageCode), key -> new ReentrantLock());
        lock.lock();

        try {
            // 获取文件存储器
            SmartFileStoragePO smartFileStorage;
            if (fileStorageId != null) {
                smartFileStorage = this.getById(fileStorageId);
            } else if (StringUtils.hasText(fileStorageCode)) {
                smartFileStorage = this.getByCode(fileStorageCode);
            } else {
                smartFileStorage = this.getDefault();
            }
            if (smartFileStorage == null) {
                throw new SmartFileException(String.format("获取文件存储器失败，请检查是否存在对应的文件存储器，存储器编码：%s", fileStorageCode));
            }
            FileStorageService fileStorageService = this.actualFileServiceMap.get(smartFileStorage.getStorageType());
            if (fileStorageService == null) {
                throw new SmartFileException(String.format("获取文件存储器失败，未找到对应的文件执行器，执行器名称：%s", smartFileStorage.getStorageType().name()));
            }
            fileStorageService.init(
                    FileStorageInitProperties.builder()
                            .properties(smartFileStorage.getStorageConfig())
                            .encryptedYn(Boolean.TRUE.equals(smartFileStorage.getEncryptedYn()))
                            .privateKey(smartFileStorage.getPrivateKey())
                            .publicKey(smartFileStorage.getPublicKey())
                            .fileStorageId(smartFileStorage.getId())
                            .build()
            );
            FileStorageServiceCacheData cacheData = new FileStorageServiceCacheData(smartFileStorage.getId(), smartFileStorage.getStorageCode(), fileStorageService);
            FILE_STORAGE_SERVICE_ID_MAP.put(smartFileStorage.getId(), cacheData);
            if (StringUtils.hasText(fileStorageCode)) {
                FILE_STORAGE_SERVICE_CODE_MAP.put(fileStorageCode, cacheData);
            } else {
                FILE_STORAGE_SERVICE_CODE_MAP.put(DEFAULT_FILE_STORAGE_CODE, cacheData);
            }
            return cacheData;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 重置存储器
     * @param fileStorageIdList 需要重置的存储器ID
     */
    private synchronized void destroyFileStorageById(List<Long> fileStorageIdList) {
        Set<Long> needDestroyIdList = fileStorageIdList.stream()
                .filter(FILE_STORAGE_SERVICE_ID_MAP::containsKey)
                .collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(needDestroyIdList)) {
            return;
        }
        this.destroyFileStorage(
                this.lambdaQuery()
                        .select(SmartFileStoragePO::getId, SmartFileStoragePO::getStorageType, SmartFileStoragePO::getStorageCode)
                        .in(SmartFileStoragePO::getId, needDestroyIdList)
                        .list()
        );
    }

    /**
     * 重置存储器
     * @param fileStorageList 需要重置的存储器列表
     */
    private synchronized void destroyFileStorage(List<SmartFileStoragePO> fileStorageList) {
        if (CollectionUtils.isEmpty(fileStorageList)) {
            return;
        }
        Map<FileStorageTypeEnum, List<SmartFileStoragePO>> typeIdMap = fileStorageList.stream()
                .collect(
                        Collectors.groupingBy(
                                SmartFileStoragePO::getStorageType
                        )
                );
        typeIdMap.forEach((type, list) -> {
            FileStorageService fileStorageService = this.actualFileServiceMap.get(type);
            list.forEach(item -> {
                FILE_STORAGE_SERVICE_ID_MAP.remove(item.getId());
                FILE_STORAGE_SERVICE_CODE_MAP.remove(item.getStorageCode());
                fileStorageService.destroy(item.getId());
            });
        });
    }

    private record FileStorageLockObject(Long fileStorageId, String fileStorageCode) {
    }
}