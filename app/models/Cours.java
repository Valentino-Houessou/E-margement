package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @ManyToOne(cascade=CascadeType.ALL)
    public Matiere saMatiere;
    @ManyToOne(cascade=CascadeType.PERSIST)
    public Salle saSalle;
    @ManyToOne(cascade=CascadeType.PERSIST)
    public Periode saPeriode;
    @ManyToOne(cascade=CascadeType.PERSIST)
    public Promotion saPromo;
    @OneToMany(mappedBy = "sonCours")
    public List<Presence> sesPresences;
    public boolean signatureEnseignant;

    public static Finder<Long, Cours> find = new Finder<Long, Cours>(Cours.class);

    public Cours(Enseignant sonEnseignant, String type, Timestamp heureDebut, Timestamp heureFin, Matiere saMatiere, Salle saSalle, Periode saPeriode, Promotion saPromo) {
        this.sonEnseignant = sonEnseignant;
        this.type = type;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.saMatiere = saMatiere;
        this.saSalle = saSalle;
        this.saPeriode = saPeriode;
        this.saPromo = saPromo;
        this.sesPresences = new ArrayList<Presence>();
    }

    public static Cours create(Enseignant sonEnseignant, String type, String heureDebut, String heureFin, Matiere saMatiere, Salle saSalle, Periode saPeriode, Promotion saPromo) {
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

        Cours cours = new Cours(sonEnseignant, type, hd, hf, saMatiere, saSalle, saPeriode, saPromo);
        cours.save();
        return cours;
    }

    public static Cours update(int id, Enseignant sonEnseignant, String type, String heureDebut, String heureFin, Matiere saMatiere, Salle saSalle, Periode saPeriode, Promotion saPromo) {
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
        cours.saPromo = saPromo;

        cours.update();

        return cours;
    }

    public static void delete (int id) {
        Cours cours = find.where().eq("id", id).findUnique();
        Ebean.delete(cours);
    }


    public static List<Cours> findByEnseignant(long id, String date){
        return find.where().eq("son_enseignant_id",id).like("heureDebut",date + " %").findList();
    }

    /**
     * Retourne une liste de cours correspondant à la maitère dont l'ID est passé en paramètre
     * @param id : id de la matière associée au cours
     * @return une liste de cours, null sinon
     */
    public static List<Cours> findByMatiere(long id) {
        return find.where().eq("sa_matiere_id", id).findList();
    }

    public static List<Cours> findAll(){
        return find.all();
    }

    public static Cours findbyId(long id) {
        return find.byId(id);
    }

    /**
     * Retourne tous les cours par jours
     * @param idmatiere
     * @return
     */
    public static List<Cours> findListCoursByIdMatiere(int idmatiere)
    {

        List<Cours> cours = find.where().eq("sa_matiere_id", idmatiere).findList();

        return cours;
    }

    /**
     * Retourne tous les cours par jours
     * @param idmatiere
     * @return
     */
    public static List<Cours> findListCoursByIdMatiereIdPromotion(int idmatiere, int idpromotion)
    {

        List<Cours> cours = find.where().eq("sa_matiere_id", idmatiere).eq("sa_promo_id",idpromotion).findList();

        return cours;
    }

    /**
     * Affecte ou retire un cours à un enseignant
     * @param idcours
     * @param idprofesseur
     */
    public static void affecterRetirerCours(int idcours, Enseignant idprofesseur)
    {
        Cours lecoursAmodifier = Cours.find.where().eq("id", idcours).findUnique();

        lecoursAmodifier.sonEnseignant = idprofesseur;

        lecoursAmodifier.update();
    }

    /**
     * Affecter tous les cours d'une matière sélectionné à un prof
     * @param idmatiere
     * @param idpromo
     * @param idprofesseur
     */
    public static void affecterTousLesCours(int idmatiere, int idpromo ,Enseignant idprofesseur)
    {
        List<Cours> lesCoursAaffecter = Cours.find.where().eq("sa_matiere_id",idmatiere).eq("sa_promo_id",idpromo).findList();

        if(lesCoursAaffecter != null){
            for(Cours c : lesCoursAaffecter){
                c.sonEnseignant = idprofesseur;

                c.update();
            }
        }
    }

    /**
     * Retirer tous les cours d'une matière sélectionné à un prof
     * @param idmatiere
     * @param idpromo
     * @param idprofesseur
     */
    public static void retirerTousLesCours(int idmatiere, int idpromo ,Enseignant idprofesseur)
    {
        List<Cours> lesCoursAaffecter = Cours.find.where().eq("sa_matiere_id",idmatiere).eq("sa_promo_id",idpromo).eq("son_enseignant_id",idprofesseur.id).findList();

        if(lesCoursAaffecter != null){
            for(Cours c : lesCoursAaffecter){
                c.sonEnseignant = null;

                c.update();
            }
        }
    }

    public static Cours findCoursByDebutAndFin(Date debut, Date fin){
        return find.where().eq("heure_debut", debut).eq("heure_fin", fin).findUnique();
    }
}
