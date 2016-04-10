package com.myPerceptron.visualization;

import com.myPerceptron.MainApp;
import com.myPerceptron.algorithms.TrainingSample;
import com.myPerceptron.utils.AlertUtils;
import com.myPerceptron.utils.FileUtils;
import com.myPerceptron.utils.ImageUtils;
import com.myPerceptron.utils.Matrix;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Vika on 22.02.2016.
 */
public class OpeningLayoutController {

    @FXML
    private Canvas canvas;

    @FXML
    private Button clear;

    @FXML
    private Button saveAPicture;

    @FXML
    private RadioButton letterV;

    @FXML
    private RadioButton letterZ;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab recognitionTab;

    @FXML
    private Tab trainingTab;

    @FXML
    private Tab learningTab;


    private GraphicsContext gc;
    private MainApp mainApp;

    private ToggleGroup radioButtonsGroup;
    private File imagesDir;


    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }


    @FXML
    private void initialize() {
        gc = canvas.getGraphicsContext2D();
        radioButtonsGroup = new ToggleGroup();
        letterV.setToggleGroup(radioButtonsGroup);
        letterZ.setToggleGroup(radioButtonsGroup);

        drawCanvasBorder();

        // Clear away portions as the user drags the mouse
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                gc.fillRect(e.getX() - 2, e.getY() - 2, 5, 5);
            }
        });
    }

    @FXML
    private void onClearButtonClick() {
        drawCanvasBorder();
    }

    private void drawCanvasBorder() {
        gc.setFill(Color.RED);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.WHITE);
        gc.fillRect(2, 2, canvas.getWidth() - 4, canvas.getHeight() - 4);
        gc.setFill(Color.BLACK);
    }

    @FXML
    private void onSaveAPictureButtonClick() {
        try {

            WritableImage snapshot = getPicture();

            File imagesDir = FileUtils.createImagesDir();
            FilenameFilter letterVFilter = FileUtils.createFileNameFilter("V(");
            FilenameFilter letterZFilter = FileUtils.createFileNameFilter("Z(");
            int pictureVCount = imagesDir.list(letterVFilter).length;
            int pictureZCount = imagesDir.list(letterZFilter).length;

            Toggle selected = radioButtonsGroup.getSelectedToggle();
            if (selected != null) {
                String prefix = selected == letterV ? "V(" : "Z(";
                int pictureNumber = selected == letterV ? ++pictureVCount : ++pictureZCount;

                File output = FileUtils.createPictureFile(imagesDir, prefix, pictureNumber);
                ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", output);

                radioButtonsGroup.selectToggle(null);

            } else {
                AlertUtils.showAlert("Please specify the letter before saving.", Alert.AlertType.WARNING);
            }
        } catch (IOException ex) {
            Logger.getLogger(OpeningLayoutController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onShowPictureGridClick() {
        Rectangle2D letterArea = ImageUtils.getLetterBorders(getPicture());

        int xSegmentLength = (int) letterArea.getWidth() / 10;
        int ySegmentLength = (int) letterArea.getHeight() / 10;
        int xMod10 = (int) letterArea.getWidth() % 10;
        int yMod10 = (int) letterArea.getHeight() % 10;
        double x = letterArea.getMinX();
        double y = letterArea.getMinY();


        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                gc.setStroke(Color.YELLOW);
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

    @FXML
    private void onConvertImagesToTrainingSampleClick() throws IOException {

        imagesDir = new File(".\\src\\com\\myPerceptron\\data\\images");
        mainApp.setTrainingSample(new TrainingSample(imagesDir));

    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    @FXML
    private void handleNew() {

        showNewPerceptronCreationLayout();
        //toLearningTab();

    }

    @FXML
    private void onLearnButtonClick() throws Exception {
        mainApp.backPropagation();
    }

    @FXML
    private void onTestMatrixClick() {
        Matrix matrix = new Matrix(4, 2);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                matrix.setElement(i, j, (i + 1) * (j + 1));
            }
        }

        matrix.show();
        double [] scalarVector = {10, 2};

        matrix.multipleColumnsOnScalarVector(scalarVector);
        matrix.show();
    }

    @FXML
    private void onRecognitionButtonClick() throws Exception {

        WritableImage snapshot = getPicture();

        double result = mainApp.solve(snapshot);

        AlertUtils.showAlert("Result = " + result, Alert.AlertType.INFORMATION);
    }

    private void toLearningTab() {
        recognitionTab.setDisable(true);
        trainingTab.setDisable(true);
        tabPane.getSelectionModel().select(learningTab);
    }

    private WritableImage getPicture() {
        Rectangle2D viewport = new Rectangle2D(
                canvas.getLayoutX() + 2, canvas.getLayoutY() + 2, canvas.getWidth() - 4, canvas.getHeight() - 4);
        SnapshotParameters params = new SnapshotParameters();
        params.setViewport(viewport);
        WritableImage snapshot = new WritableImage((int) viewport.getWidth(), (int) viewport.getHeight());
        canvas.snapshot(params, snapshot);
        return snapshot;
    }

    private void showNewPerceptronCreationLayout() {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/com/myPerceptron/visualization/NewPerceptronCreationLayout.fxml"));
            AnchorPane perceptronCreationLayout = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Creating New Perceptron");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            Scene scene = new Scene(perceptronCreationLayout);
            dialogStage.setScene(scene);

            NewPerceptronCreationLayoutController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public File getImagesDir() {
        return imagesDir;
    }

}
