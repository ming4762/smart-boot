package com.smart.file.manager.controller;

import com.google.common.collect.ImmutableList;
import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.crud.query.PageSortQuery;
import com.smart.file.manager.model.SysFilePO;
import com.smart.file.manager.service.SysFileService;
<<<<<<< HEAD
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
=======
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
>>>>>>> 22d0df4 (文件管理模块重构，优化使用体验)
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
=======
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
>>>>>>> 22d0df4 (文件管理模块重构，优化使用体验)

/**
 * @author shizhongming
 * 2020/1/27 7:51 下午
 */
@Slf4j
<<<<<<< HEAD
@Api(value = "文件管理", tags = "文件管理")
public class SysFileController extends BaseController<SysFileService, SysFilePO> {

    /**
     * 上传文件
     * @param multipartFile 文件
     * @param fileName 文件名
     * @param type 文件类型
     * @return 保存的文件信息
     */
    @ApiOperation(value = "上传文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "文件信息", dataTypeClass = MultipartFile.class, required = true),
            @ApiImplicitParam(name = "fileName", value = "文件名", dataTypeClass = String.class),
            @ApiImplicitParam(name = "type", value = "文件类型", defaultValue = "TEMP", dataTypeClass = String.class)
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
    @ApiOperation(value = "批量上传文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "files", value = "文件集合", required = true),
            @ApiImplicitParam(name = "type", value = "文件类型", defaultValue = "TEMP", dataTypeClass = String.class),
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
    @ApiOperation(value = "下载文件")
    public void download(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        final SysFileBO file = this.service.download(id);
        if (file != null) {
            //设置文件名并转码
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getFile().getFileName(), StandardCharsets.UTF_8.name()));
            response.setContentType(file.getFile().getContentType());
            IOUtils.copy(file.getInputStream(), response.getOutputStream());
        }
    }

    /**
     * 批量删除文件
     * @param ids 文件ID列表
     * @return 是否删除成功
     */
    @SneakyThrows
    @ApiOperation("批量删除文件")
=======
@Tag(name = "文件管理")
@Controller
@RequestMapping("sys/file")
public class SysFileController extends BaseController<SysFileService, SysFilePO> {

>>>>>>> 22d0df4 (文件管理模块重构，优化使用体验)
    @Override
    @PostMapping("list")
    @Operation(summary = "查询文件列表", method = "POST")
    @PreAuthorize("hasPermission('sys:file', 'query')")
    @ResponseBody
    public Result<Object> list(@RequestBody @NonNull PageSortQuery parameter) {
        return super.list(parameter);
    }
}
