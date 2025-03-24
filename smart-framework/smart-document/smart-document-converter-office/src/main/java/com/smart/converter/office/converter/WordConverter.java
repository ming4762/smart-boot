package com.smart.converter.office.converter;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.smart.converter.office.constants.OfficeAppConstants;
import com.smart.converter.office.constants.WdSaveFormat;
import com.smart.converter.office.exception.ConvertTypeNotSupportException;
import lombok.NonNull;

/**
 * @author ShiZhongMing
 * 2021/8/26 9:47
 * @since 1.0
 */
public class WordConverter extends AbstractOfficeConverter {

    private ActiveXComponent app = null;

    private Dispatch doc = null;

    public WordConverter(@NonNull String fromPath) {
        super(fromPath);
    }

    @Override
    public OfficeConverter toFormat(@NonNull ConvertFileType fileType) {
        if (!WdSaveFormat.class.equals(fileType.getClass())) {
            throw new ConvertTypeNotSupportException(fileType.getClass().getName(), WdSaveFormat.class.getName());
        }
        return super.toFormat(fileType);
    }

    @Override
    protected void doExecute() {
        this.app = OfficeAppConstants.WORD.createApp();
        // 设置word不可见，否则会弹出word界面
        app.setProperty("Visible", false);
        // 获取word中所有打开的文档，并返回Documents对象
        Dispatch docs = app.getProperty("Documents").toDispatch();
        // 调用Documents对象中的Open方法打开文档，返回打开文档的Document对象
        this.doc = Dispatch.call(docs, "Open", this.fromPath, false, true)
                .toDispatch();
        // 调用SaveAs2进行文档转换
        Dispatch.call(doc, "SaveAs2", toPath, fileType.getValue());
    }
    @Override
    public void close() {
        if (this.doc != null) {
            // 关闭文档
            Dispatch.call(this.doc, "Close", false);
        }
        if (this.app != null) {
            // 关闭应用
            this.app.invoke("Quit", 0);
        }
        super.close();
    }
}
