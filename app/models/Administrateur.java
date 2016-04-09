package models;


import javax.persistence.*;
import com.avaje.ebean.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Entity
public class Administrateur extends Model {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int id;
    public String statut;
    @OneToOne
    public Utilisateur sonUtilisateur;
    public boolean referentCFA;

    //Finder for retrieve data in database
    public static Finder<Long, Administrateur> find = new Finder<Long, Administrateur>(Administrateur.class);

    /**
     * Constructeur
     * @param statut
     * @param sonUtilisateur
     */
    public Administrateur(String statut, Utilisateur sonUtilisateur, boolean referentCFA) {
        this.statut = statut;
        this.sonUtilisateur = sonUtilisateur;
        this.referentCFA = referentCFA;
    }

    /**
     * Création d'un nouveau profil administrateur
     * @param nom
     * @param prenom
     * @param adresseMail
     * @param motDePasse
     * @param dateDeNaissance
     * @param lienPhoto
     * @param statut
     * @return
     */
    public static Administrateur create(String nom,String prenom,String adresseMail,String motDePasse,String dateDeNaissance,String lienPhoto, String statut, boolean referentCFA)
    {
        Utilisateur user =  Utilisateur.create(nom, prenom, adresseMail,motDePasse, dateDeNaissance, lienPhoto);
        Utilisateur.droitAdmin(user.id); // Affectation automatique de son droit "ADMINISTRATEUR"
        Administrateur admin = new Administrateur(statut, user, referentCFA);

        admin.save();
        return admin;
    }

    /**
     * Mise à jours d'un profil administrateur
     * @param id
     * @param nom
     * @param prenom
     * @param adresseMail
     * @param motDePasse
     * @param dateDeNaissance
     * @param lienPhoto
     * @param statut
     * @param referentCFA
     * @return
     */
    public static Administrateur update(int id, String nom,String prenom,String adresseMail,String motDePasse,String dateDeNaissance,String lienPhoto, String statut, boolean referentCFA)
    {
        Administrateur admin = find.where().eq("id", id).findUnique();

        if((statut != null) && (statut != ""))
        {
            admin.statut = statut;
        }

        admin.referentCFA = referentCFA;

        Utilisateur.updateUtilisateur(admin.sonUtilisateur.id, nom, prenom, adresseMail, motDePasse, dateDeNaissance, lienPhoto);
        admin.update();
        return admin;
    }

    /**
     * Suppression d'un profil administrateur
     * @param id
     */
    public static void delete(long id) {
        //Suppression dans la table Administrateur
        Administrateur admin = find.where().eq("id", id).findUnique();

        admin.delete();

        // suppression dans la table modules
        Utilisateur utilisateur = admin.sonUtilisateur;

        utilisateur.sesModules.remove(Module.findByLibelle("ADMINISTRATEURS"));

        utilisateur.update();

        // Suppression dans la table utilisateur
        utilisateur.delete();
    }

    /**
     *
     * @param user
     * @return
     */
    public static boolean utilisateurAdmin(Utilisateur user){
        return (Administrateur.find.where().eq("sonUtilisateur", user).findUnique()) != null ? true : false;
    }

    /**
     * Récupération du profil de l'administrateur
     * @param id
     * @return
     */
    public static Administrateur findById (long id){
        return find.ref(id);
    }

    /**
     * Récupérer les administrateurs
     * @return les administrateurs
     */
    public static List<Administrateur> findAll()
    {
        List<Administrateur> lesAdmin = find.all();

        // Trie des utilisateurs par ordre croissant par rapport à leur nom de famille
        Collections.sort(lesAdmin, new Comparator<Administrateur>() {
            @Override
            public int compare(Administrateur tc1, Administrateur tc2) {
                return tc1.sonUtilisateur.nom.compareTo(tc2.sonUtilisateur.nom);
            }
        });

        return lesAdmin;
    }
}

