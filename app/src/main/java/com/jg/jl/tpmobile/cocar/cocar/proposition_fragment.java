package com.jg.jl.tpmobile.cocar.cocar;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import com.jg.jl.tpmobile.cocar.cocar.webService.webService;

/**
 * Created by Jiimmy on 2015-03-04.
 */
public class proposition_fragment extends Fragment {
    View rootView;
    private webService web = new webService();
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

//Exécution asynchrone pour obtenir les données
private class backCreate extends  AsyncTask<Void, Void, Void>{
    @Override
    protected void onPreExecute() {
        getActivity().setProgressBarIndeterminateVisibility(true);
    }

    @Override
    protected Void doInBackground(Void... params) {
        listConducteur = web.getConducteur(getActivity());
        listPassager = web.getPassager(getActivity());
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
        session = new SessionManager(getActivity().getApplicationContext());
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
                        map.put("infoSupp", "\nNombre de kilomètre du point de départ : "+
                                listConducteur.get(i).get_disDep() + " km\nNombre de kilomètre de la destination : "+
                                listConducteur.get(i).get_disDest() + " km\nCourriel du demandeur : \n" + listConducteur.get(i).get_identifiant() +
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
                        map.put("description", "Départ : " + listPassager.get(i).get_depart() + "\nDestination : " + listPassager.get(i).get_destination()
                                + "\nNombre de passager : " + listPassager.get(i).get_nombrePassager());
                        map.put("infoSupp", "\nNombre de kilomètre du point de départ : "+
                                listPassager.get(i).get_disDep() + " km\nNombre de kilomètre de la destination : "+
                                listPassager.get(i).get_disDest() + " km\nCourriel : " + listPassager.get(i).get_identifiant()
                                + "\n");
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
                adb.setPositiveButton("Proposer",new btnProposer(1,map.get("id"),map.get("type")));
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
    private String m_Type;
    private class btnProposer implements DialogInterface.OnClickListener{

        public btnProposer(int p_nbPlace, String p_id,String p_type){
            p_id = p_id.substring(26);
            m_nbPlace = p_nbPlace;
            m_id = p_id;
            m_Type = p_type;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(m_Type.equals("Conducteur")){
                new updateConducteur().execute((Void)null);
            }else{
                new updatePassager().execute((Void)null);
            }
            Toast.makeText(getActivity(), "Update réussi", Toast.LENGTH_SHORT).show();
        }
    }

    private class updateConducteur extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            getActivity().setProgressBarIndeterminateVisibility(true);

        }
        @Override
        protected Void doInBackground(Void... params) {
            web.getConduEtInsertSQL(m_id, m_nbPlace, getActivity());
            return null;
        }
    }

    private class updatePassager extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            getActivity().setProgressBarIndeterminateVisibility(true);
        }
        @Override
        protected Void doInBackground(Void... params) {
            web.getPassEtInsertSQL(getActivity(), m_id);
            return null;
        }
    }
}