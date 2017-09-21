package com.kemendikbud.paud.model;

/**
 * Created by akbar on 29/08/17.
 */

public class Category {
    private String category_id;
    private String category_name;

    public Category(String idKategori, String namaKategori, String keterangan){
        this.category_id = idKategori;
        this.category_name = namaKategori;
    }

    public Category(){
    }

    public String getIdKategori() {
        return category_id;
    }

    public void setIdKategori(String idKategori) {
        this.category_id = idKategori;
    }

    public String getNamaKategori() {
        return category_name;
    }

    public void setNamaKategori(String namaKategori) {
        this.category_name = namaKategori;
    }

}
