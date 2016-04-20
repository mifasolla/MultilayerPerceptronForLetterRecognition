package com.myPerceptron; /**
 * Created by Vika on 23.02.2016.
 */

import com.myPerceptron.algorithms.BackPropagationAlgorithm;
import com.myPerceptron.algorithms.TrainingSample;
import com.myPerceptron.perceptron.Perceptron;
import com.myPerceptron.utils.AlertUtils;
import com.myPerceptron.utils.ImageUtils;
import com.myPerceptron.utils.Matrix;
import com.myPerceptron.visualization.OpeningLayoutController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane openingLayout;
    private TrainingSample ts;
    private Perceptron perceptron;
    private BackPropagationAlgorithm bpa;
    private double[] generalizationError;
    private double[] trainingError;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Multilayer Perceptron");

        initOpeningLayout();
    }

    public void initOpeningLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/com/myPerceptron/visualization/OpeningLayout.fxml"));
            openingLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(openingLayout);
            primaryStage.setScene(scene);

            // Give the controller access to the main app.
            OpeningLayoutController controller = loader.getController();
            controller.setMainApp(this);


            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setTrainingSample(TrainingSample ts) {
        this.ts = ts;
    }

    public void setPerceptron(Perceptron perceptron) {
        this.perceptron = perceptron;
    }

    public void backPropagation() throws Exception {
        bpa = new BackPropagationAlgorithm(perceptron, ts);
        generalizationError = bpa.getGeneralizationError();
        trainingError = bpa.getTrainingError();
    }

    public double solve(WritableImage image) throws Exception {
        double[] inputArray = ImageUtils.getVectorFromImage(image);

        Matrix inputVector = new Matrix(inputArray.length, 1);
        inputVector.setVerticalVector(inputArray);

        if (!inputVector.cutRow(0).isNull()) {
            perceptron.calculateOutputs(inputVector);
            return perceptron.getLayers(perceptron.getLayersCount() - 1).getNeuronOutputs().getElement(0, 0);
        } else {
            AlertUtils.showAlert("Input vector is null. Try another picture.", Alert.AlertType.INFORMATION);
            return -2;
        }
    }

    public double solve(Matrix inputVector) throws Exception {
        if (!inputVector.cutRow(0).isNull()) {

            perceptron.calculateOutputs(inputVector);
            perceptron.getLayers(perceptron.getLayersCount() - 1).getNeuronOutputs().show();

            return perceptron.getLayers(perceptron.getLayersCount() - 1).getNeuronOutputs().getElement(0, 0);
        } else {
            AlertUtils.showAlert("Input vector is null. Try another picture.", Alert.AlertType.INFORMATION);
            return -2;
        }
    }

    public void savePerceptron(File file) throws FileNotFoundException, UnsupportedEncodingException {
        perceptron.saveTo(file);
    }

    public boolean hasNullPerceptron() {
        if (perceptron == null) return true;
        return false;
    }

    public double[] getGeneralizationError() {
       return generalizationError;
    }

    public double[] getTrainingError() {
       return trainingError;
    }
}
