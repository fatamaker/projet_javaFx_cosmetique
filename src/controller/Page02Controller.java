package controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.models.Categorie;
import application.models.CategorieM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class Page02Controller implements Initializable {

    @FXML private TableView<Categorie> categorieTable;
    @FXML private TableColumn<Categorie, Integer> idCol;
    @FXML private TableColumn<Categorie, String> nomCol;
    @FXML private TextField nomField;

    private final ObservableList<Categorie> categories = FXCollections.observableArrayList();
    private final CategorieM categorieM = new CategorieM();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        nomCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNom()));
        categorieTable.setItems(categories);
        chargerCategories();
    }

    public void chargerCategories() {
        categories.clear();
        categories.addAll(categorieM.getAllCategories());
    }

    public void ajouterCategorie() {
        String nom = nomField.getText();
        if (!nom.isEmpty()) {
            categorieM.ajouterCategorie(nom);
            chargerCategories();
            nomField.clear();
        }
    }

    public void supprimerCategorie() {
        Categorie selected = categorieTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            categorieM.supprimerCategorie(selected.getId());
            chargerCategories();
        }
    }
}
