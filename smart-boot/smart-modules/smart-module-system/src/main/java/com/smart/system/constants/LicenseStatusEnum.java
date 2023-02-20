package com.smart.system.constants;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

/**
 * License状态枚举类
 * @author zhongming4762
 * 2022/12/17 20:26
 */
public enum LicenseStatusEnum implements IEnum<String> {

    /**
     * 10：创建
     * 20：已生成license
     * 30: 更新状态，需要重新生成
     */
    CREATE("10"),
    GENERATOR("20"),
    UPDATE("30")

    ;
    @Getter
    public final String value;

    LicenseStatusEnum(String value) {
        this.value = value;
    }
}
