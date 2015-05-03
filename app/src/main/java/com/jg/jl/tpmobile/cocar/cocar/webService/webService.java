package com.jg.jl.tpmobile.cocar.cocar.webService;

import android.app.Activity;
import android.util.Log;

import com.jg.jl.tpmobile.cocar.cocar.ParcoursConducteur;
import com.jg.jl.tpmobile.cocar.cocar.ParcoursConducteurRepo;
import com.jg.jl.tpmobile.cocar.cocar.ParcoursPassager;
import com.jg.jl.tpmobile.cocar.cocar.ParcoursPassagerRepo;
import com.jg.jl.tpmobile.cocar.cocar.SessionManager;
import com.jg.jl.tpmobile.cocar.cocar.User;
import com.jg.jl.tpmobile.cocar.cocar.UserRepo;
import com.jg.jl.tpmobile.cocar.cocar.jsonParser.jsonParser;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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
    private final static String REST_CONDUCTEUR = "/conducteur";
    private final static String REST_PASSAGER = "/passager";
    private final static String REST_USER = "/user";
    private final static String REST_NBPLACE = "/nbPlace/";
    private final static String REST_LAT = "/lat/";
    private final static String REST_LONG = "/long/";
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

    public User getUserByEmail(String email){
        User unUser = new User();
        try {
            URI uri = new URI("http", WEB_SERVICE_URL, REST_USER + "/" + email, null, null);
            HttpGet get = new HttpGet(uri);
            String body = m_ClientHttp.execute(get,new BasicResponseHandler());
            Log.i(TAG, "Reçu user: " + body);
            unUser = jsonParser.parseUser(body);

        }catch (Exception e) {
            m_exception = e;
        }
        return unUser;
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

    public void getConduEtInsertSQL(String m_id,int m_nbPlace,Activity activity)
    {
        try{
        URI uri = new URI("http",WEB_SERVICE_URL,REST_CONDUCTEUR + "/" +m_id + REST_NBPLACE + m_nbPlace,null,null);
        URI uri2 = new URI("http",WEB_SERVICE_URL,REST_CONDUCTEUR + "/" + m_id + REST_LAT +
                vectLongLat[0] + REST_LONG + vectLongLat[1],null,null);
        HttpGet get = new HttpGet(uri2);
        ArrayList<ParcoursConducteur> m_listeCondu;
        HttpPut put = new HttpPut(uri);
        put.addHeader("Content-Type", "application/json");
        ParcoursConducteurRepo repCondu = new ParcoursConducteurRepo(activity.getApplicationContext());
        String body = m_ClientHttp.execute(get,new BasicResponseHandler());
        m_listeCondu = jsonParser.parseConducteurListe(body);
        m_ClientHttp.execute(put,new BasicResponseHandler());
        unParcoursCond = m_listeCondu.get(0);
        unParcoursCond.set_nombreDePlace(m_nbPlace);
        repCondu.insert(unParcoursCond);
        }catch (Exception e){
            m_exception = e;
        }
    }

    public void getPassEtInsertSQL(Activity activity, String m_id){
        try {
            ParcoursPassagerRepo repPass = new ParcoursPassagerRepo(activity.getApplicationContext());
            ArrayList<ParcoursPassager> m_listePass;
            URI uri = new URI("http",WEB_SERVICE_URL,REST_PASSAGER + "/" + m_id + REST_LAT +
                    vectLongLat[0] + REST_LONG + vectLongLat[1],null,null);
            HttpGet get = new HttpGet(uri);
            String body = m_ClientHttp.execute(get,new BasicResponseHandler());
            m_listePass = jsonParser.parsePassagerListe(body);
            unParcoursPassa = m_listePass.get(0);
            repPass.insert(unParcoursPassa);
        }catch (Exception e){
            m_exception = e;
        }
    }
}
