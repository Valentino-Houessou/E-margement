package models;

import javax.persistence.*;
import com.avaje.ebean.*;

import java.util.Collection;

@Entity
public class Matiere extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String libelle;
    public String libelleAbregee;
    public String semestre;
    public int nombreHeures;


    public static Finder<Long,Matiere> find = new Finder<Long, Matiere>(Matiere.class);

    public static Matiere findById(long id) {

        return find.ref(id);
    }
}
