package com.jg.jl.tpmobile.cocar.cocar.webService;

import android.app.Activity;
import android.util.Log;

import com.jg.jl.tpmobile.cocar.cocar.NoteDepart;
import com.jg.jl.tpmobile.cocar.cocar.ParcoursConducteur;
import com.jg.jl.tpmobile.cocar.cocar.ParcoursConducteurRepo;
import com.jg.jl.tpmobile.cocar.cocar.ParcoursPassager;
import com.jg.jl.tpmobile.cocar.cocar.ParcoursPassagerRepo;
import com.jg.jl.tpmobile.cocar.cocar.SessionManager;
import com.jg.jl.tpmobile.cocar.cocar.User;
import com.jg.jl.tpmobile.cocar.cocar.UserRepo;
import com.jg.jl.tpmobile.cocar.cocar.jsonParser.jsonParser;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;

/**
 * Created by Jiimmy on 2015-04-12.
 */
public class webService {
    private final static String WEB_SERVICE_URL = "appcocar.appspot.com";
    //private final static String WEB_SERVICE_URL = "10.0.2.2:8080";
    private final static String REST_CONDUCTEUR = "/conducteur";
    private final static String REST_PASSAGER = "/passager";
    private final static String REST_USER = "/user";
    private final static String REST_NBPLACE = "/nbPlace/";
    private final static String REST_LAT = "/lat/";
    private final static String REST_LONG = "/long/";
    private final static String REST_DEPART = "/depart";
    private final static String REST_RATE = "/rating/";
    private final static String REST_DEPARTPREVU = "/departPrevu/";
    private final static String REST_TYPE = "/type/";
    private final static String REST_IDDEP  = "/idDep/";
    private HttpClient m_ClientHttp = new DefaultHttpClient();
    private Exception m_exception;
    SessionManager session;
    private final String TAG = this.getClass().getSimpleName();
    private String[] vectLongLat = null;
    ParcoursConducteur unParcoursCond = new ParcoursConducteur();
    ParcoursPassager unParcoursPassa = new ParcoursPassager();


    public void putConduc(ParcoursConducteur conduc) {
        try {
            URI uri = new URI("http", WEB_SERVICE_URL, REST_CONDUCTEUR, null, null);
            HttpPut put = new HttpPut(uri);
            JSONObject obj = jsonParser.conducteurToJSONObject(conduc);
            put.setEntity(new StringEntity(obj.toString()));
            put.addHeader("Content-Type", "application/json");
            m_ClientHttp.execute(put, new BasicResponseHandler());
        } catch (Exception e) {
            m_exception = e;
        }
    }


    public void updateNote(String id, Float note,String user){
        try {
            URI uri = new URI("http", WEB_SERVICE_URL, REST_DEPART + "/" + id + REST_RATE + note + REST_USER + "/"
                    + user, null, null);
            HttpPost post = new HttpPost(uri);
            m_ClientHttp.execute(post,new BasicResponseHandler());
        }catch (Exception e)
        {
            m_exception = e;
        }

    }


    public void putUser(User user)
    {
        try{
            URI uri = new URI("http",WEB_SERVICE_URL,REST_USER + "/" + user.get_identification(),null,null);
            HttpPut put = new HttpPut(uri);
            JSONObject obj = jsonParser.userToJSONObject(user);
            put.setEntity(new StringEntity(obj.toString()));
            put.addHeader("Content-Type", "application/json");
            m_ClientHttp.execute(put, new BasicResponseHandler());
        } catch (Exception e) {
            m_exception = e;
        }
    }

    public void putDepart(String id1,String id2,String idParcours,int nbPass,String type)
    {
        try{
            URI uri = new URI("http",WEB_SERVICE_URL,REST_DEPART,null,null);
            HttpPut put = new HttpPut(uri);
            JSONObject obj = jsonParser.SSSIIToJSONObject(id1, id2, idParcours, nbPass, type);
            put.setEntity(new StringEntity(obj.toString()));
            put.addHeader("Content-Type", "application/json");
            m_ClientHttp.execute(put, new BasicResponseHandler());
        }catch (Exception e) {
            m_exception = e;
        }
    }


    public void deletePar(String idPar,String idDep,String type,String place)
    {
        try{
            URI uri = new URI("http",WEB_SERVICE_URL,REST_DEPART + "/" +idPar + REST_IDDEP + idDep
                    + REST_TYPE + type + REST_NBPLACE + place,null,null);
            HttpDelete delete = new HttpDelete(uri);
            m_ClientHttp.execute(delete);

        }catch (Exception e) {
            m_exception = e;
        }
    }

    public void putPassager(ParcoursPassager passager) {
        try {
            URI uri = new URI("http", WEB_SERVICE_URL, REST_PASSAGER, null, null);
            HttpPut put = new HttpPut(uri);
            JSONObject obj = jsonParser.passagerToJSONObject(passager);
            put.setEntity(new StringEntity(obj.toString()));
            put.addHeader("Content-Type", "application/json");
            m_ClientHttp.execute(put, new BasicResponseHandler());

        } catch (Exception e) {
            m_exception = e;
        }
    }


    public ArrayList<ParcoursConducteur> getDepartCondu(Activity activity){
        session = new SessionManager(activity);
        ArrayList<ParcoursConducteur> liste = new ArrayList<>();
        try {
            URI uri = new URI("http",WEB_SERVICE_URL,REST_DEPARTPREVU + session.getIdentification() + REST_TYPE + "Conducteur",null,null);
            HttpGet get = new HttpGet(uri);
            String body = m_ClientHttp.execute(get, new BasicResponseHandler());
            Log.i(TAG, "Reçu conduteur: " + body);
            liste = jsonParser.parseConducteurListe(body);
        }catch (Exception e) {
            m_exception = e;
        }
        return liste;
    }

