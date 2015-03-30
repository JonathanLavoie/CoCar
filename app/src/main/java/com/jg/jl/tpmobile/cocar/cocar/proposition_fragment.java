package com.jg.jl.tpmobile.cocar.cocar;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Jiimmy on 2015-03-04.
 */
public class proposition_fragment extends Fragment {
    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.proposition_layout, container, false);
        chargementProposition();
        return rootView;
    }

    private void chargementProposition() {
        final ListView maListe = (ListView) rootView.findViewById(R.id.listviewperso);
        ArrayList<HashMap<String, String>> listMap = new ArrayList<>();
        HashMap<String, String> map;
        ParcoursConducteurRepo unParcoursConducteur = new ParcoursConducteurRepo(getActivity());
        ParcoursPassagerRepo unParcoursPassager = new ParcoursPassagerRepo(getActivity());

        ArrayList<ParcoursPassager> listPassager = unParcoursPassager.getAllParcours();
        ArrayList<ParcoursConducteur> listConducteur = unParcoursConducteur.getAllParcours();
        String type;
        for (int i = 0; i < listPassager.size(); i++) {
            map = new HashMap<>();
            type = "Passager";
            map.put("img", String.valueOf(R.drawable.passager));
            map.put("date", listPassager.get(i).get_date()+ " " + listPassager.get(i).get_heure());
            map.put("description", type.toUpperCase() + "\nDestination : " + listPassager.get(i).get_destination()
            + "\nNombre de passager: " + listPassager.get(i).get_nombrePassager());
            listMap.add(map);
        }
        for (int i = 0; i < listConducteur.size(); i++) {
            map = new HashMap<>();
            type = "Conducteur";
            map.put("img", String.valueOf(R.drawable.car72));
            map.put("date", listConducteur.get(i).get_date() + " " + listConducteur.get(i).get_heure());
            map.put("description", type.toUpperCase());
            listMap.add(map);
        }

        if (listMap.isEmpty())
        {
            TextView tv = new TextView(getActivity());
            tv.setText("Aucune propostion");
            tv.setPadding(50,250,0,0);
            RelativeLayout lst = (RelativeLayout)rootView.findViewById(R.id.rlpropo);
            lst.addView(tv);
        }
        listMap = triBulle(listMap);

        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), listMap, R.layout.layout_proposition_personnalise,
                new String[]{"img", "date", "description"}, new int[]{R.id.img, R.id.titre, R.id.description});
        maListe.setAdapter(adapter);

        maListe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                HashMap<String,String> map = (HashMap<String,String>)  maListe.getItemAtPosition(position);
                adb.setTitle("Item cliquer");
                adb.setMessage("Date et heure : " + map.get("date") +" \nType : " +  map.get("description"));
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
                SimpleDateFormat date = new SimpleDateFormat("MM/dd/yyyy HH:mm");
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