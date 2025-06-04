package model;

public class User {
    private int id;
    private String nik;
    private String nama;
    private String password;
    private String alamat;
    private String noTelp;
    private String role;

    public User(String nik, String nama, String password, String alamat, String noTelp, String role) {
        this.nik = nik;
        this.nama = nama;
        this.password = password;
        this.alamat = alamat;
        this.noTelp = noTelp;
        this.role = role;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNik() { return nik; }
    public void setNik(String nik) { this.nik = nik; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public String getNoTelp() { return noTelp; }
    public void setNoTelp(String noTelp) { this.noTelp = noTelp; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
