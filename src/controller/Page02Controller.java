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
    private Categorie selectedCategorie = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());
        nomCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNom()));
        categorieTable.setItems(categories);
        chargerCategories();

        // Remplir le champ nomField lors de la sélection d'une ligne
        categorieTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedCategorie = newSelection;
                nomField.setText(selectedCategorie.getNom());
            }
        });
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
            selectedCategorie = null;
        }
    }

    public void supprimerCategorie() {
        Categorie selected = categorieTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            categorieM.supprimerCategorie(selected.getId());
            chargerCategories();
            nomField.clear();
            selectedCategorie = null;
        }
    }

    public void modifierCategorie() {
        if (selectedCategorie != null) {
            String nouveauNom = nomField.getText();
            if (!nouveauNom.isEmpty()) {
                selectedCategorie.setNom(nouveauNom);
                categorieM.modifierCategorie(selectedCategorie);
                chargerCategories();
                nomField.clear();
                selectedCategorie = null;
            }
        } else {
            showAlert("Veuillez sélectionner une catégorie à modifier.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Avertissement");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
