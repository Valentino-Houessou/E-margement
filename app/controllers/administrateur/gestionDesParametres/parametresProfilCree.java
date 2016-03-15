package controllers.administrateur.gestionDesParametres;

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

    private static parametresProfilCree INSTANCE = null;

    /**
     * Constructeur par défaut
     */
    public parametresProfilCree()
    {
        this.nom = "";
        this.prenom = "";
        this.adresseMail = "";
        this.mdp = "";
        this.datenaissance = "";
        this.status = "";
        this.droits = "";
        this.lienphoto = "";
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
