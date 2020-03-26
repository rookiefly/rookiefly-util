package com.rookiefly.test.commons.ocr;

import net.sourceforge.tess4j.Tesseract;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class OCRTest {
    /**
     * @param srImage 图片路径
     * @param ZH_CN   是否使用中文训练库,true-是
     * @return 识别结果
     */
    public static String findOCR(String srImage, boolean ZH_CN) {
        try {
            double start = System.currentTimeMillis();
            InputStream inputStream = ClassLoader.getSystemResourceAsStream(srImage);
            if (inputStream == null) {
                return "图片不存在";
            }
            BufferedImage textImage = ImageIO.read(inputStream);
            Tesseract instance = new Tesseract();
            //设置训练库
            instance.setDatapath("C:\\Program Files (x86)\\Tesseract-OCR\\tessdata");
            if (ZH_CN) {
                //中文识别
                instance.setLanguage("chi_sim");
            }
            String result = instance.doOCR(textImage);
            double end = System.currentTimeMillis();
            System.out.println("耗时" + (end - start) / 1000 + " s");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "发生错误";
        }
    }

    public static void main(String[] args) {
        String result = findOCR("test1.png", false);
        System.out.println(result);
    }
}