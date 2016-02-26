package com.myPerceptron.algorithms;

import javafx.scene.control.Alert;

import java.io.File;

/**
 * Created by Vika on 15.02.2016.
 */
public class TrainingSample {

    int vectorLength;

    double [] inputVector;
    double [] desiredResponseVector;
    File imagesDir;

    public TrainingSample(File imagesDir) {
        this.imagesDir = imagesDir;
    }

    public TrainingSample(int vectorLength, double[] inputVector, double[] desiredResponseVector) {
        this.vectorLength = vectorLength;
        this.inputVector = inputVector;
        this.desiredResponseVector = desiredResponseVector;
    }

    public double [] getInputVector() {
        return inputVector;
    }

    public double [] getDesiredResponseVector() {
        return desiredResponseVector;
    }

    private void createTrainingSample() {
        File [] imagesList = null;
        if (isRightDirectory(imagesDir)) {
            imagesList = imagesDir.listFiles();
        } else {
            // throw some exception?
        }

        if (isRightImagesList(imagesList)) {
            // create training sample
        } else {
            // throw some exception?
        }
    }

    private boolean isRightDirectory(File imagesDir) {
        if(imagesDir.exists()) {
            if(imagesDir.isDirectory()) {
                return true;
            } else {
                Alert notDirectoryAlert = new Alert(Alert.AlertType.WARNING);
                notDirectoryAlert.setTitle("Not a directory!");
                notDirectoryAlert.setContentText("Please verify the path to the images directory.");
                notDirectoryAlert.showAndWait();
            }
        } else {
            Alert notExistAlert = new Alert(Alert.AlertType.WARNING);
            notExistAlert.setTitle("The directory does not exist!");
            notExistAlert.setContentText("Please verify the path to the images directory.");
            notExistAlert.showAndWait();
        }
        return false;
    }

    private boolean isRightImagesList(File[] imagesList) {
        if(imagesList != null) {
            if (imagesList.length >= 100) {
                return true;
            } else {
                int picturesCount = 100 - imagesList.length;
                Alert smallSampleAlert = new Alert(Alert.AlertType.WARNING);
                smallSampleAlert.setTitle("A small training sample length");
                smallSampleAlert.setContentText("Please, add at least " + picturesCount+ " more training images.");
            }
        } else {
            Alert noSampleAlert = new Alert(Alert.AlertType.WARNING);
            noSampleAlert.setTitle("No images!");
            noSampleAlert.setContentText("The training sample is null. Please, add at least 100 training images.");
        }
        return false;
    }
}
