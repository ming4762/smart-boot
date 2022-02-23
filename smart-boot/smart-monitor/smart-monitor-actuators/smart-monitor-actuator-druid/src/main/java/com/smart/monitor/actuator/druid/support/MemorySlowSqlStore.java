package com.smart.monitor.actuator.druid.support;

import com.alibaba.druid.proxy.jdbc.StatementProxy;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.springframework.lang.NonNull;

import java.util.*;

/**
 * 内存存储慢SQL
 * @author ShiZhongMing
 * 2021/4/7 9:03
 * @since 1.0
 */
public class MemorySlowSqlStore extends AbstractSlowSqlStore {

    private final Collection<SlowSqlData> slowSqlDataList = Collections.synchronizedCollection(new CircularFifoQueue<>(10000));

    @Override
    public void save(@NonNull StatementProxy statementProxy, long millis, String parameter) {
        this.slowSqlDataList.add(this.createSlowSqlData(statementProxy, millis, parameter));
    }

    @Override
    @NonNull
    public List<SlowSqlData> list(boolean clear) {
        List<SlowSqlData> result = new ArrayList<>(slowSqlDataList);
        if (clear) {
            slowSqlDataList.clear();
        }
        return result;
    }

    @Override
    @NonNull
    public List<SlowSqlData> listByDatasourceName(@NonNull String datasourceName, boolean clear) {
        List<SlowSqlData> result = new LinkedList<>();
        Iterator<SlowSqlData> iterator = slowSqlDataList.iterator();
        while (iterator.hasNext()) {
            final SlowSqlData slowSqlData = iterator.next();
            if (datasourceName.equals(slowSqlData.getDatasourceName())) {
                result.add(slowSqlData);
                if (clear) {
                    iterator.remove();
                }
            }
        }
        return result;
    }
}
