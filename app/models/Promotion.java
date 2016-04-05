package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import com.avaje.ebean.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;



@Entity
public class Promotion extends Model{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String anneeScolaire;
    public String groupe;
    public String type;

    @ManyToMany(cascade=CascadeType.PERSIST)
    public List<Etudiant> sesEtudiants;

    public String filiere;
    @ManyToMany(cascade=CascadeType.PERSIST)
    public List<Matiere> sesMatieres;


    /**
     * Constructeur avec paramètres
     * @param anneeScolaire
     * @param groupe
     * @param type
     * @param filiere
     */
    public Promotion(String anneeScolaire, String groupe, String type,String filiere) {
        this.anneeScolaire=anneeScolaire;
        this.groupe=groupe;
        this.type=type;
        this.filiere=filiere;

        this.sesMatieres = new ArrayList<Matiere>();

        this.save();
    }

    /**
     * Ebean
     */
    public static Finder<Long,Promotion> find = new Finder<Long, Promotion>(Promotion.class);


    /**
     * Obtenir une promotion en passant la "filière" comme paramètre
     * @param filiere
     * @return Liste de promotion de la filière
     */
    public static  List<Promotion> getPromotionByFiliere(String filiere)
    {
        List<Promotion> promotion = find.where().eq("filiere", filiere).findList();

        return promotion;
    }

    /**
     * Obtenir le type de la promotion [Classique ou Apprentis]
     * @return type
     */
    public String getType()
    {
        return this.type;
    }


    public static Promotion updatePromotion(long id, String anneeScolaire, String groupe, String type,String filiere){
        Promotion promotion = find.ref(id);
        if (anneeScolaire != null)
            promotion.anneeScolaire = anneeScolaire;
        if (groupe != null)
            promotion.groupe = groupe;
        if (type != null)
            promotion.type = type;

        if (filiere != null)
           promotion.filiere = filiere;

        promotion.update();

        return promotion;
    }


    public static void AddEtudiantPromotion (long id, long idEtudiant){
        Promotion promotion=find.ref(id);
        promotion.sesEtudiants.add(Etudiant.findById(idEtudiant));
        promotion.update();
    }

    public static void AddMatierePromotion (long id, long idMatiere){
        Promotion promotion=find.ref(id);
        promotion.sesEtudiants.add(Etudiant.findById(idMatiere));
        promotion.update();
    }

    public static List<Promotion> findAll(){
        return find.all();
    }

    public static Promotion findbyId(long id){
        return find.byId(id);
    }

    /**
     * Rechercher les matiières enseignés d'une promotion
     * @param idPromo
     * @return
     */
    public static List<Matiere> getMatiereParPromotion(int idPromo){

        Promotion lapromo = Promotion.find.where().eq("id",idPromo).findUnique();
        List<Matiere> LesMatieres= lapromo.sesMatieres;

        Collections.sort(LesMatieres, new Comparator<Matiere>() {
            @Override
            public int compare(Matiere tc1, Matiere tc2) {
                return tc1.getLibelle().compareTo(tc2.getLibelle());
            }
        });
        return  LesMatieres;
    }

    public static List<Promotion> getPromotionByYear(int year)
    {
        return find.where().ilike("anneeScolaire", "%" + year + "%").findList();
    }

    public static List<Promotion> getAllPromotion()
    {
        return find.all();
    }

    public static List<Matiere> getSerina(int idDeSerina)
    {
        // 0 - Je trouve l'étudiant
        Etudiant serina = Etudiant.find.where().eq("numero_etudiant",idDeSerina).findUnique();

        // 1 - Je trouve sa promotion
        List<Promotion> lesPromotions = Promotion.getAllPromotion();

        long idDeLaPromotion = 0;

        for(Promotion promotions : lesPromotions)
        {
            for(Etudiant etudiants : promotions.sesEtudiants)
            {
                if(etudiants.id == serina.id)
                {
                    idDeLaPromotion = promotions.id;
                }
            }
        }

        // 2 - Je récupére ses cours
        Promotion lapromo = Promotion.find.where().eq("id",idDeLaPromotion).findUnique();
        List<Matiere> LesMatieres= lapromo.sesMatieres;

        return  LesMatieres;

    }
}

