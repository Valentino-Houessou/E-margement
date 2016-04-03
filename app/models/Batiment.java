package models;

import javax.persistence.*;
import com.avaje.ebean.*;

import java.util.List;

@Entity
public class Batiment extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String libelle;
    @ManyToOne(cascade=CascadeType.PERSIST)
    public Universite sonUniversite;

    public Batiment()
    {

    }

    /**
     * Ebean
     */
    public static Finder<Long,Batiment> find = new Finder<Long, Batiment>(Batiment.class);

    /**
     * Obtenir tous les batiments d'une université
     * @return
     */
    public static  List<Batiment> getBatimentByUniversite(int universite)
    {
        List<Batiment> batiments = find.where().eq("sonUniversite.id", universite).findList();

        return batiments;
    }

    /**
     * Retourne les informations d'un batiment grâce à son id
     * @param id
     * @return un batiment
     */
    public static Batiment getBatiment(int id)
    {
        Batiment bat = find.where().eq("id", id).findUnique();

        return bat;
    }

    public static List<Batiment> findAll(){
        return find.all();
    }

    public static Batiment findById(int id){
        return find.byId(Long.parseLong(String.valueOf(id)));
    }

    public static Batiment findByLibelle(String libelle){
        return find.where().eq("libelle", libelle).findUnique();
    }
}
