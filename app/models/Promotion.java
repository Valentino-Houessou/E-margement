package models;

import javax.persistence.*;
import com.avaje.ebean.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
