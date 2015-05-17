package com.jg.jl.tpmobile.cocar.cocar;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jg.jl.tpmobile.cocar.cocar.webService.webService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Jiimmy on 2015-03-04.
 */
public class depart_fragment extends Fragment{
    View rootView;
    ArrayList<ParcoursPassager> listPassager;
    ArrayList<ParcoursConducteur> listConducteur;
    webService web = new webService();
    String deleteIdPar,deleteIdDep,deleteType,deletePlace;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.depart_layout,container,false);

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        new backDepart().execute((Void)null);
    }

    private class backDepart extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            //TODO recupéré les valeurs...
            listConducteur = web.getDepartCondu(getActivity());
            listPassager = web.getDepartPass(getActivity());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            chargementDepart();
        }
    }

    //Méthode qui charge tous les donnée du fragment départ.
    private void chargementDepart() {
        final ListView maListe = (ListView) rootView.findViewById(R.id.lstDepart);
        ArrayList<HashMap<String, String>> listMap = new ArrayList<>();
        HashMap<String, String> map;
        String type;
        for (int i = 0; i < listPassager.size(); i++) {
            map = new HashMap<>();
            type = "Passager";
            map.put("type", type);
            map.put("img", String.valueOf(R.drawable.car72));
            map.put("etat","Vous êtes le conducteur");
            String[] idPar = listPassager.get(i).get_ID().split(";");
            map.put("idDep",idPar[1]);
            map.put("id","Numéro de parcours : " + idPar[0]);
            map.put("idPar",idPar[0]);
            map.put("date",map.get("etat") + "\n" + "Date : " + listPassager.get(i).get_date()+ " " + listPassager.get(i).get_heure());
            map.put("description", "Départ : " + listPassager.get(i).get_depart() + "\nDestination : " + listPassager.get(i).get_destination()
                    + "\nNombre de passager : " + listPassager.get(i).get_nombrePassager());
            map.put("Depart",  listPassager.get(i).get_depart());
            map.put("Destination", listPassager.get(i).get_destination());
            map.put("NbrPlace",  "" + listPassager.get(i).get_nombrePassager());
            map.put("infoSupp", "\nCourriel : " + listPassager.get(i).get_identifiant() + "\n");
            listMap.add(map);
        }
        for (int i = 0; i < listConducteur.size(); i++) {
            map = new HashMap<>();
            type = "Conducteur";
            map.put("type", type);
            map.put("img", String.valueOf(R.drawable.passager));
            String id = listConducteur.get(i).get_ID();
            String[] idPar = id.split(";");
            map.put("idDep",idPar[1]);
            map.put("id","Numéro de parcours : " + idPar[0]);
            map.put("idPar",idPar[0]);
            map.put("etat","Vous êtes le passager");
            map.put("date", map.get("etat") + "\n" + "Date : " + listConducteur.get(i).get_date() + " " + listConducteur.get(i).get_heure());
            map.put("description", "Depart : " + listConducteur.get(i).get_depart() +
                    "\nDestination : " + listConducteur.get(i).get_destination() +
                    "\nNombre de place disponible : " + listConducteur.get(i).get_nombreDePlace());
            map.put("Depart",  listConducteur.get(i).get_depart());
            map.put("Destination", listConducteur.get(i).get_destination());
            map.put("NbrPlace",  "" + listConducteur.get(i).get_nombreDePlace());
            map.put("infoSupp", "\nCourriel du demandeur : \n" + listConducteur.get(i).get_identifiant() +
                    "\nKm max à parcourir : " + listConducteur.get(i).get_KM() + "\n");
            listMap.add(map);
        }

        if (listMap.isEmpty())
        {
            TextView tv = new TextView(getActivity());
            tv.setText("Aucun départ de prévu");
            tv.setPadding(50,250,0,0);
            RelativeLayout lst = (RelativeLayout)rootView.findViewById(R.id.rldepart);
            lst.addView(tv);
        }
        else {
            listMap = triBulle(listMap);

            SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), listMap, R.layout.layout_proposition_personnalise,
                    new String[]{"img", "id","date","description"}, new int[]{R.id.img, R.id.titre,R.id.date,R.id.description});
            maListe.setAdapter(adapter);

            maListe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                HashMap<String, String> map;
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                    map = (HashMap<String, String>) maListe.getItemAtPosition(position);
                    adb.setTitle("Aperçu");
                    adb.setMessage(map.get("id") + "\n" + map.get("date") + " \nType : " + map.get("type")
                            + "\n" + map.get("description") + map.get("infoSupp"));
                    adb.setPositiveButton("OK", null);
                    adb.setNeutralButton("Afficher Carte", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(getActivity(), googleMap.class);
                            Bundle b = new Bundle();
                            String Depart[] = map.get("Depart").split(";");
                            String Dest[] = map.get("Destination").split(";");
                            float latDepart = Float.parseFloat(Depart[0]);
                            float longDepart = Float.parseFloat(Depart[1]);
                            float latDest = Float.parseFloat(Dest[0]);
                            float longDest = Float.parseFloat(Dest[1]);

                            UserRepo repo = new UserRepo(getActivity());
                            User unUser = repo.getUser();
                            String[] LatLong = unUser.get_adresse().split(";");
                            float latCur = Float.parseFloat(LatLong[0]);
                            float longCur = Float.parseFloat(LatLong[1]);
                            b.putFloat("latCurr", latCur);
                            b.putFloat("longCurr", longCur);

                            b.putFloat("lat", latDepart);
                            b.putFloat("long", longDepart);
                            b.putFloat("latDest", latDest);
                            b.putFloat("longDest", longDest);
                            i.putExtras(b);
                            startActivity(i);
                        }
                    });
                    adb.setNegativeButton("Supprimer", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getActivity(),map.get("idPar") + " va être supprimer bientôt",Toast.LENGTH_SHORT).show();
                            deleteIdPar = map.get("idPar");
                            deleteIdDep = map.get("idDep");
                            deleteType = map.get("type");
                            deletePlace = map.get("NbrPlace");
                            new delete().execute((Void)null);
                        }
                    });
                    adb.show();
                }
            });
        }
    }


    private class delete extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            web.deletePar(deleteIdPar,deleteIdDep,deleteType,deletePlace);
            return null;
        }
    }


    //permet de faire un tri des depart en date du départ le plus prochain.
    public ArrayList<HashMap<String, String>> triBulle(ArrayList<HashMap<String, String>> list) {
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
}
