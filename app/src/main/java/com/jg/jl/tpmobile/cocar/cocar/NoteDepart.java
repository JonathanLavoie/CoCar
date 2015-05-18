package com.jg.jl.tpmobile.cocar.cocar;

/**
 * Created by Jiimmy on 2015-05-13.
 */
public class NoteDepart {
    int nbPassager = 0;
    String idParcours = "";
    float rate = 0;
    String idUser1 = "";
    String idUser2 = "";

    public String getIdNote() {
        return idNote;
    }

    public void setIdNote(String idNote) {
        this.idNote = idNote;
    }

    String idNote = "";
    public int getNbPassager() {
        return nbPassager;
    }

    public void setNbPassager(int nbPassager) {
        this.nbPassager = nbPassager;
    }

    public String getIdParcours() {
        return idParcours;
    }

    public void setIdParcours(String idParcours) {
        this.idParcours = idParcours;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getIdUser1() {
        return idUser1;
    }

    public void setIdUser1(String idUser1) {
        this.idUser1 = idUser1;
    }

    public String getIdUser2() {
        return idUser2;
    }

    public void setIdUser2(String idUser2) {
        this.idUser2 = idUser2;
    }
}
