package controllers.administrateur.gestionDesParametres;

import models.Batiment;
import models.Filiere;
import models.Promotion;
import models.Universite;

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
    private int selectionPromotion;

    private String etape;

    private static parametresEtudiant INSTANCE = null;

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

    public int getSelectionPromotion() {
        return selectionPromotion;
    }

    public void setSelectionPromotion(int selectionPromotion) {
        this.selectionPromotion = selectionPromotion;
    }

    public String getEtape() {
        return etape;
    }

    public void setEtape(String etape) {
        this.etape = etape;
    }
}
