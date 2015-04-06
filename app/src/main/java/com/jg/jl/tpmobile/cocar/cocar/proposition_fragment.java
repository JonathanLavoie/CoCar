package com.jg.jl.tpmobile.cocar.cocar;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import com.jg.jl.tpmobile.cocar.cocar.WebServices.jsonParser;

/**
 * Created by Jiimmy on 2015-03-04.
 */
public class proposition_fragment extends Fragment {
    View rootView;
    private final String TAG = this.getClass().getSimpleName();
    private final static String WEB_SERVICE_URL = "10.0.2.2:8080";
    private final static String REST_CONDUCTEUR = "/conducteur";
    private final static String REST_PASSAGER = "/passager";
    private final static String REST_NBPLACE = "/nbPlace/";

    private HttpClient m_ClientHttp = new DefaultHttpClient();
    private ArrayList<ParcoursConducteur> listConducteur = null;
    private ArrayList<ParcoursPassager> listPassager = null;
    SessionManager session;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.proposition_layout, container, false);
        return rootView;
    }

    //chargement asynchrone
    @Override
    public void onStart() {
        super.onStart();
        new backCreate().execute((Void)null);
    }

    //méthode qui permet d'obtenir les conducteurs
      public ArrayList<ParcoursConducteur> getConducteur() {
            Exception m_exception;
            ArrayList<ParcoursConducteur> liste = null;
            session = new SessionManager(getActivity().getApplicationContext());
            try{
                URI uri = new URI("Http", WEB_SERVICE_URL, REST_CONDUCTEUR, null, null);
                HttpGet get = new HttpGet(uri);
                String body = m_ClientHttp.execute(get,new BasicResponseHandler());
                Log.i(TAG,"Reçu conduteur: " + body);
                liste = jsonParser.parseConducteurListe(body);

            }catch (Exception e){
                m_exception = e;
            }
            return liste;
        }
    public ArrayList<ParcoursPassager> getPassager() {
        Exception m_exception;
        ArrayList<ParcoursPassager> liste = null;
        session = new SessionManager(getActivity().getApplicationContext());
        try{
            URI uri = new URI("Http", WEB_SERVICE_URL, REST_PASSAGER, null, null);
            HttpGet get = new HttpGet(uri);
            String body = m_ClientHttp.execute(get,new BasicResponseHandler());
            Log.i(TAG,"Reçu passager : " + body);
            liste = jsonParser.parsePassagerListe(body);

        }catch (Exception e){
            m_exception = e;
        }
        return liste;
    }

//Exécution asynchrone pour obtenir les données
private class backCreate extends  AsyncTask<Void, Void, Void>{
    @Override
    protected void onPreExecute() {
        getActivity().setProgressBarIndeterminateVisibility(true);
    }

