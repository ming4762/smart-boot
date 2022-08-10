package com.smart.kettle.core.log.modifier;

import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.logging.BaseLogTable;
import org.pentaho.di.core.logging.LogStatus;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ShiZhongMing
 * 2021/7/22 19:16
 * @since 1.0
 */
public class LogModifierHandler implements ApplicationContextAware {


    private Map<Class<? extends BaseLogTable>, List<LogRecordModifier>> logRecordModifierMap = new HashMap<>(0);

    public RowMetaAndData modifyLogRecord(Class<? extends BaseLogTable> supportClass, RowMetaAndData rowMetaAndData, LogStatus status, Object subject, Object parent) {
        if (logRecordModifierMap.get(supportClass) != null) {
            for (LogRecordModifier logRecordModifier : logRecordModifierMap.get(supportClass)) {
                rowMetaAndData = logRecordModifier.modifyLogRecord(rowMetaAndData, status, subject, parent);
            }
        }
        return rowMetaAndData;
    }


    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.logRecordModifierMap = Arrays.stream(applicationContext.getBeanNamesForType(LogRecordModifier.class))
                .map(item -> applicationContext.getBean(item, LogRecordModifier.class))
                .sorted(Comparator.comparingInt(Ordered::getOrder))
                .collect(Collectors.groupingBy(LogRecordModifier :: support));
    }
}
