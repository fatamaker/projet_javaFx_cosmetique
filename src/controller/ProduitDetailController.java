package controller;

import application.models.Panier;
import application.models.Produit;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ProduitDetailController {

    @FXML
    private ImageView imageView;

    @FXML
    private Label nomLabel, descriptionLabel, prixLabel, stockLabel, categorieLabel;

    @FXML
    private Spinner<Integer> quantiteSpinner;

    @FXML
    private Button ajouterPanierButton;

    private Produit produit;  // garder le produit courant

    @FXML
    public void initialize() {
        quantiteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));

        ajouterPanierButton.setOnAction(e -> {
            int quantite = quantiteSpinner.getValue();
            Panier.getInstance().ajouterProduit(produit, quantite);
            System.out.println("Ajouté au panier: " + produit.getNom() + " x" + quantite);
         
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Produit ajouté au panier avec succès !");
            alert.showAndWait();
        });
    }

    public void setProduit(Produit produit) {
        this.produit = produit;

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
