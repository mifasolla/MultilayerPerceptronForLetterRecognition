package com.myPerceptron; /**
 * Created by Vika on 23.02.2016.
 */

import com.myPerceptron.visualization.NewPerceptronCreationLayoutController;
import com.myPerceptron.visualization.OpeningLayoutController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
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
    public Stage getPrimaryStage () {
        return primaryStage;
    }

    public void showNewPerceptronCreationLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/com/myPerceptron/visualization/NewPerceptronCreationLayout.fxml"));
            AnchorPane perceptronCreationLayout = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Creating new perceptron");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(perceptronCreationLayout);
            dialogStage.setScene(scene);

            NewPerceptronCreationLayoutController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
