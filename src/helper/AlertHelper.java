package helper;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Window;

public class AlertHelper {

    public static boolean result = false;

    public static void showAlert(AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // You can customize this
        alert.setContentText(message);

        // Attach the alert to the given window if provided
        if (owner != null) {
            alert.initOwner(owner);
        }

        alert.showAndWait();
    }
}
