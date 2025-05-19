package controller;

import application.models.Categorie;
import application.models.CategorieM;
import application.models.Produit;
import application.models.ProduitM;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Page01Controller implements Initializable {

    @FXML private TextField nomField;
    @FXML private TextField descriptionField;
    @FXML private TextField prixField;
    @FXML private TextField stockField;
    @FXML private ChoiceBox<Categorie> categorieChoiceBox;

    @FXML private TableView<Produit> produitTable;
    @FXML private TableColumn<Produit, String> nomColumn;
    @FXML private TableColumn<Produit, String> descriptionColumn;
    @FXML private TableColumn<Produit, Double> prixColumn;
    @FXML private TableColumn<Produit, Integer> stockColumn;
    @FXML
    private TableColumn<Produit, String> categorieColumn;
    @FXML private TableColumn<Produit, ImageView> imageColumn;
    
    
    @FXML private Button uploadImageButton;
    @FXML private ImageView productImageView;

    private final CategorieM categorieManager = new CategorieM();
    private final ProduitM manager = new ProduitM();
    private final ObservableList<Produit> data = FXCollections.observableArrayList();
    
   

    private String selectedImagePath = null;

    @FXML
    private void handleImageUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File file = fileChooser.showOpenDialog(uploadImageButton.getScene().getWindow());
        if (file != null) {
            try {
                // Dossier où copier l’image
                File imagesDir = new File("images");
                if (!imagesDir.exists()) {
                    imagesDir.mkdirs();
                }

                // Nouveau fichier dans le dossier images
                File dest = new File(imagesDir, file.getName());

                // Copier le fichier
                java.nio.file.Files.copy(file.toPath(), dest.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                // Mettre à jour le chemin relatif
                selectedImagePath = dest.toURI().toString(); // ou simplement "images/" + file.getName()

                // Afficher l’image dans l’ImageView
                productImageView.setImage(new Image(selectedImagePath));
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Erreur lors de l'importation de l'image.");
            }
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
      
        categorieChoiceBox.setItems(FXCollections.observableArrayList(categorieManager.getAllCategories()));
        
        

        
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        categorieColumn.setCellValueFactory(cellData ->
        new SimpleStringProperty(cellData.getValue().getCategorie().getNom())
    );

        
       

      
        data.setAll(manager.findAll());
        produitTable.setItems(data);
        
        imageColumn.setCellValueFactory(cellData -> {
            String path = cellData.getValue().getImagePath();
            ImageView imageView = new ImageView();
            if (path != null) {
                imageView.setImage(new Image(path, 50, 50, true, true));
            }
            return new ReadOnlyObjectWrapper<>(imageView);
        });

       
        produitTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nomField.setText(newSelection.getNom());
                descriptionField.setText(newSelection.getDescription());
                prixField.setText(String.valueOf(newSelection.getPrix()));
                stockField.setText(String.valueOf(newSelection.getStock()));
                categorieChoiceBox.setValue(newSelection.getCategorie());
            }
        });
    }

    @FXML
    public void addProduct() {
        if (nomField.getText().isEmpty() || descriptionField.getText().isEmpty() || prixField.getText().isEmpty()
                || stockField.getText().isEmpty() || categorieChoiceBox.getValue() == null) {
            showAlert("Tous les champs doivent être remplis !");
            return;
        }

        try {
            double prix = Double.parseDouble(prixField.getText());
            int stock = Integer.parseInt(stockField.getText());

            Produit produit = new Produit(
                    nomField.getText(),
                    descriptionField.getText(),
                    prix,
                    stock,
                    categorieChoiceBox.getValue(),
                    selectedImagePath
            );

            if (manager.create(produit)) {
                data.setAll(manager.findAll());
                clearFields();
            }
        } catch (NumberFormatException e) {
            showAlert("Prix et stock doivent être des nombres valides !");
        }
    }

    @FXML
    public void updateProduct() {
        Produit selected = produitTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                double prix = Double.parseDouble(prixField.getText());
                int stock = Integer.parseInt(stockField.getText());

                selected.setNom(nomField.getText());
                selected.setDescription(descriptionField.getText());
                selected.setPrix(prix);
                selected.setStock(stock);
                selected.setCategorie(categorieChoiceBox.getValue());
                selected.setImagePath(selectedImagePath);

                if (manager.update(selected)) {
                    data.setAll(manager.findAll());
                    produitTable.refresh();
                    clearFields();
                }
            } catch (NumberFormatException e) {
                showAlert("Prix et stock doivent être des nombres valides !");
            }
        } else {
            showAlert("Aucun produit sélectionné !");
        }
    }

    @FXML
    public void removeSelectedProduct() {
        Produit selected = produitTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (manager.delete(selected.getId())) {
                data.setAll(manager.findAll());
                clearFields();
            }
        } else {
            showAlert("Aucun produit sélectionné !");
        }
    }

    private void clearFields() {
        nomField.clear();
        descriptionField.clear();
        prixField.clear();
        stockField.clear();
        categorieChoiceBox.setValue(null);
        produitTable.getSelectionModel().clearSelection();
        productImageView.setImage(null);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Avertissement");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
