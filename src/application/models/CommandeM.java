package application.models;

import database.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CommandeM {
    private Connection connection;

    public CommandeM() {
        connection = DbConnection.getDatabaseConnection().getConnection();
    }

    public boolean ajouterCommande(Commande commande) {
        try {
        	String insertCommande = "INSERT INTO commande(utilisateur_id, nom_client, adresse, telephone, email, total, date_commande) VALUES (?, ?, ?, ?, ?, ?, ?)";

        	PreparedStatement psCommande = connection.prepareStatement(insertCommande, Statement.RETURN_GENERATED_KEYS);
        	psCommande.setInt(1, commande.getUtilisateurId());
        	psCommande.setString(2, commande.getNomClient());
        	psCommande.setString(3, commande.getAdresse());
        	psCommande.setString(4, commande.getTelephone());
        	psCommande.setString(5, commande.getEmail());
        	psCommande.setDouble(6, commande.getTotal());
        	psCommande.setTimestamp(7, java.sql.Timestamp.valueOf(commande.getDateCommande()));

            psCommande.executeUpdate();

            ResultSet rs = psCommande.getGeneratedKeys();
            int commandeId = -1;
            if (rs.next()) {
                commandeId = rs.getInt(1);
            }

            String insertLigne = "INSERT INTO ligne_commande(commande_id, produit_id, quantite) VALUES (?, ?, ?)";
            PreparedStatement psLigne = connection.prepareStatement(insertLigne);

            for (LigneCommande lc : commande.getLignes()) {
                psLigne.setInt(1, commandeId);
                psLigne.setInt(2, lc.getProduit().getId());
                psLigne.setInt(3, lc.getQuantite());
                psLigne.executeUpdate();
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Commande> getCommandesParUtilisateur(int utilisateurId) {
        List<Commande> commandes = new ArrayList<>();
        
        String query = "SELECT * FROM commande WHERE utilisateur_id = ?";
        

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, utilisateurId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int commandeId = rs.getInt("id");
                LocalDateTime dateCommande = rs.getTimestamp("date_commande").toLocalDateTime();

                List<LigneCommande> lignes = getLignesParCommande(commandeId);
                Commande commande = new Commande(
                	    rs.getInt("utilisateur_id"),
                	    rs.getString("nom_client"),
                	    rs.getString("adresse"),
                	    rs.getString("telephone"),
                	    rs.getString("email"),
                	    rs.getDouble("total"),
                	    lignes,
                	    dateCommande
                	);

                commande.setId(commandeId); 

                commandes.add(commande);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return commandes;
    }

   
    private List<LigneCommande> getLignesParCommande(int commandeId) {
        List<LigneCommande> lignes = new ArrayList<>();
        String sql = "SELECT * FROM ligne_commande WHERE commande_id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, commandeId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int produitId = rs.getInt("produit_id");
                int quantite = rs.getInt("quantite");

                Produit produit = getProduitById(produitId); 

                if (produit != null) {
                    LigneCommande ligne = new LigneCommande(produit, quantite);
                    lignes.add(ligne);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lignes;
    }
    
    private Produit getProduitById(int id) {
        Produit produit = null;
        String sql = "SELECT * FROM produit WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int categorieId = rs.getInt("categorie");

                Categorie categorie = getCategorieById(categorieId); 

                produit = new Produit(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("description"),
                    rs.getDouble("prix"),
                    rs.getInt("stock"),
                    categorie,
                    rs.getString("image_path")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return produit;
    }

    
    private Categorie getCategorieById(int id) {
        Categorie categorie = null;
        String sql = "SELECT * FROM categorie WHERE id = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                categorie = new Categorie(
                    rs.getInt("id"),
                    rs.getString("nom")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categorie;
    }




}