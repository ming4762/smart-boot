package com.smart.framework.kettle.core.model;

import lombok.Getter;
import lombok.Setter;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.RepositoryElementMetaInterface;
import org.pentaho.di.repository.RepositoryObjectType;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.ZoneId;

/**
 * @author ShiZhongMing
 * 2021/7/23 15:10
 * @since 1.0
 */
@Getter
@Setter
public class RepositoryElementMetaData implements Serializable {
    @Serial
    private static final long serialVersionUID = 4156363454876149890L;

    private String modifiedUser;

    private ZonedDateTime modifiedDate;

    private RepositoryObjectType objectType;
    private String description;
    private boolean deleted;
    private transient ObjectId objectId;
    private String name;

    private RepositoryElementMetaData() {}

    public static RepositoryElementMetaData createByRepositoryElementMeta(RepositoryElementMetaInterface repositoryElementMeta) {
        RepositoryElementMetaData data = new RepositoryElementMetaData();
        data.setModifiedUser(repositoryElementMeta.getModifiedUser());
        data.setModifiedDate(repositoryElementMeta.getModifiedDate().toInstant().atZone(ZoneId.systemDefault()).toZonedDateTime());
        data.setObjectId(repositoryElementMeta.getObjectId());
        data.setObjectType(repositoryElementMeta.getObjectType());
        data.setDescription(repositoryElementMeta.getDescription());
        data.setDeleted(repositoryElementMeta.isDeleted());
        data.setName(repositoryElementMeta.getName());
        return data;
    }
}
