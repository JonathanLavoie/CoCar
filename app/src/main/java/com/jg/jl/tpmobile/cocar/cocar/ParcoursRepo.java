package com.jg.jl.tpmobile.cocar.cocar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Jonathan Lavoie on 05/03/2015.
 */
public class ParcoursRepo {
    private DBHelper dbHelper;

    /**
     * Constructeur
     * @param context - Contexte de l'application
     */
    public ParcoursRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    /**
     * Méthode pour ajouter une relation entre un parcours passager et conducteur
     * @param parcours - Un Parcours
     */
    public void insert(Parcours parcours) {
        //Ouvre la connexion
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Parcours.KEY_ConducteurID, parcours.get_ConducteurID());
        values.put(Parcours.KEY_PassagerID, parcours.get_PassagerID());
        values.put(Parcours.KEY_Date, parcours.get_date().toString());
        values.put(Parcours.KEY_Heure, parcours.get_heure());

        // Ajoute les données
        db.insert(Parcours.TABLE, null, values);
        db.close(); // Ferme la BD
    }

    /**
     * Supprime le parcours
     * @param user_identification
     */
    //public void delete(String user_identification) {

       // SQLiteDatabase db = dbHelper.getWritableDatabase();
       // db.delete(User.TABLE, User.KEY_Identification + "= ?", new String[] { user_identification });
       // db.close();
    //}

    /**
     * Retourne le parcours a partir d'un ID
     * @param ID - ID du parcours
     * @return - Parcours
     */
    public Parcours getParcoursByID(String ID){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Création de la requête
        String selectQuery =  "SELECT  " +
                Parcours.KEY_ID + "," +
                Parcours.KEY_ConducteurID + "," +
                Parcours.KEY_Heure + "," +
                Parcours.KEY_PassagerID +
                " FROM " + Parcours.TABLE
                + " WHERE " +
                Parcours.KEY_ID + "=?";

        // Création d'un nouveau parcours
        Parcours parcours = new Parcours();
        // Création d'un cursor
        Cursor cursor = db.rawQuery(selectQuery, new String[] { ID } );

        if (cursor.moveToFirst()) {
            do {
                parcours.set_ID(cursor.getInt(cursor.getColumnIndex(Parcours.KEY_ID)));
                parcours.set_ConducteurID(cursor.getInt(cursor.getColumnIndex(Parcours.KEY_ConducteurID)));
                parcours.set_PassagerID(cursor.getInt(cursor.getColumnIndex(Parcours.KEY_PassagerID)));
               // String date = cursor.getString(cursor.getColumnIndex(ParcoursConducteur.KEY_Date));
                //DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
                //try {
                //    Date dateBon = format.parse(date);
                //    parcours.set_date(dateBon);
                //} catch (ParseException e) {
                //    e.printStackTrace();
                //}
                parcours.set_date(cursor.getString(cursor.getColumnIndex(Parcours.KEY_Date)));
                parcours.set_heure(cursor.getString(cursor.getColumnIndex(Parcours.KEY_Heure)));
            } while (cursor.moveToNext());
        }
        // Ferme le cursor, la BD et renvois l'utilisateur selectionner
        cursor.close();
        db.close();
        return parcours;
    }

    /**
     * Cherche tout les parcours d'un passager
     * @return - Liste de ID de parcours
     */
    public ArrayList<Integer> getParcoursListByIDPassager(Integer IDPassager) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Parcours.KEY_ID + "," +
                " FROM " + Parcours.TABLE
                + " WHERE " +
                Parcours.KEY_PassagerID + "=?";

        ArrayList<Integer> ParcoursIDList = new ArrayList<Integer>();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { IDPassager.toString() } );

        if (cursor.moveToFirst()) {
            do {
                ParcoursIDList.add(cursor.getInt(cursor.getColumnIndex(Parcours.KEY_ID)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return ParcoursIDList;
    }
    /**
     * Cherche tout les parcours d'un conducteur
     * @return - Liste de ID de parcours
     */
    public ArrayList<Integer> getParcoursListByIDConducteur(Integer IDConducteur) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Parcours.KEY_ID + "," +
                " FROM " + Parcours.TABLE
                + " WHERE " +
                Parcours.KEY_ConducteurID + "=?";

        ArrayList<Integer> ParcoursIDList = new ArrayList<Integer>();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { IDConducteur.toString() } );

        if (cursor.moveToFirst()) {
            do {
                ParcoursIDList.add(cursor.getInt(cursor.getColumnIndex(Parcours.KEY_ID)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return ParcoursIDList;
    }

}
