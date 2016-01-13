package models;
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
    @JoinColumn(name = "id")
    public Utilisateur sonUtilisateur;
    @ManyToMany(cascade=CascadeType.PERSIST)
    //public List<Matiere> sesMatieres;

    public static Finder<Long, Enseignant> find = new Finder<Long, Enseignant>(Enseignant.class);

   //TODO : Quand on cré un enseignant il faut ajouter le module Enseignant dans la liste de module de son utilisateur Vous pouvez utiliser la fonction droitEnseignant() de utilisateur*/
    public Enseignant(String statut, Utilisateur utilisateur) {
        this.statut = statut;
        this.sonUtilisateur = utilisateur;
        //this.sesMatieres = new ArrayList<Matiere>();
        this.save();
    }

    public static Enseignant update(int id, String statut, Utilisateur utilisateur) {
        Enseignant enseignant = find.where().eq("id", id).findUnique();

        enseignant.statut = statut;
        enseignant.sonUtilisateur = utilisateur;

        enseignant.save();

        return enseignant;

    }

    //TODO : Il faudrait vérifier si il n'a pas un compte admin avant de le supprimer. Vous pouvez utiliser les fonction utilisateurAdmin et utilisateurEnseignant
    public static void delete(int id) {
        Enseignant enseignant = find.where().eq("id", id).findUnique();
        Utilisateur utilisateur = enseignant.sonUtilisateur;
        Ebean.delete(enseignant);
        Ebean.delete(utilisateur);
    }

    public static boolean utilisateurEnseignant(Utilisateur user){
        return (Enseignant.find.where().eq("sonUtilisateur", user).findUnique()) != null ? true : false;
    }

    public static List<Enseignant> findAll() {
        return find.all();
    }
}
