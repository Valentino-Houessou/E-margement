package controllers.administrateur.gestionDesParametres;

import models.Cours;
import models.Enseignant;


import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Yoan D on 11/03/2016.
 */
public class parametresProfilCree {

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

    private List<Cours> lesCoursDuProf;

    /**
     * Constructeur par défaut
     */
    private parametresProfilCree()
    {
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

    /**
     * Remet à zero les paramettres
     */
    public void remiseAzero()
    {
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
