package com.rookiefly.commons.qrcode;

public class LogoImage {

    private String imgPath; //图片路径
    private int height; //图片高度
    private int width;  //图片宽度

    public LogoImage() {
    }

    public LogoImage(String imgPath, int width, int height) {
        this.imgPath = imgPath;
        this.width = width;
        this.height = height;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
