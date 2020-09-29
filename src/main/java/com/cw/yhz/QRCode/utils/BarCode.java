package com.cw.yhz.QRCode.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * 功能描述：条形码帮助类
 */
public class BarCode {

    /**
     * 功能描述：条形码编码生成（针对图片文件）
     *
     * @param contents 条形码内容
     * @param width    宽度
     * @param height   高度
     * @param imgPath  生成的条形码存储 url
     */
    public void encode(String contents, int width, int height, String imgPath) {
        int codeWidth = 3 + (7 * 6) + 5 + (7 * 6) + 3;
        codeWidth = Math.max(codeWidth, width);
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,
                    BarcodeFormat.EAN_13, codeWidth, height, null);
            MatrixToImageWriter
                    .writeToFile(bitMatrix, "png", new File(imgPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能描述：条形码编码生成（针对 Base64）
     *
     * @param contents  条形码内容
     * @param width     宽度
     * @param height    高度
     * @return          条形码Base64字符串
     */
    public String encode(String contents, int width, int height) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();     //流操作对象
        BASE64Encoder base64 = new BASE64Encoder();                 //Base64操作对象

        int codeWidth = 3 + (7 * 6) + 5 + (7 * 6) + 3;
                codeWidth = Math.max(codeWidth, width);

        String base64Img = null;
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,
                    BarcodeFormat.EAN_13, codeWidth, height, null);
            MatrixToImageWriter.writeToStream(bitMatrix, "png", os);

            base64Img = base64.encode(os.toByteArray());     //从流中取出的图片，转base64编码
            os.close();     //关闭流
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return base64Img;
    }

    /**
     * 功能描述：条形码解码（针对图片文件）
     *
     * @param imgPath 需要被解码的条形码 url
     * @return 条形码内容
     */
    public String decode(String imgPath) {
        BufferedImage image = null;
        Result result = null;
        try {
            image = ImageIO.read(new File(imgPath));
            if (image == null) {
                System.out.println("the decode image may be not exit.");
            }
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            result = new MultiFormatReader().decode(bitmap, null);
            return result.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 功能描述：条形码解码（针对 Base64）
     *
     * @param base64String  条形码 base64 字符串
     * @return              条形码内容
     */
    public String decodes(String base64String) {
        BASE64Decoder decoder = new BASE64Decoder();
        Result result = null;
        try{
            byte[] bytes1 = decoder.decodeBuffer(base64String);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
            BufferedImage image = ImageIO.read(bais);
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            result = new MultiFormatReader().decode(bitmap, null);
            return result.getText();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 功能描述：根据图片文件生成、解析条形码
     *
     * @param args
     */
    public static void main(String[] args) {
        /** 编码 **/
        String imgPath = "/Users/ouhaolan/Applications/ProjectStorage/IdeaProject";
        // 益达无糖口香糖的条形码
        String contents = "6923450657713";


        int width = 105, height = 50;
        BarCode barCode = new BarCode();
        String base64Img = barCode.encode(contents, width, height);
        System.out.println("<img src='data:image/png;base64," + base64Img + "'/>");

        /** 解码 **/
        String decodeContent = barCode.decodes(base64Img);
        System.out.println("解码内容如下：");
        System.out.println(decodeContent);
    }
}
