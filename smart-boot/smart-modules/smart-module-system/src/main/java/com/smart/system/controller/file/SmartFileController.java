package com.smart.system.controller.file;

import com.smart.commons.core.message.Result;
import com.smart.crud.constants.CrudCommonEnum;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.file.core.parameter.FileSaveParameter;
import com.smart.file.core.pojo.bo.FileHandlerResult;
import com.smart.file.core.service.FileService;
import com.smart.system.model.file.SmartFilePO;
import com.smart.system.pojo.dto.file.FileUploadDTO;
import com.smart.system.service.file.SmartFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shizhongming
 * 2020/1/27 7:51 下午
 */
@Slf4j
@Tag(name = "文件管理", description = "文件管理")
@RequestMapping("smart/file")
@RestController
public class SmartFileController extends BaseController<SmartFileService, SmartFilePO> {

    private final FileService fileService;

    public SmartFileController(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    @PostMapping("list")
    @Operation(summary = "查询文件列表", method = "POST")
    @PreAuthorize("hasPermission('smart:file', 'query')")
    @ResponseBody
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        parameter.getParameter().put(CrudCommonEnum.WITH_ALL.name(), true);
        return super.list(parameter);
    }

    /**
     * 上传文件
     * @return 保存的文件信息
     */
    @Operation(summary = "上传文件")
    @PostMapping("upload")
    @PreAuthorize("hasPermission('smart:file', 'upload')")
    @ResponseBody
    public Result<FileHandlerResult> upload(FileUploadDTO parameter) {
        return Result.success(this.fileService.save(parameter.getFile(), FileSaveParameter.builder()
                .filename(parameter.getFileName())
                .type(parameter.getType())
                .fileStorageId(parameter.getFileStorageId())
                .build()));
    }

    /**
     * 批量上传文件
     * @param multipartFileList 文件列表
     * @param type 文件类型
     * @return 保存的文件信息
     */
    @Operation(summary = "批量上传文件")
    @PreAuthorize("hasPermission('smart:file', 'upload')")
    @PostMapping("batchUpload")
    @Parameters({
            @Parameter(name = "files", description = "文件集合", required = true),
            @Parameter(name = "type", description = "文件类型"),
    })
    @ResponseBody
    public Result<List<FileHandlerResult>> batchUpload(
            @RequestParam("files")List<MultipartFile> multipartFileList,
            @RequestParam(value = "type", required = false) String type
    ) {
        if (multipartFileList.isEmpty()) {
            return Result.success(new ArrayList<>(0));
        }
        return Result.success(
                multipartFileList.stream()
                        .map(item -> this.fileService.save(item, FileSaveParameter.builder().type(type).build()))
                        .collect(Collectors.toList())
        );
    }

    /**
     * 批量删除文件
     * @param ids 文件ID列表
     * @return 是否删除成功
     */
    @PreAuthorize("hasPermission('smart:file', 'delete')")
    @PostMapping("batchDeleteFile")
    @Operation(summary = "批量删除文件")
    @ResponseBody
    public Result<List<FileHandlerResult>> batchDeleteFile(@RequestBody List<Long> ids)  {
        return Result.success(
                this.fileService.batchDelete(ids)
        );
    }
}