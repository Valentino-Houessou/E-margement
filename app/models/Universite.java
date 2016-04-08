package models;


import javax.persistence.*;
import com.avaje.ebean.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Entity
public class Universite extends Model{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String libelle;

    public Universite(String libelle) {
        this.libelle = libelle;
    }


    public static Universite create(String libelle) {

        Universite univ  = new Universite(libelle);

        univ.save();
        return univ;
    }

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

        // Trie des universités par ordre croissant par rapport au nom
        Collections.sort(universite, new Comparator<Universite>() {
            @Override
            public int compare(Universite tc1, Universite tc2) {
                return tc1.libelle.compareTo(tc2.libelle);
            }
        });

        return universite;
    }



}
