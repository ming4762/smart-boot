package com.smart.file.manager.controller;

import com.google.common.collect.ImmutableList;
import com.smart.commons.core.message.Result;
import com.smart.crud.controller.BaseController;
import com.smart.file.manager.model.SysFilePO;
import com.smart.file.manager.pojo.bo.SysFileBO;
import com.smart.file.manager.pojo.dto.SaveFileDTO;
import com.smart.file.manager.service.SysFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shizhongming
 * 2020/1/27 7:51 下午
 */
@Slf4j
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
    @Override
    public Result<Boolean> batchDeleteById(@RequestBody List<Serializable> ids)  {
        return Result.success(this.service.batchDeleteFile(ids));
    }

}
