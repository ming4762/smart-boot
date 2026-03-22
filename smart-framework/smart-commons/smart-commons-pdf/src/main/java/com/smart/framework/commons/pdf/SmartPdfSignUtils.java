package com.smart.framework.commons.pdf;

import com.smart.framework.commons.core.file.AutoDeleteFileInputStream;
import com.smart.framework.commons.pdf.data.PdfSignatureData;
import com.smart.framework.commons.pdf.data.PdfSignatureImageData;
import com.smart.framework.commons.pdf.data.PdfSignatureValidationResult;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.io.RandomAccessReadBufferedFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.PDSignature;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureInterface;
import org.apache.pdfbox.pdmodel.interactive.digitalsignature.SignatureOptions;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;

import java.io.*;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.time.ZoneId;
import java.util.*;

/**
 * PDF签名工具类
 * @author shizhongming
 * 2025/10/15 14:25
 * @since 5.0.0
 */
public class SmartPdfSignUtils {

    private SmartPdfSignUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final String KEY_STORE_TYPE = "PKCS12";
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    private static final String PROVIDER_NAME = "BC";

    /**
     * 对PDF文件进行签名
     * 适用于大文件
     * @param pdfFile 待签名的PDF文件
     * @param keyStoreInputStream 密钥库输入流
     * @param keyStorePassword 密钥库密码
     * @param pdfSignatureData PDF签名数据
     * @param outputStream 输出流
     */
    @SneakyThrows(IOException.class)
    public static void sign(
            @NonNull File pdfFile,
            @NonNull InputStream keyStoreInputStream,
            @NonNull String keyStorePassword,
            @NonNull PdfSignatureData pdfSignatureData,
            @NonNull OutputStream outputStream,
            PdfSignatureImageData imageData
    ) {
        try (PDDocument document = Loader.loadPDF(new RandomAccessReadBufferedFile(pdfFile))) {
            doSign(document, keyStoreInputStream, keyStorePassword, pdfSignatureData, outputStream, imageData);
        }
    }

    /**
     * 对PDF文件进行签名
     * 大文件慎用，内存占用大，可能OOM
     * @param pdfInputStream 待签名的PDF文件输入流
     * @param keyStoreInputStream 密钥库输入流
     * @param keyStorePassword 密钥库密码
     * @param pdfSignatureData PDF签名数据
     * @param outputStream 输出流
     */
    @SneakyThrows(Exception.class)
    public static void sign(
            @NonNull InputStream pdfInputStream,
            @NonNull InputStream keyStoreInputStream,
            @NonNull String keyStorePassword,
            @NonNull PdfSignatureData pdfSignatureData,
            @NonNull OutputStream outputStream,
            PdfSignatureImageData imageData
    ) {
        try (PDDocument document = Loader.loadPDF(new RandomAccessReadBuffer(pdfInputStream))) {
            doSign(document, keyStoreInputStream, keyStorePassword, pdfSignatureData, outputStream, imageData);
        }
    }

    /**
     * 对PDF文件进行签名
     * @param pdfInputStream 待签名的PDF文件输入流
     * @param keyStoreInputStream 密钥库输入流
     * @param keyStorePassword 密钥库密码
     * @param pdfSignatureData PDF签名数据
     * @return 签名后的PDF文件输入流
     */
    @SneakyThrows(IOException.class)
    public static InputStream sign(
            @NonNull InputStream pdfInputStream,
            @NonNull InputStream keyStoreInputStream,
            @NonNull String keyStorePassword,
            @NonNull PdfSignatureData pdfSignatureData,
            PdfSignatureImageData imageData
    ) {
        File tempFile = File.createTempFile("smart-pdf-sign", ".pdf");
        try (OutputStream outputStream = new FileOutputStream(tempFile)) {
            sign(pdfInputStream, keyStoreInputStream, keyStorePassword, pdfSignatureData, outputStream, imageData);
        }
        return new AutoDeleteFileInputStream(tempFile);
    }

