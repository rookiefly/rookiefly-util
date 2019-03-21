package com.rookiefly.commons.imagetool;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class TipPanel extends JPanel implements MouseListener {
    private static final long serialVersionUID = 1L;
    static final Rectangle RECT_THIS = new Rectangle(5, 5, 150, 100);
    ScreenCapture captureFrame;
    JLabel lblEsc = new JLabel("双击选定,按ESC退出截屏");
    JLabel lblPixel = new JLabel();
    JLabel lblRect = new JLabel();
    JLabel lblColor = new JLabel();

    public TipPanel(ScreenCapture captureFrame) {
        this.captureFrame = captureFrame;

        setOpaque(true);
        setLayout(null);
        setBackground(new Color(223, 236, 245));
        setBounds(RECT_THIS);
        addMouseListener(this);

        this.lblEsc.setSize(134, 20);
        this.lblEsc.setForeground(Color.RED);
        this.lblEsc.setLocation((getWidth() - this.lblEsc.getWidth()) / 2, 5);
        add(this.lblEsc);

        this.lblPixel.setBounds(10, 30, 100, 20);
        add(this.lblPixel);

        this.lblRect.setBounds(10, 50, 100, 20);
        add(this.lblRect);

        this.lblColor.setBounds(10, 75, 60, 18);
        this.lblColor.setOpaque(true);
        this.lblColor.setBorder(new LineBorder(Color.GRAY));
        this.lblColor.setHorizontalAlignment(0);
        this.lblColor.setVerticalAlignment(0);
        add(this.lblColor);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.setComposite(AlphaComposite.getInstance(3, 0.93F));

        super.paintComponent(g);
    }

    public void convertPosition() {
        if (getX() == RECT_THIS.x) {
            setLocation(this.captureFrame.getWidth() - RECT_THIS.x - RECT_THIS.width, RECT_THIS.y);
            return;
        }
        if (getX() != RECT_THIS.x)
            setLocation(RECT_THIS.x, RECT_THIS.y);
    }

    public void reset() {
        setLocation(RECT_THIS.x, RECT_THIS.y);
        this.lblPixel.setText(null);
    }

    public void setTipLabel(MouseEvent evt) {
        Dimension dim = null;

        if (this.captureFrame.captureLabel.getWidth() != 0) {
            dim = this.captureFrame.captureLabel.getSize();
        }

        StringBuffer buf = new StringBuffer("坐标: ");
        Color color;
        if (evt.getSource() == this.captureFrame) {
            buf.append(evt.getX() + "," + evt.getY());
            color = this.captureFrame.robot.getPixelColor(evt.getX(), evt.getY());
        } else {
            JComponent comp = (JComponent) evt.getSource();
            buf.append(evt.getX() + comp.getX() + "," + (evt.getY() + comp.getY()));
            color = this.captureFrame.robot.getPixelColor(evt.getX() + comp.getX(), evt.getY() + comp.getY());
        }
        this.lblPixel.setText(buf.toString());
        if (dim != null) {
            this.lblRect.setText("区域: " + dim.width + "×" + dim.height);
        }
        this.lblColor.setBackground(color);
        this.lblColor.setText(("<html><span style='font-family:Verdana'>" + (color.getRed() < 16 ? "0" : "")
                + Integer.toString(color.getRed(), 16) + (color.getGreen() < 16 ? "0" : "")
                + Integer.toString(color.getGreen(), 16) + (color.getBlue() < 16 ? "0" : "") + Integer
                .toString(color.getBlue(), 16)).toUpperCase());
    }

    public void mouseEntered(MouseEvent evt) {
        convertPosition();
    }

    public void mouseClicked(MouseEvent evt) {
    }

    public void mousePressed(MouseEvent evt) {
    }

    public void mouseReleased(MouseEvent evt) {
    }

    public void mouseExited(MouseEvent evt) {
    }
}