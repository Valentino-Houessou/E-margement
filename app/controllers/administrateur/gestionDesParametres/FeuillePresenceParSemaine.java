package controllers.administrateur.gestionDesParametres;


import models.Cours;
import models.Etudiant;
import models.Promotion;
import models.Utilisateur;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FeuillePresenceParSemaine {

    // Construction de la feuille de présence
    private List<Utilisateur> lesProfesseurs;
    private List<Utilisateur> lesEtudiants;
    private List<Cours> coursDuJourDeLaPromotion;

    private String selectionDate;

    public FeuillePresenceParSemaine() {
    }

    public FeuillePresenceParSemaine(List<Utilisateur> lesProfesseurs, List<Utilisateur> lesEtudiants, List<Cours> coursDuJourDeLaPromotion) {
        this.lesProfesseurs = lesProfesseurs;
        this.lesEtudiants = lesEtudiants;
        this.coursDuJourDeLaPromotion = coursDuJourDeLaPromotion;
    }

    public List<Utilisateur> getLesProfesseurs() {
        return lesProfesseurs;
    }

    public void setLesProfesseurs(List<Utilisateur> lesProfesseurs) {
        this.lesProfesseurs = lesProfesseurs;
    }

    public List<Utilisateur> getLesEtudiants() {
        return lesEtudiants;
    }

    public void setLesEtudiants(List<Utilisateur> lesEtudiants) {
        this.lesEtudiants = lesEtudiants;
    }

    public List<Cours> getCoursDuJourDeLaPromotion() {
        return coursDuJourDeLaPromotion;
    }

    public void setCoursDuJourDeLaPromotion(List<Cours> coursDuJourDeLaPromotion) {
        this.coursDuJourDeLaPromotion = coursDuJourDeLaPromotion;
    }

    public String getSelectionDate() {
        return selectionDate;
    }

    public void setSelectionDate(String selectionDate) {
        this.selectionDate = selectionDate;
    }
}
