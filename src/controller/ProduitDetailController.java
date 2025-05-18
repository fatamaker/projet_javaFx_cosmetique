package controller;

import application.models.Produit;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ProduitDetailController {

    @FXML
    private ImageView imageView;

    @FXML
    private Label nomLabel, descriptionLabel, prixLabel, stockLabel, categorieLabel;

    public void setProduit(Produit produit) {
        nomLabel.setText(produit.getNom());
        descriptionLabel.setText(produit.getDescription());
        prixLabel.setText("Prix: " + produit.getPrix() + " DT");
        stockLabel.setText("Stock: " + produit.getStock());
        categorieLabel.setText("Catégorie: " + produit.getCategorie().getNom());

        if (produit.getImagePath() != null) {
            imageView.setImage(new Image(produit.getImagePath()));
        }
    }
}
