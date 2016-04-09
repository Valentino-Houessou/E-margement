package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import com.avaje.ebean.*;
import java.util.ArrayList;
import java.util.Date;
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

    public Presence(){

    }


    public static Finder<Integer,Presence> find = new Finder<Integer,Presence>(Presence.class);

    public static int getNombreAbsence(long idetudiant){
        return find.where().eq("son_etudiant_id",idetudiant)
                .eq("emergement",0)
                .eq("motif",null)
                .eq("justificatif", null)
                .eq("sonCours.signatureEnseignant", true)
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

    public static List<Presence> findbyCours(long id_cours){
        return find.where().eq("son_cours_id",id_cours).findList();
    }

    public static Presence findbyEtudiant(long id_etu,long id_cours){
        return find.where().eq("son_etudiant_id", id_etu).eq("son_cours_id",id_cours).findUnique();
    }

    /**
     * Retourne les créneaux d'absence d'un étudiant donné
     * @param idEtudiantUser
     * @return
     */
    public static List<Presence> getCreaneauxAbsences(String idEtudiantUser)
    {
        // 1 - Je cherche l'étudiant
        Etudiant etu = Etudiant.find.where().eq("numero_etudiant",idEtudiantUser).findUnique();

        // 2 - Je récupére toutes ses absences
        return Presence.find.where().eq("son_etudiant_id", etu.id)
                .eq("emergement",0)
                .eq("sonCours.signatureEnseignant", true)
                .findList();
    }

    /**
     * @param promotion : la promotion concernée
     * @param theDate : est la date concernée
     * @return Retourne la liste des absences pour la date et la
     */
    public static List<Presence> getAbsences(int promotion, String theDate){
        return find.where()
                .eq("emergement", 0)
                .eq("DATE(sonCours.heureDebut)", theDate)
                .eq("sonCours.saPromo.id", promotion)
                .findList();
    }

    /**
     * @param presenceId : l'id de la presence
     * @return Retourne le lien de la présence
     */
    public static String getJustificatif(int presenceId){
        Presence pres =  find.byId(presenceId);
        return pres.justificatif;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Presence presence = (Presence) o;

        if (sonCours != null ? !sonCours.equals(presence.sonCours) : presence.sonCours != null) return false;
        return sonEtudiant != null ? sonEtudiant.equals(presence.sonEtudiant) : presence.sonEtudiant == null;

    }

    @Override
    public int hashCode() {
        int result = sonCours != null ? sonCours.hashCode() : 0;
        result = 31 * result + (sonEtudiant != null ? sonEtudiant.hashCode() : 0);
        return result;
    }
}
