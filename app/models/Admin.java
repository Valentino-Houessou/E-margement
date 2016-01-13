package models;


import javax.persistence.*;
import com.avaje.ebean.*;

@Entity
public class Admin  extends Model {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int id;
    public String statut;
    @OneToOne
    public Utilisateur sonUtilisateur;

    //TODO : Quand on cré un etudiant il faut ajouter le module etudiant dans la liste de module de son utilisateur Vous pouvez utiliser la fonction droitEtudiant() de utilisateur*/

    //Finder for retrieve data in database
    public static Finder<Long,Admin> find = new Finder<Long,Admin>(Admin.class);

    public static boolean utilisateurAdmin(Utilisateur user){
        return (Admin.find.where().eq("sonUtilisateur", user).findUnique()) != null ? true : false;
    }

}

