package com.smart.module.file.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.framework.commons.core.utils.SmartIdGenerator;
import com.smart.framework.file.core.service.FileService;
import com.smart.module.file.model.SmartFilePO;
import com.smart.module.file.service.SmartFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 清理过期文件任务
 * @author shizhongming
 * 2024/3/13 14:27
 * @since 3.0.0
 */
@Component
@Slf4j
public class ClearExpireFileTask implements InitializingBean {

    private final FileService fileService;

    private final SmartFileService smartFileService;

    public ClearExpireFileTask(FileService fileService, SmartFileService smartFileService) {
        this.fileService = fileService;
        this.smartFileService = smartFileService;
    }

    @Override
    public void afterPropertiesSet() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = this.createThreadPoolTaskScheduler();
        threadPoolTaskScheduler.initialize();

        threadPoolTaskScheduler.scheduleWithFixedDelay(() -> {
            try {
                SmartIdGenerator.nextId();
                log.info("start clear expire file");
                long startTime = System.nanoTime();
                this.clear();
                log.debug("clear file complete, use[{}]ms", TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }, Duration.ofHours(1));
    }

    private void clear() {
        // 查询过期文件
        List<SmartFilePO> fileList = this.smartFileService.list(
                new QueryWrapper<SmartFilePO>().lambda()
                        .select(SmartFilePO::getFileId)
                        .lt(SmartFilePO::getExpireTime, LocalDateTime.now())
        );
        if (CollectionUtils.isEmpty(fileList)) {
            return;
        }
        fileList.forEach(file -> this.fileService.batchDelete(fileList.stream().map(SmartFilePO::getFileId).toList()));
    }

    private ThreadPoolTaskScheduler createThreadPoolTaskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(1);
        taskScheduler.setRemoveOnCancelPolicy(false);
        taskScheduler.setThreadNamePrefix("file-clear");
        return taskScheduler;
    }
}
