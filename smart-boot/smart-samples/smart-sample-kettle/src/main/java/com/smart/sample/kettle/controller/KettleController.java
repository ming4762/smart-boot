package com.smart.sample.kettle.controller;

import com.smart.commons.core.message.Result;
import com.smart.kettle.core.properties.KettleDatabaseRepositoryProperties;
import com.smart.kettle.core.service.KettleService;
import com.smart.starter.kettle.xxl.pojo.dto.KettleTransExecuteDTO;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.core.plugins.PluginFolder;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.di.trans.Trans;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ShiZhongMing
 * 2022/1/24 11:26
 * @since 1.0
 */
@RestController
@RequestMapping("kettle")
@Slf4j
public class KettleController {

    private final KettleService kettleService;

    private final KettleDatabaseRepositoryProperties repositoryProperties;

    public KettleController(KettleService kettleService, KettleDatabaseRepositoryProperties repositoryProperties) {
        this.kettleService = kettleService;
        this.repositoryProperties = repositoryProperties;
    }

    @SneakyThrows
    @PostConstruct
    public void initKettlePlugin() {
        // 添加SAP插件
        StepPluginType.getInstance().getPluginFolders().add(new PluginFolder("C:\\Users\\ShiZhong\\Documents\\soft\\data-integration-9.2\\plugins\\kettle-sap-plugin", false, true));
    }

    @PostMapping("executeKettleDbTrans")
    public Result<Boolean> executeKettleDbTrans(@RequestBody KettleTransExecuteDTO parameter) {
        Trans trans = this.kettleService.executeDbTransfer(
                this.repositoryProperties,
                parameter.getTransName(),
                parameter.getDirectoryName(),
                parameter.getParams().toArray(new String[0]),
                parameter.getVariableMap(),
                parameter.getParameterMap(),
                parameter.getLogLevel(),
                null
        );
        return Result.success(true);
    }

    @PostMapping("executeKettleTrans")
    public Result<Boolean> executeKettleTrans(@RequestBody KettleTransExecuteDTO parameter) {
        this.kettleService.executeFileTransfer(
                "C:\\360极速浏览器下载\\sync_bom.ktr",
                parameter.getParams().toArray(new String[0]),
                parameter.getVariableMap(),
                parameter.getParameterMap(),
                parameter.getLogLevel(),
                null
        );
        return Result.success(true);
    }
}
