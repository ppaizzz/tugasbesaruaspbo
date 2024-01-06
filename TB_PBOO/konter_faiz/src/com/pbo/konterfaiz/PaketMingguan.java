package com.pbo.konterfaiz;

// Kelas turunan Paket
class PaketMingguan extends Paket {
    private int minggu;

    // Constructor untuk PaketMingguan
    public PaketMingguan(int id, String nama, int harga, int kuota, int masaBerlaku, int minggu) {
        super(id, nama, harga, kuota, masaBerlaku);
        this.minggu = minggu;
    }

    // Override method info() dari kelas Paket
    @Override
    public void info() {
        super.info();
        System.out.println("Minggu: " + minggu);
    }
}
