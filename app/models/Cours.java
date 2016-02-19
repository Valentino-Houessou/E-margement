package models;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.avaje.ebean.*;

@Entity
public class Cours extends Model{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String type;
    /*@Column(columnDefinition = "datetime")
    public Timestamp dateDuCour;*/
    @Column(columnDefinition = "datetime")
    public Timestamp heureDebut;
    @Column(columnDefinition = "datetime")
    public Timestamp heureFin;
    @ManyToOne(cascade=CascadeType.PERSIST)
    public Enseignant sonEnseignant;
    @ManyToOne(cascade=CascadeType.PERSIST)
    public Matiere saMatiere;
    @ManyToOne(cascade=CascadeType.PERSIST)
    public Salle saSalle;
    @ManyToOne(cascade=CascadeType.PERSIST)
    public Periode saPeriode;
    @OneToMany(mappedBy = "sonCours")
    public List<Presence> sesPresences;

    public static Finder<Long, Cours> find = new Finder<Long, Cours>(Cours.class);

    public Cours(Enseignant sonEnseignant, String type, Timestamp heureDebut, Timestamp heureFin, Matiere saMatiere, Salle saSalle, Periode saPeriode) {
        this.sonEnseignant = sonEnseignant;
        this.type = type;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.saMatiere = saMatiere;
        this.saSalle = saSalle;
        this.saPeriode = saPeriode;
        this.sesPresences = new ArrayList<Presence>();
    }

    public static Cours create(Enseignant sonEnseignant, String type, String heureDebut, String heureFin, Matiere saMatiere, Salle saSalle, Periode saPeriode) {
        Timestamp hd = null;
        Timestamp hf = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date ddnd = formatter.parse(heureDebut);
            hd = new Timestamp(ddnd.getTime());
            ddnd = formatter.parse(heureFin);
            hf = new Timestamp(ddnd.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Cours cours = new Cours(sonEnseignant, type, hd, hf, saMatiere, saSalle, saPeriode);
        cours.save();
        return cours;
    }

    public static Cours update(int id, Enseignant sonEnseignant, String type, String heureDebut, String heureFin, Matiere saMatiere, Salle saSalle, Periode saPeriode) {
        Cours cours = find.where().eq("id", id).findUnique();

        Timestamp hd = null;
        Timestamp hf = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date ddnd = formatter.parse(heureDebut);
            hd = new Timestamp(ddnd.getTime());
            ddnd = formatter.parse(heureFin);
            hf = new Timestamp(ddnd.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        cours.sonEnseignant = sonEnseignant;
        cours.type = type;
        cours.heureDebut = hd;
        cours.heureFin = hf;
        cours.saMatiere = saMatiere;
        cours.saSalle = saSalle;
        cours.saPeriode = saPeriode;

        cours.update();

        return cours;
    }

    public static void delete (int id) {
        Cours cours = find.where().eq("id", id).findUnique();
        Ebean.delete(cours);
    }

   /* public static String getNomCours(int idcours){
        return find.where().eq("cours.id",idcours)
                .findUnique().saMatiere.getLibelle();
     }*/

    }


