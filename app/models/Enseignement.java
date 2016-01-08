package models;

import javax.persistence.*;

/**
 * Created by Bigval-Mac on 08/01/16.
 */
@Entity
public class Enseignement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
}
