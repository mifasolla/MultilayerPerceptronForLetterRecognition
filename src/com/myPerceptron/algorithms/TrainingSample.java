package com.myPerceptron.algorithms;

import com.myPerceptron.utils.AlertUtils;
import com.myPerceptron.utils.ImageUtils;
import com.myPerceptron.utils.Matrix;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.util.Random;


/**
 * Created by Vika on 15.02.2016.
 */
public class TrainingSample {

    private Matrix trainingSample;
    private Matrix desiredResponseVector;
    private File imagesDir;


    public TrainingSample(File imagesDir) throws IOException {

        this.imagesDir = imagesDir;

        createTrainingSample();

        AlertUtils.showAlert("The training sample is successfully created.",
                Alert.AlertType.INFORMATION);

    }

    public TrainingSample(Matrix trainingSample, Matrix desiredResponseVector) {

        this.trainingSample = trainingSample;
        this.desiredResponseVector = desiredResponseVector;
    } // for tests

    public Matrix getInputVector(int number) {
        return trainingSample.getVectorFromColumn(number);
    }

    public Matrix getDesiredResponseVector() {
        return desiredResponseVector;
    }

    public int size() {
        return trainingSample.getColumnCount();
    }

    private void createTrainingSample() throws IOException {
        File[] imagesList = null;

        if (isRightDirectory(imagesDir)) {
            imagesList = imagesDir.listFiles();
        } else {
            // throw some exception?
        }

        if (isRightImagesList(imagesList)) {

            trainingSample = new Matrix(101, imagesList.length); // матрица из столбцов-входящих векторов
            desiredResponseVector = new Matrix(imagesList.length, 1); // столбец

            for (int k = 0; k < imagesList.length; k++) {

                Image image = new Image(imagesList[k].toURI().toString());

                if (imagesList[k].getName().contains("V")) desiredResponseVector.setElement(k, 0, 1);
                else if (imagesList[k].getName().contains("Z")) desiredResponseVector.setElement(k, 0, -1);
                else AlertUtils.showAlert("Image name is written in the wrong format." + imagesList[k].getName(),
                            Alert.AlertType.WARNING);

                double[] trainingVector = ImageUtils.getVectorFromImage(image);

                trainingSample.setColumn(k, trainingVector);

            }
        } else {
            // throw some exception?
        }
    }

    private boolean isRightDirectory(File imagesDir) {
        if (imagesDir.exists()) {
            if (imagesDir.isDirectory()) {
                return true;
            } else {
                AlertUtils.showAlert("Not a directory! Please verify the path to the images directory.",
                        Alert.AlertType.WARNING);
            }
        } else {
            AlertUtils.showAlert("The directory does not exist! Please verify the path to the images directory.",
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
                AlertUtils.showAlert("A small training sample length. Please, add to the folder at least " + picturesCount +
                        " more training images.", Alert.AlertType.WARNING);
            }
        } else {
            AlertUtils.showAlert("The training sample is null. Please, add at least 100 training images.",
                    Alert.AlertType.WARNING);
        }
        return false;
    }

    public void shuffle() {
        for (int i = trainingSample.getColumnCount() - 1; i >= 0; i--) {
            int index = new Random().nextInt(i + 1);
            trainingSample.swapColumns(i, index);
            desiredResponseVector.swapRows(i, index);
        }
    }
}
