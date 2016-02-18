package controllers;


import models.Matiere;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

/**
 * Created by Miage on 15/01/2016.
 */
public  class MatiereController extends Controller {

    /* Ceci est une classe test */
    /* NE PAS TOUCHER */

    public Result index() {
        System.out.println("MatiereController.index : " + Matiere.showAll());
        System.out.println("MatiereController.index : " + Matiere.show(1));
        Matiere matiere = new Matiere("Test One","TO","6",24);
        matiere.save();
        System.out.println("ok1");
        matiere.libelleAbregee = "T1";
        matiere.update();
        System.out.println("ok2");
        matiere.delete();
        System.out.println("ok3");
        return ok(index.render("Your new application is ready."));
    }

    public Result createMatiere(){
        return ok("OT");
    }

}
