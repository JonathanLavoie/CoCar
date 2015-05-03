package com.jg.jl.tpmobile.cocar.cocar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Jonathan Lavoie on 19/02/2015.
 */
public class UserRepo {
    private DBHelper dbHelper;

    /**
     * Constructeur
     * @param context - Contexte de l'application
     */
    public UserRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    /**
     * Méthode pour ajouter un utilisateur a la BD
     * @param user - Un Utilisateur
     */
    public void insert(User user) {
        //Ouvre la connexion
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(User.KEY_Identification, user.get_identification());
        values.put(User.KEY_nom, user.get_nom());
        values.put(User.KEY_motPasse, user.get_motPasse());
        values.put(User.KEY_adresse, user.get_adresse());
        values.put(User.KEY_phone, user.get_phone());
        values.put(User.KEY_sumRate, 0);
        values.put(User.KEY_countRate, 0);

        // Ajoute les données
        db.insert(User.TABLE, null, values);
        db.close(); // Ferme la BD
    }

    /**
     * Supprime l'usager
     * @param user_identification
     */
    public void delete(String user_identification) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(User.TABLE, User.KEY_Identification + "= ?", new String[] { user_identification });
        db.close();
    }

    /**
     * Modifie l'usager
     * @param user - String Usager
     */
    public void update(User user) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(User.KEY_Identification, user.get_identification());
        values.put(User.KEY_nom, user.get_nom());
        values.put(User.KEY_motPasse, user.get_motPasse());
        values.put(User.KEY_adresse, user.get_adresse());
        values.put(User.KEY_phone, user.get_phone());
        values.put(User.KEY_sumRate, user.get_sumRate());
        values.put(User.KEY_countRate, user.get_countRate());

        // Modifie l'usager avec les nouvelle valeur
        db.update(User.TABLE, values, User.KEY_Identification + "= ?", new String[] { user.get_identification() });
        db.close();
    }

    /**
     * Retourne l'usager a partir de l'identifiant
     * @return - L'utilisateur
     */
    public User getUser(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Création de la requête
        String selectQuery =  "SELECT  " +
                User.KEY_Identification + "," +
                User.KEY_nom + "," +
                User.KEY_motPasse + "," +
                User.KEY_adresse + "," +
                User.KEY_phone + "," +
                User.KEY_sumRate + "," +
                User.KEY_countRate +
                " FROM " + User.TABLE;

        // Création d'un nouveau utilisateur
        User user = new User();
        // Création d'un cursor
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                user.set_identification(cursor.getString(cursor.getColumnIndex(User.KEY_Identification)));
                user.set_nom(cursor.getString(cursor.getColumnIndex(User.KEY_nom)));
                user.set_motPasse(cursor.getString(cursor.getColumnIndex(User.KEY_motPasse)));
                user.set_adresse(cursor.getString(cursor.getColumnIndex(User.KEY_adresse)));
                user.set_phone(cursor.getString(cursor.getColumnIndex(User.KEY_phone)));
                user.set_sumRate(cursor.getInt(cursor.getColumnIndex(User.KEY_sumRate)));
                user.set_countRate(cursor.getInt(cursor.getColumnIndex(User.KEY_sumRate)));
            } while (cursor.moveToNext());
        }
        // Ferme le cursor, la BD et renvois l'utilisateur selectionner
        cursor.close();
        db.close();
        return user;
    }

    public boolean checkNotEmpty()
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + User.TABLE;
        Cursor cursor =  db.rawQuery(query,null);
        while (cursor.moveToNext())
        {
            return true;
        }
        return false;
    }
}
