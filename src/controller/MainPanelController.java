package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainPanelController implements Initializable {

    @FXML
    private BorderPane borderPane;

    public static BorderPane staticBorderPane;

    // Menu buttons
    @FXML private Button home;
    @FXML private Button page01;
    @FXML private Button page02;
    @FXML private Button Panier;
    @FXML private Button page04;
    @FXML private Button page05;
    @FXML private Button page06;
    @FXML private Button page07;
    @FXML private Button page08;
    @FXML private Button page09;
    @FXML private Button page10;

    private List<Button> menuButtons;

    // Charts
    @FXML
    private AreaChart<?, ?> chartPurchase;

    @FXML
    private AreaChart<?, ?> chartSale;

    @FXML
    private LineChart<?, ?> chartReceipt;

    private static String currentRole;

    private static final Logger LOGGER = Logger.getLogger(MainPanelController.class.getName());

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        staticBorderPane = borderPane;
        initializeMenuButtons();
        configureMenuBasedOnRole();
        loadDefaultView(); // Load default view according to role
    }

    private void initializeMenuButtons() {
        menuButtons = new ArrayList<>();
        menuButtons.add(home);
        menuButtons.add(page01);
        menuButtons.add(page02);
        menuButtons.add(Panier);
        menuButtons.add(page04);
        menuButtons.add(page05);
        menuButtons.add(page06);
        menuButtons.add(page07);
        menuButtons.add(page08);
        menuButtons.add(page09);
        menuButtons.add(page10);
    }

    private void configureMenuBasedOnRole() {
        if (currentRole == null) {
            LOGGER.warning("Aucun rôle défini pour l'utilisateur");
            return;
        }

        // Hide all buttons first
        menuButtons.forEach(button -> {
            if (button != null) {
                button.setVisible(false);
                button.setManaged(false);
            }
        });

        if ("ADMIN".equals(currentRole)) {
            // ADMIN sees only Page01 and Page02 buttons
            setButtonVisibility(page01, true);
            setButtonVisibility(page02, true);
            LOGGER.info("Configuration du menu pour ADMIN terminée");
        } else if ("USER".equals(currentRole)) {
            // USER sees Home, Page03 and Page04 buttons
            setButtonVisibility(home, true);
            setButtonVisibility(Panier, true);
            setButtonVisibility(page04, true);
            LOGGER.info("Configuration du menu pour USER terminée");
        } else {
            LOGGER.warning("Rôle inconnu: " + currentRole);
        }
    }

    private void setButtonVisibility(Button button, boolean visible) {
        if (button != null) {
            button.setVisible(visible);
            button.setManaged(visible);
        }
    }

    private void loadDefaultView() {
        if ("ADMIN".equals(currentRole)) {
            loadView("Page01View", null); // Default view for ADMIN
        } else if ("USER".equals(currentRole)) {
            loadView("HomeView", null);  // Default view for USER
        }
    }

    public static void setCurrentRole(String role) {
        currentRole = role;
    }

    private void loadView(String viewName, ActionEvent event) {
        try {
            if (hasAccess(viewName)) {
                URL resource = getClass().getResource("/view/" + viewName + ".fxml");
                if (resource == null) {
                    LOGGER.severe("Fichier FXML introuvable: " + viewName);
                    showErrorAlertStatic("La page demandée n'existe pas");
                    return;
                }

                FXMLLoader loader = new FXMLLoader(resource);
                Parent root = loader.load();
                borderPane.setCenter(root);

                if (event != null && event.getSource() instanceof Button) {
                    highlightActiveButton((Button) event.getSource());
                }

                LOGGER.info("Chargement réussi de la vue: " + viewName);
            } else {
                showAccessDeniedAlert();
                LOGGER.warning("Tentative d'accès non autorisé à: " + viewName + " par rôle: " + currentRole);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement de la vue: " + viewName, ex);
            showErrorAlert("Erreur lors du chargement de la page: " + ex.getMessage());
            loadDefaultView();
        }
    }

    private boolean hasAccess(String viewName) {
        if (currentRole == null) return false;

        if ("ADMIN".equals(currentRole)) {
            return viewName.matches("Page01View|Page02View");
        } else if ("USER".equals(currentRole)) {
            return viewName.matches("HomeView|Page03View|Page04View|Panier");
        }
        return false;
    }

    private void highlightActiveButton(Button activeButton) {
        menuButtons.forEach(button -> {
            if (button != null && button.isVisible()) {
                String style = button == activeButton
                        ? "-fx-text-fill:#f0f0f0;-fx-background-color:#2b2a26;"
                        : "-fx-text-fill:#f0f0f0;-fx-background-color:#404040;";
                button.setStyle(style);
            }
        });
    }

    private void showAccessDeniedAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Accès refusé");
        alert.setHeaderText(null);
        alert.setContentText("Vous n'avez pas la permission d'accéder à cette page.");
        alert.showAndWait();
        loadDefaultView(); // Return to default view
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static void showErrorAlertStatic(String message) {
        // Static method helper to show alert from static context (if needed)
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void changeButtonBackground(ActionEvent e) {
        Iterator<Button> iteratorMenus = menuButtons.iterator();
        Button clickedButton = (Button) e.getSource();

        while (iteratorMenus.hasNext()) {
            Button otherButton = iteratorMenus.next();
            if (clickedButton == otherButton) {
                clickedButton.setStyle("-fx-text-fill:#f0f0f0;-fx-background-color:#2b2a26;");
            } else {
                if (otherButton != null) {
                    otherButton.setStyle("-fx-text-fill:#f0f0f0;-fx-background-color:#404040;");
                }
            }
        }
    }

    @FXML
    private void clear() {
        borderPane.setCenter(null);
    }

    @FXML
    private void loadFXML(String fileName) {
        Parent parent;
        try {
            parent = FXMLLoader.load(getClass().getResource("/view/" + fileName + ".fxml"));
            borderPane.setCenter(parent);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    // Navigation methods with role checks where needed

    @FXML
    private void loadHomeView(ActionEvent e) {
        if ("USER".equals(currentRole)) {
            loadView("HomeView", e);
        } else {
            showAccessDeniedAlert();
        }
    }

    @FXML
    private void loadPage01View(ActionEvent e) {
        loadView("Page01View", e);
    }

    @FXML
    private void loadPage02View(ActionEvent e) {
        loadView("Page02View", e);
    }

    @FXML
    private void loadPage03View(ActionEvent e) {
        loadView("Panier", e);
    }

    @FXML
    private void loadPage04View(ActionEvent e) {
        loadView("Page04View", e);
    }

    @FXML
    private void loadPage05View(ActionEvent e) {
        loadFXML("Page05View");
        changeButtonBackground(e);
    }

    @FXML
    private void loadPage06View(ActionEvent e) {
        loadFXML("Page06View");
        changeButtonBackground(e);
    }

    @FXML
    private void loadPage07View(ActionEvent e) {
        loadFXML("Page07View");
        changeButtonBackground(e);
    }

    @FXML
    private void loadPage08View(ActionEvent e) {
        loadFXML("Page08View");
        changeButtonBackground(e);
    }

    @FXML
    private void loadPage09View(ActionEvent e) {
        loadFXML("Page09View");
        changeButtonBackground(e);
    }

    @FXML
    private void loadPage10View(ActionEvent e) {
        loadFXML("Page10View");
        changeButtonBackground(e);
    }

    @FXML
    private void close() throws IOException {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Connexion");
        stage.getIcons().add(new Image("/asset/icon.png"));
        stage.show();
        LOGGER.info("Déconnexion de l'utilisateur, retour à l'écran de connexion");
    }
}
