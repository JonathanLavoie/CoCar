package com.jg.jl.tpmobile.cocar.cocar;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.jg.jl.tpmobile.cocar.cocar.webService.webService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class creation_fragment extends Fragment {
    View rootView;
    ParcoursConducteur conduc = new ParcoursConducteur();
    ParcoursPassager passager = new ParcoursPassager();
    EditText txtSetDepart;
    EditText txtSetDepartLong;
    EditText txtSetDestination;
    EditText txtSetDestinationLong;
    EditText km;
    EditText nb;
    EditText date;
    EditText time;
    EditText nbPass;
    ArrayList<ParcoursPassager> mdecoPass = new ArrayList<>();
    ArrayList<ParcoursConducteur> mdecoCondu = new ArrayList<>();
    private webService web = new webService();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.creation_layout,container,false);
        getActivity().registerReceiver(this.m_conn,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        chargementListe();
        return rootView;
    }


    private void chargementListe(){
        ListView maListe = (ListView)rootView.findViewById(R.id.listCreation);
        String[] tokens = {"Vous êtes un conducteur","Vous êtes un passager"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(rootView.getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, tokens);
        maListe.setAdapter(adapter);

        maListe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        CreateConducteur();
                        break;
                    case 1:
                        CreatePassager();
                        break;
                }
            }
        });
    }

    //Methode qui survient avec un clique sur creer un conducteur.
    private void CreateConducteur(){

        View setView = View.inflate(getActivity(),R.layout.creer_conducteur,null);
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
                        Pattern p2 = Pattern.compile("([01]?[0-9]|2[0-3]):[0-5][0-9]");
                        Matcher m2 = p2.matcher(time.getText().toString());
                        try {
                            depart = Double.parseDouble(txtSetDepart.getText().toString());
                            departlong = Double.parseDouble(txtSetDepartLong.getText().toString());
                            destination = Double.parseDouble(txtSetDestination.getText().toString());
                            destinationlong = Double.parseDouble(txtSetDestinationLong.getText().toString());
                        } catch (NumberFormatException nfe) {
                            valide = false;
                            message = "Les longitudes et latitudes doivent être numérique";
                        }
                        if (depart < 46 || depart > 47 ||
                                departlong > -71 || departlong < -72 ||
                                destination < 46 || destination > 47 ||
                                destinationlong > -71 || destinationlong < -72) {
                            valide = false;
                            if (message == "") {
                                message = "La latitude doit être entre 46 et 47. La longitude doit être entre -71 et -72";
                            }
                        }
                        try {
                            nbPlace = Double.parseDouble(nb.getText().toString());
                        } catch (NumberFormatException nfe) {
                            valide = false;
                            if (message == "") {
                                message = "Les nombres de places doivent être numériques";
                            }
                        }
                        if (nbPlace < 1 || nbPlace > 6) {
                            if (message == "") {
                                message = "Le nombre de place doit être entre 1 et 6";
                            }
                            valide = false;
                        }
                        if (!m.find()) {
                            if (message == "") {
                                message = "La date n'est pas conforme";
                            }
                            valide = false;
                        }
                        if (!m2.find()) {
                            if (message == "") {
                                message = "L'heure n'est pas conforme";
                            }
                            valide = false;
                        }
                        if (valide) {
                            ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                            NetworkInfo m3G = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                            SessionManager session = new SessionManager(getActivity().getApplicationContext());
                            String latiD = txtSetDepart.getText().toString().replace(',', '.');
                            String longiD = txtSetDepartLong.getText().toString().replace(',', '.');
                            String coordonneeDepart = latiD + ";" + longiD;
                            String lati = txtSetDestination.getText().toString().replace(',', '.');
                            String longi = txtSetDestinationLong.getText().toString().replace(',', '.');
                            String coordonneeDestination = lati + ";" + longi;
                            conduc.set_depart(coordonneeDepart);
                            conduc.set_destination(coordonneeDestination);
                            conduc.set_nombreDePlace(Integer.parseInt(nb.getText().toString().trim()));
                            conduc.set_KM(Integer.parseInt(km.getText().toString().trim()));
                            conduc.set_date(date.getText().toString());
                            conduc.set_heure(time.getText().toString());
                            conduc.set_identifiant(session.getIdentification());
                            if ((mWifi != null && mWifi.isConnected()) || (m3G != null && m3G.isConnected())) {

                                new putConducteur().execute((Void) null);
                                Toast.makeText(getActivity(), "Parcours Creer", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                mdecoCondu.add(conduc);
                                Util.afficherAlertBox(getActivity(),"Aucune connexion internet trouvé, \nmais votre parcours va être envoyé au retour de la connexion","Erreur WIFI non trouvé");
                            }

                            d.dismiss();
                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        d.show();
    }

    //Insert un conducteur dans le web service.
    private class putConducteur extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            getActivity().setProgressBarIndeterminateVisibility(true);
        }
        @Override
        protected Void doInBackground(Void... params) {
            web.putConduc(conduc);
            return null;
        }
    }
    //méthode qui survient après un clique sur creer un passager.
    private void CreatePassager() {

        View setView = View.inflate(getActivity(), R.layout.creer_passager, null);
        txtSetDepart = (EditText) setView.findViewById(R.id.txtDepartP);
        txtSetDepartLong = (EditText) setView.findViewById(R.id.txtDepartP2);
        txtSetDestinationLong = (EditText) setView.findViewById(R.id.txtDestinationP2);
        txtSetDestination = (EditText) setView.findViewById(R.id.txtDestinationP);
        date = (EditText) setView.findViewById(R.id.dateD);
        time = (EditText) setView.findViewById(R.id.heureD);
        nbPass = (EditText) setView.findViewById(R.id.nbPassP);
        final AlertDialog d = new AlertDialog.Builder(getActivity())
                .setTitle("Creer un passager")
                .setView(setView)
                .setNegativeButton("Annuler", null)
                .setPositiveButton("Creer", null)
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
                        Pattern p2 = Pattern.compile("([01]?[0-9]|2[0-3]):[0-5][0-9]");
                        Matcher m2 = p2.matcher(time.getText().toString());
                        try {
                            depart = Double.parseDouble(txtSetDepart.getText().toString());
                            departlong = Double.parseDouble(txtSetDepartLong.getText().toString());
                            destination = Double.parseDouble(txtSetDestination.getText().toString());
                            destinationlong = Double.parseDouble(txtSetDestinationLong.getText().toString());
                        } catch (NumberFormatException nfe) {
                            valide = false;
                            message = "Les longitudes et latitudes doivent être numérique";
                        }
                        if (depart < 46 || depart > 47 ||
                                departlong > -71 || departlong < -72 ||
                                destination < 46 || destination > 47 ||
                                destinationlong > -71 || destinationlong < -72) {
                            valide = false;
                            if (message == "") {
                                message = "La latitude doit être entre 46 et 47. La longitude doit être entre -71 et -72";
                            }
                        }
                        try {
                            nbPlace = Double.parseDouble(nbPass.getText().toString());
                        } catch (NumberFormatException nfe) {
                            valide = false;
                            if (message == "") {
                                message = "Les nombres de places doivent être numériques";
                            }
                        }
                        if (nbPlace < 1 || nbPlace > 6) {
                            if (message == "") {
                                message = "Le nombre de place doit être entre 1 et 6";
                            }
                            valide = false;
                        }
                        if (!m.find()) {
                            if (message == "") {
                                message = "La date n'est pas conforme";
                            }
                            valide = false;
                        }
                        if (!m2.find()) {
                            if (message == "") {
                                message = "L'heure n'est pas conforme";
                            }
                            valide = false;
                        }
                        if (valide) {
                            ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                            NetworkInfo m3G = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                            SessionManager session = new SessionManager(getActivity().getApplicationContext());
                            String latiD = txtSetDepart.getText().toString().replace(',', '.');
                            String longiD = txtSetDepartLong.getText().toString().replace(',', '.');
                            String coordonneeDepart = latiD + ";" + longiD;
                            String lati = txtSetDestination.getText().toString().replace(',', '.');
                            String longi = txtSetDestinationLong.getText().toString().replace(',', '.');
                            String coordonneeDestination = lati + ";" + longi;
                            passager.set_depart(coordonneeDepart);
                            passager.set_destination(coordonneeDestination);
                            passager.set_date(date.getText().toString());
                            passager.set_heure(time.getText().toString());
                            passager.set_identifiant(session.getIdentification());
                            passager.set_nombrePassager(Integer.parseInt(nbPass.getText().toString()));
                            if ((mWifi != null && mWifi.isConnected()) || (m3G != null && m3G.isConnected())) {
                                new putPassager().execute((Void) null);
                                Toast.makeText(getActivity(), "Passager Creer", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                mdecoPass.add(passager);
                                Util.afficherAlertBox(getActivity(),"Aucune connexion internet trouvé, \n" +
                                        "mais votre parcours va être envoyé au retour de la connexion","Erreur WIFI non trouvé");
                            }
                                d.dismiss();
                            } else {
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    );
                }
            }

            );
            d.show();
        }


    private BroadcastReceiver m_conn = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo m3G = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if ((mWifi != null && mWifi.isConnected()) || (m3G != null && m3G.isConnected())) {
                if (!mdecoPass.isEmpty())
                {
                    for (int i =0; i < mdecoPass.size(); i++)
                    {
                        passager = mdecoPass.get(i);
                        new putPassager().execute((Void) null);
                    }
                }
                if (!mdecoCondu.isEmpty())
                {
                    for (int i =0; i < mdecoCondu.size(); i++)
                    {
                        conduc = mdecoCondu.get(i);
                        new putConducteur().execute((Void) null);
                    }
                }
            }
        }
    };

                //insert un passager dans le service web.
        private class putPassager extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            getActivity().setProgressBarIndeterminateVisibility(true);
        }
        @Override
        protected Void doInBackground(Void... params) {
            web.putPassager(passager);
            return null;
        }
    }
}
