package com.smart.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.smart.commons.core.exception.SystemException;
import com.smart.crud.service.BaseServiceImpl;
import com.smart.module.api.system.dto.SerialCodeCreateDTO;
import com.smart.module.api.system.parameter.SerialCodeCreateParameter;
import com.smart.system.mapper.SmartSerialNoMapper;
import com.smart.system.model.SmartSerialNoPO;
import com.smart.system.service.SmartSerialNoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
* smart_serial_no - 业务编码表 Service实现类
* @author SmartCodeGenerator
* 2023年6月2日 上午9:56:44
*/
@Service
public class SmartSerialNoServiceImpl extends BaseServiceImpl<SmartSerialNoMapper, SmartSerialNoPO> implements SmartSerialNoService {

    private static final String PREFIX_KEY = "{PREFIX}";

    private static final String DATE_KEY = "{DATE}";

    private static final String NUMBER_KEY = "{NUMBER}";

    /**
     * 批量获取业务编码
     *
     * @param parameterList 参数列表
     * @return 业务编码列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SerialCodeCreateDTO> createSerial(List<SerialCodeCreateParameter> parameterList) {
        if (CollectionUtils.isEmpty(parameterList)) {
            return Collections.emptyList();
        }
        // 查询编码配置
        List<SmartSerialNoPO> serialConfigList = this.list(
                new QueryWrapper<SmartSerialNoPO>().lambda()
                        .in(SmartSerialNoPO::getCode, parameterList.stream().map(SerialCodeCreateParameter::getCode).toList())
                        .eq(SmartSerialNoPO::getUseYn, Boolean.TRUE)
                        .last("for update")
        );
        // 验证是否有对应的业务编码
        Map<String, SmartSerialNoPO> configMap = serialConfigList.stream()
                .collect(Collectors.toMap(SmartSerialNoPO::getCode, item -> item));
        Set<String> codes = configMap.keySet();
        String notCode = parameterList.stream()
                .map(SerialCodeCreateParameter::getCode)
                .filter(code -> !codes.contains(code))
                .collect(Collectors.joining(","));
        if (StringUtils.isNotBlank(notCode)) {
            throw new SystemException("以下编码为找对编码规则：" + notCode);
        }
        // 生成编码
        return parameterList.stream()
                .map(item -> this.doCreateSerial(configMap.get(item.getCode()), item))
                .toList();
    }

    /**
     * 创建业务编码
     * @param serialConfig 配置信息
     * @param parameter 参数信息
     * @return 业务编码
     */
    private SerialCodeCreateDTO doCreateSerial(SmartSerialNoPO serialConfig, SerialCodeCreateParameter parameter) {
        SerialCodeCreateDTO dto = new SerialCodeCreateDTO();
        BeanUtils.copyProperties(serialConfig, dto);
        // 格式化日期
        String dateFormat = DateTimeFormatter.ofPattern(serialConfig.getDateFormat()).format(LocalDateTime.now());

        List<Long> numberList = new ArrayList<>(parameter.getNumber());
        Long currentValue = serialConfig.getCurrentValue();
        // 判断是否重置当前值，日期变化重置当前值
        if (LocalDate.now().isAfter(serialConfig.getLastCurrentDate())) {
            currentValue = 1L;
        }
        for (int i = 0; i < parameter.getNumber(); i++) {
            numberList.add(Long.valueOf(currentValue.toString()));
            currentValue = currentValue + serialConfig.getStepValue();
            if (serialConfig.getMaxValue() != -1 && currentValue > serialConfig.getMaxValue()) {
                throw new SystemException(String.format("生成业务编码失败，超出最大值，编码：%s，原当前值：%s，当前值：%s，步长：%s，最大值：%s", serialConfig.getCode(), serialConfig.getCurrentValue(), currentValue, serialConfig.getStepValue(), serialConfig.getMaxValue()));
            }
        }
        // 生成编号
        Integer serialLength = serialConfig.getSerialLength();
        List<String> serialList = numberList.stream()
                .map(item -> {
                    String numberStr = item.toString();
                    if (numberStr.length() > serialLength) {
                        throw new SystemException(String.format("生成业务编号失败，当前值长度超出流水长度，编码：%s，流水长度：%s，当前值：%s", serialConfig.getCode(), serialLength, numberStr.length()));
                    }
                    String serialNumber = StringUtils.leftPad(numberStr, serialLength, serialConfig.getFill());

                    String serial = StringUtils.replace(serialConfig.getSerialFormat(), PREFIX_KEY, serialConfig.getPrefix());
                    serial = StringUtils.replace(serial, DATE_KEY, dateFormat);
                    serial = StringUtils.replace(serial, NUMBER_KEY, serialNumber);
                    return serial;
                }).toList();
        dto.setSerialList(serialList);
        dto.setCurrentValue(currentValue);
        // 更新当前值
        this.update(
                new UpdateWrapper<SmartSerialNoPO>().lambda()
                        .set(SmartSerialNoPO::getCurrentValue, currentValue)
                        .set(SmartSerialNoPO::getLastCurrentDate, LocalDate.now())
                        .eq(SmartSerialNoPO::getId, serialConfig.getId())
        );

        return dto;
    }
}