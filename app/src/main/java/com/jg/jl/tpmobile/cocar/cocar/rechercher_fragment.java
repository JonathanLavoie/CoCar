package com.jg.jl.tpmobile.cocar.cocar;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.pm.PackageInstaller;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.lang.reflect.Array;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.app.Activity;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Created by Jiimmy on 2015-03-04.
 */
public class rechercher_fragment extends ListFragment{
    private String[] m_Tokens = {"Rechercher un parcours"};
    private ArrayList<String> m_Items = new ArrayList<String>();
    private ArrayAdapter<String> m_Adapter;
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        m_Items.addAll(Arrays.asList(m_Tokens));
        m_Adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,m_Items);
        this.setListAdapter(m_Adapter);
        rootView = inflater.inflate(R.layout.rechercher_layout,container,false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        this.registerForContextMenu(this.getListView());
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.menu_rechercher, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.menu_conducteur:
                CreateConducteur(menuInfo.position);
                break;
            case R.id.menu_passager:
                CreatePassager(menuInfo.position);
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void CreateConducteur(int position){

        View setView = View.inflate(getActivity(),R.layout.recherche_conducteur,null);
        EditText txtSetDepart = (EditText) setView.findViewById(R.id.txtDepartC);
        EditText txtSetDestination = (EditText) setView.findViewById(R.id.txtDestinationC);
        EditText km = (EditText) setView.findViewById(R.id.nbKM);
        EditText nb = (EditText) setView.findViewById(R.id.nbPass);
        EditText date = (EditText) setView.findViewById(R.id.dateC);
        EditText time = (EditText) setView.findViewById(R.id.heureC);

        new AlertDialog.Builder(getActivity())
        .setTitle("Creer un parcours")
                .setView(setView)
                .setNegativeButton("Annuler",null)
                .setPositiveButton("Creer",new BtnCreateConducteur(position,txtSetDepart,
                        txtSetDestination,km,nb,date,time))
                .show();
    }

    private class BtnCreateConducteur implements DialogInterface.OnClickListener {
        private int m_position;
        private EditText m_depart;
        private EditText m_destination;
        private EditText m_nbPass;
        private EditText m_nbKM;
        private EditText m_time;
        private EditText m_date;
        public BtnCreateConducteur(int p_position,EditText p_depart,EditText p_destination,
                                   EditText p_km, EditText p_pass, EditText p_time, EditText p_date
                                   ){
            this.m_depart = p_depart;
            this.m_position = p_position;
            this.m_destination = p_destination;
            this.m_nbPass = p_pass;
            this.m_nbKM = p_km;
            this.m_date = p_date;
            this.m_time = p_time;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            ParcoursConducteurRepo repo = new ParcoursConducteurRepo(getActivity());
            ParcoursConducteur conduc = new ParcoursConducteur();
            conduc.set_depart(m_depart.toString());
            conduc.set_destination(m_destination.toString());
            conduc.set_nombreDePlace(Integer.parseInt(m_nbPass.getText().toString().trim()));
            conduc.set_KM(Integer.parseInt(m_nbKM.getText().toString().trim()));
            conduc.set_date(m_date.toString());
            conduc.set_heure(m_time.toString());
            repo.insert(conduc);
            Toast.makeText(getActivity(),"Parcours Creer",Toast.LENGTH_SHORT).show();
        }
    }

    private void CreatePassager(int position){

        View setView = View.inflate(getActivity(),R.layout.recherche_passager,null);
        EditText txtSetDepart = (EditText) setView.findViewById(R.id.txtDepartP);
        EditText txtSetDestination = (EditText) setView.findViewById(R.id.txtDestinationP);
        EditText date = (EditText) setView.findViewById(R.id.dateD);
        EditText time = (EditText) setView.findViewById(R.id.heureD);
        EditText nbPass = (EditText) setView.findViewById(R.id.nbPassP);
        new AlertDialog.Builder(getActivity())
                .setTitle("Rechercher un conducteur")
                .setView(setView)
                .setNegativeButton("Annuler",null)
                .setPositiveButton("Rechercher",new BtnCreatePassager(position,txtSetDepart,
                        txtSetDestination,nbPass,date,time))
                .show();
    }
    private class BtnCreatePassager implements DialogInterface.OnClickListener {
        private int m_position;
        private  EditText m_depart;
        private EditText m_destination;
        private EditText m_date;
        private EditText m_time;
        private EditText m_pass;
        public BtnCreatePassager(int p_position,EditText p_depart,EditText p_destination,EditText p_pass,
                                 EditText p_date,EditText p_time){
            this.m_depart = p_depart;
            this.m_position = p_position;
            this.m_destination = p_destination;
            this.m_date = p_date;
            this.m_time = p_time;
            this.m_pass = p_pass;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            ParcoursPassagerRepo repo = new ParcoursPassagerRepo(getActivity());
            ParcoursPassager passager = new ParcoursPassager();
            passager.set_depart(m_depart.toString());
            passager.set_destination(m_destination.toString());
            passager.set_date(m_date.toString());
            passager.set_heure(m_time.toString());
            passager.set_nombrePassager(Integer.parseInt(m_pass.toString()));
            repo.insert(passager);
            Toast.makeText(getActivity(),"Passager Creer",Toast.LENGTH_SHORT).show();
        }
    }
}
