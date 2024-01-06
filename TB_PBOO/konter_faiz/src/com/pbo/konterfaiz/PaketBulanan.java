package com.pbo.konterfaiz;

class PaketBulanan extends Paket {
    private int bulan;

    public PaketBulanan(int id, String nama, int harga, int kuota, int masaBerlaku, int bulan) {
        super(id, nama, harga, kuota, masaBerlaku);
        this.bulan = bulan;
    }

    @Override
    public void info() {
        super.info();
        System.out.println("Bulan: " + bulan);
    }
}
