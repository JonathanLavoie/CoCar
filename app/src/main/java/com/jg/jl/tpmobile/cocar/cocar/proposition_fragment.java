package com.jg.jl.tpmobile.cocar.cocar;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    HashMap<String,String> mapp;
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
        ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo m3G = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);


        final ListView maListe = (ListView) rootView.findViewById(R.id.listviewperso);
        ArrayList<HashMap<String, String>> listMap = new ArrayList<>();
        if(mWifi.isConnected() || m3G.isConnected()) {
            session = new SessionManager(getActivity().getApplicationContext());

            HashMap<String, String> map;
            String type;
            DateFormat dateCurrent = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            Date date1 = null;
            if (!listConducteur.isEmpty()) {
                for (int i = 0; i < listConducteur.size(); i++) {
                    Exception exc;
                    if (!listConducteur.get(i).get_identifiant().equals(session.getIdentification())) {
                        try {
                            String strDate = listConducteur.get(i).get_date() + " " + listConducteur.get(i).get_heure();
                            date1 = dateCurrent.parse(strDate);
                        } catch (Exception ex) {
                            exc = ex;
                        }

                        if (date.before(date1)) {
                            map = new HashMap<>();

                            type = "Conducteur";
                            map.put("type", type);
                            map.put("img", String.valueOf(R.drawable.car72));
                            map.put("id", "Identifiant de parcours : " + listConducteur.get(i).get_ID());
                            map.put("idParcours", listConducteur.get(i).get_ID());
                            map.put("id1", session.getIdentification());
                            map.put("id2", listConducteur.get(i).get_identifiant());
                            map.put("Depart",listConducteur.get(i).get_depart());
                            map.put("Destination", listConducteur.get(i).get_destination());
                            map.put("nbPass", String.valueOf(listConducteur.get(i).get_nombreDePlace()));
                            map.put("date", "Date : " + listConducteur.get(i).get_date() + " " + listConducteur.get(i).get_heure());
                            map.put("description", "Depart : " + listConducteur.get(i).get_depart() +
                                    "\nDestination : " + listConducteur.get(i).get_destination() +
                                    "\nNombre de place disponible : " + listConducteur.get(i).get_nombreDePlace());
                            map.put("infoSupp", "\nNombre de kilomètre du point de départ : " +
                                    listConducteur.get(i).get_disDep() + " km\nNombre de kilomètre de la destination : " +
                                    listConducteur.get(i).get_disDest() + " km\nCourriel du demandeur : \n" + listConducteur.get(i).get_identifiant() +
                                    "\nKm max à parcourir : " + listConducteur.get(i).get_KM() + "\n");
                            listMap.add(map);

                        }
                    }
                }
            }

            if (!listPassager.isEmpty()) {
                for (int i = 0; i < listPassager.size(); i++) {
                    Exception exc;
                    if (!listPassager.get(i).get_identifiant().equals(session.getIdentification())) {
                        try {
                            String strDate = listPassager.get(i).get_date() + " " + listPassager.get(i).get_heure();
                            date1 = dateCurrent.parse(strDate);
                        } catch (Exception ex) {
                            exc = ex;
                        }
                        if (date.before(date1)) {
                            map = new HashMap<>();
                            type = "Passager";
                            map.put("type", type);
                            map.put("img", String.valueOf(R.drawable.passager));
                            map.put("id", "Identifiant de parcours : " + listPassager.get(i).get_ID());
                            map.put("idParcours", listPassager.get(i).get_ID());
                            map.put("id1", session.getIdentification());
                            map.put("id2", listPassager.get(i).get_identifiant());
                            map.put("Depart",listPassager.get(i).get_depart());
                            map.put("Destination", listPassager.get(i).get_destination());
                            map.put("nbPass",String.valueOf(listPassager.get(i).get_nombrePassager()));
                            map.put("date", "Date : " + listPassager.get(i).get_date() + " " + listPassager.get(i).get_heure());
                            map.put("description", "Départ : " + listPassager.get(i).get_depart() + "\nDestination : " + listPassager.get(i).get_destination()
                                    + "\nNombre de passager : " + listPassager.get(i).get_nombrePassager());
                            map.put("infoSupp", "\nNombre de kilomètre du point de départ : " +
                                    listPassager.get(i).get_disDep() + " km\nNombre de kilomètre de la destination : " +
                                    listPassager.get(i).get_disDest() + " km\nCourriel : " + listPassager.get(i).get_identifiant()
                                    + "\n");
                            listMap.add(map);
                        }
                    }
                }
            }
            listMap = triBulleMap(listMap);
        }
        else
        {
            Util.afficherAlertBox(getActivity(),"Aucune connexion internet trouvé","Erreur WIFI non trouvé");
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
        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), listMap, R.layout.layout_proposition_personnalise,
                new String[]{"img", "id","date","description"}, new int[]{R.id.img, R.id.titre,R.id.date,R.id.description});
        maListe.setAdapter(adapter);

        //Création de l'évènement click sur un élément de la liste.
        maListe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mapp = (HashMap<String,String>)  maListe.getItemAtPosition(position);
                if (mapp.get("type") == "Conducteur") {
                    proposerCondu();
                } else {
                    proposerPass();
                }
            }
        });
    }
    private int m_nbPlace;
    private String m_id;
    private String m_Type;
    private String m_idParcours;
    private String m_id1;
    private String m_id2;


    public void proposerPass(){
        View setView = View.inflate(getActivity(),R.layout.propo_condu_layout,null);
        TextView tvDescrip = (TextView) setView.findViewById(R.id.descPropo);
        TextView label = (TextView) setView.findViewById(R.id.labelNbPass);
        final NumberPicker np = (NumberPicker) setView.findViewById(R.id.numberPicker);
        np.setVisibility(View.INVISIBLE);
        label.setVisibility(View.INVISIBLE);
        tvDescrip.setText(mapp.get("id") + "\n" + mapp.get("date") +
                " \nType : " + mapp.get("type") + "\n" +
                mapp.get("description")
                + mapp.get("infoSupp"));
        final AlertDialog d = new AlertDialog.Builder(getActivity())
                .setTitle("Parcours " + mapp.get("type")).setView(setView)
                .setNegativeButton("Annuler", null).setNeutralButton("Voir Carte", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getActivity(), googleMap.class);
                        Bundle b = new Bundle();
                        String[] Depart = mapp.get("Depart").split(";");
                        String[] Dest = mapp.get("Destination").split(";");
                        float latDepart = Float.parseFloat(Depart[0]);
                        float longDepart = Float.parseFloat(Depart[1]);
                        float latDest = Float.parseFloat(Dest[0]);
                        float longDest = Float.parseFloat(Dest[1]);

                        UserRepo repo = new UserRepo(getActivity());
                        User unUser = repo.getUser();
                        String[] LatLong = unUser.get_adresse().split(";");
                        float latCur = Float.parseFloat(LatLong[0]);
                        float longCur = Float.parseFloat(LatLong[1]);
                        b.putFloat("latCurr",latCur);
                        b.putFloat("longCurr",longCur);

                        b.putFloat("lat", latDepart);
                        b.putFloat("long", longDepart);
                        b.putFloat("latDest", latDest);
                        b.putFloat("longDest",longDest);
                        i.putExtras(b);
                        startActivity(i);
                    }
                })
                .setPositiveButton("Proposer", null)
                .create();
        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
               Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        m_nbPlace = np.getValue();
                        m_Type = mapp.get("type");
                        m_idParcours = mapp.get("idParcours");
                        m_id1 = mapp.get("id1");
                        m_id2 = mapp.get("id2");
                        new updatePassager().execute((Void)null);
                        Toast.makeText(getActivity(),"Passager ajouter dans départ",Toast.LENGTH_SHORT).show();
                        d.dismiss();
                    }
                });
            }
        });
        d.show();
    }


    public void proposerCondu()
    {
        View setView = View.inflate(getActivity(),R.layout.propo_condu_layout,null);
        TextView tvDescrip = (TextView) setView.findViewById(R.id.descPropo);
        final NumberPicker np = (NumberPicker) setView.findViewById(R.id.numberPicker);
        np.setWrapSelectorWheel(true);
        np.setMinValue(1);
        np.setMaxValue(Integer.parseInt(mapp.get("nbPass")));
        tvDescrip.setText(mapp.get("id") + "\n" + mapp.get("date") +
                " \nType : " + mapp.get("type") + "\n" +
                mapp.get("description")
                + mapp.get("infoSupp"));
        final AlertDialog d = new AlertDialog.Builder(getActivity())
                .setTitle("Parcours " + mapp.get("type")).setView(setView)
                .setNegativeButton("Annuler", null).setNeutralButton("Voir Carte", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getActivity(), googleMap.class);
                        Bundle b = new Bundle();
                        String Depart[] = mapp.get("Depart").split(";");
                        String Dest[] = mapp.get("Destination").split(";");
                        float latDepart = Float.parseFloat(Depart[0]);
                        float longDepart = Float.parseFloat(Depart[1]);
                        float latDest = Float.parseFloat(Dest[0]);
                        float longDest = Float.parseFloat(Dest[1]);

                        UserRepo repo = new UserRepo(getActivity());
                        User unUser = repo.getUser();
                        String[] LatLong = unUser.get_adresse().split(";");
                        float latCur = Float.parseFloat(LatLong[0]);
                        float longCur = Float.parseFloat(LatLong[1]);
                        b.putFloat("latCurr",latCur);
                        b.putFloat("longCurr",longCur);

                        b.putFloat("lat", latDepart);
                        b.putFloat("long", longDepart);
                        b.putFloat("latDest", latDest);
                        b.putFloat("longDest", longDest);
                        i.putExtras(b);
                        startActivity(i);
                    }
                })
                .setPositiveButton("Proposer",null)
                .create();
        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        m_nbPlace = np.getValue();
                        m_id = mapp.get("id");
                        m_Type = mapp.get("type");
                        m_idParcours = mapp.get("idParcours");
                        m_id1 = mapp.get("id1");
                        m_id2 = mapp.get("id2");
                        new updateConducteur().execute((Void)null);
                        Toast.makeText(getActivity(),"Conducteur ajouter dans départ",Toast.LENGTH_SHORT).show();
                        d.dismiss();
                    }
                });
            }
        });
        d.show();
    }
    //permet de tri en date de la proposition la plus proche de la date actuelle.
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

    private class updateConducteur extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            getActivity().setProgressBarIndeterminateVisibility(true);


        }
        @Override
        protected Void doInBackground(Void... params) {
            web.getCondu(m_idParcours, m_nbPlace);
            web.putDepart(m_id1, m_id2, m_idParcours, m_nbPlace,m_Type);
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
            web.getPass(m_idParcours);
            web.putDepart(m_id1,m_id2,m_idParcours,Integer.parseInt(mapp.get("nbPass")),m_Type);
            return null;
        }
    }
}