package controller;

import database.DbConnection;
import helper.AlertHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

public class LoginAdminController {

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    private final Connection con;

    Window window;

    public LoginAdminController() {
        DbConnection dbc = DbConnection.getDatabaseConnection();
        con = dbc.getConnection();
    }

    @FXML
    private void loginAdmin() {
        String user = username.getText();
        String pass = password.getText();

        try {
            String query = "SELECT * FROM users WHERE user_name = ? AND password = ? AND role = 'ADMIN'";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Définir le rôle avant de charger le MainPanel
                MainPanelController.setCurrentRole("ADMIN");

                Stage stage = (Stage) username.getScene().getWindow();
                stage.close();

                Parent root = FXMLLoader.load(getClass().getResource("/view/MainPanelView.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Admin Panel");
                stage.show();
            } else {
                AlertHelper.showAlert(Alert.AlertType.ERROR, username.getScene().getWindow(),
                        "Erreur", "Nom d'utilisateur ou mot de passe incorrect, ou vous n'êtes pas admin.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertHelper.showAlert(Alert.AlertType.ERROR, username.getScene().getWindow(),
                    "Erreur", "Une erreur est survenue lors de la connexion.");
        }
    }
}

