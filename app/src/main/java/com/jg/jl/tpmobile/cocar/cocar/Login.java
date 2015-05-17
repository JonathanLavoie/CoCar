package com.jg.jl.tpmobile.cocar.cocar;

/**
 * Created by Jonathan Lavoie on 19/02/2015.
 */

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.jg.jl.tpmobile.cocar.cocar.webService.webService;


public class Login extends ActionBarActivity {

    // Champs texte pour l'indentifiant et le mot de passe
    EditText txtIndentifiant, txtMotPasse;
    String identifiant,motDePass;
    User utilisateur;
    // Session pour garder les information du login
    SessionManager session;
    UserRepo repo = new UserRepo(this);
    webService web = new webService();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        // Remplit une nouvelle session
        session = new SessionManager(getApplicationContext());

        // Attribut les textBox a des varibales
        txtIndentifiant = (EditText) findViewById(R.id.email);
        txtMotPasse = (EditText) findViewById(R.id.password);

        if(repo.checkNotEmpty())
        {
            utilisateur = repo.getUser();
            session.createLoginSession(utilisateur.get_identification(), utilisateur.get_nom());
            Intent i = new Intent(Login.this,BaseActivity.class);
            startActivity(i);
        }
    }

    /**
     * Méthode qui ouvre la page inscription
     * @param view -
     */
    public void inscriptionActivity(View view)
    {
        Intent i = new Intent(Login.this,InscriptionActivity.class);
        startActivity(i);
    }

    /**
     * Méthode pour se connecter
     * @param view
     */
    public void login(View view) {
        identifiant = txtIndentifiant.getText().toString();
        motDePass = txtMotPasse.getText().toString();

        // Verifier si l'utilisateur a entrez l'identification et le mot de passe.
        if(identifiant.trim().length() > 0 && motDePass.trim().length() > 0){

            // Verifier si l'utilisateur a entrez un bon identifiant et mot de passe.


            new getUser().execute((Void)null);

        } else {
            Util.afficherAlertBox(Login.this, "Entrez votre Email et votre mot de passe","Erreur");
        }
    }

    private class getUser extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            utilisateur = web.getUserByEmail(identifiant);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo m3G = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if(mWifi.isConnected() || m3G.isConnected()) {
                if (utilisateur.get_motPasse().equals(Util.encryptPassword(motDePass.trim()))) {
                    // Ajoute les Shared preferences et redirige vers la page recherche
                    session.createLoginSession(identifiant.trim(), utilisateur.get_nom());
                    repo.insert(utilisateur);
                    Intent i = new Intent(Login.this, BaseActivity.class);
                    startActivity(i);
                } else {
                    Util.afficherAlertBox(Login.this, "Identification ou mot de passe incorrect", "Erreur");
                }
            }
            else
            {
                Util.afficherAlertBox(Login.this,"Aucune connexion internet trouvé\nConnexion impossible","Erreur WIFI non trouvé");
            }
        }
    }
}
