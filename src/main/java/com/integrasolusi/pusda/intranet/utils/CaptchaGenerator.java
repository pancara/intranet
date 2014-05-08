package com.integrasolusi.pusda.intranet.utils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.springframework.core.io.ClassPathResource;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * Programmer   : pancara
 * Date         : Jan 4, 2011
 * Time         : 3:49:45 PM
 */
public class CaptchaGenerator {
    private String path;
    private String base;
    private String type;

    private BufferedImage createCaptchaImage(String captchaText, File background) throws IOException {
        FileInputStream fis = new FileInputStream(background);
        JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(fis);
        BufferedImage image = decoder.decodeAsBufferedImage();
        fis.close();

        Graphics2D g = image.createGraphics();
        g.setTransform(new AffineTransform());
        g.setColor(Color.white);

        Font font = new Font("Verdana", Font.BOLD, 26);
        g.setFont(font);

        FontRenderContext frc = g.getFontRenderContext();
        Rectangle2D textBounds = g.getFont().getStringBounds(captchaText, frc);

        int x = (int) (image.getWidth() - textBounds.getWidth()) / 2;
        int y = (int) textBounds.getHeight();
        g.drawString(captchaText, x, y);
        return image;
    }

    public void generateCaptchaImage(String captchaText, OutputStream outputStream) {
        try {
            File file = getImageFile();
            BufferedImage image = createCaptchaImage(captchaText, file);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(outputStream);
            encoder.encode(image);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getImageFile() throws IOException {
        Random random = new Random(System.currentTimeMillis());
        try {
            int index = random.nextInt(3);
            String filename = String.format("%s/%s-%d.%s", path, base, index, type);
            ClassPathResource resource = new ClassPathResource(filename);
            return resource.getFile();
        } catch (IOException e) {
            String filename = String.format("%s/%s.%s", path, base, type);
            ClassPathResource resource = new ClassPathResource(filename);
            return resource.getFile();

        }
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public void setType(String type) {
        this.type = type;
    }
}
