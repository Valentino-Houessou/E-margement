package controllers.administrateur.gestionDesParametres;

import models.*;


import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Yoan D on 11/03/2016.
 */
public class parametresProfilCree {

    private int idprofesseur;
    private String nom;
    private String prenom;
    private String adresseMail;
    private String mdp;
    private String datenaissance;
    private String status;
    private String droits;
    private String lienphoto;

    private Enseignant lenseignant;

    private static parametresProfilCree INSTANCE = null;

    // Affichage des cours affectés au professeur
    private List<Cours> lesCoursDuProf;

    // Gestion des listes déroulantes
    private List<Universite> listeUniversite;   // Affiche la liste de tout les universités enregistré dans la base
    private List<Batiment> listeBatiments;      // Affiche la liste des batiments d'une université sélectionné
    private List<Filiere> listeFilieres;        // Affiche la liste des filières d'un batiment d'une université
    private List<Promotion> listePromotion;     // Affiche la liste des promotion d'une filière choisie

    private int selectionUniversite;
    private int selectionBatiment;
    private String selectionFiliere;
    private int selectionPromotion;

    private String etapeListes; // Gére les listes déroulantes pour "Affecter un cours" à un enseignant

    // Affichage des matières d'une promotion choisie
    private List<Matiere> lesMatieres;
    private int selectionMatiere;

    // Affichage de tous les cours d'une matiere qu'une promotion a
    private List<Cours> lesCoursDelaMatiereDeLaPromotion;

    /**
     * Constructeur par défaut
     */
    private parametresProfilCree()
    {
        this.idprofesseur=0;
        this.nom = "";
        this.prenom = "";
        this.adresseMail = "";
        this.mdp = "";
        this.datenaissance = "";
        this.status = "";
        this.droits = "";
        this.lienphoto = "";
        this.lenseignant = null;
        this.lesCoursDuProf = null;
        this.listeUniversite = null;
        this.etapeListes="";
        this.listeBatiments=null;
        this.listeFilieres=null;
        this.listePromotion=null;
        this.selectionUniversite=0;
        this.selectionBatiment=0;
        this.selectionFiliere="";
        this.selectionPromotion=0;
        this.lesMatieres = null;
        this.selectionMatiere=0;
        this.lesCoursDelaMatiereDeLaPromotion=null;
    }

    /** Point d'accès pour l'instance unique du singleton **/
    public static parametresProfilCree getInstance()
    {
        if (INSTANCE == null)
        {
            synchronized(parametresProfilCree.class)
            {
                if (INSTANCE == null)
                {
                    INSTANCE = new parametresProfilCree();
                }
            }
        }
        return INSTANCE;
    }

    public int getIdprofesseur() {
        return idprofesseur;
    }

