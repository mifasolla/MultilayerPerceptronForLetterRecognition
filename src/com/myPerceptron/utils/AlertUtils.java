package com.myPerceptron.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

/**
 * Created by Vika on 10.03.2016.
 */
public final class AlertUtils {

    private AlertUtils() {
    }

    public static void showAlert(String title, String contentText, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public static int showIntegerInputDialog(String title, String contentText) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText("Data input dialog");
        dialog.setContentText(contentText);
        Optional<String> result = dialog.showAndWait();
        int input;
        if (result.isPresent()) {
            try {
                input = Integer.parseInt(result.get());
                if(input > 0)
                    return input;
                else {
                    showAlert("Unacceptable value!","Please, enter a positive non-zero number!", Alert.AlertType.WARNING);
                    return showIntegerInputDialog(title, contentText);
                }
            } catch (NumberFormatException e) {
                showAlert("Not a number!", "Please, enter an integer value", Alert.AlertType.WARNING);
                return showIntegerInputDialog(title, contentText);
            }
        } else {
            showAlert("Null", "Please, enter a number", Alert.AlertType.WARNING);
            return showIntegerInputDialog(title, contentText);
        }
    }
}
