package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class KendaraanDAO {
    public boolean insert(KendaraanTambah kendaraan) throws SQLException {
        Connection conn = KoneksidB.getKoneksi();
        String sql = "INSERT INTO kendaraan (user_id, nomor_polisi, merk, jenis, tahun, harga_kendaraan, cc) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setInt(1, kendaraan.getUserId());
        pstmt.setString(2, kendaraan.getNomorPolisi());
        pstmt.setString(3, kendaraan.getMerk());
        pstmt.setString(4, kendaraan.getJenis());
        pstmt.setInt(5, kendaraan.getTahun());
        pstmt.setDouble(6, kendaraan.getHarga());
        pstmt.setString(7, kendaraan.getCc());

        int result = pstmt.executeUpdate();
        pstmt.close();
        conn.close();
        return result > 0;
    }
}