    public void setIdprofesseur(int idprofesseur) {
        this.idprofesseur = idprofesseur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresseMail() {
        return adresseMail;
    }

    public void setAdresseMail(String adresseMail) {
        this.adresseMail = adresseMail;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getDatenaissance() {
        return datenaissance;
    }

    public void setDatenaissance(String datenaissance) {
        this.datenaissance = datenaissance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDroits() {
        return droits;
    }

    public void setDroits(String droits) {
        this.droits = droits;
    }

    public String getLienphoto() {
        return lienphoto;
    }

    public void setLienphoto(String lienphoto) {
        this.lienphoto = lienphoto;
    }

    public Enseignant getLenseignant() {
        return lenseignant;
    }

    public void setLenseignant(Enseignant lenseignant) {
        this.lenseignant = lenseignant;

        this.setIdprofesseur(this.lenseignant.id);
        this.setNom(this.lenseignant.sonUtilisateur.nom);
        this.setPrenom(this.lenseignant.sonUtilisateur.prenom);
        this.setAdresseMail(this.lenseignant.sonUtilisateur.adresseMail);

        if((this.lenseignant.sonUtilisateur.dateDeNaissance != null) && (!this.lenseignant.sonUtilisateur.dateDeNaissance.equals("")))
        {
            String dtn = new SimpleDateFormat("dd/MM/yyyy").format(this.lenseignant.sonUtilisateur.dateDeNaissance);
            this.setDatenaissance(dtn);
            this.setStatus(this.lenseignant.statut);
        }


        if(this.lenseignant.sonUtilisateur.sesModules.size() > 1)
        {
            this.setDroits("OUI"); // L'enseignant est aussi administrateur
        }else{
            this.setDroits("NON"); // L'enseignant est que enseignant
        }

        if((this.lenseignant.sonUtilisateur.lienPhoto != null) && (!this.lenseignant.sonUtilisateur.lienPhoto.equals("")))
        {
            String [] lien = this.lenseignant.sonUtilisateur.lienPhoto.split("/"); // On retire le public et on garde le dossier photos-utilisateurs/photo...
            this.setLienphoto(lien[1]+"/"+lien[2]);
        }
    }

    public List<Cours> getLesCoursDuProf() {
        return lesCoursDuProf;
    }

    public void setLesCoursDuProf(List<Cours> lesCoursDuProf) {
        this.lesCoursDuProf = lesCoursDuProf;
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

    public List<Batiment> getListeBatiments() {
        return listeBatiments;
    }

    public void setListeBatiments(List<Batiment> listeBatiments) {
        this.listeBatiments = listeBatiments;
    }

    public String getEtapeListes() {
        return etapeListes;
    }

    public void setEtapeListes(String etapeListes) {
        this.etapeListes = etapeListes;
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

    public List<Promotion> getListePromotion() {
        return listePromotion;
    }

    public void setListePromotion(List<Promotion> listePromotion) {
        this.listePromotion = listePromotion;
    }

    public int getSelectionPromotion() {
        return selectionPromotion;
    }

    public void setSelectionPromotion(int selectionPromotion) {
        this.selectionPromotion = selectionPromotion;
    }

    public List<Matiere> getLesMatieres() {
        return lesMatieres;
    }

    public void setLesMatieres(List<Matiere> lesMatieres) {
        this.lesMatieres = lesMatieres;
    }


    public int getSelectionMatiere() {
        return selectionMatiere;
    }

    public void setSelectionMatiere(int selectionMatiere) {
        this.selectionMatiere = selectionMatiere;
    }

    public List<Cours> getLesCoursDelaMatiereDeLaPromotion() {
        return lesCoursDelaMatiereDeLaPromotion;
    }

    public void setLesCoursDelaMatiereDeLaPromotion(List<Cours> lesCoursDelaMatiereDeLaPromotion) {
        this.lesCoursDelaMatiereDeLaPromotion = lesCoursDelaMatiereDeLaPromotion;
    }

    /**
     * Remet à zero les paramettres
     */
    public void remiseAzero()
    {
        this.idprofesseur=0;
        this.nom = "";
        this.prenom = "";
        this.adresseMail = "";
        this.mdp = "";
        this.datenaissance = "";
        this.status = "";
        this.droits = "";
        this.lienphoto = "";
        this.lenseignant = null;
        this.lesCoursDuProf = null;
        this.listeUniversite = null;
        this.etapeListes="";
        this.listeBatiments=null;
        this.listeFilieres=null;
        this.listePromotion=null;
        this.selectionUniversite=0;
        this.selectionBatiment=0;
        this.selectionFiliere="";
        this.selectionPromotion=0;
        this.lesMatieres = null;
        this.selectionMatiere=0;
        this.lesCoursDelaMatiereDeLaPromotion=null;
    }

    /**
     * Initialisation des paramettres pour affichage
     * @param nom
     * @param prenom
     * @param adresseMail
     * @param datenaissance
     * @param status
     * @param droits
     * @param lienphoto
     */
    public void affectation(String nom, String prenom, String adresseMail, String datenaissance, String status, String droits, String lienphoto)
    {
        this.nom = nom;
        this.prenom = prenom;
        this.adresseMail = adresseMail;
        this.datenaissance = datenaissance;
        this.status = status;
        this.droits = droits;
        if(lienphoto != "")
        {
            String [] lien = lienphoto.split("/"); // On retire le public et on garde le dossier photos-utilisateurs/photo...
            this.lienphoto = lien[1]+"/"+lien[2];

        }
    }
}
