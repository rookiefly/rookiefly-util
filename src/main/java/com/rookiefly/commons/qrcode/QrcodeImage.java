package com.rookiefly.commons.qrcode;

import java.awt.image.BufferedImage;

import jp.sourceforge.qrcode.data.QRCodeImage;

public class QrcodeImage implements QRCodeImage {

    private BufferedImage bufferedImage;

    public QrcodeImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public int getHeight() {
        return bufferedImage.getHeight();
    }

    public int getPixel(int x, int y) {
        return bufferedImage.getRGB(x, y);
    }

    public int getWidth() {
        return bufferedImage.getWidth();
    }

}