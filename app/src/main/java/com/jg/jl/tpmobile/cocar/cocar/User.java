package com.jg.jl.tpmobile.cocar.cocar;

/**
 * Created by Jonathan Lavoie on 19/02/2015.
 */
public class User {
    // Variable de la class d'un usager
    private String _nom, _identifiant, _motDePasse, _address, _phone;

    //Constructeur
    public User (String nom, String identifiant, String motDePasse, String address, String phone)
    {
        this._nom = nom;
        this._identifiant = identifiant;
        this._motDePasse = Util.encryptPassword(motDePasse);
        this._address = address;
        this._phone = phone;
    }

    //------------------------------------------
    // -----------Set des variable--------------
    //------------------------------------------
    public void set_nom(String nom) {
        this._nom = nom;
    }

    public void set_identifiant(String identifiant) {
        this._identifiant = identifiant;
    }

    public void set_motDePasse(String motDePasse) {
        this._motDePasse = Util.encryptPassword(motDePasse);
    }

    public void set_address(String address) {
        this._address = address;
    }

    public void set_phone(String phone) {
        this._phone = phone;
    }
    //------------------------------------------
    // -----------Get des variable--------------
    //------------------------------------------
    public String get_nom() {
        return _nom;
    }

    public String get_identifiant() {
        return _identifiant;
    }

    public String get_motDePasse() {
        return _motDePasse;
    }

    public String get_address() {
        return _address;
    }

    public String get_phone() {
        return _phone;
    }
}
