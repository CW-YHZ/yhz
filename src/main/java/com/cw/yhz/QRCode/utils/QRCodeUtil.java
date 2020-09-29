package com.cw.yhz.QRCode.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述：二维码帮助类
 */
public class QRCodeUtil {

    private static final int width = 300;       // 默认二维码宽度
    private static final int height = 300;      // 默认二维码高度
    private static final String format = "png"; // 默认二维码文件格式
    private static final Map<EncodeHintType, Object> hints = new HashMap();// 二维码参数

    static {
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");                   // 字符编码
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // 容错等级 L、M、Q、H 其中 L 为最低, H 为最高
        hints.put(EncodeHintType.MARGIN, 2);                                // 二维码与图片边距
    }

    /**
     * 功能描述：返回一个 BufferedImage 对象
     *
     * @param content 二维码内容
     * @param width   宽
     * @param height  高
     */
    public static BufferedImage toBufferedImage(String content, int width, int height) throws WriterException, IOException {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    /**
     * 功能描述：将二维码图片输出到一个流中
     *
     * @param content 二维码内容
     * @param stream  输出流
     * @param width   宽
     * @param height  高
     */
    public static void writeToStream(String content, OutputStream stream, int width, int height) throws WriterException, IOException {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        MatrixToImageWriter.writeToStream(bitMatrix, format, stream);
    }

    /**
     * 功能描述：生成二维码图片文件
     *
     * @param content 二维码内容
     * @param path    文件保存路径
     * @param width   宽
     * @param height  高
     */
    public static void createQRCode(String content, String path, int width, int height) throws WriterException, IOException {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        //toPath() 方法由 jdk1.7 及以上提供
        MatrixToImageWriter.writeToFile(bitMatrix, format, new File(path));
    }

    /**
     * 功能描述：返回一个 base64 二维码
     *
     * @param content 二维码内容
     * @param width   宽
     * @param height  高
     */
    public static String base64Img(String content, int width, int height) throws WriterException, IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();     //流操作对象
        BASE64Encoder base64 = new BASE64Encoder();                 //Base64操作对象

        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);

        //把二维码数据、图片格式、把流对象传递过去，该方法会将生成好的二维码储存在流中
        MatrixToImageWriter.writeToStream(bitMatrix, "png", os);

        String base64Img = base64.encode(os.toByteArray());     //从流中取出的图片，转base64编码
        os.close();     //关闭流
        return "data:image/png;base64," + base64Img;
    }


    /**
     * 功能描述：解析二维码
     *
     * @param image 读入的二维码图片
     * @return
     */
    public static String decodeQrCode(BufferedImage image) {
        String content = null;
        try {
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map<DecodeHintType, Object> hints = new HashMap<>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            Result result = new MultiFormatReader().decode(binaryBitmap, hints);    // 对图像进行解码
            content = result.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * 功能描述：解析二维码
     *
     * @param base64String 二维码Base64编码解析
     * @return
     */
    public static String base64StringToImage(String base64String) {
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] bytes1 = decoder.decodeBuffer(base64String);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
            BufferedImage bi1 = ImageIO.read(bais);
            return decodeQrCode(bi1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
