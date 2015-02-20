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

    public UserRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insert(User user) {
        //Open connection to write data
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(User.KEY_Identification, user.get_identification());
        values.put(User.KEY_nom, user.get_nom());
        values.put(User.KEY_motPasse, user.get_motPasse());
        values.put(User.KEY_adresse, user.get_adresse());
        values.put(User.KEY_phone, user.get_phone());
        values.put(User.KEY_sumRate, 0);
        values.put(User.Key_countRate, 0);

        // Inserting Row
        long student_Id = db.insert(User.TABLE, null, values);
        db.close(); // Closing database connection
        return (int) student_Id;
    }

    public void delete(String user_identification) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(User.TABLE, User.KEY_Identification + "= ?", new String[] { user_identification });
        db.close(); // Closing database connection
    }

    public void update(User user) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(User.KEY_Identification, user.get_identification());
        values.put(User.KEY_nom, user.get_nom());
        values.put(User.KEY_motPasse, user.get_motPasse());
        values.put(User.KEY_adresse, user.get_adresse());
        values.put(User.KEY_phone, user.get_phone());
        values.put(User.KEY_sumRate, user.get_sumRate());
        values.put(User.Key_countRate, user.get_countRate());

        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(User.TABLE, values, User.KEY_Identification + "= ?", new String[] { user.get_identification() });
        db.close(); // Closing database connection
    }

    public User getUserByIdentification(String identification){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                User.KEY_Identification + "," +
                User.KEY_nom + "," +
                User.KEY_motPasse + "," +
                User.KEY_adresse + "," +
                User.KEY_phone + "," +
                User.KEY_sumRate + "," +
                User.Key_countRate +
                " FROM " + User.TABLE
                + " WHERE " +
                User.KEY_Identification + "=?";

        User user = new User();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { identification } );

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

        cursor.close();
        db.close();
        return user;
    }

}
