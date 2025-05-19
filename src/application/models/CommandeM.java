package application.models;

import database.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CommandeM {
    private Connection connection;

    public CommandeM() {
        connection = DbConnection.getDatabaseConnection().getConnection();
    }

    public boolean ajouterCommande(Commande commande) {
        try {
        	String insertCommande = "INSERT INTO commande( nom_client, adresse, telephone, email, total) VALUES ( ?, ?, ?, ?, ?)";
        	PreparedStatement psCommande = connection.prepareStatement(insertCommande, Statement.RETURN_GENERATED_KEYS);
        	//psCommande.setInt(1, commande.getUtilisateurId());
        	psCommande.setString(2, commande.getNomClient());
        	psCommande.setString(3, commande.getAdresse());
        	psCommande.setString(4, commande.getTelephone());
        	psCommande.setString(5, commande.getEmail());
        	psCommande.setDouble(6, commande.getTotal());
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
}