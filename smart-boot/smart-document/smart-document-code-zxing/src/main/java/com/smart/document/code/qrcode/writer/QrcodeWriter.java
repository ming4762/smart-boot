package com.smart.document.code.qrcode.writer;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

/**
 * This object renders a QR Code as a BitMatrix 2D array of greyscale values.
 * @author dswitkin@google.com (Daniel Switkin)
 * @author ShiZhongMing
 * 2021/8/10 19:05
 * @since 1.0
 */
public class QrcodeWriter implements Writer {

    private static final int QUIET_ZONE_SIZE = 5;

    @Override
    public BitMatrix encode(String contents, BarcodeFormat barcodeFormat, int width, int height) throws WriterException {
        return null;
    }

    @Override
    public BitMatrix encode(String contents, BarcodeFormat barcodeFormat, int width, int height, Map<EncodeHintType, ?> hints) throws WriterException {
        return this.encodeX(contents, barcodeFormat, width, height, hints).getBitMatrix();
    }

    public QrcodeBitMatrix encodeX(String contents, BarcodeFormat format, int width, int height,
                                   Map<EncodeHintType, ?> hints) throws WriterException {

        if (contents.isEmpty()) {
            throw new IllegalArgumentException("Found empty contents");
        }

        if (format != BarcodeFormat.QR_CODE) {
            throw new IllegalArgumentException("Can only encode QR_CODE, but got " + format);
        }

        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Requested dimensions are too small: " + width + 'x' + height);
        }

        ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.L;
        int quietZone = QUIET_ZONE_SIZE;
        if (hints != null) {
            if (hints.containsKey(EncodeHintType.ERROR_CORRECTION)) {
                errorCorrectionLevel = ErrorCorrectionLevel
                        .valueOf(hints.get(EncodeHintType.ERROR_CORRECTION).toString());
            }
            if (hints.containsKey(EncodeHintType.MARGIN)) {
                quietZone = Integer.parseInt(hints.get(EncodeHintType.MARGIN).toString());
            }
        }

        QRCode code = Encoder.encode(contents, errorCorrectionLevel, hints);
        BitMatrix bitMatrix = renderResult(code, width, height, quietZone);
        return new QrcodeBitMatrix(code, bitMatrix);

    }

    private static BitMatrix renderResult(QRCode code, int width, int height, int quietZone) {
        ByteMatrix input = code.getMatrix();
        if (input == null) {
            throw new IllegalStateException();
        }
        int inputWidth = input.getWidth();
        int inputHeight = input.getHeight();
        int outputWidth = Math.max(width, inputWidth);
        int outputHeight = Math.max(height, inputHeight);

        int multiple = Math.min(outputWidth / inputWidth, outputHeight / inputHeight);

        outputWidth = inputWidth * multiple + quietZone * 2;
        outputHeight = inputHeight * multiple + quietZone * 2;

        BitMatrix output = new BitMatrix(outputWidth, outputHeight);

        for (int inputY = 0, outputY = quietZone; inputY < inputHeight; inputY++, outputY += multiple) {
            // Write the contents of this row of the barcode
            for (int inputX = 0, outputX = quietZone; inputX < inputWidth; inputX++, outputX += multiple) {
                if (input.get(inputX, inputY) == 1) {
                    output.setRegion(outputX, outputY, multiple, multiple);
                }
            }
        }
        return output;
    }

    @AllArgsConstructor
    @Getter
    public static class QrcodeBitMatrix {
        private final QRCode qrcode;

        private final BitMatrix bitMatrix;
    }
}
