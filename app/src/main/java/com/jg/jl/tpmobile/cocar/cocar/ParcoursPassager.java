package com.jg.jl.tpmobile.cocar.cocar;

import java.util.Date;

/**
 * Created by Jonathan Lavoie on 04/03/2015.
 */
public class ParcoursPassager {
    // Labels table name
    public static final String TABLE = "ParcoursConducteur";

    // Labels Table Columns names
    public static final String KEY_ID = "ID";
    public static final String KEY_Depart = "Depart";
    public static final String KEY_Destination = "Destination";
    public static final String KEY_Frequence = "Frequence";
    public static final String KEY_Date = "Date";
    public static final String KEY_NombrePassager = "NombrePassager";
    public static final String KEY_Heure = "Heure";

    // property help us to keep data
    private String _depart, _destination, _frequence, _heure;
    int _ID, _nombrePassager;
    String _date;

    // Constructeur
    public ParcoursPassager() {
        _heure ="";
        _depart = "";
        _destination = "";
        _frequence = "";
        _ID = 0;
        _nombrePassager = 0;
        _date = "";
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

    public void set_date(String _date) {
        this._date = _date;
    }

    public void set_nombrePassager(int _nombrePassager) {
        this._nombrePassager = _nombrePassager;
    }

    public void set_heure(String _heure) {
        this._heure = _heure;
    }

    //--------------------------------------------
    //-------------Get des variables--------------
    //--------------------------------------------
    public int get_ID(){return this._ID;}

    public String get_depart() {
        return _depart;
    }

    public String get_frequence() {
        return _frequence;
    }

    public String get_date() {
        return _date;
    }

    public String get_destination() {
        return _destination;
    }

    public int get_nombrePassager() {
        return _nombrePassager;
    }

    public String get_heure() {
        return _heure;
    }

    //--------------------------------------------
    //-----------------Méthodes-------------------
    //--------------------------------------------
}