package com.rookiefly.commons.imagetool;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;

public class Toolkit {
    private static final ClassLoader cl = Toolkit.class.getClassLoader();

    public static URL getRc(String url) {
        return cl.getResource(url);
    }

    public static ImageIcon getIconRc(String url) {
        return new ImageIcon(getRc(url));
    }

    public static Image getImageRc(String url) {
        return getIconRc(url).getImage();
    }

    public static BufferedImage getBuffRc(String url) {
        try {
            return ImageIO.read(getRc(url));
        } catch (IOException ex) {
        }
        return null;
    }

    public static BufferedImage transImage(Image image) {
        BufferedImage buffImage = new BufferedImage(image.getWidth(null), image.getHeight(null), 2);
        Graphics g = buffImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return buffImage;
    }

    public static BufferedImage transImage(BufferedImage buff) {
        BufferedImage buffImage = new BufferedImage(buff.getWidth(), buff.getHeight(), 2);
        Graphics2D g2D = buffImage.createGraphics();
        g2D.drawImage(buff, 0, 0, null);
        g2D.dispose();
        return buffImage;
    }

    public static String fileToString(String url) throws Exception {
        File file = new File(getRc(url).toURI());
        FileInputStream fis = new FileInputStream(file);
        byte[] b = new byte[(int) file.length()];
        fis.read(b);
        fis.close();
        String result = new String(b, "GBK");
        b = null;
        return result;
    }

    public static String fileToString2(String url) throws Exception {
        File file = new File(getRc(url).toURI());
        FileReader reader = new FileReader(file);
        StringWriter writer = new StringWriter();
        char[] buf = new char[1000];
        while (true) {
            int n = reader.read(buf, 0, 1000);
            if (n == -1) {
                break;
            }
            writer.write(buf, 0, n);
        }

        return writer.toString();

    }

    public static String imageSaveChooser(Component parent) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogType(1);
        chooser.addChoosableFileFilter(getImageFileFilter());

        int option = chooser.showOpenDialog(parent);
        if (option == 0) {
            File file = chooser.getSelectedFile();
            if (file != null) {
                return file.getAbsolutePath();
            }
        }
        return null;
    }

    public static String imageOpenChooser(Component parent) {
        JFileChooser chooser = new JFileChooser();
        chooser.addChoosableFileFilter(getImageFileFilter());

        int r = chooser.showOpenDialog(parent);
        if (r == 0) {
            return chooser.getSelectedFile().getAbsolutePath();
        }
        chooser = null;
        return null;
    }

    public static FileFilter getImageFileFilter() {
        return new FileFilter() {
            public boolean accept(File f) {
                String name = f.getName().toLowerCase();
                return (name.endsWith(".gif")) || (name.endsWith(".jpg")) || (name.endsWith(".jpeg"))
                        || (name.endsWith(".png")) || (f.isDirectory());
            }

            public String getDescription() {
                return "Image files (*.gif, *.jpg, *.jpeg, *.png)";
            }
        };
    }

    public static void sleep(long mms) {
        try {
            Thread.sleep(mms);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
