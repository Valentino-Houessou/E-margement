package controllers.administrateur.gestionDesParametres;

import models.*;

import javax.rmi.CORBA.Util;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class Singleton pour géré tous les parametres de la fonction exporté feuille de présence
 */
public class parametresExportationFeuillesPresence
{
    private String etape;
    private int selectionUniversite;
    private int selectionBatiment;
    private String selectionFiliere;
    private int selectionPromotion;
    private String selectionDate;

    private List<Universite> listeUniversites; // Affiche la liste de tout les universités enregistré dans la base
    private List<Batiment> listeBatiments;     // Affiche la liste des batiments d'une université sélectionné
    private List<Filiere> listeFilieres;        // Affiche la liste des filières d'un batiment d'une université
    private List<Promotion> listePromotion;    // Affiche la liste des promotions d'une filière

    // Construction de la feuille de présence
    private List<Utilisateur> lesProfesseurs;
    private List<Utilisateur> lesEtudiants;
    private List<Cours> coursDuJourDeLaPromotion;


    private static parametresExportationFeuillesPresence INSTANCE = null;

    private parametresExportationFeuillesPresence()
    {
        this.etape = "vide";
        this.selectionUniversite = 0;
        this.selectionBatiment = 0;
        this.selectionFiliere = "vide";
        this.selectionPromotion = 0;
        this.selectionDate = "vide";

        this.listeUniversites = null;
        this.listeBatiments = null;
        this.listeFilieres = null;
        this.listePromotion = null;


        this.lesProfesseurs = null;
        this.lesEtudiants = null;
        this.coursDuJourDeLaPromotion = null;



    }

    /** Point d'accès pour l'instance unique du singleton **/
    public static parametresExportationFeuillesPresence getInstance()
    {
        if (INSTANCE == null)
        {
            synchronized(parametresExportationFeuillesPresence.class)
            {
                if (INSTANCE == null)
                {
                    INSTANCE = new parametresExportationFeuillesPresence();
                }
            }
        }
        return INSTANCE;
    }

    public String getEtape() {
        return etape;
    }

    public void setEtape(String etape) {
        this.etape = etape;
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

    public String getSelectionDate() {
        return selectionDate;
    }

    public void setSelectionDate(String selectionDate) {
        this.selectionDate = selectionDate;
    }

    public List<Universite> getListeUniversites() {
        return listeUniversites;
    }

    public void setListeUniversites(List<Universite> listeUniversites) {
        this.listeUniversites = listeUniversites;
    }

    public List<Batiment> getListeBatiments() {
        return listeBatiments;
    }

    public void setListeBatiments(List<Batiment> listeBatiments) {
        this.listeBatiments = listeBatiments;
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

    public List<Cours> getCoursDuJourDeLaPromotion() {
        return coursDuJourDeLaPromotion;
    }

    public void setCoursDuJourDeLaPromotion(List<Cours> coursDuJourDeLaPromotion) {
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

    /**
     * Récupérer :
     * 1 - La liste des cours d'une promotion par rapportà un jour donnée (Libellé / heure début - heure fin
     * 2 - La liste des professeurs qui enseigne ses cours
     * 3 - Les étudiants qui ont badget pour se cours
     * @param idpromotionselectionnee
     * @param datechoisie
     * @return Les cours d'une promotion pour une date sélectionné
     * @throws ParseException
     */
    public static List<Cours> getCoursDuJourDeLaPromotion(int idpromotionselectionnee, String datechoisie) throws ParseException
    {

        // 1 - Récupération des cours d'une promotion qui ont eux lieu à la date choisie
        // La requête récupére en même temps le libellé de la matière et les étudiants présents au cours
        SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd" );
        Date date;
        date = originalFormat.parse(datechoisie);
        String resultatParse = targetFormat.format(date);

        List<Cours> lesCoursDuJourDeLaPromotion = Cours.find.where().eq("DATE(heure_Debut)",resultatParse).eq("sa_promo_id", idpromotionselectionnee).findList();

        return lesCoursDuJourDeLaPromotion;
    }

    /**
     * Remonte l'identitée des étudiants qui sont dans une promotion donnée
     * @param idpromotionselectionnee
     * @return Les étudiants de la promotion
     */
    public static List<Utilisateur> getEtudiantsParPromotion(int idpromotionselectionnee)
    {
        // 1 - Retrouver la promotion
        Promotion lapromotionchoisie = Promotion.find.where().eq("id", idpromotionselectionnee).findUnique();

        List<Etudiant> lesEtudiants = lapromotionchoisie.sesEtudiants;

        List<Utilisateur> utilisateurs = new ArrayList<Utilisateur>();

        for (Etudiant etudiant : lesEtudiants) {

            utilisateurs.add(etudiant.sonUtilisateur);
        }

        // Trie des utilisateurs par ordre croissant par rapport à leur nom de famille
        Collections.sort(utilisateurs, new Comparator<Utilisateur>() {
            @Override
            public int compare(Utilisateur tc1, Utilisateur tc2) {
                return tc1.nom.compareTo(tc2.nom);
            }
        });

        return utilisateurs;
    }



}



