package com.smart.converter.office.converter;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.smart.converter.office.constants.OfficeAppConstants;
import com.smart.converter.office.constants.PpSaveAsFileType;
import com.smart.converter.office.exception.ConvertTypeNotSupportException;
import lombok.NonNull;

/**
 * @author ShiZhongMing
 * 2021/8/26 12:53
 * @since 1.0
 */
public class PowerPointConverter extends AbstractOfficeConverter{

    private ActiveXComponent app = null;

    private Dispatch ppt = null;

    public PowerPointConverter(@NonNull String fromPath) {
        super(fromPath);
    }

    @Override
    public OfficeConverter toFormat(@NonNull ConvertFileType fileType) {
        if (!PpSaveAsFileType.class.equals(fileType.getClass())) {
            throw new ConvertTypeNotSupportException(fileType.getClass().getName(), PpSaveAsFileType.class.getName());
        }
        return super.toFormat(fileType);
    }

    @Override
    protected void doExecute() {
        this.app = OfficeAppConstants.POWER_POINT.createApp();
        // 获取ppt中所有打开的文档，并返回Presentations象
        Dispatch ppts = app.getProperty("Presentations").toDispatch();
        // 调用Presentations对象中的Open方法打开文档，返回打开文档的Presentation对象
        ppt = Dispatch.call(ppts,
                "Open",
                this.fromPath,
                // ReadOnly
                true,
                // 指定文件是否有标题
                true,
                false
        ).toDispatch();
        // 调用Presentation对象的SaveAs方法，将文档保存为pdf文件
        Dispatch.call(ppt, "SaveAs", this.toPath, this.fileType.getValue());
    }

    @Override
    public void close() {
        if (this.ppt != null) {
            // 关闭文档
            Dispatch.call(this.ppt, "Close");
        }
        if (this.app != null) {
            // 关闭应用
            this.app.invoke("Quit");
        }
        super.close();
    }
}
