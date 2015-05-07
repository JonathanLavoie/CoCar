package com.jg.jl.tpmobile.cocar.cocar.jsonParser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jg.jl.tpmobile.cocar.cocar.ParcoursConducteur;
import com.jg.jl.tpmobile.cocar.cocar.ParcoursPassager;
import com.jg.jl.tpmobile.cocar.cocar.User;

/**
 * Created by Jiimmy on 2015-04-01.
 */
public class jsonParser{

    public static User parseUser(String p_body) throws JSONException{
        User unUser = new User();
        JSONArray array = new JSONArray(p_body);
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonUser = array.getJSONObject(i);
            unUser.set_identification(jsonUser.getString("email"));
            unUser.set_nom(jsonUser.getString("nom"));
            unUser.set_motPasse(jsonUser.getString("PW"));
            unUser.set_adresse(jsonUser.getString("adresse"));
            unUser.set_phone(jsonUser.getString("phone"));
            unUser.set_sumRate(jsonUser.getInt("sumRate"));
            unUser.set_countRate(jsonUser.getInt("countRate"));
        }
        return unUser;
    }
    public static ArrayList<ParcoursConducteur> parseConducteurListe(String p_body) throws JSONException{
        ArrayList<ParcoursConducteur> liste = new ArrayList<>();
        JSONArray array = new JSONArray(p_body);
        for (int i = 0; i < array.length(); i++){
            JSONObject jsonCond = array.getJSONObject(i);
            String date = jsonCond.getString("dateHeureC");
            String[] vectDate = date.split("T");
            ParcoursConducteur condu = new ParcoursConducteur();
            condu.set_ID(jsonCond.getString("id"));
            condu.set_depart(jsonCond.getString("departC"));
            condu.set_destination(jsonCond.getString("destinationC"));
            condu.set_date(vectDate[0]);
            condu.set_heure(vectDate[1]);
            condu.set_identifiant(jsonCond.getString("identifiantCree"));
            condu.set_KM(jsonCond.getInt("nbKm"));
            condu.set_nombreDePlace(jsonCond.getInt("nombrePlace"));
            condu.set_disDep(jsonCond.getDouble("disDep"));
            condu.set_disDest(jsonCond.getDouble("disDest"));
            liste.add(condu);
        }
        return liste;
    }

    public static ArrayList<ParcoursPassager> parsePassagerListe(String p_body) throws JSONException{
        ArrayList<ParcoursPassager> liste = new ArrayList<>();
        JSONArray array = new JSONArray(p_body);
        for (int i = 0; i < array.length(); i++){
            JSONObject jsonCond = array.getJSONObject(i);
            String date = jsonCond.getString("dateHeureP");
            String[] vectDate = date.split("T");
            ParcoursPassager pass = new ParcoursPassager();
            pass.set_depart(jsonCond.getString("departP"));
            pass.set_destination(jsonCond.getString("destinationP"));
            pass.set_ID(jsonCond.getString("id"));
            pass.set_date(vectDate[0]);
            pass.set_heure(vectDate[1]);
            pass.set_identifiant(jsonCond.getString("identifiantCree"));
            pass.set_nombrePassager(jsonCond.getInt("nombrePassager"));
            pass.set_disDep(jsonCond.getDouble("disDep"));
            pass.set_disDest(jsonCond.getDouble("disDest"));
            liste.add(pass);
        }
        return liste;
    }


    public static JSONObject conducteurToJSONObject(ParcoursConducteur condu) throws JSONException{
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("departC",condu.get_depart());
        jsonObj.put("destinationC",condu.get_destination());
        jsonObj.put("dateHeureC", condu.get_date() + " " + condu.get_heure());
        jsonObj.put("nbKm",condu.get_KM());
        jsonObj.put("nombrePlace",condu.get_nombreDePlace());
        jsonObj.put("identifiantCree",condu.get_identifiant());
        return jsonObj;
    }
    public static JSONObject passagerToJSONObject(ParcoursPassager pass) throws JSONException{
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("departP",pass.get_depart());
        jsonObj.put("destinationP",pass.get_destination());
        jsonObj.put("dateHeureP", pass.get_date() + " " + pass.get_heure());
        jsonObj.put("nombrePassager",pass.get_nombrePassager());
        jsonObj.put("identifiantCree",pass.get_identifiant());
        return jsonObj;
    }

    public static JSONObject userToJSONObject(User user) throws JSONException{
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("nom",user.get_nom());
        jsonObj.put("PW",user.get_motPasse());
        jsonObj.put("adresse",user.get_adresse());
        jsonObj.put("phone",user.get_phone());
        jsonObj.put("sumRate","0");
        jsonObj.put("countRate","0");
        return jsonObj;
    }
}
