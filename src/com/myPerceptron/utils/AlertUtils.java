package com.myPerceptron.utils;

import javafx.scene.control.Alert;

/**
 * Created by Vika on 10.03.2016.
 */
public final class AlertUtils {

    private AlertUtils() {
    }

    public static void showAlert(String contentText, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Message");
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    /*public static int showIntegerInputDialog(String title, String contentText) {
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
                    showAlert("Please, enter a positive non-zero number!", Alert.AlertType.WARNING);
                    return showIntegerInputDialog(title, contentText);
                }
            } catch (NumberFormatException e) {
                showAlert("Please, enter an integer value", Alert.AlertType.WARNING);
                return showIntegerInputDialog(title, contentText);
            }
        } else {
            showAlert("Please, enter a number", Alert.AlertType.WARNING);
            return showIntegerInputDialog(title, contentText);
        }
    }*/
}
