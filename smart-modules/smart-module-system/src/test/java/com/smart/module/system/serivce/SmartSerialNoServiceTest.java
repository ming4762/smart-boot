package com.smart.module.system.serivce;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.smart.module.api.system.dto.SerialCodeCreateDTO;
import com.smart.module.api.system.parameter.SerialCodeCreateParameter;
import com.smart.module.system.mapper.SmartSerialNoMapper;
import com.smart.module.system.model.SmartSerialNoPO;
import com.smart.module.system.service.impl.SmartSerialNoServiceImpl;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author shizhongming
 * 2025/7/15 20:17
 * @since 5.0.0
 */
@ExtendWith(MockitoExtension.class)
class SmartSerialNoServiceTest {

    @Mock
    private SmartSerialNoMapper smartSerialNoMapper;

    @InjectMocks
    private SmartSerialNoServiceImpl smartSerialNoService;

    @BeforeEach
    void setUp() {
        // 构造一个最简 GlobalConfig，注册实体
        Configuration configuration = new Configuration();
        MapperBuilderAssistant assistant =
                new MapperBuilderAssistant(configuration, "");
        // （可选）设置 namespace，MP 内部会用它来生成缓存 key
        assistant.setCurrentNamespace("com.smart.module.system.model.SmartSerialNoPO");
        TableInfoHelper.initTableInfo(assistant, SmartSerialNoPO.class);
        // 把 mock 的 mapper 注入到 ServiceImpl 父类的 baseMapper 字段
        ReflectionTestUtils.setField(smartSerialNoService, "baseMapper", smartSerialNoMapper);
    }

    @Test
    void testCreateSerial() {
        SmartSerialNoPO mockModel = new SmartSerialNoPO();
        mockModel.setCode("CESHI");
        mockModel.setPrefix("CES");
        mockModel.setDateFormat("yyyyMMdd");
        mockModel.setSerialLength(6);
        mockModel.setMinValue(1L);
        mockModel.setMaxValue(-1L);
        mockModel.setStepValue(1);
        mockModel.setFill("0");
        mockModel.setCurrentValue(2L);
        mockModel.setLastCurrentDate(LocalDate.now());
        mockModel.setSerialFormat("{PREFIX}{DATE}{NUMBER}");
        when(smartSerialNoMapper.selectList(any(Wrapper.class))).thenReturn(
              List.of(mockModel)
        );
        List<SerialCodeCreateDTO> result = this.smartSerialNoService.createSerial(
                List.of(
                        new SerialCodeCreateParameter("CESHI", 1)
                        )
        );
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getSerialList()).isNotEmpty();
        assertThat(result.getFirst().getSerialList().getFirst()).endsWith("000002");
    }
}
