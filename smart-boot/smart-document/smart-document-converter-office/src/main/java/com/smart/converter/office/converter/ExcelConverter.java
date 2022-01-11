package com.smart.converter.office.converter;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.smart.converter.office.constants.OfficeAppConstants;
import com.smart.converter.office.constants.XlFileFormat;
import com.smart.converter.office.exception.ConvertTypeNotSupportException;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * excel转换工具
 * @author ShiZhongMing
 * 2021/8/25 15:47
 * @since 1.0
 */
public class ExcelConverter extends AbstractOfficeConverter {

    /**
     * 调用ExportAsFixedFormat格式列表
     */
    private static final List<Integer> EXPORT_FORMAT_LIST = new ArrayList<>(2);

    static {
        EXPORT_FORMAT_LIST.add(0);
        EXPORT_FORMAT_LIST.add(1);
    }

    private ActiveXComponent app = null;

    private Dispatch excel = null;

    public ExcelConverter(@NonNull String fromPath) {
        super(fromPath);
    }

    @Override
    public OfficeConverter toFormat(@NonNull ConvertFileType fileType) {
        if (!XlFileFormat.class.equals(fileType.getClass())) {
            throw new ConvertTypeNotSupportException(fileType.getClass().getName(), XlFileFormat.class.getName());
        }
        return super.toFormat(fileType);
    }

    @Override
    protected void doExecute() {
        this.app = OfficeAppConstants.EXCEL.createApp();
        // 设置excel不可见，否则会弹出excel界面
        app.setProperty("Visible", false);
        // 获取excel中所有打开的文档，并返回Workbooks象
        Dispatch excels = app.getProperty("Workbooks").toDispatch();
        // 调用Workbooks对象中的Open方法打开文档，返回打开文档的Workbook对象
        excel = Dispatch.call(excels, "Open", this.fromPath, false, true)
                .toDispatch();
        if (EXPORT_FORMAT_LIST.contains(fileType.getValue())) {
            Dispatch.call(excel, "ExportAsFixedFormat", fileType.getValue(), toPath);
        } else {
            Dispatch.call(excel, "SaveAs", toPath, fileType.getValue());
        }
    }

    @Override
    public void close() {
        if (this.excel != null) {
            // 关闭文档
            Dispatch.call(excel, "Close", false);
        }
        if (this.app != null) {
            // 关闭应用
            app.invoke("Quit");
        }
        super.close();
    }
}
