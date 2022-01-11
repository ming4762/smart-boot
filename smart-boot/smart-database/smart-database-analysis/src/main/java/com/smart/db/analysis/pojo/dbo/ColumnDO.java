package com.smart.db.analysis.pojo.dbo;

import com.smart.db.analysis.annotation.DatabaseField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * 字段DO
 * @author ShiZhongMing
 * 2020/7/25 17:17
 * @since 0.0.6
 */
@Getter
@Setter
@ToString
public class ColumnDO extends AbstractTableBaseDO {
    private static final long serialVersionUID = -617702057646747452L;

    @DatabaseField("COLUMN_NAME")
    private String columnName;

    @DatabaseField(value = "DATA_TYPE")
    private Integer dataType;

    @DatabaseField("TYPE_NAME")
    private String typeName;

    @DatabaseField(value = "COLUMN_SIZE")
    private Integer columnSize;

    @DatabaseField(value = "BUFFER_LENGTH")
    private Integer bufferLength;

    @DatabaseField(value = "DECIMAL_DIGITS")
    private Integer decimalDigits;

    @DatabaseField(value = "NUM_PREC_RADIX")
    private Integer numPrecRadix;

    @DatabaseField("NULLABLE")
    private Integer nullable;

    @DatabaseField("REMARKS")
    private String remarks;

    @DatabaseField("COLUMN_DEF")
    private String columnDef;

    @DatabaseField("SQL_DATA_TYPE")
    private Integer sqlDataType;

    @DatabaseField("SQL_DATETIME_SUB")
    private Integer sqlDatetimeSub;

    @DatabaseField("CHAR_OCTET_LENGTH")
    private Integer charOctetLength;

    @DatabaseField("ORDINAL_POSITION")
    private Integer ordinalPosition;

    @DatabaseField("IS_NULLABLE")
    private String isNullable;

    @DatabaseField("SS_IS_SPARSE")
    private Integer ssIsSparse;

    @DatabaseField("SS_IS_COLUMN_SET")
    private Integer ssIsColumnSet;

    @DatabaseField("SS_IS_COMPUTED")
    private Integer ssIsComputed;

    @DatabaseField("IS_AUTOINCREMENT")
    private String autoincrement;

    @DatabaseField("SS_UDT_CATALOG_NAME")
    private String ssUdtCatalogName;

    @DatabaseField("SS_UDT_SCHEMA_NAME")
    private String ssUdtSchemaName;

    @DatabaseField("SS_UDT_ASSEMBLY_TYPE_NAME")
    private String ssUdtAssemblyTypeName;

    @DatabaseField("SS_XML_SCHEMACOLLECTION_CATALOG_NAME")
    private String ssXmlSchemacollectionCatalogName;

    @DatabaseField("SS_XML_SCHEMACOLLECTION_SCHEMA_NAME")
    private String ssXmlSchemacollectionSchemaName;

    @DatabaseField("SS_XML_SCHEMACOLLECTION_NAME")
    private String ssXmlSchemacollectionName;

    @DatabaseField("SS_DATA_TYPE")
    private Boolean ssDataType;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ColumnDO columnDO = (ColumnDO) o;
        return Objects.equals(columnName, columnDO.columnName)
                && Objects.equals(dataType, columnDO.dataType)
                && Objects.equals(typeName, columnDO.typeName)
                && Objects.equals(columnSize, columnDO.columnSize)
                && Objects.equals(bufferLength, columnDO.bufferLength)
                && Objects.equals(decimalDigits, columnDO.decimalDigits)
                && Objects.equals(numPrecRadix, columnDO.numPrecRadix)
                && Objects.equals(nullable, columnDO.nullable)
                && Objects.equals(remarks, columnDO.remarks)
                && Objects.equals(columnDef, columnDO.columnDef)
                && Objects.equals(sqlDataType, columnDO.sqlDataType)
                && Objects.equals(sqlDatetimeSub, columnDO.sqlDatetimeSub)
                && Objects.equals(charOctetLength, columnDO.charOctetLength)
                && Objects.equals(ordinalPosition, columnDO.ordinalPosition)
                && Objects.equals(isNullable, columnDO.isNullable)
                && Objects.equals(ssIsSparse, columnDO.ssIsSparse)
                && Objects.equals(ssIsColumnSet, columnDO.ssIsColumnSet)
                && Objects.equals(ssIsComputed, columnDO.ssIsComputed)
                && Objects.equals(autoincrement, columnDO.autoincrement)
                && Objects.equals(ssUdtCatalogName, columnDO.ssUdtCatalogName)
                && Objects.equals(ssUdtSchemaName, columnDO.ssUdtSchemaName)
                && Objects.equals(ssUdtAssemblyTypeName, columnDO.ssUdtAssemblyTypeName)
                && Objects.equals(ssXmlSchemacollectionCatalogName, columnDO.ssXmlSchemacollectionCatalogName)
                && Objects.equals(ssXmlSchemacollectionSchemaName, columnDO.ssXmlSchemacollectionSchemaName)
                && Objects.equals(ssXmlSchemacollectionName, columnDO.ssXmlSchemacollectionName)
                && Objects.equals(ssDataType, columnDO.ssDataType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnName, dataType, typeName, columnSize, bufferLength, decimalDigits, numPrecRadix, nullable, remarks, columnDef, sqlDataType, sqlDatetimeSub, charOctetLength, ordinalPosition, isNullable, ssIsSparse, ssIsColumnSet, ssIsComputed, autoincrement, ssUdtCatalogName, ssUdtSchemaName, ssUdtAssemblyTypeName, ssXmlSchemacollectionCatalogName, ssXmlSchemacollectionSchemaName, ssXmlSchemacollectionName, ssDataType);
    }
}
