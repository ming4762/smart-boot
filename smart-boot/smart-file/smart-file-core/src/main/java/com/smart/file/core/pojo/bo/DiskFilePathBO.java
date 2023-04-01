package com.smart.file.core.pojo.bo;

import com.smart.commons.core.utils.Base64Utils;
import com.smart.file.core.parameter.FileStorageSaveParameter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 文件路径业务类
 * @author shizhongming
 * 2020/1/15 8:36 下午
 */
@NoArgsConstructor
@ToString
public class DiskFilePathBO {

    public static final String ID_CUT = "_";

    private static final String POINT_STR = ".";

    public static final String FILE_SEPARATOR = "/";

    private static final String SEPARATOR_REPLACE = "@@";

    /**
     * 时间格式化工具
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd")
            .withZone(ZoneId.systemDefault());

    private String basePath;

    private String folderPath;

    @Getter
    private String filename;

    private String timestamp;

    public DiskFilePathBO(String basePath, FileStorageSaveParameter parameter) {
        this.basePath = basePath;
        this.filename = parameter.getFilename();
        // 如果文件名中存在路径分隔符，则进行替换
        if (StringUtils.contains(this.filename, FILE_SEPARATOR)) {
            this.filename = StringUtils.replace(this.filename, FILE_SEPARATOR, SEPARATOR_REPLACE);
        }
        this.folderPath = parameter.getFolder();
        if (StringUtils.isBlank(this.folderPath)) {
            this.folderPath = FORMATTER.format(Instant.now());
        }
        this.timestamp = String.valueOf(System.currentTimeMillis());
    }

    /**
     * 获取文件夹路径
     * @return 文件夹路径
     */
    public String getFolderPath() {
        return this.basePath + FILE_SEPARATOR + this.folderPath;
    }

    /**
     * 获取文件路径
     * @return 文件路径
     */
    public String getFilePath() {
        return this.getFilePath(false);
    }

    /**
     * 获取路径，首个字符是否是分隔符
     * @param deleteFirstSeparator true，首个字符是分隔符，则删除分隔符
     * @return 文件路径
     */
    public String getFilePath(boolean deleteFirstSeparator) {
        String path = this.getFolderPath() + FILE_SEPARATOR + this.getDiskFilename();
        if (deleteFirstSeparator && path.startsWith(FILE_SEPARATOR)) {
            return path.substring(1);
        }
        return path;
    }

    /**
     * 获取存储文件名
     * @return 文件名
     */
    public String getDiskFilename() {
        if (!this.filename.contains(POINT_STR)) {
            return this.filename + ID_CUT + this.timestamp;
        }
        return this.filename.substring(0, this.filename.lastIndexOf(POINT_STR))
                + ID_CUT + this.timestamp + this.filename.substring(this.filename.lastIndexOf(POINT_STR));
    }

    /**
     * 获取实际文件名
     * @return 实际文件名
     */
    public String getActualFilename() {
        String name = this.filename;
        if (StringUtils.contains(this.filename, SEPARATOR_REPLACE)) {
            name = StringUtils.replace(this.filename, SEPARATOR_REPLACE, FILE_SEPARATOR);
        }
        return name;
    }

    /**
     * 获取文件ID
     * @return 文件ID
     */
    public String getFileId() {
        String fileId = String.join(SEPARATOR_REPLACE, this.folderPath, this.timestamp, this.filename);
        return Base64Utils.encode(fileId);
    }

    /**
     * 通过ID创建
     * @param id 文件ID
     * @param basePath 基础路径
     * @return 本地文件信息
     */
    @NonNull
    public static DiskFilePathBO createById(@NonNull String id, @NonNull String basePath) {
        String idStr = Base64Utils.decodeStr(id);
        final String[] ids = idStr.split(DiskFilePathBO.SEPARATOR_REPLACE);
        DiskFilePathBO diskFilePath = new DiskFilePathBO();
        diskFilePath.basePath = basePath;
        diskFilePath.folderPath = ids[0];
        diskFilePath.timestamp = ids[1];
        diskFilePath.filename = ids[2];
        return diskFilePath;
    }
}
