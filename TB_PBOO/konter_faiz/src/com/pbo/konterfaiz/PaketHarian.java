package com.pbo.konterfaiz;

// Kelas turunan Paket
class PaketHarian extends Paket {
    private int hari;

    public PaketHarian(int id, String nama, int harga, int kuota, int masaBerlaku, int hari) {
        super(id, nama, harga, kuota, masaBerlaku);
        this.hari = hari;
    }

    @Override
    public void info() {
        super.info();
        System.out.println("Hari: " + hari);
    }
}

