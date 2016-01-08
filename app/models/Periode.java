package models;

import javax.persistence.*;
import java.sql.Timestamp;
import com.avaje.ebean.*;

@Entity
public class Periode extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String libelle;
    @Column(columnDefinition = "datetime")
    public Timestamp dateDeDeut;
    @Column(columnDefinition = "datetime")
    public Timestamp dateFin;
}
