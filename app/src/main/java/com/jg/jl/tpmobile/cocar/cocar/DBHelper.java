package com.jg.jl.tpmobile.cocar.cocar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jonathan Lavoie on 19/02/2015.
 */

public class DBHelper  extends SQLiteOpenHelper {
    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 4;

    // Database Name
    private static final String DATABASE_NAME = "CoCar.db";

    public DBHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //All necessary tables you like to create will create here

        String CREATE_TABLE_STUDENT = "CREATE TABLE " + User.TABLE  + "("
                + User.KEY_Identification  + " TEXT PRIMARY KEY,"
                + User.KEY_nom + " TEXT, "
                + User.KEY_motPasse + " TEXT, "
                + User.KEY_adresse + " TEXT, "
                + User.KEY_phone + " TEXT, "
                + User.KEY_sumRate + " INTEGER, "
                + User.Key_countRate + " INTEGER) ";
        db.execSQL(CREATE_TABLE_STUDENT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE);

        // Create tables again
        onCreate(db);

    }

}
