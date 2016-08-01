package com.cooker.util;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by thinhly on 7/15/16.
 */
public class ImageScaling {
    private final static Logger logger = LoggerFactory.getLogger(ImageScaling.class);

    private static final int IMG_TINY_WIDTH = 100;
    private static final int IMG_TINY_HEIGHT = 100;
    private static final int IMG_SMALL_WIDTH = 100;
    private static final int IMG_SMALL_HEIGHT = 100;
    private static final int IMG_MEDIUM_WIDTH = 100;
    private static final int IMG_MEDIUM_HEIGHT = 100;
    private static final int IMG_LARGE_WIDTH = 100;
    private static final int IMG_LARGE_HEIGHT = 100;

    private static final String IMG_FILE_FORMAT = ".png";
    private static final String IMG_FILE_PATH = "";

    public static String resizeTinyImage(BufferedImage originalImage, String regconizeText){
        return resizeImageWithHint(originalImage, IMG_TINY_WIDTH, IMG_TINY_HEIGHT, regconizeText);
    }

    public static String resizeSmallImage(BufferedImage originalImage, String regconizeText){
        return resizeImageWithHint(originalImage, IMG_SMALL_WIDTH, IMG_SMALL_HEIGHT, regconizeText);
    }

    public static String resizeMediumImage(BufferedImage originalImage, String regconizeText){
        return resizeImageWithHint(originalImage, IMG_MEDIUM_WIDTH, IMG_MEDIUM_HEIGHT, regconizeText);
    }

    public static String resizeLargeImage(BufferedImage originalImage, String regconizeText){
        return resizeImageWithHint(originalImage, IMG_LARGE_WIDTH, IMG_LARGE_HEIGHT, regconizeText);
    }

    private static String resizeImage(BufferedImage originalImage, int width, int height, String regconizeText){
        int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        String filename = IMG_FILE_PATH + MD5.getMD5(String.valueOf(System.currentTimeMillis()))
                            + regconizeText + IMG_FILE_FORMAT;
        try {
            ImageIO.write(resizedImage, IMG_FILE_FORMAT, new File(filename));
        } catch (IOException e) {
            logger.error(e.toString());
            return "";
        }
        return filename;
    }

    private static String resizeImageWithHint(BufferedImage originalImage, int width, int height, String regconizeText){
        int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        g.setComposite(AlphaComposite.Src);

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        String filename = IMG_FILE_PATH + MD5.getMD5(String.valueOf(System.currentTimeMillis()))
                + regconizeText + IMG_FILE_FORMAT;
        try {
            ImageIO.write(resizedImage, IMG_FILE_FORMAT, new File(filename));
        } catch (IOException e) {
            logger.error(e.toString());
            return "";
        }
        return filename;
    }
}
