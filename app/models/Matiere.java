package models;

import javax.persistence.*;
import com.avaje.ebean.*;

import java.util.List;

@Entity
public class Matiere extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String libelle;
    public String libelleAbregee;
    public String semestre;
    public int nombreHeures;

    public Matiere(String libelle, String libelleAbregee, String semestre, int nombreHeures) {
        this.libelle = libelle;
        this.libelleAbregee = libelleAbregee;
        this.semestre = semestre;
        this.nombreHeures = nombreHeures;
    }
    public static Finder<Long,Matiere> find = new Finder<Long, Matiere>(Matiere.class);

    public static Matiere show(long id) {
        return find.ref(id);
    }

    public static List<Matiere> showAll(){
        return find.all();
    }

    public void insert(){
        Ebean.save(this);
    }

    public void update(String libelle, String libelleAbregee, String semestre, String nombreHeures) {
        this.libelle = (libelle != null) ? libelle : this.libelle;
        this.libelleAbregee = (libelleAbregee != null) ? libelleAbregee : this.libelleAbregee;
        this.semestre = (semestre != null) ? semestre : this.semestre;
        this.nombreHeures = nombreHeures != null ? Integer.parseInt(nombreHeures) : this.nombreHeures ;
        Ebean.update(this);
    }

    public void delete(){
        Ebean.delete(this);
    }


}
