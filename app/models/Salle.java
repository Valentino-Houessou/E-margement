package models;

import javax.persistence.*;
import com.avaje.ebean.*;

import java.util.List;

@Entity
public class Salle extends Model{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String libelle;
    @ManyToOne(cascade=CascadeType.PERSIST)
    public Batiment sonBatiment;

    public static Finder<Long, Salle> find = new Finder<Long, Salle>(Salle.class);

    public Salle(String libelle, Batiment sonBatiment) {
        this.libelle = libelle;
        this.sonBatiment = sonBatiment;
    }

    public static List<Salle> findAll(){
        return find.all();
    }

    public static Salle findById(int id){
        return find.byId(Long.parseLong(String.valueOf(id)));
    }

    public static Salle findByLibelle(String libelle){
        return find.where().eq("libelle", libelle).findUnique();
    }

    public static List<Salle> findByBatiment(int id){
        return find.where().eq("son_batiment_id", id).findList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Salle salle = (Salle) o;

        return libelle.equals(salle.libelle);

    }

    @Override
    public int hashCode() {
        return libelle.hashCode();
    }
}
