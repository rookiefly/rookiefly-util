package com.rookiefly.commons.qrcode;

import com.swetake.util.Qrcode;
import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.exception.DecodingFailedException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;

public class QrcodeUtil {
    private static final int SIZE = 12;
    private static final String IMG_TYPE = "png";
    private static final String LOGO_IMG = "https://avatars1.githubusercontent.com/u/3352212?s=460&v=4";
    private static QrcodeUtil qrcodeUtil = new QrcodeUtil();

    public static QrcodeUtil getInstance() {
        return qrcodeUtil;
    }

    /**
     * 生成二维码(QRCode)图片
     *
     * @param content 存储内容
     * @param imgPath 图片路径
     */
    public void encodeQRCode(String content, String imgPath) {
        this.encodeQRCode(content, imgPath, IMG_TYPE, SIZE);
    }

    /**
     * 生成二维码(QRCode)图片
     *
     * @param content 存储内容
     * @param output  输出流
     */
    public void encodeQRCode(String content, OutputStream output) {
        this.encodeQRCode(content, output, IMG_TYPE, SIZE);
    }

    /**
     * 生成二维码(QRCode)图片
     *
     * @param content 存储内容
     * @param imgPath 图片路径
     * @param imgType 图片类型
     */
    public void encodeQRCode(String content, String imgPath, String imgType) {
        this.encodeQRCode(content, imgPath, imgType, SIZE);
    }

    /**
     * 生成二维码(QRCode)图片
     *
     * @param content 存储内容
     * @param output  输出流
     * @param imgType 图片类型
     */
    public void encodeQRCode(String content, OutputStream output, String imgType) {
        this.encodeQRCode(content, output, imgType, SIZE);
    }

