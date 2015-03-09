package com.jg.jl.tpmobile.cocar.cocar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Jonathan Lavoie on 05/03/2015.
 */
public class ParcoursConducteurRepo {
    private DBHelper dbHelper;

    /**
     * Constructeur
     * @param context - Contexte de l'application
     */
    public ParcoursConducteurRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    /**
     * Méthode pour ajouter un parcours Passager
     * @param parcoursConducteur - Un Parcours de passager
     */
    public void insert(ParcoursConducteur parcoursConducteur) {
        //Ouvre la connexion
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ParcoursConducteur.KEY_Depart, parcoursConducteur.get_depart());
        values.put(ParcoursConducteur.KEY_Destination, parcoursConducteur.get_destination());
        values.put(ParcoursConducteur.KEY_Frequence, parcoursConducteur.get_frequence());
        values.put(ParcoursConducteur.KEY_Date, parcoursConducteur.get_date().toString());
        values.put(ParcoursConducteur.KEY_NombrePlace, parcoursConducteur.get_nombreDePlace());
        values.put(ParcoursConducteur.KEY_KM, parcoursConducteur.get_KM());
        values.put(ParcoursConducteur.KEY_Heure, parcoursConducteur.get_heure());

        // Ajoute les données
        db.insert(ParcoursConducteur.TABLE, null, values);
        db.close(); // Ferme la BD
    }

    /**
     * Supprime
     * @param IDParcoursConducteur - ID du parcours conducteur
     */
    public void delete(String IDParcoursConducteur) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(ParcoursConducteur.TABLE, ParcoursConducteur.KEY_ID + "= ?", new String[] { IDParcoursConducteur });
        db.close();
    }

    /**
     * Retourne le parcours
     * @param identification
     * @return - ParcoursPassager
     */
    public ParcoursConducteur getParcoursConducteur(String identification){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Création de la requête
        String selectQuery =  "SELECT  " +
                ParcoursConducteur.KEY_ID + "," +
                ParcoursConducteur.KEY_Depart + "," +
                ParcoursConducteur.KEY_Destination + "," +
                ParcoursConducteur.KEY_Frequence + "," +
                ParcoursConducteur.KEY_Date + "," +
                ParcoursConducteur.KEY_NombrePlace + "," +
                ParcoursConducteur.KEY_KM + "," +
                ParcoursConducteur.KEY_Heure +
                " FROM " + ParcoursConducteur.TABLE
                + " WHERE " +
                ParcoursConducteur.KEY_ID + "=?";

        // Création d'un parcours Conducteur
        ParcoursConducteur parcoursConducteur = new ParcoursConducteur();
        // Création d'un cursor
        Cursor cursor = db.rawQuery(selectQuery, new String[] { identification } );

        if (cursor.moveToFirst()) {
            do {
                parcoursConducteur.set_ID(cursor.getInt(cursor.getColumnIndex(ParcoursConducteur.KEY_ID)));
                parcoursConducteur.set_depart(cursor.getString(cursor.getColumnIndex(ParcoursConducteur.KEY_Depart)));
                parcoursConducteur.set_destination(cursor.getString(cursor.getColumnIndex(ParcoursConducteur.KEY_Destination)));
                parcoursConducteur.set_frequence(cursor.getString(cursor.getColumnIndex(ParcoursConducteur.KEY_Frequence)));
                // Date conversion a faire
                String date = cursor.getString(cursor.getColumnIndex(ParcoursConducteur.KEY_Date));
                //DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
                //try {
                 //   Date dateBon = date;
                  //  parcoursConducteur.set_date(dateBon);
                //} catch (ParseException e) {
                  //  e.printStackTrace();
                //}
                parcoursConducteur.set_date(cursor.getString(cursor.getColumnIndex(ParcoursConducteur.KEY_Date)));
                parcoursConducteur.set_nombreDePlace(cursor.getInt(cursor.getColumnIndex(ParcoursConducteur.KEY_NombrePlace)));
                parcoursConducteur.set_KM(cursor.getInt(cursor.getColumnIndex(ParcoursConducteur.KEY_KM)));
                parcoursConducteur.set_heure(cursor.getString(cursor.getColumnIndex(ParcoursConducteur.KEY_Heure)));
            } while (cursor.moveToNext());
        }
        // Ferme le cursor, la BD et renvois l'utilisateur selectionner
        cursor.close();
        db.close();
        return parcoursConducteur;
    }
}
