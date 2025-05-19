package controller;

import application.models.*;
import helper.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
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
        
        Utilisateur utilisateur = SessionManager.getUtilisateurConnecte();
        if (utilisateur != null) {
            nomClientField.setText(utilisateur.getNom());
                       emailField.setText(utilisateur.getEmail());
                       nomClientField.setEditable(false);
                       emailField.setEditable(false);
        }

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
        Utilisateur utilisateur = SessionManager.getUtilisateurConnecte();
        String adresse = adresseField.getText();
        String telephone = telephoneField.getText();

        if (utilisateur == null) {
            System.out.println("Utilisateur non authentifié");
            return;
        }

     

        List<LigneCommande> lignes = new ArrayList<>();
        for (Map.Entry<Produit, Integer> entry : Panier.getInstance().getDetails().entrySet()) {
            lignes.add(new LigneCommande(entry.getKey(), entry.getValue()));
        }

        LocalDateTime dateCommande = LocalDateTime.now(); 

        Commande commande = new Commande(
            utilisateur.getId(),               
            utilisateur.getNom(),             
            adresse,                          
            telephone,                         
            utilisateur.getEmail(),           
            Panier.getInstance().getSousTotal(),
            lignes,
            dateCommande                      
        );

        CommandeM commandeM = new CommandeM();
        boolean success = commandeM.ajouterCommande(commande);

        if (success) {
            System.out.println("Commande passée avec succès !");
            
            ProduitM produitM = new ProduitM();
            for (LigneCommande ligne : lignes) {
                Produit produit = ligne.getProduit();
                int quantiteCommandee = ligne.getQuantite();
                int nouveauStock = produit.getStock() - quantiteCommandee;
                
                if (nouveauStock >= 0) {
                    produitM.mettreAJourStock(produit.getId(), nouveauStock);
                } else {
                    System.out.println("Stock insuffisant pour le produit : " + produit.getNom());
                }
            }

            Panier.getInstance().viderPanier();
            afficherPanier(); 
        } else {
            System.out.println("Erreur lors de la commande.");
        }
    }

}
