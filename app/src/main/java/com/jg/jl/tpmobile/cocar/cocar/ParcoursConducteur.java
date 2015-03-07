package com.jg.jl.tpmobile.cocar.cocar;

import java.util.Date;

/**
 * Created by Jonathan Lavoie on 04/03/2015.
 */
public class ParcoursConducteur {
    // Labels table name
    public static final String TABLE = "ParcoursConducteur";

    // Labels Table Columns names
    public static final String KEY_ID = "ID";
    public static final String KEY_Depart = "Depart";
    public static final String KEY_Destination = "Destination";
    public static final String KEY_Frequence = "Frequence";
    public static final String KEY_Date = "Date";
    public static final String KEY_NombrePlace = "NombrePlace";
    public static final String KEY_KM = "KM";


    // property help us to keep data
    private String _depart, _destination, _frequence;
    int _nombreDePlace, _ID, _KM;
    Date _date;

    // Constructeur
    public ParcoursConducteur() {
        _depart = "";
        _destination = "";
        _frequence = "";
        _nombreDePlace = 0;
        _ID = 0;
        _date = new Date();
        _KM = 0;
    };

    //--------------------------------------------
    //-------------Set des variables--------------
    //--------------------------------------------
    public void set_ID(int _ID) {
        this._ID = _ID;
    }

    public void set_depart(String _depart) {
        this._depart = _depart;
    }

    public void set_destination(String _destination) {
        this._destination = _destination;
    }

    public void set_frequence(String _frequence) {
        this._frequence = _frequence;
    }

    public void set_date(Date _date) {
        this._date = _date;
    }

    public void set_nombreDePlace(int _nombreDePlace) {
        this._nombreDePlace = _nombreDePlace;
    }

    public void set_KM(int _KM) {
        this._KM = _KM;
    }

    //--------------------------------------------
    //-------------Get des variables--------------
    //--------------------------------------------
    public int get_ID(){return this._ID;}

    public String get_depart() {
        return _depart;
    }

    public int get_nombreDePlace() {
        return _nombreDePlace;
    }

    public String get_frequence() {
        return _frequence;
    }

    public Date get_date() {
        return _date;
    }

    public String get_destination() {
        return _destination;
    }

    public int get_KM() {
        return _KM;
    }

    //--------------------------------------------
    //-----------------Méthodes-------------------
    //--------------------------------------------
    public void enleverPlace() {
        this._nombreDePlace = this._nombreDePlace - 1;
    }
}