    @Override
    protected Void doInBackground(Void... params) {
        listConducteur = getConducteur();
        listPassager = getPassager();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        getActivity().setProgressBarIndeterminateVisibility(false);
        chargementProposition();
    }
}

    //Permet de charger tous les propositions disponibles dans le service web
    private void chargementProposition() {
        final ListView maListe = (ListView) rootView.findViewById(R.id.listviewperso);
        ArrayList<HashMap<String, String>> listMap = new ArrayList<>();
        HashMap<String, String> map;
        String type;
        DateFormat dateCurrent = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Date date1 = null;
        if(!listConducteur.isEmpty())
        {
            for (int i = 0; i < listConducteur.size(); i++) {
                Exception exc;
                if (!listConducteur.get(i).get_identifiant().equals(session.getIdentification())) {
                    try {
                        String strDate =listConducteur.get(i).get_date() + " " + listConducteur.get(i).get_heure();
                        date1 = dateCurrent.parse(strDate);
                    }catch (Exception ex)
                    {
                        exc = ex;
                    }

                    if(date.before(date1)) {
                        map = new HashMap<>();
                        type = "Conducteur";
                        map.put("type", type);
                        map.put("img", String.valueOf(R.drawable.car72));
                        map.put("id", "Identifiant du parcours : " + listConducteur.get(i).get_ID());
                        map.put("date", "Date : " + listConducteur.get(i).get_date() + " " + listConducteur.get(i).get_heure());
                        map.put("description", "Depart : " + listConducteur.get(i).get_depart() +
                                "\nDestination : " + listConducteur.get(i).get_destination() +
                                "\nNombre de place disponible : " + listConducteur.get(i).get_nombreDePlace());
                        map.put("infoSupp", "\nCourriel du demandeur : \n" + listConducteur.get(i).get_identifiant() +
                                "\nKm max à parcourir : " + listConducteur.get(i).get_KM() + "\n");
                        listMap.add(map);

                    }
                }
            }
        }

        if (!listPassager.isEmpty())
        {
            for (int i = 0; i < listPassager.size(); i++) {
                Exception exc;
                if (!listPassager.get(i).get_identifiant().equals(session.getIdentification())) {
                    try {
                        String strDate =listPassager.get(i).get_date() + " " + listPassager.get(i).get_heure();
                        date1 = dateCurrent.parse(strDate);
                    }catch (Exception ex)
                    {
                        exc = ex;
                    }
                    if(date.before(date1)) {
                        map = new HashMap<>();
                        type = "Passager";
                        map.put("type", type);
                        map.put("img", String.valueOf(R.drawable.passager));
                        map.put("id", "Identifiant de parcours : " + listPassager.get(i).get_ID());
                        map.put("date", "Date : " + listPassager.get(i).get_date() + " " + listPassager.get(i).get_heure());
                        map.put("description", "Destination : " + listPassager.get(i).get_destination()
                                + "\nNombre de passager: " + listPassager.get(i).get_nombrePassager());
                        map.put("infoSupp", "\nCourriel : " + listPassager.get(i).get_identifiant() + "\n");
                        listMap.add(map);
                    }
                }
            }
        }

        //Si aucune proposition n'est disponible
        if (listMap.isEmpty())
        {
            TextView tv = new TextView(getActivity());
            tv.setText("Aucune propostion");
            tv.setPadding(50,250,0,0);
            RelativeLayout lst = (RelativeLayout)rootView.findViewById(R.id.rlpropo);
            lst.addView(tv);
        }

        listMap = triBulleMap(listMap);
        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), listMap, R.layout.layout_proposition_personnalise,
                new String[]{"img", "id","date","description"}, new int[]{R.id.img, R.id.titre,R.id.date,R.id.description});
        maListe.setAdapter(adapter);

        //Création de l'évènement click sur un élément de la liste.
        maListe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                HashMap<String,String> map = (HashMap<String,String>)  maListe.getItemAtPosition(position);
                adb.setTitle("Parcours " + map.get("type"));

                adb.setMessage(map.get("id") + "\n" + map.get("date") +
                            " \nType : " + map.get("type") + "\n" +
                            map.get("description")
                            + map.get("infoSupp")+
                            "\n/!\\ Attention cette action est définitif, vous ne pouvez pas annuler une fois accepté./!\\ ");

                adb.setNegativeButton("Annuler", null);
                adb.setPositiveButton("Proposer",new btnProposer(1,map.get("id")));
                adb.show();
            }
        });
    }

    public ArrayList<HashMap<String, String>> triBulleMap(ArrayList<HashMap<String, String>> list) {
        for (int i = 0; i <= list.size() - 2; i++) {
            for (int j = list.size() - 1; i < j; j--) {
                HashMap<String, String> hashmap1 = list.get(j);
                HashMap<String, String> hashmap2 = list.get(j - 1);
                SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date1;
                Date date2;
                try {
                    String sub1 = hashmap1.get("date").substring(7);
                    String sub2 = hashmap2.get("date").substring(7);
                    date1 = date.parse(sub1);
                    date2 = date.parse(sub2);
                    if (date1.before(date2)) {
                        HashMap<String, String> tempo = list.get(j);
                        list.set(j, list.get(j - 1));
                        list.set(j - 1, tempo);
                    }
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return list;
    }
    private int m_nbPlace;
    private String m_id;
    ParcoursConducteur unParcours = new ParcoursConducteur();
    private class btnProposer implements DialogInterface.OnClickListener{

        public btnProposer(int p_nbPlace, String p_id){
            p_id = p_id.substring(26);
            m_nbPlace = p_nbPlace;
            m_id = p_id;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            new updateConducteur().execute((Void)null);
            Toast.makeText(getActivity(), "Update réussi", Toast.LENGTH_SHORT).show();

        }
    }


    private class updateConducteur extends AsyncTask<Void,Void,Void>{
        Exception m_exception;

        @Override
        protected void onPreExecute() {
            getActivity().setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String strHttp = "http://10.0.2.2:8080/conducteur/" + m_id;
                String strPut = strHttp + "/nbPlace/" + m_nbPlace;
                URI uri = new URI("http",WEB_SERVICE_URL,REST_CONDUCTEUR + "/" +m_id + REST_NBPLACE + m_nbPlace,null,null);
                ArrayList<ParcoursConducteur> m_listeCondu;
                HttpPut put = new HttpPut(uri);
                put.addHeader("Content-Type", "application/json");
                m_ClientHttp.execute(put,new BasicResponseHandler());

                uri = new URI(strHttp);
                HttpGet get = new HttpGet(uri);
                String body = m_ClientHttp.execute(get,new BasicResponseHandler());
                m_listeCondu = jsonParser.parseConducteurListe(body);
                ParcoursConducteurRepo repCondu = new ParcoursConducteurRepo(getActivity().getApplicationContext());
                unParcours = m_listeCondu.get(0);
                unParcours.set_nombreDePlace(m_nbPlace);
                repCondu.insert(unParcours);
            }catch (Exception e){
                m_exception = e;
            }
            return null;
        }
    }
}