package controllers.administrateur;


import play.*;
import play.mvc.*;

import views.html.*;
import models.*;

public class administrateurController extends Controller {

    /**
     * adminIndex()
     * Redirection vers la page d'accueil de l'administrateur
     * @return
     */
    public  Result adminIndex()
    {


        return ok(views.html.administrateur.indexAdministrateur.render("Administration"));
    }

    public  Result setAdmin() {
        return Results.TODO;
    }

    public  Result updateAdmin() {
        return Results.TODO;
    }

    public  Result deleteAdmin() {
        return Results.TODO;
    }
}
