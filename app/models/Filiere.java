package models;

import javax.persistence.*;
import com.avaje.ebean.*;

import java.util.List;

@Entity
public class Filiere extends Model{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String codefiliere;
    public String libelle;
    public String annee;
    @ManyToOne(cascade=CascadeType.PERSIST)
    public Batiment sonBatiment;


    public Filiere()
    {
        this.codefiliere="vide";
        this.libelle = "vide";
        this.annee = "vide";
    }

    public Filiere(String codefiliere, String libelle, String annee)
    {
        this.codefiliere= codefiliere;
        this.libelle = libelle;
        this.annee = annee;
    }

    /**
     * Ebean
     */
    public static Finder<Long,Filiere> find = new Finder<Long, Filiere>(Filiere.class);

    /**
     * Obtenir toutes les filières d'une université dans un batiment donné d'une université
     * @return les filières d'un batiment
     */
    public static List<Filiere> getFilieresByBatiment(int batiment)
    {
        List<Filiere> filieres = find.where().eq("sonBatiment.id", batiment).findList();

        return filieres;
    }

    public static Filiere create(String codefiliere, String libelle, String annee, Batiment sonBat){

        Filiere fi = new Filiere(codefiliere,libelle,annee);
        fi.sonBatiment=sonBat;

        fi.save();

        return fi;
    }

}
