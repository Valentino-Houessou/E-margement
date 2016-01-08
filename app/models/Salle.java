package models;

import javax.persistence.*;

/**
 * Created by Bigval-Mac on 08/01/16.
 */

@Entity
public class Salle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String libelle;
}
