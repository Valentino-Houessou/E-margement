package views.models;

import javax.persistence.*;
import com.avaje.ebean.*;


@Entity
public class Module extends Model{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public long id;
    public String libelle;

    //Finder for retrieve data in database
    public static Finder<Long,Module> find = new Finder<Long,Module>(Module.class);

    public static Module findByLibelle(String libelle){
        return ((libelle == "ENSEIGNANTS") || (libelle == "ETUDIANTS") || (libelle == "ADMINISTRATEURS"))
            ? find.where().eq("libelle", libelle).findUnique() : null;
    }
}
