package com.cw.yhz.QRCode.controller;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.ouhl.utildemo.QRCode.utils.QRCodeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

@Controller
@RequestMapping("/qrcode")
public class QRCodeController {

    private final int width = 600, height = 600;            //默认宽度，高度
    private final String url = "https://www.baidu.com";     //默认二维码路径

    /**
     * 功能描述：生成二维码
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("getQrcode")
    public Object getQRCode() {
        try {
            return "<img src='" + QRCodeUtil.base64Img(url, width, height) + "'/>";
        } catch (Exception e) {
            return "出错了";
        }
    }

    /**
     * 功能描述：解析二维码
     * @param file
     */
    public void analysisCode(File file) {
        try {
            MultiFormatReader formatReader = new MultiFormatReader();
            BufferedImage image = ImageIO.read(file);

            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                    new BufferedImageLuminanceSource(image)));

            HashMap hints = new HashMap();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");// 字符集

            Result result = formatReader.decode(binaryBitmap, hints);

            System.out.println("解析之后的结果:" + result.toString());
            System.out.println("二维码格式类型:" + result.getBarcodeFormat());
            System.out.println("二维码文本内容:" + result.getText());
        } catch (Exception e) {

        }

    }
}
