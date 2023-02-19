package com.smart.system.controller.file;

import com.smart.auth.core.annotation.TempToken;
import com.smart.file.core.pojo.bo.FileDownloadResult;
import com.smart.file.core.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author zhongming4762
 * 2023/2/19
 */
@Controller
@RequestMapping
public class FileController {


    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * 上传文件
     * @param multipartFile 文件
     * @param fileName 文件名
     * @param type 文件类型
     * @return 保存的文件信息
     */
    @Operation(summary = "上传文件")
    @PostMapping("smart/file/upload")
    @PreAuthorize("hasPermission('smart:file', 'upload')")
    @Parameters({
            @Parameter(name = "file", description = "文件信息", required = true),
            @Parameter(name = "fileName", description = "文件名"),
            @Parameter(name = "type", description = "文件类型")
    })
    @ResponseBody
    public Result<FileHandlerResult> upload(
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam(value = "fileName", required = false) String fileName,
            @RequestParam(value = "type", required = false) FileTypeEnum type
    ) {
        return Result.success(this.fileService.save(multipartFile, FileSaveParameter.builder()
                        .filename(fileName)
                        .type(type)
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
    @PostMapping("smart/file/batchUpload")
    @Parameters({
            @Parameter(name = "files", description = "文件集合", required = true),
            @Parameter(name = "type", description = "文件类型"),
    })
    @ResponseBody
    public Result<List<FileHandlerResult>> batchUpload(
            @RequestParam("files")List<MultipartFile> multipartFileList,
            @RequestParam(value = "type", required = false) FileTypeEnum type
    ) {
        if (multipartFileList.isEmpty()) {
            return Result.success(List.of());
        }
        return Result.success(
                multipartFileList.stream()
                        .map(item -> this.fileService.save(item, FileSaveParameter.builder().type(type).build()))
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
    @RequestMapping(value = "public/file/download/{id}", method = {RequestMethod.GET, RequestMethod.POST})
    @TempToken(resource = "smart:file:download")
    public void download(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        FileDownloadResult downloadResult = this.fileService.download(id);
        if (downloadResult != null) {
            //设置文件名并转码
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(downloadResult.getFileName(), StandardCharsets.UTF_8.name()));
            response.setContentType(downloadResult.getContentType());
            try (InputStream inputStream = downloadResult.getInputStream()) {
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
    public Result<List<FileHandlerResult>> batchDeleteById(@RequestBody List<Long> ids)  {
        return Result.success(
                this.fileService.batchDelete(ids)
        );
    }
}
