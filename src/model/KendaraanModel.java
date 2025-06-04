package model;

import java.sql.*;
import java.util.*;

public class KendaraanModel {

    public List<Object[]> getKendaraanByUser(int userId) throws SQLException {
        List<Object[]> list = new ArrayList<>();
        try (Connection conn = KoneksidB.getKoneksi();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM kendaraan WHERE user_id = ?")) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = new Object[]{
                        rs.getInt("id"),
                        rs.getString("nomor_polisi"),
                        rs.getString("merk"),
                        rs.getString("jenis"),
                        rs.getInt("tahun"),
                        rs.getDouble("harga_kendaraan"), // Sesuai dengan nama kolom di database
                        rs.getString("cc")
                    };
                    System.out.println("Retrieved row for userId " + userId + ": " + Arrays.toString(row));
                    list.add(row);
                }
            }
        }
        if (list.isEmpty()) {
            System.out.println("No data found for userId: " + userId + " at " + new java.util.Date());
        }
        return list;
    }

    public Object[] getKendaraanById(int kendaraanId, int userId) throws SQLException {
        try (Connection conn = KoneksidB.getKoneksi();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM kendaraan WHERE id = ? AND user_id = ?")) {
            pstmt.setInt(1, kendaraanId);
            pstmt.setInt(2, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Object[] data = new Object[]{
                        rs.getString("nomor_polisi"),
                        rs.getString("merk"),
                        rs.getString("jenis"),
                        rs.getInt("tahun"),
                        rs.getDouble("harga_kendaraan"), // Sesuai dengan nama kolom di database
                        rs.getString("cc")
                    };
                    System.out.println("Retrieved detail for kendaraanId " + kendaraanId + ": " + Arrays.toString(data));
                    return data;
                }
            }
        }
        System.out.println("No data found for kendaraanId: " + kendaraanId + ", userId: " + userId + " at " + new java.util.Date());
        return null;
    }

    public boolean updateKendaraan(int kendaraanId, int userId, String nomorPolisi, String merk, String jenis, int tahun, double harga, String cc) throws SQLException {
        try (Connection conn = KoneksidB.getKoneksi();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE kendaraan SET nomor_polisi=?, merk=?, jenis=?, tahun=?, harga_kendaraan=?, cc=? WHERE id=? AND user_id=?")) {
            pstmt.setString(1, nomorPolisi);
            pstmt.setString(2, merk);
            pstmt.setString(3, jenis);
            pstmt.setInt(4, tahun);
            pstmt.setDouble(5, harga);
            pstmt.setString(6, cc);
            pstmt.setInt(7, kendaraanId);
            pstmt.setInt(8, userId);

            int result = pstmt.executeUpdate();
            return result > 0;
        }
    }

    public boolean deleteKendaraan(int kendaraanId, int userId) throws SQLException {
        try (Connection conn = KoneksidB.getKoneksi();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM kendaraan WHERE id = ? AND user_id = ?")) {
            pstmt.setInt(1, kendaraanId);
            pstmt.setInt(2, userId);
            int result = pstmt.executeUpdate();
            return result > 0;
        }
    }
}