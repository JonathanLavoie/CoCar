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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.app.Activity;
import android.widget.Button;
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
                CreateConducteur();
                break;
            case R.id.menu_passager:
                CreatePassager();
                break;
        }
        return super.onContextItemSelected(item);
    }

    EditText txtSetDepart;
    EditText txtSetDepartLong;
    EditText txtSetDestination;
    EditText txtSetDestinationLong;
    EditText km;
    EditText nb;
    EditText date;
    EditText time;
    private void CreateConducteur(){

        View setView = View.inflate(getActivity(),R.layout.recherche_conducteur,null);
        txtSetDepart = (EditText) setView.findViewById(R.id.txtDepartC);
        txtSetDepartLong = (EditText) setView.findViewById(R.id.txtDepartCLong);
        txtSetDestination = (EditText) setView.findViewById(R.id.txtDestinationC);
        txtSetDestinationLong = (EditText) setView.findViewById(R.id.txtDestinationCLong);
        km = (EditText) setView.findViewById(R.id.nbKM);
        nb = (EditText) setView.findViewById(R.id.nbPass);
        date = (EditText) setView.findViewById(R.id.dateC);
        time = (EditText) setView.findViewById(R.id.heureC);

        final AlertDialog d = new AlertDialog.Builder(getActivity())
        .setTitle("Nouveau parcours")
                .setView(setView)
                .setNegativeButton("Annuler",null)
                .setPositiveButton("Creer", null)
                //.setPositiveButton("Creer",new BtnCreateConducteur(txtSetDepart,txtSetDepartLong,
                        //txtSetDestination,txtSetDestinationLong,km,nb,date,time))
                .create();
        d.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        boolean valide = true;
                        String message = "";
                        double depart = 0;
                        double departlong = 0;
                        double destination = 0;
                        double destinationlong = 0;
                        double nbPlace = 0;
                        Pattern p = Pattern.compile("^[0-9]{4}-[0-1][0-9]-[0-3][0-9]$");
                        Matcher m = p.matcher(date.getText().toString());
                        try {
                            depart = Double.parseDouble(txtSetDepart.getText().toString());
                            departlong = Double.parseDouble(txtSetDepartLong.getText().toString());
                            destination = Double.parseDouble(txtSetDestination.getText().toString());
                            destinationlong = Double.parseDouble(txtSetDestinationLong.getText().toString());
                        } catch(NumberFormatException nfe) {
                            valide = false;
                            message = "Les longitudes et latitudes doivent être numérique";
                        }
                        if (depart < 46 || depart > 47 ||
                                 departlong > -71 || departlong < -72 ||
                                 destination < 46 || destination > 47 ||
                                 destinationlong > -71 || destinationlong < -72)
                        {
                            valide = false;
                            if (message == "") {
                                message = "La latitude doit être entre 46 et 47. La longitude doit être entre -71 et -72";
                            }
                        }
                        try {
                            nbPlace = Double.parseDouble(nb.getText().toString());
                        } catch(NumberFormatException nfe) {
                            valide = false;
                            message = "Les nombres de places doivent être numériques";
                        }
                        if (nbPlace < 1 || nbPlace > 6)
                        {
                            if (message == "") {
                                message = "Le nombre de place doit être entre 1 et 6";
                            }
                            valide = false;
                        }
                        else if (!m.find())
                        {
                            if (message == "") {
                                message = "La date n'est pas conforme";
                            }
                            valide = false;
                        }
                        if (valide){
                            SessionManager session = new SessionManager(getActivity().getApplicationContext());
                            String coordonneeDepart = txtSetDepart.getText().toString() + "," + txtSetDepartLong.getText().toString();
                            String coordonneeDestination = txtSetDestination.getText().toString() + "," + txtSetDestinationLong.getText().toString();
                            conduc.set_depart(coordonneeDepart);
                            conduc.set_destination(coordonneeDestination);
                            conduc.set_nombreDePlace(Integer.parseInt(nb.getText().toString().trim()));
                            conduc.set_KM(Integer.parseInt(km.getText().toString().trim()));
                            conduc.set_date(date.getText().toString());
                            conduc.set_heure(time.getText().toString());
                            conduc.set_identifiant(session.getIdentification());
                            new putConducteur().execute((Void)null);
                            Toast.makeText(getActivity(),"Parcours Creer",Toast.LENGTH_SHORT).show();

                            d.dismiss();
                        }
                        else
                        {
                            Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        d.show();
    }

    private class BtnCreateConducteur implements DialogInterface.OnClickListener {
        private EditText m_depart;
        private EditText m_destination;
        private EditText m_nbPass;
        private EditText m_nbKM;
        private EditText m_time;
        private EditText m_date;
        private EditText m_departLong;
        private EditText m_destinationLong;
        public BtnCreateConducteur(EditText p_depart,EditText p_departLong,EditText p_destination, EditText p_destinationLong,
                                   EditText p_km, EditText p_pass, EditText p_date, EditText p_time
                                   ){
            this.m_depart = p_depart;
            this.m_departLong = p_departLong;
            this.m_destination = p_destination;
            this.m_destinationLong = p_destinationLong;
            this.m_nbPass = p_pass;
            this.m_nbKM = p_km;
            this.m_date = p_date;
            this.m_time = p_time;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
                SessionManager session = new SessionManager(getActivity().getApplicationContext());
                String coordonneeDepart = m_depart.getText().toString() + "," + m_departLong.getText().toString();
                String coordonneeDestination = m_destination.getText().toString() + "," + m_destinationLong.getText().toString();
                conduc.set_depart(coordonneeDepart);
                conduc.set_destination(coordonneeDestination);
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
    private void CreatePassager(){

        View setView = View.inflate(getActivity(),R.layout.recherche_passager,null);
        EditText txtSetDepart = (EditText) setView.findViewById(R.id.txtDepartP);
        EditText txtSetDepartLong = (EditText) setView.findViewById(R.id.txtDepartP2);
        EditText txtSetDestinationLong = (EditText) setView.findViewById(R.id.txtDestinationP2);
        EditText txtSetDestination = (EditText) setView.findViewById(R.id.txtDestinationP);
        EditText date = (EditText) setView.findViewById(R.id.dateD);
        EditText time = (EditText) setView.findViewById(R.id.heureD);
        EditText nbPass = (EditText) setView.findViewById(R.id.nbPassP);
        new AlertDialog.Builder(getActivity())
                .setTitle("Creer un passager")
                .setView(setView)
                .setNegativeButton("Annuler",null)
                .setPositiveButton("Creer",new BtnCreatePassager(txtSetDepart,txtSetDepartLong,
                        txtSetDestination,txtSetDestinationLong,nbPass,date,time))
                .show();
    }
    private class BtnCreatePassager implements DialogInterface.OnClickListener {

        private  EditText m_depart;
        private EditText m_departLong;
        private EditText m_destination;
        private EditText m_destinationLong;
        private EditText m_date;
        private EditText m_time;
        private EditText m_pass;
        public BtnCreatePassager(EditText p_depart,EditText p_departLong,EditText p_destination,EditText p_destinationLong,EditText p_pass,
                                 EditText p_date,EditText p_time){
            this.m_depart = p_depart;
            this.m_departLong = p_departLong;
            this.m_destination = p_destination;
            this.m_destinationLong = p_destinationLong;
            this.m_date = p_date;
            this.m_time = p_time;
            this.m_pass = p_pass;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            SessionManager session = new SessionManager(getActivity().getApplicationContext());
            String coordonneeDepart = m_depart.getText().toString() + "," + m_departLong.getText().toString();
            String coordonneeDestination = m_destination.getText().toString() + "," + m_destinationLong.getText().toString();
            passager.set_depart(coordonneeDepart);
            passager.set_destination(coordonneeDestination);
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
