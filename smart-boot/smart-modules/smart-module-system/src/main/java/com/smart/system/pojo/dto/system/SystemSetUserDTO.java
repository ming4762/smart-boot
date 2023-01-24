package com.smart.system.pojo.dto.system;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhongming4762
 * 2023/1/23 20:04
 */
@Getter
@Setter
@ToString
public class SystemSetUserDTO implements Serializable {

    private Long systemId;

    private List<Long> userIdList;
}
