package com.jg.jl.tpmobile.cocar.cocar;

/**
 * Created by Jonathan Lavoie on 19/02/2015.
 */

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class Login extends ActionBarActivity {

    // Champs texte pour l'indentifiant et le mot de passe
    EditText txtIndentifiant, txtMotPasse;

    // Session pour garder les information du login
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Remplit une nouvelle session
        session = new SessionManager(getApplicationContext());

        // Attribut les textBox a des varibales
        txtIndentifiant = (EditText) findViewById(R.id.email);
        txtMotPasse = (EditText) findViewById(R.id.password);
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
        String indentifiant = txtIndentifiant.getText().toString();
        String motDePass = txtMotPasse.getText().toString();

        if(indentifiant.trim().length() > 0 && motDePass.trim().length() > 0){

        } else {
            Util.afficherAlertBox(Login.this, "Entrez votre Email et votre mot de passe","Erreur");
        }
    }
}
