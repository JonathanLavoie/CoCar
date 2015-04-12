package com.jg.jl.tpmobile.cocar.cocar;

/**
 * Created by Jonathan Lavoie on 05/03/2015.
 */
public class Parcours {
    // Labels table name
    public static final String TABLE = "Parcours";

    // Labels Table Columns names
    public static final String KEY_ID = "ID";
    public static final String KEY_ConducteurID = "ConducteurID";
    public static final String KEY_PassagerID = "PassagerID";
    public static final String KEY_Date = "Date";
    public static final String KEY_Heure = "Heure";

    String _heure;
    int _ID, _PassagerID, _ConducteurID;
    String _date;

    public Parcours () {
        _heure ="";
        _ID = 0;
        _PassagerID = 0;
        _ConducteurID = 0;
        _date = "";
    }
    //--------------------------------------------
    //-------------Set des variables--------------
    //--------------------------------------------

    public void set_ID(int _ID) {
        this._ID = _ID;
    }

    public void set_ConducteurID(int _ConducteurID) {
        this._ConducteurID = _ConducteurID;
    }

    public void set_PassagerID(int _PassagerID) {
        this._PassagerID = _PassagerID;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public void set_heure(String _heure) {
        this._heure = _heure;
    }

    //--------------------------------------------
    //-------------Get des variables--------------
    //--------------------------------------------
    public int get_ID() {
        return _ID;
    }

    public int get_ConducteurID() {
        return _ConducteurID;
    }

    public int get_PassagerID() {
        return _PassagerID;
    }

    public String get_date() {
        return _date;
    }

    public String get_heure() {
        return _heure;
    }
}
