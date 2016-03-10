package com.rookiefly.commons.imagetool;

import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.DataBuffer;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    MainFrame                 mainFrame;
    String                    imageFileName;
    BufferedImage             buffImage;
    BufferedImage             srcImage;

    public ImagePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (this.buffImage != null)
            g.drawImage(this.buffImage, 0, 0, null);
    }

    public void setCaptureImage(BufferedImage buff) {
        this.buffImage = (this.srcImage = buff);
    }

    public boolean imageCheck() {
        if (this.buffImage == null) {
            alert("还未载入图片");
            return false;
        }
        return true;
    }

    public void loadImage(String imageFileName) {
        if (imageFileName == null)
            return;
        try {
            this.imageFileName = imageFileName;
            BufferedImage tempImage = ImageIO.read(new File(imageFileName));
            if (tempImage == null) {
                alert("载入图片失败");
                return;
            }

            this.buffImage = Toolkit.transImage(tempImage);
            this.srcImage = this.buffImage;
            this.mainFrame.fitToImage(this.buffImage);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void saveImage() {
        if (!imageCheck()) {
            return;
        }
        this.imageFileName = Toolkit.imageSaveChooser(this.mainFrame);

        if (this.imageFileName == null) {
            return;
        }
        int buffType = 1;

        String format = null;
        String fileName = this.imageFileName.toLowerCase();
        if (fileName.endsWith(".gif")) {
            Iterator imageWriters = ImageIO.getImageWritersBySuffix("GIF");
            if (!imageWriters.hasNext()) {
                alert("不支持保存为GIF格式图片, 请换一种格式保存");
                return;
            }
            format = "gif";
        } else if (fileName.endsWith(".jpg")) {
            format = "jpg";
        } else if (fileName.endsWith(".jpeg")) {
            format = "jpeg";
        } else if (fileName.endsWith(".png")) {
            format = "png";
            buffType = 2;
        } else {
            alert("不支持的图像格式");
            return;
        }
        try {
            BufferedImage dest = new BufferedImage(this.buffImage.getWidth(), this.buffImage.getHeight(), buffType);
            dest.getGraphics().drawImage(this.buffImage, 0, 0, this.buffImage.getWidth(), this.buffImage.getHeight(),
                null);
            File file = new File(this.imageFileName);
            if (file.exists()) {
                file.delete();
            }
            ImageIO.write(dest, format, file);
            alert("保存图片成功 " + this.imageFileName);
        } catch (Exception e) {
            alert("保存图片时出错");
        }
    }

    public void alert(String info) {
        JOptionPane.showMessageDialog(this.mainFrame, info, "ImageTool", 1);
    }

    public void blur() {
        if (!imageCheck()) {
            return;
        }
        float weight = 0.1111111F;
        float[] elements = new float[9];
        for (int i = 0; i < 9; i++) {
            elements[i] = weight;
        }
        convoleOp(elements);
    }

    public void sharpen() {
        if (!imageCheck()) {
            return;
        }
        float[] elements = { 0.0F, -1.0F, 0.0F, -1.0F, 5.0F, -1.0F, 0.0F, -1.0F, 0.0F };
        convoleOp(elements);
    }

    public void edge() {
        if (!imageCheck()) {
            return;
        }
        float[] elements = { 0.0F, -1.0F, 0.0F, 1.0F, 4.0F, -1.0F, 0.0F, -1.0F, 0.0F };
        convoleOp(elements);
    }

    public void mirror(boolean isLeft) {
        if (!imageCheck()) {
            return;
        }
        int w = this.buffImage.getWidth();
        int h = this.buffImage.getHeight();
        BufferedImage filteredImage = new BufferedImage(w, h, this.buffImage.getType());
        DataBuffer dbDst = filteredImage.getRaster().getDataBuffer();
        DataBuffer dbSrc = this.buffImage.getRaster().getDataBuffer();
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (isLeft)
                    dbDst.setElem(i * w + j, dbSrc.getElem(i * w + (w - j - 1)));
                else {
                    dbDst.setElem(i * w + j, dbSrc.getElem((h - i - 1) * w + j));
                }
            }
        }
        this.buffImage = filteredImage;
        repaint();
    }

    public void rotate(int radius) {
        if (!imageCheck()) {
            return;
        }
        long t1 = System.nanoTime();
        AffineTransform transform = new AffineTransform();
        int w = this.buffImage.getWidth();
        int h = this.buffImage.getHeight();
        if (radius == 1) {
            w = h = Math.max(w, h);
        }
        transform.setToRotation(radius * 3.141592653589793D / 2.0D, w / 2, h / 2);
        AffineTransformOp op = new AffineTransformOp(transform, null);
        BufferedImage filteredImage = new BufferedImage(w, h, this.buffImage.getType());
        op.filter(this.buffImage, filteredImage);
        if ((radius == 1) && (this.buffImage.getHeight() != this.buffImage.getWidth())) {
            if (this.buffImage.getHeight() < this.buffImage.getWidth())
                this.buffImage = filteredImage.getSubimage(w - this.buffImage.getHeight(), 0,
                    this.buffImage.getHeight(), this.buffImage.getWidth());
            else if (this.buffImage.getHeight() > this.buffImage.getWidth())
                this.buffImage = filteredImage.getSubimage(0, 0, this.buffImage.getHeight(), this.buffImage.getWidth());
        } else {
            this.buffImage = filteredImage;
        }
        long t2 = System.nanoTime();
        System.out.println("speed " + (t2 - t1) + " ns");
        this.mainFrame.fitToImage(this.buffImage);
        repaint();
    }

    public void convoleOp(float[] elems) {
        Kernel kernel = new Kernel(3, 3, elems);
        ConvolveOp op = new ConvolveOp(kernel);
        BufferedImage filteredImage = new BufferedImage(this.buffImage.getWidth(), this.buffImage.getHeight(),
            this.buffImage.getType());
        op.filter(this.buffImage, filteredImage);
        this.buffImage = filteredImage;
        repaint();
    }

    public void reset() {
        this.buffImage = this.srcImage;
        repaint();
    }
}