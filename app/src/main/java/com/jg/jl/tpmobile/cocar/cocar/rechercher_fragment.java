package com.jg.jl.tpmobile.cocar.cocar;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.pm.PackageInstaller;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.jg.jl.tpmobile.cocar.cocar.WebServices.jsonParser;
import java.lang.reflect.Array;
import java.net.URI;
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

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

/**
 * Created by Jiimmy on 2015-03-04.
 */
public class rechercher_fragment extends ListFragment{
    private String[] m_Tokens = {"Créer un parcours (Passager et Conducteur)"};
    private ArrayList<String> m_Items = new ArrayList<String>();
    private ArrayAdapter<String> m_Adapter;
    private final static String WEB_SERVICE_URL = "10.0.2.2:8080";
    private final static String REST_CONDUCTEUR = "/conducteur";
    private final static String REST_PASSAGER = "/passager";
    private HttpClient m_ClientHttp = new DefaultHttpClient();
    ParcoursConducteur conduc = new ParcoursConducteur();
    ParcoursPassager passager = new ParcoursPassager();


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
        .setTitle("Nouveau parcours")
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
                                   EditText p_km, EditText p_pass, EditText p_date, EditText p_time
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
            SessionManager session = new SessionManager(getActivity().getApplicationContext());

            conduc.set_depart(m_depart.getText().toString());
            conduc.set_destination(m_destination.getText().toString());
            conduc.set_nombreDePlace(Integer.parseInt(m_nbPass.getText().toString().trim()));
            conduc.set_KM(Integer.parseInt(m_nbKM.getText().toString().trim()));
            conduc.set_date(m_date.getText().toString());
            conduc.set_heure(m_time.getText().toString());
            conduc.set_identifiant(session.getIdentification());
            new putConducteur().execute((Void)null);
            Toast.makeText(getActivity(),"Parcours Creer",Toast.LENGTH_SHORT).show();
        }
    }
private class putConducteur extends AsyncTask<Void,Void,Void>{
    Exception m_exception;

    @Override
    protected void onPreExecute() {
        getActivity().setProgressBarIndeterminateVisibility(true);
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            URI uri = new URI("http",WEB_SERVICE_URL,REST_CONDUCTEUR,null,null);
            HttpPut put = new HttpPut(uri);
            JSONObject obj = jsonParser.conducteurToJSONObject(conduc);
            put.setEntity(new StringEntity(obj.toString()));
            put.addHeader("Content-Type", "application/json");
            m_ClientHttp.execute(put,new BasicResponseHandler());

        }catch (Exception e){
            m_exception = e;
        }
        return null;
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
                .setTitle("Creer un passager")
                .setView(setView)
                .setNegativeButton("Annuler",null)
                .setPositiveButton("Creer",new BtnCreatePassager(position,txtSetDepart,
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
            SessionManager session = new SessionManager(getActivity().getApplicationContext());
            passager.set_depart(m_depart.getText().toString());
            passager.set_destination(m_destination.getText().toString());
            passager.set_date(m_date.getText().toString());
            passager.set_heure(m_time.getText().toString());
            passager.set_identifiant(session.getIdentification());
            passager.set_nombrePassager(Integer.parseInt(m_pass.getText().toString()));
            new putPassager().execute((Void)null);
            Toast.makeText(getActivity(),"Passager Creer",Toast.LENGTH_SHORT).show();
        }
    }

    private class putPassager extends AsyncTask<Void,Void,Void>{
        Exception m_exception;

        @Override
        protected void onPreExecute() {
            getActivity().setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URI uri = new URI("http",WEB_SERVICE_URL,REST_PASSAGER,null,null);
                HttpPut put = new HttpPut(uri);
                JSONObject obj = jsonParser.passagerToJSONObject(passager);
                put.setEntity(new StringEntity(obj.toString()));
                put.addHeader("Content-Type", "application/json");
                m_ClientHttp.execute(put,new BasicResponseHandler());

            }catch (Exception e){
                m_exception = e;
            }
            return null;
        }
    }
}
