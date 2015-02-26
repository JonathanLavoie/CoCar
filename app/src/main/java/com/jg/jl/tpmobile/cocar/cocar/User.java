package com.jg.jl.tpmobile.cocar.cocar;

/**
 * Created by Jonathan Lavoie on 19/02/2015.
 */
public class User {
    // Labels table name
    public static final String TABLE = "User";

    // Labels Table Columns names
    public static final String KEY_Identification = "Identification";
    public static final String KEY_nom = "Nom";
    public static final String KEY_motPasse = "MotPasse";
    public static final String KEY_adresse = "Adresse";
    public static final String KEY_phone = "Phone";
    public static final String KEY_sumRate = "SumRate";
    public static final String KEY_countRate = "CountRate";


    // property help us to keep data
    private String _identification, _nom, _motPasse, _adresse, _phone;
    private int _sumRate, _countRate;

    // Constructeur
    public User() {
        _identification = "";
        _nom = "";
        _motPasse = "";
        _adresse = "";
        _phone = "";
        _sumRate = 0;
        _countRate = 0;
    };
    //--------------------------------------------
    //-------------Set des variables--------------
    //--------------------------------------------
    public void set_identification(String identification) {
        this._identification = identification;
    }

    public void set_nom(String nom) {
        this._nom = nom;
    }

    public void set_motPasse(String motPasse) {
        this._motPasse = motPasse;
    }

    public void set_adresse(String adresse) {
        this._adresse = adresse;
    }

    public void set_phone(String phone) {
        this._phone = phone;
    }

    public void set_sumRate(int sumRate) {
        this._sumRate = sumRate;
    }

    public void set_countRate(int countRate) {
        this._countRate = countRate;
    }

    //--------------------------------------------
    //-------------Get des variables--------------
    //--------------------------------------------

    public String get_identification() {
        return _identification;
    }

    public String get_nom() {
        return _nom;
    }

    public String get_motPasse() {
        return _motPasse;
    }

    public String get_adresse() {
        return _adresse;
    }

    public String get_phone() {
        return _phone;
    }

    public int get_sumRate() {
        return _sumRate;
    }

    public int get_countRate() {
        return _countRate;
    }

    //--------------------------------------------
    //-----------------Méthodes-------------------
    //--------------------------------------------

    /**
     * Methode pour ajouter une quote
     * @param rate - La quote sur 5
     */
    public void ajouterRate(int rate) {
        this._sumRate = this._sumRate + rate;
        this._countRate = this._countRate + 1;
    }

    /**
     * Méthode pour confirmer de mot de passe
     * @param motPasse - String Mot de passe
     * @return - Boolean Vrai,faux si le mot de passe est le meme.
     */
    public boolean verifierMotPasse(String motPasse) {
        if (this._motPasse == Util.encryptPassword(motPasse)) {
            return true;
        }
        else {
            return false;
        }
    }
}