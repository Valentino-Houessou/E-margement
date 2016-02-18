package views.models;


import javax.persistence.*;
import com.avaje.ebean.*;

import java.util.List;

@Entity
public class Administrateur extends Model {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int id;
    public String statut;
    @OneToOne
    public Utilisateur sonUtilisateur;

    //TODO : Quand on cré un etudiant il faut ajouter le module etudiant dans la liste de module de son utilisateur Vous pouvez utiliser la fonction droitEtudiant() de utilisateur*/

    //Finder for retrieve data in database
    public static Finder<Long, Administrateur> find = new Finder<Long, Administrateur>(Administrateur.class);

    public Administrateur(String statut, Utilisateur sonUtilisateur) {
        this.statut = statut;
        this.sonUtilisateur = sonUtilisateur;
    }

    public static Administrateur create(String nom,String prenom,String adresseMail,String motDePasse,String dateDeNaissance,String lienPhoto, String statut) {
        Utilisateur user =  Utilisateur.create(nom, prenom, adresseMail,motDePasse, dateDeNaissance, lienPhoto);
        Utilisateur.droitAdmin(user.id);
        Administrateur admin = new Administrateur(statut, user);

        admin.save();
        return admin;
    }

    public static Administrateur update(int id, String nom,String prenom,String adresseMail,String motDePasse,String dateDeNaissance,String lienPhoto, String statut) {
        Administrateur admin = find.where().eq("id", id).findUnique();

        admin.statut = statut;
        Utilisateur.updateUtilisateur(admin.id, nom, prenom, adresseMail, motDePasse, dateDeNaissance, lienPhoto);
        admin.update();
        return admin;

    }

    public static void delete(int id) {
        Administrateur admin = find.where().eq("id", id).findUnique();
        Utilisateur utilisateur = admin.sonUtilisateur;
        Ebean.delete(admin);
        if (!Enseignant.utilisateurEnseignant(utilisateur))
            Ebean.delete(utilisateur);
    }

    public static boolean utilisateurAdmin(Utilisateur user){
        return (Administrateur.find.where().eq("sonUtilisateur", user).findUnique()) != null ? true : false;
    }

    public static Administrateur findById (long id){
        return find.ref(id);
    }

    public static List<Administrateur> findAll() {
        return find.all();
    }

}

