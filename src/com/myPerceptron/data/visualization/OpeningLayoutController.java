package com.myPerceptron.data.visualization;

import com.myPerceptron.MainApp;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

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


    private GraphicsContext gc;
    private MainApp mainApp;

    ToggleGroup radioButtonsGroup;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }


    @FXML
    private void initialize() {
        System.out.println("init in opening controller");
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

            File imagesDir = createImagesDir();
            FilenameFilter letterVFilter = createFileNameFilter("V(");
            FilenameFilter letterZFilter = createFileNameFilter("Z(");
            int pictureVCount = imagesDir.list(letterVFilter).length;
            int pictureZCount = imagesDir.list(letterZFilter).length;

            Toggle selected = radioButtonsGroup.getSelectedToggle();
            if (selected != null) {
                String prefix = selected == letterV ? "V(" : "Z(";
                int pictureNumber = selected == letterV ? ++pictureVCount : ++pictureZCount;

                File output = createPictureFile(imagesDir, prefix, pictureNumber);
                ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", output);
                radioButtonsGroup.selectToggle(null);
            } else {
                showNoLetterSelectedAlert();
            }
        } catch (IOException ex) {
            //ex.printStackTrace();
            Logger.getLogger(OpeningLayoutController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleExit() {
        System.exit(0);
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

    private File createImagesDir() {
        File imagesDir = new File(".\\src\\com\\myPerceptron\\data\\images");
        if (imagesDir.exists()) {
            if (!imagesDir.isDirectory()) {
                imagesDir.delete();
                imagesDir.mkdir();
            }
        } else {
            imagesDir.mkdir();
        }

        return imagesDir;
    }

    private FilenameFilter createFileNameFilter(final String filter) {
        return new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name != null) {
                    if (name.contains(filter)) return true;
                }
                return false;
            }
        };
    }

    private File createPictureFile(File imagesDir, String picturePrefix, int pictureNumber) {
        return new File(imagesDir, picturePrefix + pictureNumber + ").png");
    }

    private void showNoLetterSelectedAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("No letter selected!");
        alert.setContentText("Please specify the letter before saving.");
        alert.showAndWait();
    }

}
