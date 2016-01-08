package models;

import javax.persistence.*;

/**
 * Created by Bigval-Mac on 08/01/16.
 */

@Entity
public class Presence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public boolean emergement;
    public String motif;
    public String justificatif;
}
