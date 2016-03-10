package com.rookiefly.commons.imagetool;

import java.awt.image.BufferedImage;

public abstract interface ScreenCaptureListener {
    public abstract void captureImageCreated(BufferedImage paramBufferedImage);
}
