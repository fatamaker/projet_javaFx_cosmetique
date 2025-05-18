package application.models;

import database.DbConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduitM {
    private Connection connection;

    public ProduitM() {
        connection = DbConnection.getDatabaseConnection().getConnection();
    }

    public boolean create(Produit p) {
        try {
            String query = "INSERT INTO produit(nom, description, prix, stock, categorie,image_path) VALUES (?, ?, ?, ?, ?,?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, p.getNom());
            ps.setString(2, p.getDescription());
            ps.setDouble(3, p.getPrix());
            ps.setInt(4, p.getStock());
            ps.setInt(5, p.getCategorie().getId());
            ps.setString(6, p.getImagePath());  
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Produit> findAll() {
        List<Produit> produits = new ArrayList<>();
        try {
            
            String query = "SELECT p.id, p.nom, p.description, p.prix, p.categorie,p.stock,p.image_path,c.nom AS categorie_nom FROM produit p JOIN categorie c ON p.categorie = c.id";

            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Categorie cat = new Categorie(rs.getInt("categorie"), rs.getString("categorie_nom"));
            
                Produit produit = new Produit(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("description"),
                    rs.getDouble("prix"),
                    rs.getInt("stock"),
                    cat,
                    rs.getString("image_path")
                );
                produits.add(produit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produits;
    }


    public boolean update(Produit p) {
        try {
            String query = "UPDATE produit SET nom=?, description=?, prix=?, stock=?, categorie=? ,image_path=? WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, p.getNom());
            ps.setString(2, p.getDescription());
            ps.setDouble(3, p.getPrix());
            ps.setInt(4, p.getStock());
            ps.setInt(5, p.getCategorie().getId());
            ps.setString(6, p.getImagePath());
            ps.setInt(7, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean delete(int id) {
        try {
            String query = "DELETE FROM produit WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