    @SneakyThrows(Exception.class)
    private static void doSign(
            @NonNull PDDocument document,
            @NonNull InputStream keyStoreInputStream,
            @NonNull String keyStorePassword,
            @NonNull PdfSignatureData pdfSignatureData,
            @NonNull OutputStream outputStream,
            PdfSignatureImageData imageData
    ) {
        // 加载密钥
        KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE);
        keyStore.load(keyStoreInputStream, keyStorePassword.toCharArray());
        String alias = keyStore.aliases().nextElement();
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, keyStorePassword.toCharArray());
        Certificate[] chain = keyStore.getCertificateChain(alias);

        PDSignature signature = new PDSignature();
        signature.setFilter(PDSignature.FILTER_ADOBE_PPKLITE);
        signature.setSubFilter(PDSignature.SUBFILTER_ADBE_PKCS7_DETACHED);

        signature.setName(pdfSignatureData.getSignerName());
        signature.setReason(pdfSignatureData.getReason());
        signature.setLocation(pdfSignatureData.getLocation());
        signature.setSignDate(Calendar.getInstance());

        if (imageData != null) {
            addSignatureImage(document, imageData);
        }

        SignatureInterface signatureInterface = content -> {
            try {
                List<Certificate> certList = Arrays.asList(chain);
                Store<?> certs = new JcaCertStore(certList);
                ContentSigner sha256Signer = new JcaContentSignerBuilder(SIGNATURE_ALGORITHM)
                        .setProvider(PROVIDER_NAME)
                        .build(privateKey);
                CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
                gen.addSignerInfoGenerator(
                        new JcaSignerInfoGeneratorBuilder(
                                new JcaDigestCalculatorProviderBuilder().setProvider(PROVIDER_NAME).build()
                        ).build(sha256Signer, (java.security.cert.X509Certificate) chain[0])
                );
                gen.addCertificates(certs);
                // 生成签名数据
                CMSTypedData cmsData = new CMSProcessableByteArray(content.readAllBytes());
                CMSSignedData sigData = gen.generate(cmsData, false);
                return sigData.getEncoded();
            } catch (Exception e) {
                throw new IOException(e);
            }
        };
        SignatureOptions options = new SignatureOptions();
        document.addSignature(signature, signatureInterface, options);

        document.saveIncremental(outputStream);
    }

    /**
     * 验证PDF文件是否被签名
     * 如果keyStoreInputStream为null，则只做 CMS 签名值完整性验证，不做证书链校验
     * @param pdfFile PDF文件
     * @param allSignaturesValid 是否验证所有签名，true表示验证所有签名，false表示只验证第一个签名
     * @return 是否被签名
     */
    @SneakyThrows(Exception.class)
    public static List<PdfSignatureValidationResult> verify(@NonNull File pdfFile, boolean allSignaturesValid) {
        List<PdfSignatureValidationResult> resultList = new ArrayList<>();
        try (PDDocument document = Loader.loadPDF(new RandomAccessReadBufferedFile(pdfFile))) {
            List<PDSignature> pdSignatureList = document.getSignatureDictionaries();
            if (pdSignatureList == null || pdSignatureList.isEmpty()) {
                // 没有签名
                return resultList;
            }
            for (PDSignature pdSignature : pdSignatureList) {
                String signatureName = pdSignature.getName();
                Calendar signDate = pdSignature.getSignDate();
                String subFilter = pdSignature.getSubFilter();
                String reason = pdSignature.getReason();
                String location = pdSignature.getLocation();

                try (
                        InputStream pdfInputStream1 = new FileInputStream(pdfFile);
                        InputStream pdfInputStream2 = new FileInputStream(pdfFile)) {
                    boolean isValid = verifySignatureIntegrity(pdSignature, pdfInputStream1, pdfInputStream2, allSignaturesValid);
                    PdfSignatureValidationResult result = PdfSignatureValidationResult.builder()
                            .signatureName(signatureName)
                            .signDate(signDate.toInstant().atZone(ZoneId.systemDefault()))
                            .subFilter(subFilter)
                            .reason(reason)
                            .location(location)
                            .isValid(isValid)
                            .build();
                    resultList.add(result);
                }
            }
        }

        return resultList;
    }

    @SneakyThrows(Exception.class)
    public static List<PdfSignatureValidationResult> verify(@NonNull InputStream pdfInputStream, boolean allSignaturesValid) {
        List<PdfSignatureValidationResult> resultList = new ArrayList<>();

        byte[] pdfBytes = pdfInputStream.readAllBytes();
        try (
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pdfBytes);
                PDDocument document = Loader.loadPDF(new RandomAccessReadBuffer(byteArrayInputStream))) {
            List<PDSignature> pdSignatureList = document.getSignatureDictionaries();
            if (pdSignatureList == null || pdSignatureList.isEmpty()) {
                // 没有签名
                return resultList;
            }
            for (PDSignature pdSignature : pdSignatureList) {
                String signatureName = pdSignature.getName();
                Calendar signDate = pdSignature.getSignDate();
                String subFilter = pdSignature.getSubFilter();
                String reason = pdSignature.getReason();
                String location = pdSignature.getLocation();

                boolean isValid = verifySignatureIntegrity(pdSignature, new ByteArrayInputStream(pdfBytes), new ByteArrayInputStream(pdfBytes), allSignaturesValid);
                PdfSignatureValidationResult result = PdfSignatureValidationResult.builder()
                        .signatureName(signatureName)
                        .signDate(signDate.toInstant().atZone(ZoneId.systemDefault()))
                        .subFilter(subFilter)
                        .reason(reason)
                        .location(location)
                        .isValid(isValid)
                        .build();
                resultList.add(result);
            }
        }

        return resultList;
    }

    @SneakyThrows(Exception.class)
    private static boolean verifySignatureIntegrity(PDSignature pdSignature, InputStream signedContentInputStream, InputStream signatureInputStream, boolean allSignaturesValid) {
        // 1. 获取签名覆盖的原始字节内容
        byte[] signedContent = pdSignature.getSignedContent(signedContentInputStream);
        byte[] signatureBytes = pdSignature.getContents(signatureInputStream);

        CMSSignedData cms = new CMSSignedData(new CMSProcessableByteArray(signedContent), signatureBytes);
        SignerInformationStore signers = cms.getSignerInfos();
        Collection<SignerInformation> signerCollection = signers.getSigners();
        Store<X509CertificateHolder> certStore = cms.getCertificates();
        JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();

        for (SignerInformation signer : signerCollection) {
            // 获取证书列表
            Collection<X509CertificateHolder> certHolders = certStore.getMatches(signer.getSID());
            for (X509CertificateHolder holder : certHolders) {
                X509Certificate cert = certConverter.getCertificate(holder);
                boolean isValid = signer.verify(new JcaSimpleSignerInfoVerifierBuilder().build(cert));
                if (!allSignaturesValid && isValid) {
                    return true;
                }
                if (!isValid) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 向PDF文件添加签名图片
     * @param document PDF文档
     * @param imageData 签名图片数据
     */
    @SneakyThrows(IOException.class)
    private static void addSignatureImage(PDDocument document, PdfSignatureImageData imageData) {
        if (imageData == null) {
            return;
        }
        byte[] imageBytes = imageData.getImageStream().readAllBytes();
        if (imageData.getPage() > 0) {
            int pageIndex = imageData.getPage() -1;
            PDPage page = document.getPage(pageIndex);
            addSignatureImageToPage(document, page, imageData, imageBytes);
            return;
        }
        for (PDPage page : document.getPages()) {
            addSignatureImageToPage(document, page, imageData, imageBytes);
        }
    }

    /**
     * 向PDF页面添加签名图片
     * @param document PDF文档
     * @param page PDF页面
     * @param imageData 签名图片数据
     * @param imageBytes 签名图片字节数组
     */
    @SneakyThrows(IOException.class)
    private static void addSignatureImageToPage(PDDocument document, PDPage page, PdfSignatureImageData imageData, byte[] imageBytes) {
        PDImageXObject img = PDImageXObject.createFromByteArray(document, imageBytes, "seal");
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)) {
            contentStream.drawImage(
                    img,
                    imageData.getX(),
                    imageData.getY(),
                    imageData.getWidth(),
                    imageData.getHeight()
            );
        }
    }
}
