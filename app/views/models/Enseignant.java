package views.models;
//TODO

import javax.persistence.*;
import com.avaje.ebean.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Enseignant extends Model{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int id;
    public String statut;
    @OneToOne
    public Utilisateur sonUtilisateur;
    @ManyToMany(cascade=CascadeType.PERSIST)
    public List<Matiere> sesMatieres;

    public static Finder<Long, Enseignant> find = new Finder<Long, Enseignant>(Enseignant.class);

   //TODO : Quand on cré un enseignant il faut ajouter le module Enseignant dans la liste de module de son utilisateur Vous pouvez utiliser la fonction droitEnseignant() de utilisateur*/
    public Enseignant(String statut, Utilisateur sonUtilisateur) {
        this.statut = statut;
        this.sonUtilisateur = sonUtilisateur;
        this.sesMatieres = new ArrayList<Matiere>();
    }

    public static Enseignant create(String nom,String prenom,String adresseMail,String motDePasse,String dateDeNaissance,String lienPhoto, String statut) {
        Utilisateur user =  Utilisateur.create(nom, prenom, adresseMail,motDePasse, dateDeNaissance, lienPhoto);
        Utilisateur.droitEnseignant(user.id);
        Enseignant enseignant = new Enseignant(statut, user);

        enseignant.save();
        return enseignant;
    }

    public static Enseignant update(int id, String nom,String prenom,String adresseMail,String motDePasse,String dateDeNaissance,String lienPhoto, String statut) {
        Enseignant enseignant = find.where().eq("id", id).findUnique();

        enseignant.statut = statut;
        Utilisateur.updateUtilisateur(enseignant.sonUtilisateur.id, nom, prenom, adresseMail, motDePasse, dateDeNaissance, lienPhoto);

        enseignant.update();

        return enseignant;

    }

    public static void delete(int id) {
        Enseignant enseignant = find.where().eq("id", id).findUnique();
        Utilisateur utilisateur = enseignant.sonUtilisateur;
        Ebean.delete(enseignant);
        if (!Administrateur.utilisateurAdmin(utilisateur))
            Ebean.delete(utilisateur);
    }

    public static boolean utilisateurEnseignant(Utilisateur user){
        return (Enseignant.find.where().eq("sonUtilisateur", user).findUnique()) != null ? true : false;
    }

    public static Enseignant findById (long id){
        return find.ref(id);
    }

    public static List<Enseignant> findAll() {
        return find.all();
    }
}