    public ArrayList<ParcoursPassager> getDepartPass(Activity activity){
        session = new SessionManager(activity);
        ArrayList<ParcoursPassager> liste = new ArrayList<>();
        try {
            URI uri = new URI("http",WEB_SERVICE_URL,REST_DEPARTPREVU + session.getIdentification() + REST_TYPE + "Passager",null,null);
            HttpGet get = new HttpGet(uri);
            String body = m_ClientHttp.execute(get, new BasicResponseHandler());
            Log.i(TAG, "Reçu passager: " + body);
            liste = jsonParser.parsePassagerListe(body);
        }catch (Exception e) {
            m_exception = e;
        }
        return liste;
    }


    public User getUserByEmail(String email){
        User unUser = new User();
        try {
            URI uri = new URI("http", WEB_SERVICE_URL, REST_USER + "/" + email, null, null);
            HttpGet get = new HttpGet(uri);
            String body = m_ClientHttp.execute(get, new BasicResponseHandler());
            Log.i(TAG, "Reçu user: " + body);
            unUser = jsonParser.parseUser(body);

        }catch (Exception e) {
            m_exception = e;
        }
        return unUser;
    }


    public ArrayList<NoteDepart> getNote(Activity activity){
        ArrayList<NoteDepart> liste = null;
        session = new SessionManager(activity.getApplicationContext());
        try {
            URI uri = new URI("http",WEB_SERVICE_URL,REST_DEPART + "/" + session.getIdentification(),null,null);
            HttpGet get = new HttpGet(uri);
            String body = m_ClientHttp.execute(get, new BasicResponseHandler());
            Log.i(TAG, "Reçu note: " + body);
            liste = jsonParser.parseNoteDepart(body);
        }catch (Exception e) {
            m_exception = e;
        }
        return liste;
    }
    public ArrayList<ParcoursConducteur> getConducteur(Activity activity) {
        ArrayList<ParcoursConducteur> liste = null;
        session = new SessionManager(activity.getApplicationContext());
        UserRepo UtilCourant = new UserRepo(activity.getApplicationContext());
        try {
            User unUser = UtilCourant.getUser();
            String longLat = unUser.get_adresse();
            vectLongLat = longLat.split(";");
            vectLongLat[0].replace(',', '.');
            vectLongLat[1].replace(',', '.');
            URI uri = new URI("Http", WEB_SERVICE_URL, REST_CONDUCTEUR + REST_LAT +
                    vectLongLat[0] + REST_LONG + vectLongLat[1], null, null);
            HttpGet get = new HttpGet(uri);
            String body = m_ClientHttp.execute(get, new BasicResponseHandler());
            Log.i(TAG, "Reçu conduteur: " + body);
            liste = jsonParser.parseConducteurListe(body);

        } catch (Exception e) {
            m_exception = e;
        }
        return liste;
    }

    public ArrayList<ParcoursPassager> getPassager(Activity activity) {
        ArrayList<ParcoursPassager> liste = null;
        session = new SessionManager(activity.getApplicationContext());
        UserRepo UtilCourant = new UserRepo(activity.getApplicationContext());
        try {
            User unUser = UtilCourant.getUser();
            String longLat = unUser.get_adresse();
            vectLongLat = longLat.split(";");
            vectLongLat[0].replace(',', '.');
            vectLongLat[1].replace(',', '.');
            URI uri = new URI("Http", WEB_SERVICE_URL, REST_PASSAGER + REST_LAT +
                    vectLongLat[0] + REST_LONG + vectLongLat[1], null, null);
            HttpGet get = new HttpGet(uri);
            String body = m_ClientHttp.execute(get, new BasicResponseHandler());
            Log.i(TAG, "Reçu passager : " + body);
            liste = jsonParser.parsePassagerListe(body);
        } catch (
                Exception e
                )

        {
            m_exception = e;
        }
        return liste;
    }

    public void getCondu(String m_id,int m_nbPlace)
    {
        try{
        URI uri = new URI("http",WEB_SERVICE_URL,REST_CONDUCTEUR + "/" +m_id + REST_NBPLACE + m_nbPlace,null,null);
        URI uri2 = new URI("http",WEB_SERVICE_URL,REST_CONDUCTEUR + "/" + m_id + REST_LAT +
                vectLongLat[0] + REST_LONG + vectLongLat[1],null,null);
        HttpGet get = new HttpGet(uri2);
        ArrayList<ParcoursConducteur> m_listeCondu;
        HttpPut put = new HttpPut(uri);
        put.addHeader("Content-Type", "application/json");
        String body = m_ClientHttp.execute(get,new BasicResponseHandler());
        m_listeCondu = jsonParser.parseConducteurListe(body);
        m_ClientHttp.execute(put,new BasicResponseHandler());
        unParcoursCond = m_listeCondu.get(0);
        unParcoursCond.set_nombreDePlace(m_nbPlace);
        }catch (Exception e){
            m_exception = e;
        }
    }

    public void getPass(String m_id){
        try {
            ArrayList<ParcoursPassager> m_listePass;
            URI uri = new URI("http",WEB_SERVICE_URL,REST_PASSAGER + "/" + m_id + REST_LAT +
                    vectLongLat[0] + REST_LONG + vectLongLat[1],null,null);
            HttpGet get = new HttpGet(uri);
            String body = m_ClientHttp.execute(get,new BasicResponseHandler());
            m_listePass = jsonParser.parsePassagerListe(body);
            unParcoursPassa = m_listePass.get(0);
        }catch (Exception e){
            m_exception = e;
        }
    }
}
