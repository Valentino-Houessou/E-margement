package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Presence extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public boolean emergement;
    public String motif;
    public String justificatif;
    @ManyToOne(cascade=CascadeType.PERSIST)
    public Cours sonCours;
    @ManyToOne(cascade=CascadeType.PERSIST)
    public Etudiant sonEtudiant;
    public static Finder<Long,Presence> find = new Finder<Long,Presence>(Presence.class);

    public List<Presence> getPresences(){
        return findAll();
    }

    public static List<Presence> findAll(){
        return find.all();
    }
}
