package com.kemendikbud.paud.model;

/**
 * Created by akbar on 28/08/17.
 */

public class UserData{
    private String id_sekolah;
    private String id_pengguna;
    private String id_lembaga;
    private String name;
    private String email;
    private String nip;
    private String jabatan;
    private String alamat;
    private String kode_wilayah;
    private String no_telp;
    private String no_hp;

    public UserData(){
    }

    public void setIdSekolah(String idSekolah){
        this.id_sekolah = idSekolah;
    }

    public String getIdSekolah(){
        return id_sekolah;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public String getIdPengguna() {
        return id_pengguna;
    }

    public void setIdPengguna(String id_pengguna) {
        this.id_pengguna = id_pengguna;
    }

    public String getIdLembaga() {
        return id_lembaga;
    }

    public void setIdLembaga(String id_lembaga) {
        this.id_lembaga = id_lembaga;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKodeWilayah() {
        return kode_wilayah;
    }

    public void setKodeWilayah(String kode_wilayah) {
        this.kode_wilayah = kode_wilayah;
    }

    public String getNoTelp() {
        return no_telp;
    }

    public void setNoTelp(String no_telp) {
        this.no_telp = no_telp;
    }

    public String getNoHp() {
        return no_hp;
    }

    public void setNoHp(String no_hp) {
        this.no_hp = no_hp;
    }
}
