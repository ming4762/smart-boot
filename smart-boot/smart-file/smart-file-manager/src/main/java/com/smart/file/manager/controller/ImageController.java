package com.smart.file.manager.controller;

import com.smart.auth.core.annotation.TempToken;
import com.smart.file.manager.pojo.bo.SysFileBO;
<<<<<<< HEAD
import com.smart.file.manager.service.SysFileService;
=======
import com.smart.file.manager.service.FileHandler;
import jakarta.servlet.http.HttpServletResponse;
>>>>>>> 22d0df4 (文件管理模块重构，优化使用体验)
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 公共图片接口
 * @author shizhongming
 * 2020/6/28 6:13 下午
 */
@Controller
@RequestMapping
public class ImageController {

    private final FileHandler fileHandler;

    public ImageController(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    /**
     * 显示图片
     * @param id 图片ID
     * @param response HttpServletResponse
     */
    @GetMapping("public/file/show/{id}")
    @TempToken(resource = "sys:file:download")
    public void show(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        SysFileBO file = this.fileHandler.download(id);
        if (Objects.nonNull(file)) {
            var inputStream = file.getInputStream();
            try (inputStream) {
                IOUtils.copy(inputStream, response.getOutputStream());
            }
        }
    }
}
