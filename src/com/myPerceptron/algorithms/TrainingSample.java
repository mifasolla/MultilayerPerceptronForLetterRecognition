package com.myPerceptron.algorithms;

import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;

import java.io.File;
import java.io.IOException;

//import java.awt.*;

/**
 * Created by Vika on 15.02.2016.
 */
public class TrainingSample {

    private double[][] trainingSample;
    private double[] desiredResponseVector;
    private File imagesDir;


    public TrainingSample(File imagesDir) throws IOException {

        this.imagesDir = imagesDir;

        createTrainingSample();

        showAlert("Successfully completed", "The training sample is successfully created.",
                Alert.AlertType.INFORMATION);

    }

    public double[] getInputVector(int number) {
        return trainingSample[number];
    }

    public double[] getDesiredResponseVector() {
        return desiredResponseVector;
    }

    public int getInputVectorSize() {
        return getInputVector(0).length;
    }

    private double[][] createTrainingSample() throws IOException {


        File[] imagesList = null;
        if (isRightDirectory(imagesDir)) {
            imagesList = imagesDir.listFiles();
        } else {
            // throw some exception?
        }

        if (isRightImagesList(imagesList)) {

            trainingSample = new double[imagesList.length][100];
            desiredResponseVector = new double[imagesList.length];

            for (int k = 0; k < imagesList.length; k++) {

                Image image = new Image(imagesList[k].toURI().toString());

                if (imagesList[k].getName().contains("V")) desiredResponseVector[k] = 1;
                else if (imagesList[k].getName().contains("Z")) desiredResponseVector[k] = -1;
                else showAlert("Wrong name!", "Image name is written in the wrong format.", Alert.AlertType.WARNING);


                Rectangle2D letterBorders = determineLetterBorders(image);

                double[] trainingVector = getVectorFromImage(image, letterBorders);

                trainingSample[k] = trainingVector;

            }
        } else {
            // throw some exception?
        }

        return trainingSample;
    }

    private boolean isRightDirectory(File imagesDir) {
        if (imagesDir.exists()) {
            if (imagesDir.isDirectory()) {
                return true;
            } else {
                showAlert("Not a directory!", "Please verify the path to the images directory.",
                        Alert.AlertType.WARNING);
            }
        } else {
            showAlert("The directory does not exist!", "Please verify the path to the images directory.",
                    Alert.AlertType.WARNING);
        }
        return false;
    }

    private boolean isRightImagesList(File[] imagesList) {
        if (imagesList != null) {
            if (imagesList.length >= 100) {
                return true;
            } else {
                int picturesCount = 100 - imagesList.length;
                showAlert("A small training sample length",
                        "Please, add to the folder at least " + picturesCount + " more training images.",
                        Alert.AlertType.WARNING);
            }
        } else {
            showAlert("No images!", "The training sample is null. Please, add at least 100 training images.",
                    Alert.AlertType.WARNING);
        }
        return false;
    }

    private Rectangle2D determineLetterBorders(Image image) {

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

    private double[] getVectorFromImage(Image image, Rectangle2D imageBorders) {

        double[] trainingVector = new double[100];

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
                            trainingVector[xSegment * 10 + ySegment] = 1;
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

    public void showAlert(String title, String contentText, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
