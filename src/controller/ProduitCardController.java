package controller;

import application.models.Produit;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ProduitCardController {

    @FXML
    private ImageView imageView;

    @FXML
    private Label nomLabel;

    @FXML
    private Label prixLabel;

    @FXML
    private Label categorieLabel;

    @FXML
    private VBox cardContainer;

    private Produit produit;

    public void setProduit(Produit produit) {
        this.produit = produit;
        nomLabel.setText(produit.getNom());
        prixLabel.setText(produit.getPrix() + " DT");
        categorieLabel.setText(produit.getCategorie().getNom());

        if (produit.getImagePath() != null) {
            imageView.setImage(new Image(produit.getImagePath()));
        }

        // Gestion du clic
        cardContainer.setOnMouseClicked(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ProduitDetailView.fxml"));
                VBox detailRoot = loader.load();

                ProduitDetailController controller = loader.getController();
                controller.setProduit(produit);

                MainPanelController.staticBorderPane.setCenter(detailRoot);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
