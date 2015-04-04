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
    public static final String KEY_Heure = "Heure";
    public static final String KEY_IDENTIFIANT = "identifiant";


    // property help us to keep data
    private String _depart, _destination, _frequence, _heure,_identifiant;
    int _nombreDePlace, _KM;
    String _date,_ID;

    // Constructeur
    public ParcoursConducteur() {
        _heure ="";
        _depart = "";
        _destination = "";
        _frequence = "";
        _identifiant ="";
        _nombreDePlace = 0;
        _ID = "";
        _date = "";
        _KM = 0;
    };

    //--------------------------------------------
    //-------------Set des variables--------------
    //--------------------------------------------
    public void set_ID(String _ID) {
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

    public void set_nombreDePlace(int _nombreDePlace) {
        this._nombreDePlace = _nombreDePlace;
    }

    public void set_KM(int _KM) {
        this._KM = _KM;
    }

    public void set_heure(String _heure) {
        this._heure = _heure;
    }

    public void set_identifiant(String _identifiant) {
        this._identifiant = _identifiant;
    }

    //--------------------------------------------
    //-------------Get des variables--------------
    //--------------------------------------------
    public String get_ID(){return this._ID;}
    public String get_depart() {
        return _depart;
    }
    public String get_identifiant() {
        return _identifiant;
    }
    public int get_nombreDePlace() {
        return _nombreDePlace;
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

    public int get_KM() {
        return _KM;
    }

    public String get_heure() {
        return _heure;
    }

    //--------------------------------------------
    //-----------------Méthodes-------------------
    //--------------------------------------------
    public void enleverPlace() {
        this._nombreDePlace = this._nombreDePlace - 1;
    }
}
