package com.jg.jl.tpmobile.cocar.cocar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import  java.util.ArrayList;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Jonathan Lavoie on 05/03/2015.
 */
public class ParcoursPassagerRepo {
    private DBHelper dbHelper;

    /**
     * Constructeur
     * @param context - Contexte de l'application
     */
    public ParcoursPassagerRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    /**
     * Méthode pour ajouter un parcours Passager
     * @param parcoursPassager - Un Parcours de passager
     */
    public void insert(ParcoursPassager parcoursPassager) {
        //Ouvre la connexion
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ParcoursPassager.KEY_Depart, parcoursPassager.get_depart());
        values.put(ParcoursPassager.KEY_Destination, parcoursPassager.get_destination());
        values.put(ParcoursPassager.KEY_Frequence, parcoursPassager.get_frequence());
        values.put(ParcoursPassager.KEY_Date, parcoursPassager.get_date().toString());
        values.put(ParcoursPassager.KEY_NombrePassager, parcoursPassager.get_nombrePassager());
        values.put(ParcoursPassager.KEY_Heure, parcoursPassager.get_heure());

        // Ajoute les données
        db.insert(ParcoursPassager.TABLE, null, values);
        db.close(); // Ferme la BD
    }

    /**
     * Supprime
     * @param IDParcoursPassager - ID du parcours passager
     */
    public void delete(String IDParcoursPassager) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(ParcoursPassager.TABLE, ParcoursPassager.KEY_ID + "= ?", new String[] { IDParcoursPassager });
        db.close();
    }

    /**
     * Retourne le parcours
     * @param identification
     * @return - ParcoursPassager
     */
    public ParcoursPassager getParcoursPassager(String identification){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Création de la requête
        String selectQuery =  "SELECT  " +
                ParcoursPassager.KEY_ID + "," +
                ParcoursPassager.KEY_Depart + "," +
                ParcoursPassager.KEY_Destination + "," +
                ParcoursPassager.KEY_Frequence + "," +
                ParcoursPassager.KEY_Date + "," +
                ParcoursPassager.KEY_NombrePassager + "," +
                ParcoursPassager.KEY_Heure +
                " FROM " + ParcoursPassager.TABLE
                + " WHERE " +
                ParcoursPassager.KEY_ID + "=?";

        // Création d'un parcours passager
        ParcoursPassager parcoursPassager = new ParcoursPassager();
        // Création d'un cursor
        Cursor cursor = db.rawQuery(selectQuery, new String[] { identification } );

        if (cursor.moveToFirst()) {
            do {
                parcoursPassager.set_ID(cursor.getInt(cursor.getColumnIndex(ParcoursPassager.KEY_ID)));
                parcoursPassager.set_depart(cursor.getString(cursor.getColumnIndex(ParcoursPassager.KEY_Depart)));
                parcoursPassager.set_destination(cursor.getString(cursor.getColumnIndex(ParcoursPassager.KEY_Destination)));
                parcoursPassager.set_frequence(cursor.getString(cursor.getColumnIndex(ParcoursPassager.KEY_Frequence)));
                // Date conversion a faire
               // String date = cursor.getString(cursor.getColumnIndex(ParcoursPassager.KEY_Date));
                //DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
                //try {
                  //  Date dateBon = format.parse(date);
                   // parcoursPassager.set_date(dateBon);
                //} catch (ParseException e) {
                  //  e.printStackTrace();
                //}
                parcoursPassager.set_date(cursor.getString(cursor.getColumnIndex(ParcoursPassager.KEY_Date)));
                parcoursPassager.set_nombrePassager(cursor.getInt(cursor.getColumnIndex(ParcoursPassager.KEY_NombrePassager)));
                parcoursPassager.set_heure(cursor.getString(cursor.getColumnIndex(ParcoursPassager.KEY_Heure)));
            } while (cursor.moveToNext());
        }
        // Ferme le cursor, la BD et renvois l'utilisateur selectionner
        cursor.close();
        db.close();
        return parcoursPassager;
    }

    public ArrayList<ParcoursPassager> getAllParcours()
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Création de la requête
        String selectQuery =  "SELECT " +
                ParcoursPassager.KEY_ID + ", " +
                ParcoursPassager.KEY_Depart + ", " +
                ParcoursPassager.KEY_Destination + ", " +
                ParcoursPassager.KEY_Frequence + ", " +
                ParcoursPassager.KEY_Date + ", " +
                ParcoursPassager.KEY_NombrePassager + ", " +
                ParcoursPassager.KEY_Heure +
                " FROM " + ParcoursPassager.TABLE;

        // Création d'un parcours Conducteur
        ParcoursPassager parcoursPassager;
        ArrayList<ParcoursPassager> listPassager = new ArrayList<>();
        // Création d'un cursor
        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()) {
            do {
                parcoursPassager = new ParcoursPassager();
                parcoursPassager.set_ID(cursor.getInt(cursor.getColumnIndex(ParcoursPassager.KEY_ID)));
                parcoursPassager.set_depart(cursor.getString(cursor.getColumnIndex(ParcoursPassager.KEY_Depart)));
                parcoursPassager.set_destination(cursor.getString(cursor.getColumnIndex(ParcoursPassager.KEY_Destination)));
                parcoursPassager.set_frequence(cursor.getString(cursor.getColumnIndex(ParcoursPassager.KEY_Frequence)));
                // Date conversion a faire
                parcoursPassager.set_date(cursor.getString(cursor.getColumnIndex(ParcoursPassager.KEY_Date)));
                parcoursPassager.set_nombrePassager(cursor.getColumnIndex(ParcoursPassager.KEY_NombrePassager));
                parcoursPassager.set_heure(cursor.getString(cursor.getColumnIndex(ParcoursPassager.KEY_Heure)));
                listPassager.add(parcoursPassager);

            } while (cursor.moveToNext());
        }
        // Ferme le cursor, la BD et renvois l'utilisateur selectionner
        cursor.close();
        db.close();
        return listPassager;
    }
}
