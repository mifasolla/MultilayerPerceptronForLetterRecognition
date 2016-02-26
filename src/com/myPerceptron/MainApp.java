package com.myPerceptron; /**
 * Created by Vika on 23.02.2016.
 */

import com.myPerceptron.data.visualization.OpeningLayoutController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane openingLayout;

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
            loader.setLocation(MainApp.class.getResource("/com/myPerceptron/data/visualization/OpeningLayout.fxml"));
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
}