package com.jg.jl.tpmobile.cocar.cocar;

/**
 * Created by Jonathan Lavoie on 19/02/2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jg.jl.tpmobile.cocar.cocar.webService.webService;

public class InscriptionActivity extends Activity  {

    // Variable pour les champs de l'activity
    EditText txtNom, txtIndentifiant, txtMotPasse, txtlong, txtNumTel,txtLat;
    webService web = new webService();
    User user;
    User utilisateur;
    String nom,motDePasse,adresse,phone,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        // Attribut les textBox a des varibales
        txtNom = (EditText) findViewById(R.id.txtNom);
        txtIndentifiant = (EditText) findViewById(R.id.txtEmail);
        txtMotPasse = (EditText) findViewById(R.id.txtMotDePasse);
        txtLat = (EditText) findViewById(R.id.txtLatInscrip);
        txtlong = (EditText) findViewById(R.id.txtLongInscrip);
        txtNumTel = (EditText) findViewById(R.id.txtPhone);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inscription, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    /**
     * Méthode pour inscription
     * @param view
     */
    public void inscription(View view) {
        nom = txtNom.getText().toString();
        email = txtIndentifiant.getText().toString();
        motDePasse = txtMotPasse.getText().toString();
        adresse = txtLat.getText().toString() + ";" + txtlong.getText().toString();
        phone = txtNumTel.getText().toString();
        new getUser().execute((Void)null);
    }

private class addUser extends AsyncTask<Void,Void,Void>{
    @Override
    protected Void doInBackground(Void... params) {
        web.putUser(user);
        return null;
    }
}

    /**
     * Méthode pour regardé si les champs sont valide
     * @param nom
     * @param email
     * @param motPasse
     * @param phone
     * @return
     */
    public Boolean champsValide(String nom, String email, String motPasse, String Latitude, String longitude, String phone) {
        Double lat;
        Double lon;
        // Vérifie si les champs ne sont pas vide
        if(nom.length() > 0 &&
                email.length() > 0 &&
                motPasse.length() > 0 &&
                Latitude.length() > 0 &&
                longitude.length() > 0 &&
                phone.length() > 0)
        {
            // Vérifie si l'adresse courriel est valide
            if (Util.emailValide(email)) {
                // Vérifie si le numéro de téléphone est valide
                if (Util.phoneValide(phone)) {
                    // Vérifie si l'adresse courriel est utilisé
                    UserRepo repo = new UserRepo(this);

                    if (utilisateur.get_nom() == "" || utilisateur.get_nom() == null) {
                        try {
                            lat = Double.parseDouble(Latitude);
                            lon = Double.parseDouble(longitude);
                        } catch(NumberFormatException nfe) {
                            Util.afficherAlertBox(InscriptionActivity.this, "La longitude et latitude doit être numérique", "Latitude longitude erreur");
                            return false;
                        }
                        if((lat >= 46 && lat <= 47) || (lon <= -71 && lon >= -72)) {
                            return true;
                        }
                        else {
                            Util.afficherAlertBox(InscriptionActivity.this, "La latitude doit être entre 46 et 47. La longitude doit être entre -71 et -72", "Latitude longitude erreur");
                            return false;
                        }
                    }
                    else {
                        Util.afficherAlertBox(InscriptionActivity.this, "Le email est deja utiliser", "Email Utiliser");
                        return false;
                    }
                }
                else {
                    // Erreur sur la validation du numéro de téléphone
                    Util.afficherAlertBox(InscriptionActivity.this, "Le numéro de téléphone n'est pas valide", "Numéro de téléphone invalide");
                    return false;
                }
            } else {
                // Erreur sur la validation du courriel
                Util.afficherAlertBox(InscriptionActivity.this, "L'adresse courriel n'est pas valide", "Adresse courriel invalide");
                return false;
            }
        }
        else {
            // Erreur sur les champs vide
            Util.afficherAlertBox(InscriptionActivity.this, "Tous les champs sont obligatoire et ne doivent pas être vide!","Erreur");
            return false;
        }
    }

    private class getUser extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
           utilisateur =  web.getUserByEmail(email);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (champsValide(nom.trim(), email.trim(), motDePasse.trim(),txtLat.getText().toString().trim(), txtlong.getText().toString().trim(),phone.trim())) {
                // Insertion dans la bd
                user = new User();
                user.set_nom(nom);
                user.set_identification(email);
                user.set_motPasse(Util.encryptPassword(motDePasse));
                user.set_adresse(adresse);
                user.set_phone(phone);
                new addUser().execute((Void)null);

                // redirection vers la connexion
                Toast.makeText(getApplicationContext(),"Inscription réussie",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(InscriptionActivity.this, Login.class);
                startActivity(i);
                finish();
            }
        }
    }

}
