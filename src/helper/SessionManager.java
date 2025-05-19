package helper;

import application.models.Utilisateur;

public class SessionManager {
    private static Utilisateur utilisateurConnecte;

    public static void setUtilisateurConnecte(Utilisateur user) {
        utilisateurConnecte = user;
    }

    public static Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }
}
