package com.jg.jl.tpmobile.cocar.cocar;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jg.jl.tpmobile.cocar.cocar.webService.webService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.SimpleTimeZone;

/**
 * Created by Jiimmy on 2015-03-04.
 */
public class note_fragment extends Fragment{
    private View rootView;
    private SessionManager session;
    private ArrayList<NoteDepart> listNoteDepart = null;
    HashMap<String, String> mapp = null;
    RatingBar rateValue;
    webService web = new webService();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.note_layout,container,false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        new backChargement().execute((Void)null);
    }


    private class backChargement extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            listNoteDepart = web.getNote(getActivity());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            chargementNote();
        }
    }
    private void chargementNote(){
        ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo m3G = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        final ListView maListe = (ListView) rootView.findViewById(R.id.listNote);
        ArrayList<HashMap<String, String>> listMap = new ArrayList<>();

        if((mWifi != null && mWifi.isConnected()) || (m3G != null && m3G.isConnected())) {
            session = new SessionManager(getActivity().getApplicationContext());
            HashMap<String, String> map;
            if (listNoteDepart != null && !listNoteDepart.isEmpty()) {
                for (int i = 0; i < listNoteDepart.size(); i++) {
                    map = new HashMap<>();
                    map.put("nbPass", String.valueOf(listNoteDepart.get(i).getNbPassager()));
                    map.put("idUser1", listNoteDepart.get(i).getIdUser1());
                    map.put("idUser2", listNoteDepart.get(i).getIdUser2());
                    map.put("rating", String.valueOf(listNoteDepart.get(i).getRate()));
                    map.put("idParcour", listNoteDepart.get(i).getIdParcours());
                    map.put("idNote", listNoteDepart.get(i).getIdNote());
                    if (listNoteDepart.get(i).getRate() == 0) {
                        listMap.add(map);
                    }
                }
            }
        }
        else
        {
            Util.afficherAlertBox(getActivity(),"Aucune connexion internet trouvé","Erreur WIFI non trouvé");
        }
            if(listMap.isEmpty())
            {
                TextView tv = new TextView(getActivity());
                tv.setText("Aucun utilisateur à évaluer");
                tv.setPadding(50,250,0,0);
                RelativeLayout lst = (RelativeLayout)rootView.findViewById(R.id.rlnote);
                lst.addView(tv);
            }
            SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(),listMap,R.layout.layout_note_personnalise,
                    new String[]{"idUser1","idUser2","idParcour","rating"},
                    new int[]{R.id.idUser1note,R.id.idUser2note,R.id.idParcournote,R.id.idRatePerso});
            adapter.setViewBinder(new MyBinder());
            maListe.setAdapter(adapter);
            maListe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mapp = (HashMap<String, String>) maListe.getItemAtPosition(position);
                    evaluerUnUtil();
                    }
                });

    }
    class MyBinder implements SimpleAdapter.ViewBinder {
        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {
            if(view.getId() == R.id.idRatePerso){
                String stringval = (String) data;
                float ratingValue = Float.parseFloat(stringval);
                RatingBar ratingBar = (RatingBar) view;
                ratingBar.setRating(ratingValue);
                return true;
            }
            return false;
        }
    }
    private void evaluerUnUtil(){
        View setView = View.inflate(getActivity(), R.layout.evaluer_layout, null);
        rateValue = (RatingBar) setView.findViewById(R.id.idRate);
        rateValue.setRating(Float.parseFloat(mapp.get("rating")));
        final AlertDialog d = new AlertDialog.Builder(getActivity())
                .setTitle("Parcours " + mapp.get("idNote")).setView(setView)
        .setNegativeButton("Annuler", null)
        .setPositiveButton("Proposer", null).create();
     d.setOnShowListener(new DialogInterface.OnShowListener() {
         @Override
         public void onShow(DialogInterface dialog) {
             Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
             b.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     new updateNote().execute((Void)null);
                     Toast.makeText(getActivity(),mapp.get("idParcour") + " : "+ String.valueOf(rateValue.getRating()),Toast.LENGTH_SHORT).show();
                     d.dismiss();
                 }
             });
         }
     });
        d.show();
    }

    private class updateNote extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            web.updateNote(mapp.get("idNote"),rateValue.getRating(),mapp.get("idUser2"));
            return null;
        }
    }
}
