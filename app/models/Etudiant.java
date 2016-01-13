package models;
//TODO
import javax.persistence.*;
import com.avaje.ebean.*;

@Entity
public class Etudiant extends Model{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public long id;
    public String numeroEtudiant;
    public String status;
    @OneToOne
    public Utilisateur sonUtilisateur;

    //TODO : Quand on cré un etudiant il faut ajouter le module etudiant dans la liste de module de son utilisateur Vous pouvez utiliser la fonction droitEtudiant() de utilisateur*/

    //Finder for retrieve data in database
    public static Finder<Long,Etudiant> find = new Finder<Long,Etudiant>(Etudiant.class);

    public static boolean utilisateurEtudiant(Utilisateur user){
        return (Etudiant.find.where().eq("sonUtilisateur", user).findUnique()) != null ? true : false;
    }

}
