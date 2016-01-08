package models;
//TODO
import javax.persistence.*;

@Entity
public class Etudiant {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public int id;
    public String numeroEtudiant;
    public String status;
}
