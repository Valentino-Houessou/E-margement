package models;

import javax.persistence.*;
import com.avaje.ebean.*;
import java.util.ArrayList;
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


    public static Finder<Integer,Presence> find = new Finder<Integer,Presence>(Presence.class);

    public static int getNombreAbsence(int idetudiant){
        return find.where().eq("sonEtudiant.id",idetudiant)
                .findRowCount();
    }

    /**
     * Retourne une collection de tous les étudiants
     *
     * @return tous les étudiants dans une List
     */
    public static List<Presence> findAll() {
        return find.all();
    }

    /**
     * Retourne les créneaux d'absence d'un étudiant donné
     * @param idEtudiantUser
     * @return
     */
    public static List<Presence> getCreaneauxAbsences(int idEtudiantUser)
    {
        // 1 - Je cherche l'étudiant
        Etudiant etu = Etudiant.find.where().eq("numero_etudiant",idEtudiantUser).findUnique();

        // 2 - Je récupére toutes ses absences
        return Presence.find.where().eq("son_etudiant_id", etu.id)
                .eq("emergement",0)
                .findList();
    }
}