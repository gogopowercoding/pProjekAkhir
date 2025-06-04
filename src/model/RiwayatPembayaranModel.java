package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RiwayatPembayaranModel {
    public static class Riwayat {
        public String tanggal;
        public double jumlah;
        public String keterangan;

        public Riwayat(String tanggal, double jumlah, String keterangan) {
            this.tanggal = tanggal;
            this.jumlah = jumlah;
            this.keterangan = keterangan;
        }
    }

    public List<Riwayat> getRiwayatByUser(int userId) throws SQLException {
        List<Riwayat> riwayatList = new ArrayList<>();
        Connection conn = KoneksidB.getKoneksi();
        String sql = "SELECT tanggal_bayar, jumlah_bayar, keterangan FROM pembayaran WHERE user_id = ? ORDER BY tanggal_bayar DESC";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, userId);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            riwayatList.add(new Riwayat(
                rs.getString("tanggal_bayar"),
                rs.getDouble("jumlah_bayar"),
                rs.getString("keterangan")
            ));
        }

        rs.close();
        pstmt.close();
        conn.close();

        return riwayatList;
    }
}
