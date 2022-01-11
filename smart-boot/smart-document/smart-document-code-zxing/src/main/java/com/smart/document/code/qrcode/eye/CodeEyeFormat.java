package com.smart.document.code.qrcode.eye;

import com.smart.document.code.qrcode.eye.render.CodeEyeRenderer;
import com.smart.document.code.qrcode.eye.render.RBorderRPointRender;

/**
 * 码眼类型
 * @author Bosco.Liao
 * @author ShiZhongMing
 * 2021/8/11 15:47
 * @since 1.0
 */
public enum CodeEyeFormat implements CodeEyeFormatProvider {

    /**
     * Rectangle Border with Rectangle Point.
     */
    R_BORDER_R_POINT(RBorderRPointRender.class),

    /**
     * Rectangle Border with Circle Point.
     */
    R_BORDER_C_POINT(RBorderRPointRender.class),

    /**
     * Circle Border with Rectangle Point.
     */
    C_BORDER_R_POINT(RBorderRPointRender.class),

    /**
     * Circle Border with Circle Point.
     */
    C_BORDER_C_POINT(RBorderRPointRender.class),

    /**
     * RoundRectangle Border with Rectangle Point.
     */
    R2_BORDER_R_POINT(RBorderRPointRender.class),

    /**
     * RoundRectangle Border with Circle Point.
     */
    R2_BORDER_C_POINT(RBorderRPointRender.class),

    /**
     * Diagonal RoundRectangle Border with Rectangle Point.
     */
    DR2_BORDER_R_POINT(RBorderRPointRender.class),

    /**
     * Diagonal RoundRectangle Border with Circle Point.
     */
    DR2_BORDER_C_POINT(RBorderRPointRender.class);

    private final Class<? extends CodeEyeRenderer> renderClass;

    CodeEyeFormat(Class<? extends CodeEyeRenderer> renderClass) {
        this.renderClass = renderClass;
    }

    @Override
    public Class<? extends CodeEyeRenderer> getRenderClass() {
        return renderClass;
    }
}
