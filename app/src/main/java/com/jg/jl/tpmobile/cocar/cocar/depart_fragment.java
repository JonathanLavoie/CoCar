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
        ParcoursConducteurRepo unParcoursConducteur = new ParcoursConducteurRepo(getActivity());
        ParcoursPassagerRepo unParcoursPassager = new ParcoursPassagerRepo(getActivity());
        String type;
        for (int i = 0; i < listPassager.size(); i++) {
            map = new HashMap<>();
            type = "Passager";
            map.put("type", type);
            map.put("img", String.valueOf(R.drawable.passager));
            map.put("id","Numéro de parcours : " + listPassager.get(i).get_ID());
            map.put("date", "Date : " + listPassager.get(i).get_date()+ " " + listPassager.get(i).get_heure());
            map.put("description", "Départ : " + listPassager.get(i).get_depart() + "\nDestination : " + listPassager.get(i).get_destination()
                    + "\nNombre de passager : " + listPassager.get(i).get_nombrePassager());
            map.put("Depart",  listPassager.get(i).get_depart());
            map.put("Destination", listPassager.get(i).get_destination());
            map.put("NbrPlace",  "" + listPassager.get(i).get_nombrePassager());
            map.put("infoSupp", "\nCourriel : " + listPassager.get(i).get_identifiant() + "\n");
            listMap.add(map);
            unParcoursPassager.insert(listPassager.get(i));
        }
        for (int i = 0; i < listConducteur.size(); i++) {
            map = new HashMap<>();
            type = "Conducteur";
            map.put("type", type);
            map.put("img", String.valueOf(R.drawable.car72));
            map.put("id","Numéro de parcours : " + listConducteur.get(i).get_ID());
            map.put("date", "Date : " + listConducteur.get(i).get_date() + " " + listConducteur.get(i).get_heure());
            map.put("description", "Depart : " + listConducteur.get(i).get_depart() +
                    "\nDestination : " + listConducteur.get(i).get_destination() +
                    "\nNombre de place disponible : " + listConducteur.get(i).get_nombreDePlace());
            map.put("Depart",  listConducteur.get(i).get_depart());
            map.put("Destination", listConducteur.get(i).get_destination());
            map.put("NbrPlace",  "" + listConducteur.get(i).get_nombreDePlace());
            map.put("infoSupp", "\nCourriel du demandeur : \n" + listConducteur.get(i).get_identifiant() +
                    "Km max à parcourir : " + listConducteur.get(i).get_KM() + "\n");
            listMap.add(map);
            unParcoursConducteur.insert(listConducteur.get(i));
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
                            b.putFloat("lat", latDepart);
                            b.putFloat("long", longDepart);
                            b.putFloat("latDest", latDest);
                            b.putFloat("longDest",longDest);
                            i.putExtras(b);
                            startActivity(i);
                            /*String Depart[] = map.get("Depart").split(";");
                            float latDepart = Float.parseFloat(Depart[0]);
                            float longDepart = Float.parseFloat(Depart[1]);
                            String url = "https://maps.google.com/maps?z=10&t=m&q=loc:"+latDepart+"+"+longDepart+"";
                            Uri uri = Uri.parse(url);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                            String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latDepart, longDepart);
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            startActivity(intent);*/
                        }
                    });
                    adb.show();
                }
            });
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
