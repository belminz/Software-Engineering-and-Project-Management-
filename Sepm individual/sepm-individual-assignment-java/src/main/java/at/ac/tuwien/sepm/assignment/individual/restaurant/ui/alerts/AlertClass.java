package at.ac.tuwien.sepm.assignment.individual.restaurant.ui.alerts;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertClass {

    public void showWarning(String s) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("User Warning");
        alert.setHeaderText((String) null);
        alert.setContentText(s);
        alert.showAndWait();
    }

    public void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText((String) null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public boolean showConfirmation(String s) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(s);
        alert.setContentText("");
        Optional<ButtonType> resultButton = alert.showAndWait();
        return resultButton.get() == ButtonType.OK;
    }

    public String paymentMethod() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Payment Method");
        alert.setHeaderText("Please choose payment method");
        ButtonType card = new ButtonType("card");
        ButtonType cash = new ButtonType("cash");
        alert.getButtonTypes().setAll(cash, card);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == cash ? "cash" : "card";

    }
}
