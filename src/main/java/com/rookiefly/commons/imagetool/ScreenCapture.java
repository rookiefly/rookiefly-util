package com.rookiefly.commons.imagetool;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class ScreenCapture extends JFrame implements MouseListener, MouseMotionListener {
    private static final long serialVersionUID = 1L;
    public static final ScreenCapture capture = new ScreenCapture();
    JPanel image_panel;
    Dimension screen_size;
    ScreenCaptureListener mainListener;
    Image background_image = null;
    Robot robot;
    TipPanel tipPanel = new TipPanel(this);
    JLabel captureLabel = new JLabel();
    JLabel[] posLabel = new JLabel[8];

    int mouseFlag = 0;
    int posLabelIndex;
    Point startPt;
    Point endPt;

    public static ScreenCapture getInstance(ScreenCaptureListener mainListener) {
        capture.mainListener = mainListener;
        return capture;
    }

    ScreenCapture() {
        this.screen_size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == 27) {
                    ScreenCapture.this.getSelf().setVisible(false);
                    ScreenCapture.this.getSelf().mainListener.captureImageCreated(null);
                }
            }
        });
        addMouseListener(this);
        addMouseMotionListener(this);
        this.captureLabel.addMouseListener(this);
        this.captureLabel.addMouseMotionListener(this);
        setUndecorated(true);
        setSize(this.screen_size.width, this.screen_size.height);
        setCursor(new Cursor(1));

        this.image_panel = new JPanel() {
            private static final long serialVersionUID = 1L;

            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(ScreenCapture.this.background_image, 0, 0, null);
            }
        };
        this.image_panel.setPreferredSize(this.screen_size);
        this.image_panel.setLayout(null);
        getContentPane().add(this.image_panel);

        Color bgcolor = new Color(40, 89, 108);
        this.captureLabel.setCursor(new Cursor(13));
        this.captureLabel.setOpaque(false);
        this.captureLabel.setBorder(new LineBorder(bgcolor));
        this.captureLabel.setSize(0, 0);

        for (int i = 0; i < this.posLabel.length; i++) {
            this.posLabel[i] = new JLabel();
            this.posLabel[i].setOpaque(true);
            this.posLabel[i].setBackground(bgcolor);
            this.posLabel[i].setSize(5, 5);
            this.posLabel[i].setVisible(false);
            this.posLabel[i].addMouseListener(this);
            this.posLabel[i].addMouseMotionListener(this);
            this.image_panel.add(this.posLabel[i]);
        }
        setPosLabelCursor();

        this.image_panel.add(this.captureLabel);
        this.image_panel.add(this.tipPanel);
    }

    @SuppressWarnings("deprecation")
    public void show() {
        try {
            Rectangle rect = new Rectangle(0, 0, (int) this.screen_size.getWidth(), (int) this.screen_size.getHeight());

            this.robot = new Robot();
            this.background_image = this.robot.createScreenCapture(rect);
            this.image_panel.repaint();
            super.show();
        } catch (AWTException ex) {
            System.out.println("exception creating screenshot:");
            ex.printStackTrace();
        }
    }

    public ScreenCapture getSelf() {
        return this;
    }

    public void mousePressed(MouseEvent evt) {
        Object source = evt.getSource();
        if (evt.getButton() == 1) {
            this.startPt = evt.getPoint();

            for (int i = 0; i < this.posLabel.length; i++) {
                if (source == this.posLabel[i]) {
                    this.posLabelIndex = i;
                    this.mouseFlag = 3;
                    this.endPt = this.startPt;
                    return;
                }
            }

            if (source == this.captureLabel) {
                this.mouseFlag = 2;
                this.endPt = this.startPt;
            } else {
                this.mouseFlag = 1;
                for (JLabel label : this.posLabel)
                    label.setVisible(true);
            }
        }
    }

    public void mouseClicked(MouseEvent evt) {
        if ((evt.getSource() == this.captureLabel) && (evt.getClickCount() == 2)) {
            getSelf().getCapture();
            for (JLabel label : this.posLabel)
                label.setVisible(false);
        }
    }

    public void mouseReleased(MouseEvent evt) {
        this.mouseFlag = 0;
    }

    public void mouseDragged(MouseEvent evt) {
        if (this.mouseFlag == 1) {
            this.endPt = evt.getPoint();
            setLabelSize();
        } else if (this.mouseFlag == 2) {
            this.endPt = evt.getPoint();
            setLabelLocation();
        } else if (this.mouseFlag == 3) {
            this.endPt = evt.getPoint();
            int x = this.captureLabel.getX();
            int y = this.captureLabel.getY();
            int w = this.captureLabel.getWidth();
            int h = this.captureLabel.getHeight();

            if (this.posLabelIndex == 0) {
                this.captureLabel.setLocation(x + this.endPt.x - this.startPt.x, y + this.endPt.y - this.startPt.y);
                this.captureLabel.setSize(w - this.endPt.x + this.startPt.x, h - this.endPt.y + this.startPt.y);
            } else if (this.posLabelIndex == 1) {
                this.captureLabel.setLocation(x + this.endPt.x - this.startPt.x, y);
                this.captureLabel.setSize(w - this.endPt.x + this.startPt.x, h);
            } else if (this.posLabelIndex == 2) {
                this.captureLabel.setLocation(x + this.endPt.x - this.startPt.x, y);
                this.captureLabel.setSize(w - this.endPt.x + this.startPt.x, h + this.endPt.y - this.startPt.y);
            } else if (this.posLabelIndex == 3) {
                this.captureLabel.setLocation(x, y);
                this.captureLabel.setSize(w, h + this.endPt.y - this.startPt.y);
            } else if (this.posLabelIndex == 4) {
                this.captureLabel.setLocation(x, y);
                this.captureLabel.setSize(w + this.endPt.x - this.startPt.x, h + this.endPt.y - this.startPt.y);
            } else if (this.posLabelIndex == 5) {
                this.captureLabel.setLocation(x, y);
                this.captureLabel.setSize(w + this.endPt.x - this.startPt.x, h);
            } else if (this.posLabelIndex == 6) {
                this.captureLabel.setLocation(x, y + this.endPt.y - this.startPt.y);
                this.captureLabel.setSize(w + this.endPt.x - this.startPt.x, h - this.endPt.y + this.startPt.y);
            } else if (this.posLabelIndex == 7) {
                this.captureLabel.setLocation(x, y + this.endPt.y - this.startPt.y);
                this.captureLabel.setSize(w, h - this.endPt.y + this.startPt.y);
            }

            if (this.captureLabel.getWidth() < 10) {
                this.captureLabel.setLocation(x, this.captureLabel.getY());
                this.captureLabel.setSize(w, this.captureLabel.getHeight());
            }
            if (this.captureLabel.getHeight() < 10) {
                this.captureLabel.setLocation(this.captureLabel.getX(), y);
                this.captureLabel.setSize(this.captureLabel.getWidth(), h);
            }

            setPosLabelLocation();
        }

        this.tipPanel.setTipLabel(evt);
    }

    public void setLabelSize() {
        this.captureLabel.setLocation(Math.min(this.startPt.x, this.endPt.x), Math.min(this.startPt.y, this.endPt.y));
        this.captureLabel.setSize(Math.abs(this.startPt.x - this.endPt.x), Math.abs(this.startPt.y - this.endPt.y));
        setPosLabelLocation();
    }

    public void setLabelLocation() {
        Point p = this.captureLabel.getLocation();
        this.captureLabel.setLocation(p.x + this.endPt.x - this.startPt.x, p.y + this.endPt.y - this.startPt.y);
        setPosLabelLocation();
    }

    public void setPosLabelLocation() {
        int x = this.captureLabel.getX();
        int y = this.captureLabel.getY();
        int w = this.captureLabel.getWidth();
        int h = this.captureLabel.getHeight();
        this.posLabel[0].setLocation(x - 2, y - 2);
        this.posLabel[1].setLocation(x - 2, y - 2 + h / 2);
        this.posLabel[2].setLocation(x - 2, y - 3 + h);
        this.posLabel[3].setLocation(x - 2 + w / 2, y - 3 + h);
        this.posLabel[4].setLocation(x - 3 + w, y - 3 + h);
        this.posLabel[5].setLocation(x - 3 + w, y - 2 + h / 2);
        this.posLabel[6].setLocation(x - 3 + w, y - 2);
        this.posLabel[7].setLocation(x - 2 + w / 2, y - 2);
    }

    public void setPosLabelCursor() {
        this.posLabel[0].setCursor(Cursor.getPredefinedCursor(6));
        this.posLabel[1].setCursor(Cursor.getPredefinedCursor(10));
        this.posLabel[2].setCursor(Cursor.getPredefinedCursor(4));
        this.posLabel[3].setCursor(Cursor.getPredefinedCursor(9));
        this.posLabel[4].setCursor(Cursor.getPredefinedCursor(6));
        this.posLabel[5].setCursor(Cursor.getPredefinedCursor(11));
        this.posLabel[6].setCursor(Cursor.getPredefinedCursor(4));
        this.posLabel[7].setCursor(Cursor.getPredefinedCursor(8));
    }

    public void getCapture() {
        setVisible(false);
        Toolkit.sleep(200L);
        this.mainListener.captureImageCreated(this.robot.createScreenCapture(this.captureLabel.getBounds()));

        this.tipPanel.reset();
        this.captureLabel.setSize(0, 0);
    }

    public void mouseEntered(MouseEvent evt) {
    }

    public void mouseExited(MouseEvent evt) {
    }

    public void mouseMoved(MouseEvent evt) {
        this.tipPanel.setTipLabel(evt);
    }
}