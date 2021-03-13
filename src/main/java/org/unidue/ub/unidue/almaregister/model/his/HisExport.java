package org.unidue.ub.unidue.almaregister.model.his;

import org.springframework.data.domain.Persistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "his_export")
public class HisExport implements Serializable, Persistable<String> {

    @Column(name = "mtknr")
    private String mtknr;

    @Id
    @Column(name = "zim_kennung")
    private String zimKennung;

    @Column(name = "bibkz")
    private String bibkz;

    @Column(name = "geschl")
    private String geschl;

    @Column(name = "nachname")
    private String nachname;

    @Column(name = "vorname")
    private String vorname;

    @Column(name = "gebdat")
    private String gebdat;

    @Column(name = "strasse")
    private String strasse;

    @Column(name = "pozusatz")
    private String pozusatz;

    @Column(name = "plz")
    private String plz;

    @Column(name = "ort")
    private String ort;

    @Column(name = "land")
    private String land;

    @Column(name = "festnetz")
    private String festnetz;

    @Column(name = "mobil")
    private String mobil;

    @Column(name = "email")
    private String email;

    @Column(name = "immadatum")
    private String immadatum;

    @Column(name = "status")
    private String status;

    @Column(name = "semester")
    private String semester;

    @Column(name = "campus")
    private String campus;

    @Column(name = "abschluss1")
    private String abschluss1;

    @Column(name = "fach1")
    private String fach1;

    @Column(name = "fach2")
    private String fach2;

    @Column(name = "fach3")
    private String fach3;

    @Column(name = "card_currens")
    private String cardCurrens;

    @Column(name = "hoerer_status")
    private String hoererStatus;

    @Column(name = "exgr")
    private String exgr;

    @Column(name = "exmagrund")
    private String exmagrund;

    @Column(name = "exmadatum")
    private String exmadatum;

    @Column(name = "exmaantrag")
    private String exmaantrag;

    public String getMtknr() {
        return mtknr;
    }

    public void setMtknr(String mtknr) {
        this.mtknr = mtknr;
    }

    public String getZimKennung() {
        return zimKennung;
    }

    public void setZimKennung(String zimKennung) {
        this.zimKennung = zimKennung;
    }

    public String getBibkz() {
        return bibkz;
    }

    public void setBibkz(String bibkz) {
        this.bibkz = bibkz;
    }

    public String getGeschl() {
        return geschl;
    }

    public void setGeschl(String geschl) {
        this.geschl = geschl;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getGebdat() {
        return gebdat;
    }

    public void setGebdat(String gebdat) {
        this.gebdat = gebdat;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getPozusatz() {
        return pozusatz;
    }

    public void setPozusatz(String pozusatz) {
        this.pozusatz = pozusatz;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }

    public String getFestnetz() {
        return festnetz;
    }

    public void setFestnetz(String festnetz) {
        this.festnetz = festnetz;
    }

    public String getMobil() {
        return mobil;
    }

    public void setMobil(String mobil) {
        this.mobil = mobil;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImmadatum() {
        return immadatum;
    }

    public void setImmadatum(String immadatum) {
        this.immadatum = immadatum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getExgr() {
        return exgr;
    }

    public void setExgr(String exgr) {
        this.exgr = exgr;
    }

    public String getExmagrund() {
        return exmagrund;
    }

    public void setExmagrund(String exmagrund) {
        this.exmagrund = exmagrund;
    }

    public String getExmadatum() {
        return exmadatum;
    }

    public void setExmadatum(String exmadatum) {
        this.exmadatum = exmadatum;
    }

    public String getExmaantrag() {
        return exmaantrag;
    }

    public void setExmaantrag(String exmaantrag) {
        this.exmaantrag = exmaantrag;
    }

    public String getAbschluss1() {
        return abschluss1;
    }

    public void setAbschluss1(String abschluss1) {
        this.abschluss1 = abschluss1;
    }

    public String getFach1() {
        return fach1;
    }

    public void setFach1(String fach1) {
        this.fach1 = fach1;
    }

    public String getFach2() {
        return fach2;
    }

    public void setFach2(String fach2) {
        this.fach2 = fach2;
    }

    public String getFach3() {
        return fach3;
    }

    public void setFach3(String fach3) {
        this.fach3 = fach3;
    }

    public String getCardCurrens() {
        return cardCurrens;
    }

    public void setCardCurrens(String cardCurrens) {
        this.cardCurrens = cardCurrens;
    }

    public String getHoererStatus() {
        return hoererStatus;
    }

    public void setHoererStatus(String hoererStatus) {
        this.hoererStatus = hoererStatus;
    }

    public String getId() {return this.zimKennung;}

    @Override
    public boolean isNew() {
        return true;
    }

    @Override
    public String toString() {
        return mtknr + ';' + zimKennung + ';' + geschl + ';' + gebdat + ';' +bibkz + ';' +ort + ';' + plz + ';' + strasse + ';' + ort  + ';' + semester + ';' + status + ';' + campus;
    }
}
