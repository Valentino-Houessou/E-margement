package models;

import javax.persistence.*;
import com.avaje.ebean.*;

@Entity
public class Enseignement extends Model{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
}
