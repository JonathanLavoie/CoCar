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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jiimmy on 2015-03-04.
 */
public class proposition_fragment extends Fragment{
    View rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.proposition_layout,container,false);
        chargementProposition();
        return rootView;
    }

    private void chargementProposition()
    {
        ListView maListe = (ListView)rootView.findViewById(R.id.listviewperso);
        ArrayList<HashMap<String, String>> listMap = new ArrayList<>();
        HashMap<String,String> map = new HashMap<>();
        map.put("titre","Word");
        map.put("description","Texte");
        listMap.add(map);
        map = new HashMap<>();
        map.put("titre","Word2");
        map.put("description","Texte2");
        listMap.add(map);

        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(),listMap,R.layout.layout_proposition_personnalise,
                new String[] {"titre","description"}, new int[] {R.id.titre,R.id.description});
        maListe.setAdapter(adapter);

      maListe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
              adb.setTitle("Item cliquer");
              adb.setMessage("Position : " + position);
              adb.setPositiveButton("OK", null);
              adb.show();
          }
      });

    }
}
