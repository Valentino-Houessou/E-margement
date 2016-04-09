package controllers.administrateur.gestionDesParametres;

import models.Administrateur;
import models.Enseignant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yoan D on 16/03/2016.
 */
public class parametresAdmin {

    private String nom;
    private String prenom;
    private String adresseMail;
    private String mdp;
    private String datenaissance;
    private String status;
    private String lienphoto;
    private int referantCFA;

    private List<Administrateur> lesAdmin;
    private List<Enseignant> lesEnseingnantAdmin;
    private Administrateur profilAdmin;

    private static parametresAdmin INSTANCE = null;

    private parametresAdmin()
    {
        this.nom = "";
        this.prenom = "";
        this.adresseMail = "";
        this.mdp = "";
        this.datenaissance = "";
        this.status = "";
        this.lienphoto = "";
        this.referantCFA = 0;

        this.lesAdmin = null;
        this.lesEnseingnantAdmin = null;
        this.profilAdmin = null;
    }

    /** Point d'accès pour l'instance unique du singleton **/
    public static parametresAdmin getInstance()
    {
        if (INSTANCE == null)
        {
            synchronized(parametresAdmin.class)
            {
                if (INSTANCE == null)
                {
                    INSTANCE = new parametresAdmin();
                }
            }
        }
        return INSTANCE;
    }

    public List<Administrateur> getLesAdmin() {
        return lesAdmin;
    }

    public void setLesAdmin(List<Administrateur> lesAdmin) {
        this.lesAdmin = lesAdmin;
    }

    public List<Enseignant> getLesEnseingnantAdmin() {
        return lesEnseingnantAdmin;
    }

    public Administrateur getProfilAdmin() {
        return profilAdmin;
    }

    public void setProfilAdmin(Administrateur profilAdmin) {
        this.profilAdmin = profilAdmin;


        this.setNom(this.profilAdmin.sonUtilisateur.nom);
        this.setPrenom(this.profilAdmin.sonUtilisateur.prenom);
        this.setAdresseMail(this.profilAdmin.sonUtilisateur.adresseMail);

        if((this.profilAdmin.sonUtilisateur.dateDeNaissance != null) && (!this.profilAdmin.sonUtilisateur.dateDeNaissance.equals("")))
        {
            String dtn = new SimpleDateFormat("dd/MM/yyyy").format(this.profilAdmin.sonUtilisateur.dateDeNaissance);
            this.setDatenaissance(dtn);
            this.setStatus(this.profilAdmin.statut);
        }


        if((this.profilAdmin.sonUtilisateur.lienPhoto != null) && (!this.profilAdmin.sonUtilisateur.lienPhoto.equals("")))
        {
            String [] lien = this.profilAdmin.sonUtilisateur.lienPhoto.split("/"); // On retire le public et on garde le dossier photos-utilisateurs/photo...
            this.setLienphoto(lien[1]+"/"+lien[2]);
        }

        if(this.profilAdmin.referentCFA == true)
            this.referantCFA = 1;
        else
            this.referantCFA =0;
    }

    public void setLesEnseingnantAdmin(List<Enseignant> lesEnseingnantAdmin) {
        this.lesEnseingnantAdmin = lesEnseingnantAdmin; // La on récupérer toute la liste des enseignant

        // Filtre pour récupérer que les enseignants admin
        List<Enseignant> filtreEnseignantAdmin = new ArrayList<Enseignant>();

        for(Enseignant enseignant : this.lesEnseingnantAdmin)
        {
            if(enseignant.sonUtilisateur.sesModules.size() > 1){
                filtreEnseignantAdmin.add(enseignant);
            }
        }

        this.lesEnseingnantAdmin = filtreEnseignantAdmin;
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

    public String getLienphoto() {
        return lienphoto;
    }

    public void setLienphoto(String lienphoto) {
        this.lienphoto = lienphoto;
    }

    public int getReferantCFA() {
        return referantCFA;
    }

    public void setReferantCFA(int referantCFA) {
        this.referantCFA = referantCFA;
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
        this.lienphoto = "";
        this.referantCFA =0;

        this.lesAdmin = null;
        this.lesEnseingnantAdmin = null;
        this.profilAdmin = null;
    }
}
