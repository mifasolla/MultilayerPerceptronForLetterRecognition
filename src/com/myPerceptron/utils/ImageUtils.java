package com.myPerceptron.utils;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

/**
 * Created by Vika on 10.03.2016.
 */
public final class ImageUtils {
    private ImageUtils() {
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

        return new Rectangle2D(left, bottom, right - left + 1, top - bottom + 1);
    }

    public static double[] getVectorFromImage(Image image) {

        Rectangle2D imageBorders = getLetterBorders(image);

        double[] trainingVector = new double[101];
        trainingVector[0] = 1;

        int xSegmentLength = (int) imageBorders.getWidth() / 10;
        int ySegmentLength = (int) imageBorders.getHeight() / 10;
        int xMod10 = (int) imageBorders.getWidth() % 10;
        int yMod10 = (int) imageBorders.getHeight() % 10;
        int x = (int) imageBorders.getMinX();
        int y = (int) imageBorders.getMinY();
        PixelReader pixels = image.getPixelReader();

        for (int ySegmentNum = 0; ySegmentNum < 10; ySegmentNum++) {
            for (int xSegmentNum = 0; xSegmentNum < 10; xSegmentNum++) {
                int count = 0;

                for (int j = y; j < y + ySegmentLength; j++) {
                    for (int i = x; i < x + xSegmentLength; i++) {

                        int rgb = pixels.getArgb(i, j);
                        double b = ((rgb) & 0xFF);
                        double g = ((rgb >> 8) & 0xFF);
                        double r = ((rgb >> 16) & 0xFF);

                        if (r == 0 && g == 0 && b == 0) {
                            count++;
                            if(count > ((xSegmentLength*ySegmentLength)/10)) {
                                trainingVector[ySegmentNum * 10 + xSegmentNum + 1] = 1; // + 1, потому что на 0-м месте всегда стоит 1, остальные координаты вектора нужно заполнять со сдвигом на один
                                /*gc.setStroke(Color.RED);
                                gc.strokeOval(x, y, xSegmentLength, ySegmentLength);*/
                                i += xSegmentLength;
                                j += ySegmentLength;
                            }
                        }
                    }
                }
                x += xSegmentLength;
                if (xSegmentNum + 1 == 9) {
                    xSegmentLength += xMod10;
                }
            }
            x = (int) imageBorders.getMinX();
            xSegmentLength -= xMod10;
            y += ySegmentLength;

            if (ySegmentNum + 1 == 9) {
                ySegmentLength += yMod10;
            }
        }
        /*System.out.println("Vector from image:");
        for (int i = 0; i < trainingVector.length; i++) {
            System.out.println(trainingVector[i]);
        }
*/
        return trainingVector;
    }

    public static void showPictureGrid(Rectangle2D letterArea, GraphicsContext gc) {

        int xSegmentLength = (int) letterArea.getWidth() / 10;
        int ySegmentLength = (int) letterArea.getHeight() / 10;
        int xMod10 = (int) letterArea.getWidth() % 10;
        int yMod10 = (int) letterArea.getHeight() % 10;
        double x = letterArea.getMinX();
        double y = letterArea.getMinY();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                gc.setStroke(Color.BLUE);
                gc.strokeRect(x, y, xSegmentLength, ySegmentLength);
                y += ySegmentLength;
                if (j + 1 == 9) {
                    ySegmentLength += yMod10;
                }
            }
            y = letterArea.getMinY();
            ySegmentLength -= yMod10;
            x += xSegmentLength;
            if (i + 1 == 9) {
                xSegmentLength += xMod10;
            }
        }
    }
}
