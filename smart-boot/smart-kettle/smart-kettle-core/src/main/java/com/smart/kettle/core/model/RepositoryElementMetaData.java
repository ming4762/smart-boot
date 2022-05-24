package com.smart.kettle.core.model;

import lombok.Getter;
import lombok.Setter;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.RepositoryElementMetaInterface;
import org.pentaho.di.repository.RepositoryObjectType;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author ShiZhongMing
 * 2021/7/23 15:10
 * @since 1.0
 */
@Getter
@Setter
public class RepositoryElementMetaData {
    private static final long serialVersionUID = 4156363454876149890L;

    private String modifiedUser;

    private LocalDateTime modifiedDate;

    private RepositoryObjectType objectType;
    private String description;
    private boolean deleted;
    private ObjectId objectId;
    private String name;

    private RepositoryElementMetaData() {}

    public static RepositoryElementMetaData createByRepositoryElementMeta(RepositoryElementMetaInterface repositoryElementMeta) {
        RepositoryElementMetaData data = new RepositoryElementMetaData();
        data.setModifiedUser(repositoryElementMeta.getModifiedUser());
        data.setModifiedDate(repositoryElementMeta.getModifiedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        data.setObjectId(repositoryElementMeta.getObjectId());
        data.setObjectType(repositoryElementMeta.getObjectType());
        data.setDescription(repositoryElementMeta.getDescription());
        data.setDeleted(repositoryElementMeta.isDeleted());
        data.setName(repositoryElementMeta.getName());
        return data;
    }
}
