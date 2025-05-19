package application.models;

import java.util.*;

public class Panier {
    private static Panier instance;
    private Map<Produit, Integer> produits;

    private Panier() {
        produits = new HashMap<>();
    }

    public static Panier getInstance() {
        if (instance == null) {
            instance = new Panier();
        }
        return instance;
    }

    public void ajouterProduit(Produit p, int quantite) {
        produits.put(p, produits.getOrDefault(p, 0) + quantite);
    }

    public void retirerProduit(Produit p, int quantite) {
        if (produits.containsKey(p)) {
            int qte = produits.get(p) - quantite;
            if (qte <= 0) produits.remove(p);
            else produits.put(p, qte);
        }
    }

    public List<Produit> getProduits() {
        return new ArrayList<>(produits.keySet());
    }

    public int getQuantite(Produit p) {
        return produits.getOrDefault(p, 0);
    }

    public double getSousTotal() {
        return produits.entrySet().stream()
            .mapToDouble(e -> e.getKey().getPrix() * e.getValue())
            .sum();
    }

    public Map<Produit, Integer> getDetails() {
        return produits;
    }

    public void viderPanier() {
        produits.clear();
    }
}
