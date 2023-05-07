package com.example.myapplication;


public class Product {

    String felhasznalo;
    String nev;
    String kategoria;
    int ar;

    String id;

    public Product() {
    }

    public Product(String felhasznalo, String nev, String kategoria, int ar, String id) {
        this.felhasznalo = felhasznalo;
        this.nev = nev;
        this.kategoria = kategoria;
        this.ar = ar;
        this.id = id;
    }

    public String getFelhasznalo() {
        return felhasznalo;
    }



    public String getNev() {
        return nev;
    }

    public String getKategoria() {
        return kategoria;
    }

    public int getAr() {
        return ar;
    }

    public void setFelhasznalo(String felhasznalo) {
        this.felhasznalo = felhasznalo;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public void setKategoria(String kategoria) {
        this.kategoria = kategoria;
    }

    public void setAr(int ar) {
        this.ar = ar;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String _getId() {
        return id;
    }

}