    /**
     * 生成二维码(QRCode)图片
     *
     * @param content 存储内容
     * @param imgPath 图片路径
     * @param imgType 图片类型
     * @param size    二维码尺寸
     */
    public void encodeQRCode(String content, String imgPath, String imgType, int size) {
        try {
            LogoImage logoImage = new LogoImage();
            logoImage.setImgPath(LOGO_IMG);
            logoImage.setHeight(49);
            logoImage.setWidth(49);
            BufferedImage bufImg = this.generateQRCodeImage(content, imgType, size, logoImage);

            File imgFile = new File(imgPath);
            String parent = imgFile.getParent();
            File parentFile = new File(parent);
            if (!parentFile.isDirectory()) {
                parentFile.mkdirs();
            }
            if (!imgFile.exists()) {
                imgFile.createNewFile();
            }
            // 生成二维码QRCode图片
            ImageIO.write(bufImg, imgType, imgFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成二维码图片流
     *
     * @param content
     * @param imgType
     * @param size
     * @return
     */
    public InputStream encodeQRCode(String content, String imgType, int size) {
        return this.encodeQRCodeHasLogo(content, imgType, size, null);
    }

    /**
     * 生成包含logo的二维码
     *
     * @param content
     * @param imgType
     * @param size
     * @param logoImage
     * @return
     */
    public InputStream encodeQRCodeHasLogo(String content, String imgType, int size, LogoImage logoImage) {
        InputStream is = null;
        BufferedImage bufImg = this.generateQRCodeImage(content, imgType, size, logoImage);
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        ImageOutputStream imOut = null;
        try {
            imOut = ImageIO.createImageOutputStream(bs);
            ImageIO.write(bufImg, imgType, imOut);
            is = new ByteArrayInputStream(bs.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.close(bs);
            this.close(imOut);
        }
        return is;
    }

    /**
     * 关闭输入输出流
     *
     * @param o
     */
    private void close(Object o) {
        try {
            if (o != null && o instanceof InputStream) {
                ((InputStream) o).close();
            }
            if (o != null && o instanceof OutputStream) {
                ((OutputStream) o).close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成二维码(QRCode)图片
     *
     * @param content 存储内容
     * @param output  输出流
     * @param imgType 图片类型
     * @param size    二维码尺寸
     */
    public void encodeQRCode(String content, OutputStream output, String imgType, int size) {
        try {
            LogoImage logoImage = new LogoImage();
            logoImage.setImgPath(LOGO_IMG);
            logoImage.setHeight(49);
            logoImage.setWidth(49);
            BufferedImage bufImg = this.generateQRCodeImage(content, imgType, size, logoImage);
            // 生成二维码QRCode图片
            ImageIO.write(bufImg, imgType, output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成二维码(QRCode)图片的公共方法
     *
     * @param content 存储内容
     * @param imgType 图片类型
     * @param size    二维码尺寸
     * @return
     */
    private BufferedImage generateQRCodeImage(String content, String imgType, int size, LogoImage logoImage) {
        BufferedImage bufImg = null;
        try {
            Qrcode qrcodeHandler = new Qrcode();
            // 设置二维码排错率，可选L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小
            qrcodeHandler.setQrcodeErrorCorrect('M');
            qrcodeHandler.setQrcodeEncodeMode('B');
            // 设置设置二维码尺寸，取值范围1-40，值越大尺寸越大，可存储的信息越大
            qrcodeHandler.setQrcodeVersion(size);
            // 获得内容的字节数组，设置编码格式
            byte[] contentBytes = content.getBytes(Charset.defaultCharset());
            // 图片尺寸
            int imgSize = 67 + 12 * (size - 1);
            bufImg = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_RGB);
            Graphics2D gs = bufImg.createGraphics();
            // 设置背景颜色
            gs.setBackground(Color.WHITE);
            gs.clearRect(0, 0, imgSize, imgSize);

            // 设定图像颜色> BLACK
            gs.setColor(Color.BLACK);
            // 设置偏移量，不设置可能导致解析出错
            int pixoff = 2;
            // 输出内容> 二维码
            if (contentBytes.length > 0 && contentBytes.length < 800) {
                boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
                for (int i = 0; i < codeOut.length; i++) {
                    for (int j = 0; j < codeOut.length; j++) {
                        if (codeOut[j][i]) {
                            gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
                        }
                    }
                }
            } else {
                throw new Exception("QRCode content bytes length = " + contentBytes.length + " not in [0, 800].");
            }

            if (logoImage != null && logoImage.getImgPath() != null && logoImage.getImgPath().length() > 0) {
                URL url = new URL(LOGO_IMG);
                Image img = ImageIO.read(url);
                //Image img = ImageIO.read(new File(logoImg));//实例化一个Image对象。
                gs.drawImage(img, imgSize / 2 - logoImage.getWidth() / 2, imgSize / 2 - logoImage.getHeight() / 2,
                        logoImage.getWidth(), logoImage.getHeight(), null);//将logo加到二维码中
            }
            gs.dispose();
            bufImg.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bufImg;
    }

    /**
     * 解析二维码（QRCode）
     *
     * @param imgPath 图片路径
     * @return
     */
    public String decodeQRCode(String imgPath) {
        // QRCode 二维码图片的文件
        File imageFile = new File(imgPath);
        BufferedImage bufImg = null;
        String content = null;
        try {
            bufImg = ImageIO.read(imageFile);
            QRCodeDecoder decoder = new QRCodeDecoder();
            content = new String(decoder.decode(new QrcodeImage(bufImg)), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DecodingFailedException dfe) {
            dfe.printStackTrace();
        }
        return content;
    }

    /**
     * 解析二维码（QRCode）
     *
     * @param input 输入流
     * @return
     */
    public String decodeQRCode(InputStream input) {
        String content = null;
        try {
            BufferedImage bufImg = ImageIO.read(input);
            QRCodeDecoder decoder = new QRCodeDecoder();
            content = new String(decoder.decode(new QrcodeImage(bufImg)), Charset.defaultCharset());
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } catch (DecodingFailedException dfe) {
            System.out.println("Error: " + dfe.getMessage());
            dfe.printStackTrace();
        }
        return content;
    }

    public static void main(String[] args) throws IOException {
        QrcodeUtil handler = new QrcodeUtil();
        String imgPath = "/Users/rookiefly/demo_qrcode.png";
        String encoderContent = "https://github.com/rookiefly";
        String imgType = "png";
        //handler.encoderQRCode(encoderContent, imgPath);
        LogoImage li = new LogoImage();
        li.setImgPath(LOGO_IMG);
        li.setHeight(49);
        li.setWidth(49);
        InputStream is = handler.encodeQRCodeHasLogo(encoderContent, imgType, 12, li);
        File file = new File(imgPath);
        OutputStream os = new FileOutputStream(file);
        int bytesRead;
        byte[] buffer = new byte[8192];
        while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        is.close();

        System.out.println("========encoder success");
/*
        String decoderContent = handler.decodeQRCode(imgPath);
        System.out.println("解析结果如下：");
        System.out.println(decoderContent);
        System.out.println("========decoder success!!!");*/
    }
}