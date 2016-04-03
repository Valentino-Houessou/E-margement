package models;

import javax.persistence.*;
import com.avaje.ebean.*;
import com.avaje.ebean.annotation.ConcurrencyMode;
import com.avaje.ebean.annotation.EntityConcurrencyMode;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
public class Matiere extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String libelle;
    public String libelleAbregee;
    public String semestre;
    public long nombreHeures;


    public Matiere(String libelle, String libelleAbregee, String semestre, long nombreHeures) {
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
        this.nombreHeures = (nombreHeures != null) ? Long.parseLong(nombreHeures) : this.nombreHeures ;
        Ebean.update(this);
    }


    public void delete(){
        Ebean.delete(this);
    }

    public String getLibelle(){
        return this.libelle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Matiere matiere = (Matiere) o;

        return libelle.equals(matiere.libelle) && libelleAbregee.equals(matiere.libelleAbregee);

    }

    @Override
    public int hashCode() {
        int result = libelle.hashCode();
        result = 31 * result + libelleAbregee.hashCode();
        result = 31 * result + (int) (nombreHeures ^ (nombreHeures >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Matiere{" +
                "id=" + id +
                ", libelle='" + libelle + '\'' +
                ", libelleAbregee='" + libelleAbregee + '\'' +
                ", semestre='" + semestre + '\'' +
                ", nombreHeures=" + nombreHeures +
                '}';
    }
}
