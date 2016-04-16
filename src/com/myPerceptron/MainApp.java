package com.myPerceptron; /**
 * Created by Vika on 23.02.2016.
 */

import com.myPerceptron.algorithms.BackPropagationAlgorithm;
import com.myPerceptron.algorithms.TrainingSample;
import com.myPerceptron.perceptron.Perceptron;
import com.myPerceptron.utils.ImageUtils;
import com.myPerceptron.utils.Matrix;
import com.myPerceptron.visualization.OpeningLayoutController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane openingLayout;
    private TrainingSample ts;
    private Perceptron perceptron;

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
        new BackPropagationAlgorithm(perceptron, ts);
    }

    public double solve(WritableImage image) throws Exception {
        double[] inputArray = ImageUtils.getVectorFromImage(image);
        System.out.println("WEIGHTS");
        for (int i = 0; i < perceptron.getLayersCount(); i++) {
            perceptron.getLayers(i).getWeights().show();
        }

        Matrix inputVector = new Matrix(inputArray.length, 1);
        inputVector.setVerticalVector(inputArray);
        System.out.println("Input vector ");
        inputVector.show();

        perceptron.calculateOutputs(inputVector);
        System.out.println("Response");
        perceptron.getLayers(perceptron.getLayersCount() - 1).getNeuronOutputs().show();

        return perceptron.getLayers(perceptron.getLayersCount() - 1).getNeuronOutputs().getElement(0, 0);
    }
}
