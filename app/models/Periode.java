package models;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Bigval-Mac on 08/01/16.
 */
@Entity
public class Periode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String libelle;
    public Timestamp dateDeDeut;
    public Timestamp dateFin;
}
