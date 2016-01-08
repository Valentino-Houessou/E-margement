package models;

import javax.persistence.*;
import com.avaje.ebean.*;

@Entity
public class Salle extends Model{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String libelle;
    @ManyToOne
    public Batiment sonBatiment;
}
