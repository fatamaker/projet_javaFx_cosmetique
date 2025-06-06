package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainPanelController implements Initializable {

    @FXML
    private BorderPane borderPane;
    
    public static BorderPane staticBorderPane;
    
  
    @FXML private Button home;
    @FXML private Button page01;
    @FXML private Button page02;
    @FXML private Button page03;
    @FXML private Button page04;
    
    
    private List<Button> menuButtons;
    private static String currentRole;
    private static final Logger LOGGER = Logger.getLogger(MainPanelController.class.getName());

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        staticBorderPane = borderPane;
        initializeMenuButtons();
        configureMenuBasedOnRole();
        loadDefaultView(); 
    }
    
    private void loadDefaultView() {
        if ("ADMIN".equals(currentRole)) {
            loadView("Page01View", null); 
        } else if ("USER".equals(currentRole)) {
            loadView("HomeView", null); 
        }
    }
    
    public static void setCurrentRole(String role) {
        currentRole = role;
    }
    
    private void initializeMenuButtons() {
        menuButtons = new ArrayList<>();
        menuButtons.add(home);
        menuButtons.add(page01);
        menuButtons.add(page02);
        menuButtons.add(page03);
        menuButtons.add(page04);
       
    }
    
    private void configureMenuBasedOnRole() {
        if (currentRole == null) {
            LOGGER.warning("Aucun rôle défini pour l'utilisateur");
            return;
        }
        
        
        menuButtons.forEach(button -> {
            if (button != null) {
                button.setVisible(false);
                button.setManaged(false);
            }
        });
        
        if ("ADMIN".equals(currentRole)) {
          
            setButtonVisibility(page01, true);
            setButtonVisibility(page02, true);
            LOGGER.info("Configuration du menu pour ADMIN terminée");
        } 
        else if ("USER".equals(currentRole)) {
           
            setButtonVisibility(home, true);
            setButtonVisibility(page03, true);
            setButtonVisibility(page04, true);
            LOGGER.info("Configuration du menu pour USER terminée");
        }
        else {
            LOGGER.warning("Rôle inconnu: " + currentRole);
        }
    }
    
    private void setButtonVisibility(Button button, boolean visible) {
        if (button != null) {
            button.setVisible(visible);
            button.setManaged(visible);
        }
    }

    private void loadView(String viewName, ActionEvent event) {
        try {
            if (hasAccess(viewName)) {
                URL resource = getClass().getResource("/view/" + viewName + ".fxml");
                if (resource == null) {
                    LOGGER.severe("Fichier FXML introuvable: " + viewName);
                    showErrorAlert("La page demandée n'existe pas");
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
        } 
        else if ("USER".equals(currentRole)) {
            return viewName.matches("HomeView|HistoriqueCommande|Panier");
        }
        return false;
    }

    private void highlightActiveButton(Button activeButton) {
        menuButtons.forEach(button -> {
            if (button != null && button.isVisible()) {
                String style = button == activeButton 
                    ? "-fx-text-fill:#f0f0f0;-fx-background-color:#C68EA0;" 
                    : "-fx-text-fill:#f0f0f0;-fx-background-color:#E2A3B7;";
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
        loadDefaultView(); 
    }
    
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    
    @FXML private void loadHomeView(ActionEvent e) { 
        if ("USER".equals(currentRole)) {
            loadView("HomeView", e); 
        } else {
            showAccessDeniedAlert();
        }
    }
    @FXML private void loadPage01View(ActionEvent e) { loadView("Page01View", e); }
    @FXML private void loadPage02View(ActionEvent e) { loadView("Page02View", e); }
    @FXML private void loadPage03View(ActionEvent e) { loadView("Panier", e); }
    @FXML private void loadPage04View(ActionEvent e) { loadView("HistoriqueCommande", e); }

    @FXML
    private void close() throws IOException {
        Stage stage = (Stage) borderPane.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/view/role_selection.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Connexion");
        stage.getIcons().add(new Image("/asset/icon.png"));
        stage.show();
        LOGGER.info("Déconnexion de l'utilisateur, retour à l'écran de connexion");}
}