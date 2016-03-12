package com.myPerceptron.utils;

import javafx.geometry.Rectangle2D;
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
}
