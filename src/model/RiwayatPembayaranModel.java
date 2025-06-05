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
        List<Riwayat> list = new ArrayList<>();
        try (Connection conn = KoneksidB.getKoneksi();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT p.tanggal_bayar, p.jumlah_bayar, p.keterangan " +
                     "FROM pembayaran p " +
                     "JOIN pajak pj ON p.pajak_id = pj.id " +
                     "JOIN kendaraan k ON pj.kendaraan_id = k.id " +
                     "WHERE k.user_id = ?")) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String tanggal = rs.getString("tanggal_bayar");
                    double jumlah = rs.getDouble("jumlah_bayar");
                    String keterangan = rs.getString("keterangan");
                    if (keterangan == null) keterangan = "";
                    list.add(new Riwayat(tanggal, jumlah, keterangan));
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Error in getRiwayatByUser untuk userId " + userId + " pada " + new java.util.Date() + ": " + e.getMessage());
            throw e;
        }
        return list;
    }
}