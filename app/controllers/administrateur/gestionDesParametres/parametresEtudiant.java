package controllers.administrateur.gestionDesParametres;

import models.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class parametresEtudiant {

    // Gestion des listes déroulantes
    private List<Universite> listeUniversite;   // Affiche la liste de tout les universités enregistré dans la base
    private List<Batiment> listeBatiments;      // Affiche la liste des batiments d'une université sélectionné
    private List<Filiere> listeFilieres;        // Affiche la liste des filières d'un batiment d'une université
    private List<Promotion> listePromotion;     // Affiche la liste des promotion d'une filière choisie

    private int selectionUniversite;
    private int selectionBatiment;
    private String selectionFiliere;
    private long selectionPromotion;

    private String etape; // Etape d'affichage des blocs évolutifs

    private static parametresEtudiant INSTANCE = null; // Singleton

    private Promotion laPromoAgerer; // La promotion qu'on a sélectionné
    private List<Etudiant> lesEtudiants; // Les étudiants de la promotions sélectionné

    private List<Etudiant> tousLesEtudiants; // Tout les étudiants toutes promotions confondus

    // Lors de la création d'un profil étudiant - Test si l'étudiant n'existe pas
    private int checkEtudiant;

    public parametresEtudiant()
    {
        this.listeUniversite = null;
        this.listeBatiments = null;
        this.listeFilieres = null;
        this.listePromotion = null;

        this.selectionUniversite=0;
        this.selectionBatiment=0;
        this.selectionFiliere="";
        this.selectionPromotion=0;
        this.laPromoAgerer = null;
        this.lesEtudiants = null;
        this.tousLesEtudiants = null;
        this.checkEtudiant = 0;
    }

    /** Point d'accès pour l'instance unique du singleton **/
    public static parametresEtudiant getInstance()
    {
        if (INSTANCE == null)
        {
            synchronized(parametresEtudiant.class)
            {
                if (INSTANCE == null)
                {
                    INSTANCE = new parametresEtudiant();
                }
            }
        }
        return INSTANCE;
    }

    public List<Batiment> getListeBatiments() {
        return listeBatiments;
    }

    public void setListeBatiments(List<Batiment> listeBatiments) {
        this.listeBatiments = listeBatiments;
    }

    public List<Universite> getListeUniversite() {
        return listeUniversite;
    }

    public void setListeUniversite(List<Universite> listeUniversite) {
        this.listeUniversite = listeUniversite;
    }

    public List<Filiere> getListeFilieres() {
        return listeFilieres;
    }

    public void setListeFilieres(List<Filiere> listeFilieres) {
        this.listeFilieres = listeFilieres;
    }

    public List<Promotion> getListePromotion() {
        return listePromotion;
    }

    public void setListePromotion(List<Promotion> listePromotion) {
        this.listePromotion = listePromotion;
    }

    public int getSelectionUniversite() {
        return selectionUniversite;
    }

    public void setSelectionUniversite(int selectionUniversite) {
        this.selectionUniversite = selectionUniversite;
    }

    public int getSelectionBatiment() {
        return selectionBatiment;
    }

    public void setSelectionBatiment(int selectionBatiment) {
        this.selectionBatiment = selectionBatiment;
    }

    public String getSelectionFiliere() {
        return selectionFiliere;
    }

    public void setSelectionFiliere(String selectionFiliere) {
        this.selectionFiliere = selectionFiliere;
    }

    public long getSelectionPromotion() {
        return selectionPromotion;
    }

    public void setSelectionPromotion(long selectionPromotion) {
        this.selectionPromotion = selectionPromotion;
    }

    public String getEtape() {
        return etape;
    }

    public void setEtape(String etape) {
        this.etape = etape;
    }

    public Promotion getLaPromoAgerer() {
        return laPromoAgerer;
    }

    public void setLaPromoAgerer(Promotion laPromoAgerer) {
        this.laPromoAgerer = laPromoAgerer;


        if(laPromoAgerer != null)
            this.setLesEtudiants(this.laPromoAgerer.sesEtudiants);

    }

    public List<Etudiant> getLesEtudiants() {
        return lesEtudiants;
    }

    public void setLesEtudiants(List<Etudiant> lesEtudiants) {

        this.lesEtudiants = lesEtudiants;

        // Trie des utilisateurs par ordre croissant par rapport à leur nom de famille
        Collections.sort(this.lesEtudiants, new Comparator<Etudiant>() {
            @Override
            public int compare(Etudiant tc1, Etudiant tc2) {
                return tc1.sonUtilisateur.nom.compareTo(tc2.sonUtilisateur.nom);
            }
        });
    }

    public List<Etudiant> getTousLesEtudiants() {
        return tousLesEtudiants;
    }

    public void setTousLesEtudiants(List<Etudiant> tousLesEtudiants) {
        this.tousLesEtudiants = tousLesEtudiants;
    }

    public int isCheckEtudiant() {
        return checkEtudiant;
    }

    public void setCheckEtudiant(int checkEtudiant) {
        this.checkEtudiant = checkEtudiant;
    }

    public void remiseAzero(){
        this.listeBatiments = null;
        this.listeFilieres = null;
        this.listePromotion = null;

        this.selectionUniversite=0;
        this.selectionBatiment=0;
        this.selectionFiliere="";
        this.selectionPromotion=0;
        this.laPromoAgerer = null;
        this.lesEtudiants = null;
        this.tousLesEtudiants = null;
        this.checkEtudiant = 0;
    }
}
