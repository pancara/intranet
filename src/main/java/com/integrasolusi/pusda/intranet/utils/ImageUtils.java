package com.integrasolusi.pusda.intranet.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Programmer   : pancara
 * Date         : Jan 6, 2011
 * Time         : 8:55:53 PM
 */
public final class ImageUtils {
    private static ImageUtils instance = new ImageUtils();

    private ImageUtils() {
    }

    public static ImageUtils getInstance() {
        return instance;
    }

    public static Icon getIcon(String filename) {
        Image image = getImage(filename);
        return new ImageIcon(image);
    }

    public static Image getImage(String filename) {
        InputStream is = instance.getClass().getResourceAsStream(filename);
        return getImage(is);
    }

    public static Image getImage(File file) {
        try {
            InputStream is = new FileInputStream(file);
            return getImage(is);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Image getImage(InputStream is) {
        if (is != null) {
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                int ch;
                while ((ch = bis.read()) != -1) {
                    baos.write(ch);
                }
                return Toolkit.getDefaultToolkit().createImage(baos.toByteArray());
            } catch (IOException exception) {
                throw new RuntimeException("Error loading image");
            }
        }
        throw new NullPointerException("stream is null");
    }

    public static Icon getIconFromFile(String filename) {
        Image image = getImageFromFile(filename);
        return new ImageIcon(image);
    }


    public static Icon getIconFromFile(File file) {
        return new ImageIcon(getImage(file));
    }

    public static Image getImageFromFile(String filename) {
        return getImage(new File(filename));
    }

    public static Image getImageFromByteArray(byte[] bytes) {
        return Toolkit.getDefaultToolkit().createImage(bytes);
    }

    public static BufferedImage resize(BufferedImage originalImage, int scaledWidth, int scaledHeight, boolean preserveAlpha) {
        int width = 0;
        int height = 0;
        if (scaledWidth < 0 && scaledHeight < 0) {
            width = originalImage.getWidth();
            height = originalImage.getHeight();
        } else {
            if (height < 0)
                height = (int) Math.ceil((double) originalImage.getHeight() * (double) width / (double) originalImage.getWidth());
            if (width < 0)
                width = (int) Math.ceil((double) originalImage.getWidth() * (double) height / (double) originalImage.getHeight());
        }

        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage scaledImage = new BufferedImage(width, height, imageType);
        Graphics2D g = scaledImage.createGraphics();
        if (preserveAlpha) {
            g.setComposite(AlphaComposite.Src);
        }
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return scaledImage;
    }

    public static BufferedImage resize(BufferedImage image, int width, int height) {
        // calc size
        if (width < 0 && height < 0) {
            width = image.getWidth();
            height = image.getHeight();
        } else {
            if (height < 0)
                height = (int) Math.ceil((double) image.getHeight() * (double) width / (double) image.getWidth());
            if (width < 0)
                width = (int) Math.ceil((double) image.getWidth() * (double) height / (double) image.getHeight());
        }

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

}