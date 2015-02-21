package com.jg.jl.tpmobile.cocar.cocar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by Jonathan Lavoie on 20/02/2015.
 */
public class SessionManager {

    // Mode de preferences
    int PRIVATE_MODE = 0;

    // Les preferences
    SharedPreferences pref;

    // Editeur pour les preferences
    SharedPreferences.Editor editor;

    // Contexte
    Context _contexte;

    // Nom de fichier
    private static final String PREF_NAME = "AndroidHivePref";

    // La clé pour de la preference pour le login
    private static final String IS_LOGIN = "IsLoggedIn";

    // User Identification (email)
    public static final String KEY_Identification = "Identification";

    // Email address
    public static final String KEY_Nom = "Nom";

    // Constructor
    public SessionManager(Context context){
        this._contexte = context;
        pref = _contexte.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //Création et stockage du login
    public void createLoginSession(String Identification, String nom){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Met l'identification dans les preferences
        editor.putString(KEY_Identification, Identification);

        // Met le nom dans les preferences
        editor.putString(KEY_Nom, nom);

        // Sauvgarde
        editor.commit();
    }

    // Retourne l'identification (email)
    public String getIdentification() {
        return pref.getString(KEY_Identification, null);
    }

    // Retourne le nom
    public String getNom() {
        return pref.getString(KEY_Nom, null);
    }

    //------------------------------------------
    // Regard si le user est connecter sinon on
    //    redirige ver la page de connection
    //------------------------------------------
    public void checkLogin(){
        if(!pref.getBoolean(IS_LOGIN, false)){
            Intent i = new Intent(_contexte, Login.class);
            _contexte.startActivity(i);
        }
    }

    //------------------------------------------
    //-------------logout l'usager--------------
    //------------------------------------------
    public void logoutUser(){
        // Efface et commit les preferences
        editor.clear();
        editor.commit();

        // On redirige ver la page de connexion
        Intent i = new Intent(_contexte, Login.class);
        _contexte.startActivity(i);
    }
}
