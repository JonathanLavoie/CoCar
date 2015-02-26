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
    private static final int DATABASE_VERSION = 2;

    // Nom de la BD
    private static final String DATABASE_NAME = "CoCar.db";

    public DBHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Tout les tables de l'application

        String CREATE_TABLE_USER = "CREATE TABLE " + User.TABLE  + "("
                + User.KEY_Identification  + " TEXT PRIMARY KEY,"
                + User.KEY_nom + " TEXT, "
                + User.KEY_motPasse + " TEXT, "
                + User.KEY_adresse + " TEXT, "
                + User.KEY_phone + " TEXT, "
                + User.KEY_sumRate + " INTEGER, "
                + User.KEY_countRate + " INTEGER) ";
        db.execSQL(CREATE_TABLE_USER);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop tous les table
        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE);

        // Crée les tables
        onCreate(db);

    }

}
