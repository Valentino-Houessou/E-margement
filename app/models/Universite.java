package models;


import javax.persistence.*;
import com.avaje.ebean.*;

import java.util.List;

@Entity
public class Universite extends Model{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String libelle;

    /**
     * Ebean
     */
    public static Finder<Long,Universite> find = new Finder<Long, Universite>(Universite.class);

    /**
     * Obtenir tous les universités
     * @return
     */
    public static List<Universite> getUniversite()
    {
        List<Universite> universite = find.all();

        return universite;
    }
}
