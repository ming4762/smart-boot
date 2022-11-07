package com.smart.file.manager.controller;

import com.google.common.collect.ImmutableList;
import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.file.manager.model.SysFilePO;
import com.smart.file.manager.pojo.bo.SysFileBO;
import com.smart.file.manager.pojo.dto.SaveFileDTO;
import com.smart.file.manager.service.SysFileService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shizhongming
 * 2020/1/27 7:51 下午
 */
@Slf4j
@Tag(name = "文件管理", description = "文件管理")
public class SysFileController extends BaseController<SysFileService, SysFilePO> {

    /**
     * 上传文件
     * @param multipartFile 文件
     * @param fileName 文件名
     * @param type 文件类型
     * @return 保存的文件信息
     */
    @Operation(summary = "上传文件")
    @Parameters({
            @Parameter(name = "file", description = "文件信息", required = true),
            @Parameter(name = "fileName", description = "文件名"),
            @Parameter(name = "type", description = "文件类型")
    })
    public Result<SysFilePO> upload(
            @RequestParam("file")MultipartFile multipartFile,
            @RequestParam(value = "fileName", required = false) String fileName,
            @RequestParam(value = "type", required = false) String type
    ) throws IOException {
        return Result.success(this.service.saveFile(multipartFile, SaveFileDTO.builder().type(type).filename(fileName).build()));
    }


    /**
     * 批量上传文件
     * @param multipartFileList 文件列表
     * @param type 文件类型
     * @return 保存的文件信息
     */
    @Operation(summary = "批量上传文件")
    @Parameters({
            @Parameter(name = "files", description = "文件集合", required = true),
            @Parameter(name = "type", description = "文件类型"),
    })
    public Result<List<SysFilePO>> batchUpload(
            @RequestParam("files")List<MultipartFile> multipartFileList,
            @RequestParam(value = "type", required = false) String type
    ) {
        if (multipartFileList.isEmpty()) {
            return Result.success(ImmutableList.of());
        }
        return Result.success(
                multipartFileList.stream()
                        .map(item -> this.service.saveFile(item, type))
                        .collect(Collectors.toList())
        );
    }

    /**
     * 下载文件接口
     * @param id 文件ID
     * @param response HttpServletResponse
     * @throws IOException IOException
     */
    @Operation(summary = "下载文件")
    public void download(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        final SysFileBO file = this.service.download(id);
        if (file != null) {
            //设置文件名并转码
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getFile().getFileName(), StandardCharsets.UTF_8.name()));
            response.setContentType(file.getFile().getContentType());
            IOUtils.copy(file.getInputStream(), response.getOutputStream());
        }
    }

    @Override
    @PostMapping("list")
    @Operation(summary = "查询文件列表", method = "POST")
    @PreAuthorize("hasPermission('sys:file', 'query')")
    @ResponseBody
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }
}
