package com.rookiefly.commons.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * zxing QRCode generator util
 */
public class QRCodeGenerator {
    private static final String QR_CODE_IMAGE_PATH = "/Users/rookiefly/qrcode.png";

    private static final String QR_CODE_IMAGE_LOGO_PATH = "/Users/rookiefly/qrcode_logo.png";

    private static final String LOGO_IMG = "https://avatars1.githubusercontent.com/u/3352212?s=460&v=4";

    /**
     * @param text
     * @param width
     * @param height
     * @param filePath
     * @throws WriterException
     * @throws IOException
     */
    private static void generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    /**
     * @param text
     * @param width
     * @param height
     * @param filePath
     * @throws WriterException
     * @throws IOException     1. Create a map of hints and  pass them to the writer to create a QRCode with error correction set to level H (30%)
     *                         2. Instantiate a new image to use as a overlay
     *                         3. Calculate the width and height diff between the overlay image and the QRCode. These values are used in order to align the overlay image in the center of the QRCode
     *                         4. Here we set the composite before the overlay is drawn. This allows us to set the transparency (Alpha value) and how the overlay is to be rendered on top of the QRCode. See this page for more info
     */
    private static void generateQRCodeImageWithLogo(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        QRCodeWriter qrWriter = new QRCodeWriter();
        BitMatrix matrix = qrWriter.encode(text,
                BarcodeFormat.QR_CODE,
                width,
                height,
                hints);

        BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
        //BufferedImage overlay = ImageIO.read(new File("wifi-logo.gif"));
        BufferedImage overlay = ImageIO.read(new URL(LOGO_IMG));

        int overlayHeight = 49;
        int overlayWidth = 49;

        //Calculate the delta height and width
        int deltaHeight = image.getHeight() - overlayHeight;
        int deltaWidth = image.getWidth() - overlayWidth;

        //Draw the new image
        BufferedImage combined = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) combined.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        g.drawImage(overlay, Math.round(deltaWidth / 2), Math.round(deltaHeight / 2), overlayWidth, overlayHeight, null);

        File imageFile = new File(QR_CODE_IMAGE_LOGO_PATH);

        ImageIO.write(combined, "PNG", imageFile);
    }

    public static void main(String[] args) {
        try {
            generateQRCodeImage("https://github.com/rookiefly", 350, 350, QR_CODE_IMAGE_PATH);
            generateQRCodeImageWithLogo("https://github.com/rookiefly", 350, 350, QR_CODE_IMAGE_LOGO_PATH);
        } catch (WriterException e) {
            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
        }
    }
}