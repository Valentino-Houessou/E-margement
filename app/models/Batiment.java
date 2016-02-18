package models;

import javax.persistence.*;
import com.avaje.ebean.*;

@Entity
public class Batiment extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String libelle;
    @ManyToOne(cascade=CascadeType.PERSIST)
    public Universite sonUniversite;
}
