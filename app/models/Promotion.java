package models;

import javax.persistence.*;
import com.avaje.ebean.*;

import java.util.List;

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



    public Promotion(String anneeScolaire, String groupe, String type,String filiere) {
        this.anneeScolaire=anneeScolaire;
        this.groupe=groupe;
        this.type=type;
        this.filiere=filiere;

        this.save();
    }

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
}

