package controller;
import application.models.Commande;
import application.models.CommandeM;
import application.models.LigneCommande;
import application.models.Produit;
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

            // ➕ Ajouter gestion de clic sur une ligne
            tableCommandes.setRowFactory(tv -> {
                TableRow<Commande> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (!row.isEmpty() && event.getClickCount() == 1) {
                        Commande commande = row.getItem();
                        afficherDetailsCommande(commande);
                    }
                });
                return row;
            });
        }
    }


    @FXML
    private void handleRetour() {
       
    }
    
    private void afficherDetailsCommande(Commande commande) {
        StringBuilder details = new StringBuilder();
        details.append("Nom Client : ").append(commande.getNomClient()).append("\n")
               .append("Adresse : ").append(commande.getAdresse()).append("\n")
               .append("Téléphone : ").append(commande.getTelephone()).append("\n")
               .append("Email : ").append(commande.getEmail()).append("\n")
               .append("Date : ").append(commande.getDateCommande()).append("\n")
               .append("Total : ").append(commande.getTotal()).append(" dt\n\n")
               .append("🛒 Produits commandés :\n");

        for (LigneCommande ligne : commande.getLignes()) {
            Produit p = ligne.getProduit();
            details.append("- ").append(p.getNom())
                   .append(" | Quantité: ").append(ligne.getQuantite())
                   .append(" | Prix: ").append(p.getPrix()).append(" dt\n");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Détails de la commande");
        alert.setHeaderText("Commande #" + commande.getId());
        alert.setContentText(details.toString());
        alert.getDialogPane().setPrefWidth(400); // pour meilleure lisibilité
        alert.showAndWait();
    }

}
