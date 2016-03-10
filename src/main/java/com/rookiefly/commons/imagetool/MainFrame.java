package com.rookiefly.commons.imagetool;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;

public class MainFrame extends JFrame implements ActionListener, ScreenCaptureListener {

    private static final long serialVersionUID = 1L;
    public static final int   MIN_WIDTH        = 500;
    public static final int   MIN_HEIGHT       = 500;
    public ScreenCapture      captureFrame;
    ImagePanel                panel            = new ImagePanel(this);
    JMenuItem                 openItem;
    JMenuItem                 saveItem;
    JMenuItem                 exitItem;
    JMenuItem                 blurItem;
    JMenuItem                 sharpenItem;
    JMenuItem                 edgeItem;
    JMenuItem                 leftMirrorItem;
    JMenuItem                 topMirrorItem;
    JMenuItem                 rotateItem1;
    JMenuItem                 rotateItem2;
    JMenuItem                 resetItem;
    JMenuItem                 clearItem;
    JMenuItem                 captureItem;

    public MainFrame(String title) {
        super(title);

        initMenu();

        setIconImage(Toolkit.getImageRc("title.jpg"));
        setContentPane(this.panel);
        this.panel.setBackground(Color.WHITE);

        setBounds(200, 100, 500, 500);

        setDefaultCloseOperation(3);
        setVisible(true);
    }

    public void initMenu() {
        JMenuBar mb = new JMenuBar();
        setJMenuBar(mb);
        JMenu fileMenu = new JMenu("图片");
        mb.add(fileMenu);
        this.openItem = new JMenuItem("打 开");
        this.saveItem = new JMenuItem("保 存");
        this.exitItem = new JMenuItem("退 出");
        fileMenu.add(this.openItem);
        fileMenu.add(this.saveItem);
        fileMenu.addSeparator();
        fileMenu.add(this.exitItem);

        JMenu operMenu = new JMenu("操作");
        mb.add(operMenu);

        this.blurItem = new JMenuItem("模 糊");
        this.sharpenItem = new JMenuItem("锐 化");
        this.edgeItem = new JMenuItem("边缘化");
        JMenu mirrorMenu = new JMenu("镜 象");
        this.leftMirrorItem = new JMenuItem("左右翻转");
        this.topMirrorItem = new JMenuItem("上下翻转");
        mirrorMenu.add(this.leftMirrorItem);
        mirrorMenu.add(this.topMirrorItem);
        JMenu rotateMenu = new JMenu("旋 转");
        this.rotateItem1 = new JMenuItem("旋转90°");
        this.rotateItem2 = new JMenuItem("旋转180°");
        rotateMenu.add(this.rotateItem1);
        rotateMenu.add(this.rotateItem2);
        this.resetItem = new JMenuItem("还 原");

        operMenu.add(this.blurItem);
        operMenu.add(this.sharpenItem);
        operMenu.add(this.edgeItem);
        operMenu.add(mirrorMenu);
        operMenu.add(rotateMenu);
        operMenu.addSeparator();
        operMenu.add(this.resetItem);

        JMenu captureMenu = new JMenu("工具");
        mb.add(captureMenu);

        this.captureItem = new JMenuItem("截 屏");
        this.clearItem = new JMenuItem("清 屏");
        captureMenu.add(this.captureItem);
        captureMenu.addSeparator();
        captureMenu.add(this.clearItem);

        this.openItem.addActionListener(this);
        this.saveItem.addActionListener(this);
        this.exitItem.addActionListener(this);

        this.blurItem.addActionListener(this);
        this.sharpenItem.addActionListener(this);
        this.edgeItem.addActionListener(this);
        this.leftMirrorItem.addActionListener(this);
        this.topMirrorItem.addActionListener(this);
        this.rotateItem1.addActionListener(this);
        this.rotateItem2.addActionListener(this);
        this.resetItem.addActionListener(this);

        this.captureItem.addActionListener(this);
        this.clearItem.addActionListener(this);
    }

    public void captureImageCreated(BufferedImage buff) {
        if (buff != null) {
            this.panel.setCaptureImage(buff);
            fitToImage(buff);
            return;
        }
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == this.openItem) {
            this.panel.loadImage(Toolkit.imageOpenChooser(this));
        } else if (source == this.saveItem) {
            this.panel.saveImage();
        } else if (source == this.exitItem) {
            System.exit(0);
        } else if (source == this.blurItem) {
            this.panel.blur();
        } else if (source == this.sharpenItem) {
            this.panel.sharpen();
        } else if (source == this.edgeItem) {
            this.panel.edge();
        } else if (source == this.leftMirrorItem) {
            this.panel.mirror(true);
        } else if (source == this.topMirrorItem) {
            this.panel.mirror(false);
        } else if (source == this.rotateItem1) {
            this.panel.rotate(1);
        } else if (source == this.rotateItem2) {
            this.panel.rotate(2);
        } else if (source == this.resetItem) {
            this.panel.reset();
        } else if (source == this.captureItem) {
            setVisible(false);
            Toolkit.sleep(100L);
            this.captureFrame.show();
        } else if (source == this.clearItem) {
            this.panel.buffImage = null;
            this.panel.repaint();
        }
    }

    public void fitToImage(Image image) {
        int w = image.getWidth(null) < 500 ? 500 : image.getWidth(null);
        int h = image.getHeight(null) < 500 ? 500 : image.getHeight(null);
        setSize(w, h);
        setVisible(true);
    }

    public void setCaptureFrame(ScreenCapture captureFrame) {
        this.captureFrame = captureFrame;
    }

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(LookAndFeel.WINDOWS_LOOKANDFEEL);
        MainFrame mainFrame = new MainFrame("Image Tools demo");
        mainFrame.setCaptureFrame(ScreenCapture.getInstance(mainFrame));
    }
}