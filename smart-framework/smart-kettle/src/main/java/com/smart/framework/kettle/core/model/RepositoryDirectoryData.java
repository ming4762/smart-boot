package com.smart.framework.kettle.core.model;

import lombok.*;
import org.pentaho.di.repository.ObjectId;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author ShiZhongMing
 * 2021/7/23 13:49
 * @since 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepositoryDirectoryData implements Serializable {

    @Serial
    private static final long serialVersionUID = 7694434350598412064L;
    private transient ObjectId id;

    private transient ObjectId parentId;

    private String name;

    private String path;

    private List<RepositoryElementMetaData> jobTransList;
}
