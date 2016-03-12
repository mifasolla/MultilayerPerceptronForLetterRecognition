package com.myPerceptron.visualization;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Vika on 07.03.2016.
 */
public class NewPerceptronCreationLayoutController {

    @FXML
    private TextField perceptronStructure;

    private Stage dialogStage;

    @FXML
    private void initialize(URL url, ResourceBundle rb) {
        perceptronStructure.addEventFilter(KeyEvent.KEY_TYPED , validation());
    }

    @FXML
    private void handleCreate() {
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    private EventHandler<KeyEvent> validation() {
        return new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                //TextField txt_TextField = (TextField) e.getSource();
                String text = ((TextField) e.getSource()).getText();

                if(e.getCharacter().matches("[0-9-]")){
                    if((text.charAt(text.length() - 1) == '-' || text.length() == 0)&&
                            (e.getCharacter().matches("[-]") || e.getCharacter().matches("[0]"))){
                        e.consume();
                    }
                }else{
                    e.consume();
                }
            }
        };
    }
}
