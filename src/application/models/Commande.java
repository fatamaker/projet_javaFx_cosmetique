package application.models;

import java.util.List;
import java.util.Map;

public class Commande {
    private int id;
    private int utilisateurId; 
    private String nomClient;
    private String adresse;
    private String telephone;
    private String email;
    private double total;
    private List<LigneCommande> lignes;

   
    public Commande(int utilisateurId, String nomClient, String adresse, String telephone, String email, double total, List<LigneCommande> lignes) {
        this.utilisateurId = utilisateurId;
        this.nomClient = nomClient;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.total = total;
        this.lignes = lignes;
    }

   
	
	  public int getUtilisateurId() { return utilisateurId; }
	  
	  public void setUtilisateurId(int utilisateurId) { this.utilisateurId =
	  utilisateurId; }
	 

    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNomClient() {
		return nomClient;
	}
	public void setNomClient(String nomClient) {
		this.nomClient = nomClient;
	}
	public String getAdresse() {
		return adresse;
	}
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public List<LigneCommande> getLignes() {
		return lignes;
	}
	public void setLignes(List<LigneCommande> lignes) {
		this.lignes = lignes;
	}
	@Override
	public String toString() {
		return "CommandeM [nomClient=" + nomClient + ", adresse=" + adresse + ", telephone=" + telephone + ", email="
				+ email + ", total=" + total + ", lignes=" + lignes + "]";
	}

}