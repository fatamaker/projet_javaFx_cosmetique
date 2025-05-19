package controller;

import application.models.Produit;
import application.models.ProduitM;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class HomeViewController {

    @FXML
    private FlowPane productsFlowPane;

    public void initialize() {
        ProduitM produitM = new ProduitM(); // create an instance
        List<Produit> produits = produitM.findAll(); // call the method

        for (Produit produit : produits) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ProduitCard.fxml"));
                VBox card = loader.load();

                ProduitCardController controller = loader.getController();
                controller.setProduit(produit);

                productsFlowPane.getChildren().add(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}