package com.jg.jl.tpmobile.cocar.cocar;

import java.util.Date;

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

    int _ID, _PassagerID, _ConducteurID;
    Date _date;

    public Parcours () {
        _ID = 0;
        _PassagerID = 0;
        _ConducteurID = 0;
        _date = new Date();
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

    public void set_date(Date _date) {
        this._date = _date;
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

    public Date get_date() {
        return _date;
    }
}
