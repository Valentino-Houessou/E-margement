package models;
//TODO

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Utilisateur extends Model  {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public long id;
    public String nom;
    public String prenom;
    public String adresseMail;
    public String motDePasse;
    @Column(columnDefinition = "datetime")
    public Timestamp dateDeNaissance;
    public String lienPhoto;

    @ManyToMany(cascade=CascadeType.PERSIST)
    public List<Module> sesModules;

    /**
     * Constructeur avec paramètres
     * @param nom
     * @param prenom
     * @param adresseMail
     * @param motDePasse
     * @param dateDeNaissance
     * @param lienPhoto
     */
    public Utilisateur(String nom, String prenom, String adresseMail, String motDePasse,
                       Timestamp dateDeNaissance, String lienPhoto) {
        this.nom = nom;
        this.prenom = prenom;
        this.adresseMail = adresseMail;
        this.motDePasse = motDePasse;
        this.dateDeNaissance = dateDeNaissance;
        this.lienPhoto = lienPhoto;
    }

    /**
     * Use to create an user
     * @param nom
     * @param prenom
     * @param adresseMail
     * @param motDePasse
     * @param dateDeNaissance
     * @param lienPhoto
     * @return user
     */
    public static Utilisateur create(String nom, String prenom, String adresseMail, String motDePasse,
                                     String dateDeNaissance, String lienPhoto){
        Timestamp ddn = null;
        try {
            if((dateDeNaissance == null) || (dateDeNaissance.equals("")))
                dateDeNaissance = "2016-03-17 01:58:00"; // Date imaginaire pour pas que sa bug lors de l'insertion dans la base

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date ddnd = formatter.parse(dateDeNaissance);
            ddn = new Timestamp(ddnd.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Utilisateur user = new Utilisateur(nom, prenom, adresseMail,
                Utilisateur.getEncodedPassword(motDePasse), ddn, lienPhoto);
        user.save();
        return user;
    }


    /**
     * Mise à jour des informations utilisateur
     * @param id
     * @param nom
     * @param prenom
     * @param adresseMail
     * @param motDePasse
     * @param dateDeNaissance
     * @param lienPhoto
     * @return
     */
    public static Utilisateur updateUtilisateur(long id, String nom, String prenom, String adresseMail, String motDePasse,
                                     String dateDeNaissance, String lienPhoto){

        Utilisateur user = find.ref(id);

        if (nom != null)
            user.nom = nom;
        if (prenom != null)
            user.prenom = prenom;
        if (adresseMail != null)
            user.adresseMail = adresseMail;
        if ((motDePasse != null) && (!motDePasse.equals(""))){
            user.motDePasse = Utilisateur.getEncodedPassword(motDePasse);

        }
        if (dateDeNaissance != null) {
            Timestamp ddn = null;
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date ddnd = formatter.parse(dateDeNaissance);
                ddn = new Timestamp(ddnd.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            user.dateDeNaissance = ddn;
        }
        if ((lienPhoto != null) && (lienPhoto !=""))
            user.lienPhoto = lienPhoto;

        user.update();

        return user;
    }

    /**
     *
     * @param id
     */
    public static void deleteUtilisateur(long id){
        Utilisateur user = find.ref(id);
        if (!Etudiant.utilisateurEtudiant(user) &&
                !Enseignant.utilisateurEnseignant(user) &&
                !Administrateur.utilisateurAdmin(user))
            user.delete();
    }

    /**
     * Affecter les droits enseignant à l'utilisateur
     * @param id
     */
    public static void droitEnseignant (long id){
        Utilisateur user =  find.ref(id);
        user.sesModules.add(Module.findByLibelle("ENSEIGNANTS"));
        user.save();
    }

    /**
     * Affecter les droits étudiant à l'utilisateur
     * @param id
     */
    public static void droitEtudiant (long id){
        Utilisateur user =  find.ref(id);
        user.sesModules.add(Module.findByLibelle("ETUDIANTS"));
        user.update();
    }

    /**
     * Affecter les droits administrateur à l'utilisateur
     * @param id
     */
    public static void droitAdmin (long id){
        Utilisateur user =  find.ref(id);
        user.sesModules.add(Module.findByLibelle("ADMINISTRATEURS"));
        user.save();
    }

    public static void deleteDroitAdmin(long id)
    {
        Utilisateur user =  find.ref(id);

        user.sesModules.remove(Module.findByLibelle("ADMINISTRATEURS"));

        user.update();
    }

    //Finder for retrieve data in database
    public static Finder<Long,Utilisateur> find = new Finder<Long,Utilisateur>(Utilisateur.class);

    public static List<Utilisateur> findAll(){
        return Utilisateur.find.all();
    }

    public static Utilisateur findByMail(String mail){
        return  find.where().eq("adresse_mail", mail).findUnique();
    }

    public static String getEncodedPassword(String key) {
        key = "_" + key + "_";
        byte[] uniqueKey = key.getBytes();
        byte[] hash = null;
        try {
            hash = MessageDigest.getInstance("MD5").digest(uniqueKey);
        } catch (NoSuchAlgorithmException e) {
            throw new Error("no MD5 support in this VM");
        }
        StringBuffer hashString = new StringBuffer();
        for ( int i = 0; i < hash.length; ++i ) {
            String hex = Integer.toHexString(hash[i]);
            if ( hex.length() == 1 ) {
                hashString.append('0');
                hashString.append(hex.charAt(hex.length()-1));
            } else {
                hashString.append(hex.substring(hex.length()-2));
            }
        }
        return hashString.toString();
    }

    public static boolean testPassword(String clearTextTestPassword,
                                       String encodedActualPassword)
            throws NoSuchAlgorithmException
    {
        String encodedTestPassword = Utilisateur.getEncodedPassword(
                clearTextTestPassword);

        return (encodedTestPassword.equals(encodedActualPassword));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilisateur)) return false;

        Utilisateur that = (Utilisateur) o;

        if (id != that.id) return false;
        if (!nom.equals(that.nom)) return false;
        if (!prenom.equals(that.prenom)) return false;
        if (!adresseMail.equals(that.adresseMail)) return false;
        if (!motDePasse.equals(that.motDePasse)) return false;
        return !(dateDeNaissance != null ? !dateDeNaissance.equals(that.dateDeNaissance) : that.dateDeNaissance != null);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + nom.hashCode();
        result = 31 * result + prenom.hashCode();
        result = 31 * result + adresseMail.hashCode();
        result = 31 * result + motDePasse.hashCode();
        result = 31 * result + (dateDeNaissance != null ? dateDeNaissance.hashCode() : 0);
        return result;
    }

    public static Utilisateur authenticate(String email, String password){
        // return find.where().eq("adresseMail",email).eq("motDePasse",Utilisateur.getEncodedPassword(password)).findUnique(); // A mettre au lancement
        return find.where().eq("adresseMail",email).eq("motDePasse",password).findUnique();
    }

    public String status(){
        String module = "";
        for(int i = 0; i < sesModules.size(); i++){
            module += " " + sesModules.get(i).libelle;
        }
        return module;
    }
}
