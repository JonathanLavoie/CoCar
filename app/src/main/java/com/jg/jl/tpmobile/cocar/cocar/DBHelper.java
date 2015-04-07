package com.jg.jl.tpmobile.cocar.cocar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jonathan Lavoie on 19/02/2015.
 */

public class DBHelper  extends SQLiteOpenHelper {
    //Version de la BD
    // Doit être changé a chaque fois que la BD est modifier
    private static final int DATABASE_VERSION = 15;

    // Nom de la BD
    private static final String DATABASE_NAME = "CoCar.db";

    public DBHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Tout les tables de l'application
        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE);
        String CREATE_TABLE_USER = "CREATE TABLE " + User.TABLE  + "("
                + User.KEY_Identification  + " TEXT PRIMARY KEY,"
                + User.KEY_nom + " TEXT, "
                + User.KEY_motPasse + " TEXT, "
                + User.KEY_adresse + " TEXT, "
                + User.KEY_phone + " TEXT, "
                + User.KEY_sumRate + " INTEGER, "
                + User.KEY_countRate + " INTEGER) ";
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + Parcours.TABLE);
        String CREATE_TABLE_PARCOURS = "CREATE TABLE " + Parcours.TABLE + "("
                + Parcours.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Parcours.KEY_ConducteurID + " INTEGER, "
                + Parcours.KEY_PassagerID + " INTEGER, "
                + Parcours.KEY_Heure + " TEXT, "
                + Parcours.KEY_Date + " TEXT)";
        db.execSQL(CREATE_TABLE_PARCOURS);
        db.execSQL("DROP TABLE IF EXISTS " + ParcoursPassager.TABLE);
        String CREATE_TABLE_PARCOURSPASSAGER = "CREATE TABLE " + ParcoursPassager.TABLE + "("
                + ParcoursPassager.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ParcoursPassager.KEY_Depart + " TEXT, "
                + ParcoursPassager.KEY_Destination + " TEXT, "
                + ParcoursPassager.KEY_Frequence + " TEXT, "
                + ParcoursPassager.KEY_NombrePassager + " INTEGER, "
                + ParcoursPassager.KEY_Heure + " TEXT, "
                + ParcoursPassager.KEY_Date + " TEXT)";
        db.execSQL(CREATE_TABLE_PARCOURSPASSAGER);
        db.execSQL("DROP TABLE IF EXISTS " + ParcoursConducteur.TABLE);
        String CREATE_TABLE_PARCOURSCONDUCTEUR = "CREATE TABLE " + ParcoursConducteur.TABLE + "("
                + ParcoursConducteur.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ParcoursConducteur.KEY_Depart + " TEXT, "
                + ParcoursConducteur.KEY_Destination + " TEXT, "
                + ParcoursConducteur.KEY_Frequence + " TEXT, "
                + ParcoursConducteur.KEY_NombrePlace + " INTEGER, "
                + ParcoursConducteur.KEY_Date + " TEXT, "
                + ParcoursConducteur.KEY_Heure + " TEXT, "
                + ParcoursConducteur.KEY_KM + " INTEGER) ";
        db.execSQL(CREATE_TABLE_PARCOURSCONDUCTEUR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop tous les table
        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Parcours.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ParcoursPassager.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ParcoursConducteur.TABLE);

        // Crée les tables
        onCreate(db);

    }

}
