package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class MyGraphicsConverter implements TextGraphicsConverter {
    private int maxWidth = 0;
    private int maxHeight = 0;
    private double maxRatio = 0;
    private TextColorSchema schema = new MyColorShema();


    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));

        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();

        int newWidth;
        int newHeight;

        if (maxRatio != 0) checkingRatio(img);

        // если установлены высота и ширина
        if (!(maxWidth == 0 & maxHeight == 0)) {
            double widthRatio = (double) imgWidth / maxWidth;
            double heightRatio = (double) imgHeight / maxWidth;

            if (widthRatio > heightRatio) {
                newWidth = (int) (imgWidth / widthRatio);
                newHeight = (int) (imgHeight / widthRatio);
            } else {
                newWidth = (int) (imgWidth / heightRatio);
                newHeight = (int) (imgHeight / heightRatio);
            }

        // если установлена только ширина
        } else if (maxWidth != 0) {
            double widthRatio = (double)  imgWidth / maxWidth;
            newWidth = (int) (imgWidth / widthRatio);
            newHeight = (int) (imgHeight / widthRatio);

        // если установлена только длина
        } else if (maxHeight != 0) {
            double heightRatio = (double)  imgHeight / maxHeight;
            newWidth = (int) (imgWidth / heightRatio);
            newHeight = (int) (imgHeight / heightRatio);

        // не установлено ничего
        } else {
            newWidth = imgWidth;
            newHeight = imgHeight;
        }

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        WritableRaster bwRaster = bwImg.getRaster();

        int[] arrayRGB = new int[3];
        StringBuilder stringBuilder = new StringBuilder();
        for (int y = 0; y < bwRaster.getHeight(); y++) {
            for (int x = 0; x < bwRaster.getWidth(); x++) {
                int color = bwRaster.getPixel(x, y, arrayRGB)[0];
                char c = schema.convert(color);
                stringBuilder.append(c).append(c);
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }

    private void checkingRatio(BufferedImage image) throws BadImageSizeException {
        double imageRatio = (double) image.getWidth() / image.getHeight();
        if (imageRatio > maxRatio) throw new BadImageSizeException(maxRatio, imageRatio);
    }
}