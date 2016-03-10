package com.rookiefly.commons.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

/**
 * 
 * 
 * @author wanggaoxiang
 * @version $Id: MosaicUtil.java, v 0.1 2015年4月8日 下午5:47:01 wanggaoxiang<p>
 */
public class MosaicUtil {

    /**
     * 图片马赛克化
     * 
     * @param inputFile
     *            输入图片路径
     * @param outputFile
     *            输出图片路径
     * @param x
     *            绘制点的x坐标
     * @param y
     *            绘制点的y坐标
     * @param width
     *            马赛克区域宽度
     * @param height 
     *            马赛克区域高度
     * @param mosaicSize
     *            马赛克大小
     * @return
     * @throws Exception
     */
    public static boolean mosaic(String inputFile, String outputFile, int x, int y, int width, int height,
                                 int mosaicSize) throws Exception {
        // 获得源文件
        File file = new File(inputFile);
        File outFile = new File(outputFile);
        return mosaic(file, outFile, x, y, width, height, mosaicSize);
    }

    public static boolean mosaic(File inputFile, File outputFile, int x, int y, int width, int height, int mosaicSize)
                                                                                                                      throws Exception {
        if (!inputFile.exists()) {
            return false;
        }
        BufferedImage img = ImageIO.read(inputFile);
        return mosaic(img, outputFile, x, y, width, height, mosaicSize);
    }

    private static boolean mosaic(BufferedImage img, File outputFile, int x, int y, int width, int height,
                                  int mosaicSize) throws Exception {
        int yInitial = y;
        // 判断图片格式是否正确
        if (img == null) {
            return false;
        }
        int imageWidth = img.getWidth(null);
        int imageHeight = img.getHeight(null);
        // 判断马赛克大小是否超出图片范围
        if (mosaicSize <= 0 || mosaicSize > imageWidth || mosaicSize > imageHeight) {
            return false;
        }

        if (x + width > imageWidth || y + height > imageHeight) {
            return false;
        }

        int xCnt = 0;// 矩形绘制x方向个数
        int yCnt = 0;// 矩形绘制y方向个数
        if (width % mosaicSize == 0) {
            xCnt = width / mosaicSize;
        } else {
            xCnt = width / mosaicSize + 1;
        }
        if (height % mosaicSize == 0) {
            yCnt = height / mosaicSize;
        } else {
            yCnt = height / mosaicSize + 1;
        }

        // 绘制矩形并填充颜色
        Graphics gs = img.getGraphics();
        for (int i = 0; i < xCnt; i++) {
            for (int j = 0; j < yCnt; j++) {
                // 计算矩形宽高
                int mosaicWidth = mosaicSize;
                int mosaicHeight = mosaicSize;
                if (i == xCnt - 1) {
                    mosaicWidth = width - x;
                }
                if (j == yCnt - 1) {
                    mosaicHeight = height - y;
                }
                // 矩形颜色取中心像素点RGB值
                int centerX = x;
                int centerY = y;
                if (mosaicWidth % 2 == 0) {
                    centerX += mosaicWidth / 2;
                } else {
                    centerX += (mosaicWidth - 1) / 2;
                }
                if (mosaicHeight % 2 == 0) {
                    centerY += mosaicHeight / 2;
                } else {
                    centerY += (mosaicHeight - 1) / 2;
                }
                Color color = new Color(img.getRGB(centerX, centerY));
                gs.setColor(color);
                gs.fillRect(x, y, mosaicWidth, mosaicHeight);
                y = y + mosaicSize;// 计算下一个矩形的y坐标
            }
            y = yInitial;// 还原y坐标
            x = x + mosaicSize;// 计算x坐标
        }
        gs.dispose();

        // 输出图片
        FileOutputStream out = new FileOutputStream(outputFile);
        ImageIO.write(img, "JPG", out);
        out.close();
        return true;
    }
}
