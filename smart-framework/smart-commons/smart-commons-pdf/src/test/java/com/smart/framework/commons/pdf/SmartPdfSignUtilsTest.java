package com.smart.framework.commons.pdf;

import com.smart.framework.commons.pdf.data.PdfSignatureData;
import com.smart.framework.commons.pdf.data.PdfSignatureImageData;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author shizhongming
 * 2025/10/15 16:13
 * @since 5.0.0
 */
class SmartPdfSignUtilsTest {

    private static KeyStore keyStore;
    private static final String PASSWORD = "123456";

    @BeforeAll
    static void setup() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        keyStore = createTestKeyStore();
    }

    /**
     * 测试 PDF 签名
     */
    @Test
    void testSignPdf() throws Exception {
        // 生成简单 PDF
        ByteArrayOutputStream pdfBytes = new ByteArrayOutputStream();
        try (PDDocument doc = new PDDocument()) {
            for (int i = 0; i < 3; i++) {
                doc.addPage(new PDPage());
            }
            doc.save(pdfBytes);
        }
        // 生成测试印章图片
        ByteArrayOutputStream imageBytes = new ByteArrayOutputStream();
        ImageIO.write(createSealImage(), "png", imageBytes);

        // 构建签名数据
        PdfSignatureData data = new PdfSignatureData();
        data.setSignerName("测试签名人");
        data.setReason("测试用途");
        data.setLocation("北京");

        PdfSignatureImageData signatureImageData = PdfSignatureImageData.builder()
                .imageStream(new ByteArrayInputStream(imageBytes.toByteArray()))
                .page(-1)
                .x(420)
                .y(80)
                .width(100)
                .height(100)
                .build();

        // 调用签名方法
        ByteArrayInputStream pdfInput = new ByteArrayInputStream(pdfBytes.toByteArray());
        ByteArrayOutputStream signedPdfOutput = new ByteArrayOutputStream();

        try (ByteArrayOutputStream keyStoreBytes = new ByteArrayOutputStream()) {
            keyStore.store(keyStoreBytes, PASSWORD.toCharArray());
            try (InputStream keyStoreInput = new ByteArrayInputStream(keyStoreBytes.toByteArray())) {
                SmartPdfSignUtils.sign(pdfInput, keyStoreInput, PASSWORD, data, signedPdfOutput, signatureImageData);
            }
        }
        try (FileOutputStream fos = new FileOutputStream("signed.pdf")) {
            fos.write(signedPdfOutput.toByteArray());
        }

        // 验证签名是否存在
        try (PDDocument signedDoc = Loader.loadPDF(new RandomAccessReadBuffer(new ByteArrayInputStream(signedPdfOutput.toByteArray())))) {
            assertFalse(signedDoc.getSignatureDictionaries().isEmpty(), "签名信息应存在");
            PDSignature sig = signedDoc.getSignatureDictionaries().getFirst();
            assertEquals("测试签名人", sig.getName(), "签名人名称应匹配");
            assertEquals("测试用途", sig.getReason(), "签名原因应匹配");
            // 验证页数一致
            assertEquals(3, signedDoc.getNumberOfPages(), "页数应保持一致");
            // 这里只验证文件非空（图片绘制可人工查看）
            assertTrue(signedPdfOutput.size() > pdfBytes.size(), "签名后的PDF应大于原文件");
        }
    }

    /**
     * 创建临时 PKCS12 密钥库（仅用于测试）
     */
    private static KeyStore createTestKeyStore() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "BC");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();

        X500Name dnName = new X500Name("CN=Test");
        Date startDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 1);
        Date endDate = cal.getTime();

        BigInteger certSerialNumber = BigInteger.valueOf(System.currentTimeMillis());
        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256withRSA")
                .setProvider("BC")
                .build(keyPair.getPrivate());

        X509CertificateHolder certHolder = new JcaX509v3CertificateBuilder(
                dnName,
                certSerialNumber,
                startDate,
                endDate,
                dnName,
                keyPair.getPublic()
        ).build(contentSigner);

        X509Certificate cert = new JcaX509CertificateConverter()
                .setProvider("BC")
                .getCertificate(certHolder);

        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(null, null);
        ks.setKeyEntry("alias", keyPair.getPrivate(), PASSWORD.toCharArray(), new Certificate[]{cert});
        return ks;
    }

    /**
     * 生成简单的红色圆形印章图片（内存中）
     */
    private static BufferedImage createSealImage() {
        int size = 120;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(new Color(220, 0, 0, 255));
        g.fillOval(0, 0, size, size);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 14));
        g.drawString("TEST", 30, 65);
        g.dispose();
        return image;
    }
}
