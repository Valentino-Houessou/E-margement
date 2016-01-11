package models;

import javax.persistence.*;
import com.avaje.ebean.*;
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

    public Enseignant(String statut, Utilisateur sonUtilisateur, List<Matiere> sesMatieres) {
        this.statut = statut;
        this.sonUtilisateur = sonUtilisateur;
        this.sesMatieres = sesMatieres;
    }

    public static Finder<String,Enseignant> find = new Finder<String,Enseignant>(
            String.class, Enseignant.class
    );

    public static List<Enseignant> showall() {
        return find.findList();
    }

    public static Enseignant show(int id_user) {
        return find.where().eq("son_utilisateur_id",id_user).findUnique();
    }

    public void insert(){
        Ebean.insert(this);
    }

    public void update(String nom,String prenom,String adresse_mail,String mot_de_passe,String date_de_naissance,String lien_photo,String statut){
        this.statut = statut;
        this.sonUtilisateur.update(nom,prenom,adresse_mail,mot_de_passe,date_de_naissance,lien_photo);
        Ebean.save(this);
    }

    public void delete(){
        Ebean.delete(this);
    }

}
