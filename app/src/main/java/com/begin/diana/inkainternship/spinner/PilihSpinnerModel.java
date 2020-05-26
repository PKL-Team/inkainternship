package com.begin.diana.inkainternship.spinner;

public class PilihSpinnerModel {
    private String id, nama;

    public PilihSpinnerModel() {
    }

    public PilihSpinnerModel(String id, String nama_roti) {
        this.id = id;
        this.nama = nama;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
