package com.smart.file.manager.controller;

import com.smart.file.core.service.FileService;
import com.smart.module.api.file.bo.FileDownloadResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Objects;

/**
 * 公共图片接口
 * @author shizhongming
 * 2020/6/28 6:13 下午
 */
@Controller
@RequestMapping
@Tag(name = "图片接口")
public class ImageController {

    private final FileService fileService;

    public ImageController(FileService fileHandler) {
        this.fileService = fileHandler;
    }

    /**
     * 显示图片
     * @param id 图片ID
     * @param response HttpServletResponse
     */
    @GetMapping("public/file/show/{id}")
    public void show(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        FileDownloadResult file = this.fileService.download(id);
        if (Objects.nonNull(file)) {
            var inputStream = file.getInputStream();
            response.setContentType(file.getContentType());
            try (inputStream) {
                IOUtils.copy(inputStream, response.getOutputStream());
            }
        }
    }
}
