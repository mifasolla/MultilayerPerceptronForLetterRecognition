package com.myPerceptron.visualization;

import com.myPerceptron.MainApp;
import com.myPerceptron.perceptron.Perceptron;
import com.myPerceptron.utils.AlertUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Vika on 07.03.2016.
 */
public class NewPerceptronCreationLayoutController {

    @FXML
    private TextField perceptronStructure;

    private MainApp mainApp;
    private Stage dialogStage;
    private ArrayList<Integer> neuronsCountInfo;
    private final int INPUT_LENGTH = 101;
    private final int LAST_LAYER_NEURONS_COUNT = 1;

    @FXML
    private void initialize() {
        perceptronStructure.addEventFilter(KeyEvent.KEY_TYPED, validation());
    }

    @FXML
    private void handleCreate() throws IOException {
        String structure = perceptronStructure.getText();

        if (checkStructure(structure)) {
            neuronsCountInfo = parsePerceptronStructure(structure);

            mainApp.setPerceptron(new Perceptron(INPUT_LENGTH, neuronsCountInfo));
            
            dialogStage.close();
        }

    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    public void setMainApp (MainApp mainApp) { this.mainApp = mainApp; }
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    private EventHandler<KeyEvent> validation() {
        return new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
                String text = ((TextField) e.getSource()).getText();

                if (e.getCharacter().matches("[0-9-]")) {
                    if ((text.length() == 0 || text.charAt(text.length() - 1) == '-') &&
                            (e.getCharacter().matches("[-]") || e.getCharacter().matches("[0]"))) {
                        e.consume();
                    }
                } else {
                    e.consume();
                }
            }
        };
    }

    private boolean checkStructure(String structure) {
        if (structure.length() == 0) {
            AlertUtils.showAlert("Please, type the perceptron structure.", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    private ArrayList<Integer> parsePerceptronStructure(String structure) {
        ArrayList<Integer> neuronCountInfo = new ArrayList<Integer>();
        int integerStart = 0;

        if (structure.charAt(structure.length() - 1) == '-') {
            structure = structure.substring(0, structure.length() - 1);
        }

        for (int integerEnd = 0; integerEnd <= structure.length(); integerEnd++) {
            if (integerEnd == structure.length() || structure.charAt(integerEnd) == '-') {

                char[] chars = new char[integerEnd - integerStart];
                structure.getChars(integerStart, integerEnd, chars, 0);

                String integer = String.valueOf(chars);
                neuronCountInfo.add(Integer.parseInt(integer));

                integerStart = integerEnd + 1;
            }
        }
        neuronCountInfo.add(LAST_LAYER_NEURONS_COUNT);
        return neuronCountInfo;
    }
}
