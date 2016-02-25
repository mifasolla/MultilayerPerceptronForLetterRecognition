package com.myPerceptron.data.visualization;

import com.myPerceptron.MainApp;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
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

    private GraphicsContext gc;
    private MainApp mainApp;
    private int CLICKS_COUNT = 0;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }


    @FXML
    private void initialize() {
        System.out.println("init in opening controller");
        gc = canvas.getGraphicsContext2D();

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
            CLICKS_COUNT++;
            // System.out.println("Took a picture");
            Rectangle2D viewport = new Rectangle2D(
                    canvas.getLayoutX() + 2, canvas.getLayoutY() + 2, canvas.getWidth() - 4, canvas.getHeight() - 4);
            SnapshotParameters params = new SnapshotParameters();
            params.setViewport(viewport);
            WritableImage snapshot = new WritableImage((int) viewport.getWidth(), (int) viewport.getHeight());
            canvas.snapshot(params, snapshot);

            File output = new File("Picture(" + CLICKS_COUNT + ").png");
            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", output);

        } catch (IOException ex) {
            //ex.printStackTrace();
            Logger.getLogger(OpeningLayoutController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
