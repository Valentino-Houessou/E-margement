package models;

import javax.persistence.*;
import com.avaje.ebean.*;

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


    public static Finder<int,Presence> find = new Finder<int,Presence>(Presence.class);

    public static int getNombreAbsence(int idetudiant){
        return find.where().eq("sonEtudiant.id",idetudiant)
               .findRowCount();
    }

}
