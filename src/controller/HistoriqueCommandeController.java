package controller;
import application.models.Commande;
import application.models.CommandeM;
import helper.SessionManager;
import application.models.Utilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class HistoriqueCommandeController {

    @FXML private TableView<Commande> tableCommandes;
    @FXML private TableColumn<Commande, Integer> colId;
    @FXML private TableColumn<Commande, String> colNomClient;
    @FXML private TableColumn<Commande, String> colAdresse;
    @FXML private TableColumn<Commande, String> colTelephone;
    @FXML private TableColumn<Commande, Double> colTotal;

    private final CommandeM commandeM = new CommandeM();

    @FXML
    public void initialize() {
        Utilisateur user = SessionManager.getUtilisateurConnecte();
        if (user != null) {
            List<Commande> commandes = commandeM.getCommandesParUtilisateur(user.getId());
            ObservableList<Commande> observableList = FXCollections.observableArrayList(commandes);
            tableCommandes.setItems(observableList);

            colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
            colNomClient.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNomClient()));
            colAdresse.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAdresse()));
            colTelephone.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTelephone()));
            colTotal.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getTotal()).asObject());
        }
    }

    @FXML
    private void handleRetour() {
       
    }
}
