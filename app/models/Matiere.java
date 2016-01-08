package models;

import javax.persistence.*;
import com.avaje.ebean.*;

import java.util.Collection;

/**
 * Created by Bigval-Mac on 08/01/16.
 */

@Entity
public class Matiere extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String libelle;
    public String libelleAbregee;
    public String semestre;
    public int nombreHeures;
}
