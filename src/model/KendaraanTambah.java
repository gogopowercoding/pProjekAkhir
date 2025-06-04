package model;

public class KendaraanTambah {
    private int userId;
    private String nomorPolisi;
    private String merk;
    private String jenis;
    private int tahun;
    private double harga;
    private String cc;

    public KendaraanTambah(int userId, String nomorPolisi, String merk, String jenis, int tahun, double harga, String cc) {
        this.userId = userId;
        this.nomorPolisi = nomorPolisi;
        this.merk = merk;
        this.jenis = jenis;
        this.tahun = tahun;
        this.harga = harga;
        this.cc = cc;
    }

    // Getters
    public int getUserId() { return userId; }
    public String getNomorPolisi() { return nomorPolisi; }
    public String getMerk() { return merk; }
    public String getJenis() { return jenis; }
    public int getTahun() { return tahun; }
    public double getHarga() { return harga; }
    public String getCc() { return cc; }
}
