package com.integrasolusi;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Programmer   : pancara
 * Date         : Jan 7, 2011
 * Time         : 7:34:46 AM
 */
public class ScaledImageDemo {
    private static BufferedImage resize(BufferedImage image, int width, int height) {
        int type = image.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : image.getType();
        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(image, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }

    public static void main(String[] args) throws IOException {
        FileInputStream is = new FileInputStream("E:/pusda/data//source.jpg");
        JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(is);
        BufferedImage image = decoder.decodeAsBufferedImage();
        is.close();

        int w = (int) Math.ceil(image.getWidth() * 0.5);
        int h = (int) Math.ceil(image.getHeight() * 0.5);

        BufferedImage simpleImage = resize(image, w, h);
        FileOutputStream os = new FileOutputStream("E:/pusda/data/scaled_simple.jpg");
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
        encoder.encode(simpleImage);
        os.close();
    }
}
