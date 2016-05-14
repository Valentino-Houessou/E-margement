package models;

import javax.persistence.*;
import com.avaje.ebean.*;

import java.util.List;

@Entity
public class Salle extends Model{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
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

    public static Salle findById(long id){
        return find.byId(Long.parseLong(String.valueOf(id)));
    }

    public static Salle findByLibelle(String libelle){
        return find.where().eq("libelle", libelle).findUnique();
    }

    public static List<Salle> findByBatiment(long id){
        return find.where().eq("son_batiment_id", id).findList();
    }

    public  Universite getSonUniversite(){
        return sonBatiment.sonUniversite;
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

    @Override
    public String toString() {
        return "Salle{" +
                "id=" + id +
                ", libelle='" + libelle + '\'' +
                ", sonBatiment=" + sonBatiment +
                '}';
    }
}
