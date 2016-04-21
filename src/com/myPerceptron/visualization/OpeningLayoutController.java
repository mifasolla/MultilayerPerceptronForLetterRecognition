package com.myPerceptron.visualization;

import com.myPerceptron.MainApp;
import com.myPerceptron.algorithms.TrainingSample;
import com.myPerceptron.perceptron.Perceptron;
import com.myPerceptron.utils.AlertUtils;
import com.myPerceptron.utils.FileUtils;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Vika on 22.02.2016.
 */
public class OpeningLayoutController {

    @FXML
    private BorderPane borderP;

    @FXML
    private Canvas canvas;

    @FXML
    private Button clear;

    @FXML
    private Button saveAPicture;

    @FXML
    private Button saveAPictureTo;

    @FXML
    private Button loadTS;

    @FXML
    private Button train;

    @FXML
    private Button showChartButton;

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

    @FXML
    private TextField answer;

    @FXML
    private MenuItem savePerceptronAs;

    private GraphicsContext gc;
    private MainApp mainApp;

    private ToggleGroup radioButtonsGroup;
    private File imagesDir;


    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }


    @FXML
    private void initialize() {
        answer.setEditable(false);
        gc = canvas.getGraphicsContext2D();
        radioButtonsGroup = new ToggleGroup();
        letterV.setToggleGroup(radioButtonsGroup);
        letterZ.setToggleGroup(radioButtonsGroup);

        InvalidationListener listener = new InvalidationListener() {
            @Override
            public void invalidated(Observable o) {
                drawCanvasBorder();
            }
        };
        canvas.widthProperty().addListener(listener);
        canvas.heightProperty().addListener(listener);

        // Bind canvas size to stack pane size.
        canvas.widthProperty().bind(borderP.widthProperty().subtract(290));
        canvas.heightProperty().bind(borderP.heightProperty().subtract(70));


        // Clear away portions as the user drags the mouse
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                gc.fillOval(e.getX(), e.getY(), 13, 13);
            }
        });


    }

    @FXML
    private void onClearButtonClick() {
        drawCanvasBorder();
        answer.clear();
    }

    @FXML
    private void onSavePictureButtonClick() throws IOException {
        if (imagesDir != null) {
            savePicture(getPicture(), imagesDir);
        } else {
            onSavePictureToButtonClick();
        }
    }

    @FXML
    private void onSavePictureToButtonClick() {
        try {
            imagesDir = FileUtils.openDir(mainApp, ".\\src\\com\\myPerceptron\\data\\cache\\pathToImages.txt");
            if (imagesDir != null) {
                savePicture(getPicture(), imagesDir);
            }
        } catch (IOException ex) {
            Logger.getLogger(OpeningLayoutController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onConvertImagesToTrainingSampleClick() throws IOException {
        File imagesDir = FileUtils.openDir(mainApp, ".\\src\\com\\myPerceptron\\data\\cache\\pathToImages.txt");

        mainApp.setTrainingSample(new TrainingSample(imagesDir));
        toLearningTab();
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    @FXML
    private void handleNew() {
        if (showNewPerceptronCreationLayout()) {
            toTrainingTab();
        }
    }

    @FXML
    private void handleOpenPerceptron() throws FileNotFoundException, UnsupportedEncodingException {
        File file = FileUtils.openFile(mainApp, ".\\src\\com\\myPerceptron\\data\\cache\\pathToPerceptrons.txt",
               "TXT files (*.txt)", "*.txt");

        if (file != null) {
            mainApp.setPerceptron(new Perceptron(file));
        }
        toRecognitionTab();
        showChartButton.setDisable(true);
    }

    @FXML
    private void handleSavePerceptronAs() throws FileNotFoundException, UnsupportedEncodingException {
        File file = FileUtils.saveFile(mainApp,".\\src\\com\\myPerceptron\\data\\cache\\pathToPerceptrons.txt",
                "TXT files (*.txt)", "*.txt");

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".txt")) {
                file = new File(file.getPath() + ".txt");
            }
            mainApp.savePerceptron(file);
        }
    }

    @FXML
    private void onTrainButtonClick() throws Exception {
        mainApp.backPropagation();
        double[] generalizationError = mainApp.getGeneralizationError();
        double[] trainingError = mainApp.getTrainingError();
        if (generalizationError != null && trainingError != null) {
            showChartLayout(generalizationError, trainingError);
        }
        savePerceptronAs.setDisable(false);
        toRecognitionTab();
    }

    @FXML
    private void onRecognitionButtonClick() throws Exception {
        double result = mainApp.solve(getPicture());
        if (result != -2) {
            if (result < 0) {
                answer.setText("Z");
            } else {
                answer.setText("V");
            }
        }
    }

    @FXML
    private void onShowChartOfErrorsClick() {
        if (mainApp.getGeneralizationError() != null && mainApp.getTrainingError() != null) {
            showChartLayout(mainApp.getGeneralizationError(), mainApp.getTrainingError());
        }
    }

    private void drawCanvasBorder() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.RED);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.WHITE);
        gc.fillRect(2, 2, canvas.getWidth() - 4, canvas.getHeight() - 4);
        gc.setFill(Color.BLACK);
    }

    private void toLearningTab() {
        learningTab.setDisable(false);
        trainingTab.setDisable(true);
        recognitionTab.setDisable(true);
        tabPane.getSelectionModel().select(learningTab);
        train.requestFocus();
    }

    private void toTrainingTab() {
        recognitionTab.setDisable(true);
        learningTab.setDisable(true);
        trainingTab.setDisable(false);
        tabPane.getSelectionModel().select(trainingTab);
        saveAPicture.setDisable(true);
        saveAPictureTo.setDisable(true);
        clear.setDisable(true);
        loadTS.setDisable(false);
        loadTS.requestFocus();
    }

    private void allowAddingPictureTab() {
        if (!mainApp.hasNullPerceptron()) {
            recognitionTab.setDisable(false);
        }
        trainingTab.setDisable(false);
        saveAPicture.setDisable(false);
        saveAPictureTo.setDisable(false);
        clear.setDisable(false);
        loadTS.setDisable(true);
    }

    private void toRecognitionTab() {
        learningTab.setDisable(true);
        recognitionTab.setDisable(false);
        allowAddingPictureTab();
        tabPane.getSelectionModel().select(recognitionTab);

        if (mainApp.getGeneralizationError() == null && mainApp.getTrainingError() == null) {
            showChartButton.setDisable(true);
        } else {
            showChartButton.setDisable(false);
        }
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

    private boolean showNewPerceptronCreationLayout() {

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
            return controller.isOk();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showChartLayout(double[] generalizationErrors, double[] trainingErrors) {
        Stage dialogStage = new Stage();

        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Epoch Number");
        xAxis.setTickUnit(5);
        yAxis.setLabel("Percent of errors");
        yAxis.setTickUnit(10);

        //creating the chart
        final LineChart<Number, Number> lineChart =
                new LineChart<Number, Number>(xAxis, yAxis);

        lineChart.setTitle("Errors visualization");
        //defining a series
        XYChart.Series generalizationSeries = new XYChart.Series();
        generalizationSeries.setName("Generalization Error");
        for (int i = 0; i < generalizationErrors.length; i++) {
            generalizationSeries.getData().add(new XYChart.Data(i, generalizationErrors[i]));
        }

        XYChart.Series trainingSeries = new XYChart.Series();
        trainingSeries.setName("Training Error");
        for (int i = 0; i < trainingErrors.length; i++) {
            trainingSeries.getData().add(new XYChart.Data(i, trainingErrors[i]));
        }

        Scene scene = new Scene(lineChart, 800, 600);
        lineChart.getData().addAll(trainingSeries, generalizationSeries);
        lineChart.setSnapToPixel(false);

        dialogStage.setScene(scene);
        dialogStage.showAndWait();

    }

    private void savePicture(WritableImage snapshot, File imagesDir) throws IOException {

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
    }
}
