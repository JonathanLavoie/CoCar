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
        ListView maListe = (ListView) rootView.findViewById(R.id.listviewperso);
        ArrayList<HashMap<String, String>> listMap = new ArrayList<>();
        HashMap<String, String> map;
        ParcoursConducteurRepo unParcoursConducteur = new ParcoursConducteurRepo(getActivity());
        ParcoursPassagerRepo unParcoursPassager = new ParcoursPassagerRepo(getActivity());

        ArrayList<ParcoursPassager> listPassager = unParcoursPassager.getAllParcours();
        ArrayList<ParcoursConducteur> listConducteur = unParcoursConducteur.getAllParcours();
        for (int i = 0; i < listPassager.size(); i++) {
            map = new HashMap<>();
            map.put("img", String.valueOf(R.drawable.passager));
            map.put("titre", listPassager.get(i).get_date());
            map.put("description", "Passager");
            listMap.add(map);
        }
        for (int i = 0; i < listConducteur.size(); i++) {
            map = new HashMap<>();
            map.put("img", String.valueOf(R.drawable.car72));
            map.put("titre", listConducteur.get(i).get_date());
            map.put("description", "Conducteur");
            listMap.add(map);
        }
        listMap = triBulle(listMap);

        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), listMap, R.layout.layout_proposition_personnalise,
                new String[]{"img", "titre", "description"}, new int[]{R.id.img, R.id.titre, R.id.description});
        maListe.setAdapter(adapter);

        maListe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                adb.setTitle("Item cliquer");
                adb.setMessage("Position : " + position);
                adb.setNegativeButton("Annuler", null);
                adb.setPositiveButton("Proposer", null);
                adb.show();
            }
        });
    }

    public ArrayList<HashMap<String, String>> triBulle(ArrayList<HashMap<String, String>> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            HashMap<String, String> hashmap1 = list.get(i);
            HashMap<String, String> hashmap2 = list.get(i + 1);
            SimpleDateFormat date = new SimpleDateFormat("MM/dd/yyyy");
            Date date1;
            Date date2;
            try {
                date1 = date.parse(hashmap1.get("titre"));
                date2 = date.parse(hashmap2.get("titre"));
                if (date1.compareTo(date2) == -1) {
                    HashMap<String, String> tempo = list.get(i);
                    list.set(i, list.get(i + 1));
                    list.set(i + 1, tempo);
                }
                if (date1.compareTo(date2) == 1) {
                    HashMap<String, String> tempo = list.get(i + 1);
                    list.set(i, list.get(i));
                    list.set(i + 1, tempo);
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
        return inverserList(list);
    }

    public ArrayList<HashMap<String, String>> inverserList(ArrayList<HashMap<String, String>> list) {
        ArrayList<HashMap<String, String>> nouvelleList = new ArrayList<HashMap<String, String>>();
        for (int i = list.size() - 1; i >= 0; i--) {
            nouvelleList.add(list.get(i));
        }
        return nouvelleList;
    }
}