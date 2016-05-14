package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import play.api.mvc.Codec;

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
    public String type_detaille;
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
    @JsonIgnore
    @OneToMany(mappedBy = "sonCours")
    public List<Presence> sesPresences;
    public boolean signatureEnseignant;

    public static Finder<Long, Cours> find = new Finder<Long, Cours>(Cours.class);

    public Cours(Enseignant sonEnseignant, String type, String type_detaille, Timestamp heureDebut, Timestamp heureFin, Matiere saMatiere, Salle saSalle, Periode saPeriode, Promotion saPromo) {
        this.sonEnseignant = sonEnseignant;
        this.type = type;
        this.type_detaille = type_detaille;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.saMatiere = saMatiere;
        this.saSalle = saSalle;
        this.saPeriode = saPeriode;
        this.saPromo = saPromo;
        this.sesPresences = new ArrayList<Presence>();
    }

    public static Cours create(Enseignant sonEnseignant, String type, String type_detaille, String heureDebut, String heureFin, Matiere saMatiere, Salle saSalle, Periode saPeriode, Promotion saPromo) {
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

        Cours cours = new Cours(sonEnseignant, type, type_detaille, hd, hf, saMatiere, saSalle, saPeriode, saPromo);
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
        return find.where().eq("son_enseignant_id", id).like("heureDebut", date + " %").findList();
    }

    public static Cours findByDebutEtFin(Timestamp debut, Timestamp fin){
        return find.where().eq("heure_debut", debut).eq("heure_fin", fin).findUnique();
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

    public static Cours findByTypeDetailleAndMatiere(String type_detaille, Matiere saMatiere){

        return Cours.find.where().eq("type_detaille", type_detaille).eq("sa_matiere_id", saMatiere.id).findUnique();
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

    public static List<Cours> findListCoursByListMatiereEtIdEnseignant(List<Matiere> matieres, int id_enseignant)
    {
        List<Cours> cours = new ArrayList<Cours>();
        List<Cours> coursBD = Cours.findAll();

        for(Cours c : coursBD)
            for(Matiere m : matieres)
                if(c.saMatiere.equals(m) && c.sonEnseignant.id == id_enseignant)
                    cours.add(c);

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
        List<Cours> lesCoursAaffecter = Cours.find.where().eq("sa_matiere_id", idmatiere).eq("sa_promo_id", idpromo).findList();

        if(lesCoursAaffecter != null){
            for(Cours c : lesCoursAaffecter){
                c.sonEnseignant = idprofesseur;
                c.update();
            }
        }
    }

    /**
     * Vérifier si un cours n'existe pas sur le créneau précisé
     * @param idPromotion : l'identifiant d'une promotion
     * @param heureDebutJ : l'heure de début d'un cours
     * @param heureFinJ : l'heure de fin d'un cours
     */
    public static boolean checkCreneau(int idPromotion, Date heureDebutJ ,Date heureFinJ)
    {   //5h que ça ma pris de réaliser cette requête @Val
        int nbRow = Cours.find.where().eq("sa_promo_id", idPromotion)
                          .or(
                                  com.avaje.ebean.Expr.or(com.avaje.ebean.Expr.between("UNIX_TIMESTAMP(heureDebut)*1000",
                                "UNIX_TIMESTAMP(heureFin)*1000", heureDebutJ.getTime()),
                               com.avaje.ebean.Expr.between("UNIX_TIMESTAMP(heureDebut)*1000",
                                "UNIX_TIMESTAMP(heureFin)*1000", heureFinJ.getTime())),
                                  com.avaje.ebean.Expr.or(com.avaje.ebean.Expr.eq("UNIX_TIMESTAMP(heureDebut)*1000", heureDebutJ.getTime()),
                                com.avaje.ebean.Expr.eq("UNIX_TIMESTAMP(heureFin)*1000", heureFinJ.getTime()))
                            )
                .findRowCount();
        if(nbRow == 0)
            return true;
        return false;
    }

    /**
     * Retirer tous les cours d'une matière sélectionné à un prof
     * @param idmatiere
     * @param idpromo
     * @param idprofesseur
     */
    public static void retirerTousLesCours(int idmatiere, int idpromo ,Enseignant idprofesseur)
    {
        List<Cours> lesCoursAaffecter = Cours.find.where().eq("sa_matiere_id",idmatiere).eq("sa_promo_id", idpromo).eq("son_enseignant_id", idprofesseur.id).findList();

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cours cours = (Cours) o;

        if (!type.equals(cours.type)) return false;
        if (!type_detaille.equals(cours.type_detaille)) return false;
        if (!heureDebut.equals(cours.heureDebut)) return false;
        if (!heureFin.equals(cours.heureFin)) return false;
        if (!sonEnseignant.equals(cours.sonEnseignant)) return false;
        if (!saMatiere.equals(cours.saMatiere)) return false;
        if (!saSalle.equals(cours.saSalle)) return false;
        return saPromo.equals(cours.saPromo);

    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + type_detaille.hashCode();
        result = 31 * result + heureDebut.hashCode();
        result = 31 * result + heureFin.hashCode();
        result = 31 * result + sonEnseignant.hashCode();
        result = 31 * result + saMatiere.hashCode();
        result = 31 * result + saSalle.hashCode();
        result = 31 * result + saPromo.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Cours{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", type_detaille='" + type_detaille + '\'' +
                ", heureDebut=" + heureDebut +
                ", heureFin=" + heureFin +
                ", sonEnseignant=" + sonEnseignant +
                ", saMatiere=" + saMatiere +
                ", saSalle=" + saSalle +
                ", saPeriode=" + saPeriode +
                ", saPromo=" + saPromo +
                ", sesPresences=" + sesPresences +
                ", signatureEnseignant=" + signatureEnseignant +
                '}';
    }
}
