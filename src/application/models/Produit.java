package application.models;

public class Produit {
    private int id;
    private String nom;
    private String description;
    private double prix;
    private int stock;
    private Categorie categorie;
    private String imagePath;

    public Produit(int id, String nom, String description, double prix, int stock, Categorie categorie,String imagePath) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.stock = stock;
        this.categorie = categorie;
        this.imagePath = imagePath;
    }

    public Produit(String nom, String description, double prix, int stock, Categorie categorie,String imagePath) {
        this(0, nom, description, prix, stock, categorie,imagePath);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public Categorie getCategorie() { return categorie; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }
    
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }


    @Override
    public String toString() {
        return nom + " - " + (categorie != null ? categorie.getNom() : "Sans catégorie");
    }
}
