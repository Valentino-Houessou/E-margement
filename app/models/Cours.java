package models;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

import com.avaje.ebean.*;

@Entity
public class Cours extends Model{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String type;
    @Column(columnDefinition = "datetime")
    public Timestamp heureDebut;
    @Column(columnDefinition = "datetime")
    public Timestamp heureFin;
    @ManyToOne(cascade=CascadeType.PERSIST)
    public Enseignant sonEnseignant;
    @ManyToOne(cascade=CascadeType.PERSIST)
    public Matiere saMatiere;
    @ManyToOne(cascade=CascadeType.PERSIST)
    public Salle saSalle;
    @ManyToOne(cascade=CascadeType.PERSIST)
    public Periode saPeriode;
    @OneToMany(mappedBy = "sonCours")
    public List<Presence> sesPresences;
}
