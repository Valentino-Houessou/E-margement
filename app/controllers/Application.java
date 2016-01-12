package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.*;
import play.mvc.*;

import views.html.*;

import java.text.ParseException;

public class Application extends Controller {

    public Result index() {
        return ok(index.render("Your new application is ready."));
    }

    /*public Result ajoutProf(){
        JsonNode json = request().body().asJson();
        try {
            if (json == null) {
                return badRequest("Expecting Json data");
            }
            else {

            }
        } catch (ParseException e) {
            return badRequest("Can't parsed [jour]");
        }
        catch (NumberFormatException nFE) {
            return badRequest("Bad format for user id");
        }
    }*/

}
