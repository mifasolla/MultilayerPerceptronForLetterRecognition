package com.myPerc.algorithms; /**
 * Created by Vika on 23.02.2016.
 */

import com.myPerc.algorithms.data.visualization.InputDataController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane inputDataPane;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Multilayer Perceptron");

        initRootLayout();

    }



    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/com/myPerc/algorithms/data/visualization/InputData.fxml"));
            inputDataPane = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(inputDataPane);
            primaryStage.setScene(scene);

            // Give the controller access to the main app.
            InputDataController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
