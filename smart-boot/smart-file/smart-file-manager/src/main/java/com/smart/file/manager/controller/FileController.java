package com.smart.file.manager.controller;

import com.smart.auth.core.annotation.TempToken;
import com.smart.commons.core.message.Result;
import com.smart.file.manager.constants.FileTypeEnum;
import com.smart.file.manager.model.SysFilePO;
import com.smart.file.manager.pojo.bo.SysFileBO;
import com.smart.file.manager.pojo.dto.SaveFileDTO;
import com.smart.file.manager.service.FileHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author ShiZhongMing
 * 2022/8/24
 * @since 3.0.0
 */
@Controller
public class FileController {

    private final FileHandler fileHandler;

    public FileController(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    /**
     * 上传文件
     * @param multipartFile 文件
     * @param fileName 文件名
     * @param type 文件类型
     * @return 保存的文件信息
     */
    @Operation(summary = "上传文件")
    @PostMapping("sys/file/upload")
    @PreAuthorize("hasPermission('sys:file', 'upload')")
    @Parameters({
            @Parameter(name = "file", description = "文件信息", required = true),
            @Parameter(name = "fileName", description = "文件名"),
            @Parameter(name = "type", description = "文件类型")
    })
    @ResponseBody
    public Result<SysFilePO> upload(
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam(value = "fileName", required = false) String fileName,
            @RequestParam(value = "type", required = false) FileTypeEnum type
    ) {
        return Result.success(this.fileHandler.saveFile(multipartFile, SaveFileDTO.builder().type(type).filename(fileName).build()));
    }

    /**
     * 批量上传文件
     * @param multipartFileList 文件列表
     * @param type 文件类型
     * @return 保存的文件信息
     */
    @Operation(summary = "批量上传文件")
    @PreAuthorize("hasPermission('sys:file', 'upload')")
    @PostMapping("sys/file/batchUpload")
    @Parameters({
            @Parameter(name = "files", description = "文件集合", required = true),
            @Parameter(name = "type", description = "文件类型"),
    })
    @ResponseBody
    public Result<List<SysFilePO>> batchUpload(
            @RequestParam("files")List<MultipartFile> multipartFileList,
            @RequestParam(value = "type", required = false) FileTypeEnum type
    ) {
        if (multipartFileList.isEmpty()) {
            return Result.success(List.of());
        }
        return Result.success(
                multipartFileList.stream()
                        .map(item -> this.fileHandler.saveFile(item, type))
                        .toList()
        );
    }

    /**
     * 下载文件接口
     * @param id 文件ID
     * @param response HttpServletResponse
     * @throws IOException IOException
     */
    @Operation(summary = "下载文件")
    @RequestMapping(value = "/file/download/{id}", method = {RequestMethod.GET, RequestMethod.POST})
    @TempToken(resource = "sys:file:download")
    public void download(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        final SysFileBO file = this.fileHandler.download(id);
        if (file != null) {
            //设置文件名并转码
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getFile().getFileName(), StandardCharsets.UTF_8.name()));
            response.setContentType(file.getFile().getContentType());
            try (InputStream inputStream = file.getInputStream()) {
                IOUtils.copy(inputStream, response.getOutputStream());
            }
        }
    }

    /**
     * 批量删除文件
     * @param ids 文件ID列表
     * @return 是否删除成功
     */
    @PreAuthorize("hasPermission('sys:file', 'delete')")
    @PostMapping("sys/file/batchDeleteById")
    @Operation(summary = "批量删除文件")
    @ResponseBody
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> ids)  {
        return Result.success(this.fileHandler.batchDeleteFile(ids));
    }
}
