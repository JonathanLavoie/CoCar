package com.jg.jl.tpmobile.cocar.cocar;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.net.URI;
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
    private HttpClient m_ClientHttp = new DefaultHttpClient();
    private ArrayList<ParcoursConducteur> listConducteur = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.proposition_layout, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        new backCreateConducteurTest().execute((Void)null);
    }

      public ArrayList<ParcoursConducteur> getConducteur() {
            Exception m_exception;
            ArrayList<ParcoursConducteur> liste = null;
            try{
                URI uri = new URI("Http", WEB_SERVICE_URL, REST_CONDUCTEUR, null, null);
                HttpGet get = new HttpGet(uri);
                String body = m_ClientHttp.execute(get,new BasicResponseHandler());
                Log.i(TAG,"Reçu : " + body);
                liste = jsonParser.parseConducteurListe(body);

            }catch (Exception e){
                m_exception = e;
            }
            return liste;
        }
private class backCreateConducteurTest extends  AsyncTask<Void, Void, Void>{
    @Override
    protected void onPreExecute() {
        getActivity().setProgressBarIndeterminateVisibility(true);
    }

    @Override
    protected Void doInBackground(Void... params) {
        createConducteurTest();
        listConducteur = getConducteur();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        getActivity().setProgressBarIndeterminateVisibility(false);
        chargementProposition();
    }
}
    private void createConducteurTest()
    {
        Exception m_exception;
        try{
            ParcoursConducteur condu = new ParcoursConducteur();
            condu.set_ID(1);
            condu.set_depart("123 dddd");
            condu.set_destination("345 ffff");
            condu.set_date("2015-05-05");
            condu.set_heure("23:09");
            condu.set_KM(234);
            condu.set_nombreDePlace(2);

            URI uri = new URI("http",WEB_SERVICE_URL,REST_CONDUCTEUR + "/" + condu.get_ID(),null,null);
            HttpPut put = new HttpPut(uri);

            JSONObject obj = jsonParser.conducteurToJSONObject(condu);
            put.setEntity(new StringEntity(obj.toString()));
            put.addHeader("Content-Type", "application/json");
            m_ClientHttp.execute(put,new BasicResponseHandler());
            Log.i(TAG,"Put terminer");
        }catch (Exception e){
            m_exception = e;
        }
    }


    private void chargementProposition() {
        final ListView maListe = (ListView) rootView.findViewById(R.id.listviewperso);
        ArrayList<HashMap<String, String>> listMap = new ArrayList<>();
        HashMap<String, String> map;
        String type;
        if(!listConducteur.isEmpty())
        {
            for (int i = 0; i < listConducteur.size(); i++) {
                map = new HashMap<>();
                type = "Conducteur";
                map.put("img", String.valueOf(R.drawable.car72));
                map.put("id","Numéro de parcours : 2" + listConducteur.get(i).get_ID());
                map.put("date", "Date : " + listConducteur.get(i).get_date() + " " + listConducteur.get(i).get_heure());
                map.put("description", type.toUpperCase());
                listMap.add(map);
            }
        }
        /*
        ArrayList<ParcoursPassager> listPassager = unParcoursPassager.getAllParcours();


        for (int i = 0; i < listPassager.size(); i++) {
            map = new HashMap<>();
            type = "Passager";
            map.put("img", String.valueOf(R.drawable.passager));
            map.put("id","Numéro de parcours : 1" + listPassager.get(i).get_ID());
            map.put("date", "Date : " + listPassager.get(i).get_date()+ " " + listPassager.get(i).get_heure());
            map.put("description", type.toUpperCase() + "\nDestination : " + listPassager.get(i).get_destination()
                    + "\nNombre de passager: " + listPassager.get(i).get_nombrePassager());
            listMap.add(map);
        }*/

        if (listMap.isEmpty())
        {
            TextView tv = new TextView(getActivity());
            tv.setText("Aucune propostion");
            tv.setPadding(50,250,0,0);
            RelativeLayout lst = (RelativeLayout)rootView.findViewById(R.id.rlpropo);
            lst.addView(tv);
        }
        //listMap = triBulle(listMap);

        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), listMap, R.layout.layout_proposition_personnalise,
                new String[]{"img", "id","date","description"}, new int[]{R.id.img, R.id.titre,R.id.date,R.id.description});
        maListe.setAdapter(adapter);

        maListe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                HashMap<String,String> map = (HashMap<String,String>)  maListe.getItemAtPosition(position);
                adb.setTitle("Item cliquer");
                adb.setMessage(map.get("id") + "\n" +map.get("date") +" \nType : " +  map.get("description") + "\n/!\\ Attention cette action est définitif, vous ne pouvez pas annuler une fois accepté./!\\ ");
                adb.setNegativeButton("Annuler", null);
                adb.setPositiveButton("Proposer", null);
                adb.show();
            }
        });
    }

    public ArrayList<HashMap<String, String>> triBulle(ArrayList<HashMap<String, String>> list) {
        for (int i = 0; i <= list.size() - 2; i++) {
            for (int j = list.size() - 1; i < j; j--) {
                HashMap<String, String> hashmap1 = list.get(j);
                HashMap<String, String> hashmap2 = list.get(j - 1);
                SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date date1;
                Date date2;
                try {
                    date1 = date.parse(hashmap1.get("date"));
                    date2 = date.parse(hashmap2.get("date"));
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
}