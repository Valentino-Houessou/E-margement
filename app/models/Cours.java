package models;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Bigval-Mac on 08/01/16.
 */

@Entity
public class Cours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String type;
    public Timestamp heureDebut;
    public Timestamp heureFin;
}
