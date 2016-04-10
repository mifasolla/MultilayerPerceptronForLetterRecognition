package com.myPerceptron.utils;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

/**
 * Created by Vika on 10.03.2016.
 */
public final class ImageUtils {
    private ImageUtils() {
    }

    public static Rectangle2D getLetterBorders(WritableImage image) {
        int left = Integer.MAX_VALUE;
        int right = Integer.MIN_VALUE;
        int top = Integer.MAX_VALUE;
        int bottom = Integer.MIN_VALUE;

        double width = image.getWidth();
        double height = image.getHeight();

        PixelReader pixels = image.getPixelReader();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                int rgb = pixels.getArgb(i, j);
                double b = ((rgb) & 0xFF);
                double g = ((rgb >> 8) & 0xFF);
                double r = ((rgb >> 16) & 0xFF);
                double a = ((rgb >> 24) & 0xFF);

                if (r == 0 && g == 0 && b == 0) { // 0xAA000000 - black
                    if (left > i) left = i;
                    if (right < i) right = i;
                    if (top > j) top = j;
                    if (bottom < j) bottom = j;
                }
            }
        }

        return new Rectangle2D(left + 1, top + 1, right - left + 3, bottom - top + 3);
    }

    public static Rectangle2D getLetterBorders(Image image) {
        int left = Integer.MAX_VALUE;
        int right = Integer.MIN_VALUE;
        int top = Integer.MIN_VALUE;
        int bottom = Integer.MAX_VALUE;

        PixelReader pixels = image.getPixelReader();

        double width = image.getWidth();
        double height = image.getHeight();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {


                int rgb = pixels.getArgb(i, j);
                double b = ((rgb) & 0xFF);
                double g = ((rgb >> 8) & 0xFF);
                double r = ((rgb >> 16) & 0xFF);

                if (r == 0 && g == 0 && b == 0) { // 0xAA000000 - black
                    if (left > i) left = i;
                    if (right < i) right = i;
                    if (top < j) top = j;
                    if (bottom > j) bottom = j;
                }
            }
        }

        return new Rectangle2D(left, bottom, right - left, top - bottom);
    }

    public static double[] getVectorFromImage(Image image, Rectangle2D imageBorders) {

        double[] trainingVector = new double[101];

        int xSegmentLength = (int) imageBorders.getWidth() / 10;
        int ySegmentLength = (int) imageBorders.getHeight() / 10;
        int xMod10 = (int) imageBorders.getWidth() % 10;
        int yMod10 = (int) imageBorders.getHeight() % 10;
        int x = (int) imageBorders.getMinX();
        int y = (int) imageBorders.getMinY();

        for (int xSegment = 0; xSegment < 10; xSegment++) {
            for (int ySegment = 0; ySegment < 10; ySegment++) {

                for (int i = x; i < x + xSegmentLength; i++) {
                    for (int j = y; j < y + ySegmentLength; j++) {

                        PixelReader pixels = image.getPixelReader();
                        int rgb = pixels.getArgb(i, j);
                        double b = ((rgb) & 0xFF);
                        double g = ((rgb >> 8) & 0xFF);
                        double r = ((rgb >> 16) & 0xFF);

                        if (r == 0 && g == 0 && b == 0) {
                            trainingVector[xSegment * 10 + ySegment + 1] = 1;
                            i += xSegmentLength;
                            j += ySegmentLength;
                        }

                    }
                }
                y += ySegmentLength;
                if (ySegment + 1 == 9) {
                    ySegmentLength += yMod10;
                }
            }
            y = (int) imageBorders.getMinY();
            ySegmentLength -= yMod10;
            x += xSegmentLength;
            if (xSegment + 1 == 9) {
                xSegmentLength += xMod10;
            }
        }

        return trainingVector;
    }
}
