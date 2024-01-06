package com.pbo.konterfaiz;

// Kelas dasar untuk paket
class Paket implements TampilInfo {
    // atribut-atribut paket
    protected int id;
    protected String nama;
    protected int harga;
    protected int kuota;
    protected int masaBerlaku; // Dalam hari

     // Constructor untuk Paket
    public Paket(int id, String nama, int harga, int kuota, int masaBerlaku) {
        // inisialisasi atribut-atribut paket
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.kuota = kuota;
        this.masaBerlaku = masaBerlaku;
    }

    // Implementasi method dari interface TampilInfo
    @Override
    public void info() {
        // Implementasi informasi paket
        System.out.println("ID: " + id);
        System.out.println("Nama Paket: " + nama);
        System.out.println("Harga: Rp. " + harga);
        System.out.println("Kuota: " + kuota + "GB");
        System.out.println("Masa Berlaku: " + masaBerlaku + " hari");
    }
}


