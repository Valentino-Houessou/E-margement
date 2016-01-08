package models;
//TODO

import javax.persistence.*;
import com.avaje.ebean.*;

import java.util.Collection;

@Entity
public class Enseignant extends Model{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int id;
    public String statut;
    @OneToOne
    public int utilisateur_id;
    @ManyToMany(cascade=CascadeType.PERSIST)
    public Collection<Matiere> sesMatieres;

}
