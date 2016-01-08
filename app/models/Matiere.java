package models;

import javax.persistence.*;

/**
 * Created by Bigval-Mac on 08/01/16.
 */

@Entity
public class Matiere {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String libelle;
    public String libelleAbregee;
    public String semestre;
    public int nombreHeures;
}
