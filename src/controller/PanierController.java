package controller;

import application.models.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PanierController {

    @FXML
    private VBox panierVBox;

    @FXML
    private Label totalLabel;

    @FXML
    private TextField nomClientField, adresseField, telephoneField, emailField;

    @FXML
    private Button passerCommandeButton;

    @FXML
    public void initialize() {
        afficherPanier();

        passerCommandeButton.setOnAction(e -> {
            passerCommande();
        });
    }

    private void afficherPanier() {
        panierVBox.getChildren().clear();

        Map<Produit, Integer> details = Panier.getInstance().getDetails();

        if (details.isEmpty()) {
            panierVBox.getChildren().add(new Label("Le panier est vide."));
            totalLabel.setText("Total: 0 DT");
            return;
        }

        for (Map.Entry<Produit, Integer> entry : details.entrySet()) {
            Produit produit = entry.getKey();
            int quantite = entry.getValue();

            Label label = new Label(produit.getNom() + " x " + quantite + " = " + (produit.getPrix() * quantite) + " DT");
            panierVBox.getChildren().add(label);
        }

        totalLabel.setText("Total: " + Panier.getInstance().getSousTotal() + " DT");
    }

    private void passerCommande() {
        String nomClient = nomClientField.getText();
        String adresse = adresseField.getText();
        String telephone = telephoneField.getText();
        String email = emailField.getText();

        if (nomClient.isEmpty() || adresse.isEmpty() || telephone.isEmpty() || email.isEmpty()) {
            System.out.println("Veuillez remplir tous les champs du client.");
            return;
        }

        List<LigneCommande> lignes = new ArrayList<>();
        for (Map.Entry<Produit, Integer> entry : Panier.getInstance().getDetails().entrySet()) {
            lignes.add(new LigneCommande(entry.getKey(), entry.getValue()));
        }

        Commande commande = new Commande(nomClient, adresse, telephone, email, Panier.getInstance().getSousTotal(), lignes);
        CommandeM commandeM = new CommandeM();

        boolean success = commandeM.ajouterCommande(commande);
        if (success) {
            System.out.println("Commande passée avec succès !");
            Panier.getInstance().viderPanier();
            afficherPanier(); // rafraîchir affichage
        } else {
            System.out.println("Erreur lors de la commande.");
        }
    }
}
