package application.models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import database.DbConnection;

public class CategorieM {

    private Connection con;

    public CategorieM() {
    	 con = DbConnection.getDatabaseConnection().getConnection();
    }

    


  

    public List<Categorie> getAllCategories() {
        List<Categorie> list = new ArrayList<>();
        String sql = "SELECT * FROM categorie";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Categorie(rs.getInt("id"), rs.getString("nom")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void ajouterCategorie(String nom) {
        String sql = "INSERT INTO categorie(nom) VALUES (?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nom);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void supprimerCategorie(int id) {
        String sql = "DELETE FROM categorie WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
