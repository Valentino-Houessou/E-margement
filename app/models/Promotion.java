package models;

import javax.persistence.*;
import com.avaje.ebean.*;
import java.util.List;

@Entity
public class Promotion extends Model{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String anneeScolaire;
    public String groupe;
    public String type;

    @ManyToMany(cascade=CascadeType.PERSIST)
    public List<Etudiant> sesEtudiants;

    public String filiere;
    @ManyToMany(cascade=CascadeType.PERSIST)
    public List<Matiere> sesMatieres;

}
