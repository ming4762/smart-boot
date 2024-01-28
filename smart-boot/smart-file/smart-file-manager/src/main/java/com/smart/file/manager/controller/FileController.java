package com.smart.file.manager.controller;

import com.smart.auth.core.annotation.TempToken;
import com.smart.commons.core.message.Result;
import com.smart.crud.query.IdParameter;
import com.smart.file.core.service.FileService;
import com.smart.module.api.file.bo.FileDownloadResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author zhongming4762
 * 2023/2/19
 */
@Controller
@RequestMapping
@Tag(name = "文件列表")
public class FileController {


    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
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
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(downloadResult.getFilename(), StandardCharsets.UTF_8.name()));
            response.setContentType(downloadResult.getContentType());
            try (InputStream inputStream = downloadResult.getInputStream()) {
                IOUtils.copy(inputStream, response.getOutputStream());
            }
        }
    }

    @PostMapping("smart/file/getAddress")
    @ResponseBody
    public Result<String> getAddress(@RequestBody IdParameter idParameter) {
        List<String> addressList = this.fileService.listAddress(List.of(idParameter.getId()));
        if (!CollectionUtils.isEmpty(addressList)) {
            return Result.success(addressList.get(0));
        }
        return Result.failure("获取连接失败");
    }
}
