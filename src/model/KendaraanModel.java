package model;

import java.sql.*;
import java.util.*;

public class KendaraanModel {
    public List<Object[]> getKendaraanByUser(int userId) throws SQLException {
        List<Object[]> list = new ArrayList<>();
        try (Connection conn = KoneksidB.getKoneksi();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT k.id, k.nomor_polisi, k.merk, k.jenis, k.tahun, k.harga_kendaraan, k.cc, p.status " +
                     "FROM kendaraan k " +
                     "LEFT JOIN pajak p ON k.id = p.kendaraan_id " +
                     "WHERE k.user_id = ?")) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = new Object[]{
                        rs.getInt("id"),
                        rs.getString("nomor_polisi"),
                        rs.getString("merk"),
                        rs.getString("jenis"),
                        rs.getInt("tahun"),
                        rs.getDouble("harga_kendaraan"),
                        rs.getString("cc"),
                        rs.getString("status") != null ? rs.getString("status") : "BELUM_BAYAR"
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

    public List<Object[]> getAllKendaraan() throws SQLException {
        List<Object[]> list = new ArrayList<>();
        try (Connection conn = KoneksidB.getKoneksi();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM kendaraan")) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = new Object[]{
                        rs.getInt("id"),
                        rs.getString("nomor_polisi"),
                        rs.getString("merk"),
                        rs.getString("jenis"),
                        rs.getInt("tahun"),
                        rs.getDouble("harga_kendaraan"),
                        rs.getString("cc")
                    };
                    System.out.println("Retrieved row: " + Arrays.toString(row));
                    list.add(row);
                }
            }
        }
        if (list.isEmpty()) {
            System.out.println("No kendaraan data found in database at " + new java.util.Date());
        }
        return list;
    }

    public List<Object[]> getKendaraanByStatus(String statusFilter) throws SQLException {
        List<Object[]> list = new ArrayList<>();
        String dbStatus = statusFilter.equals("SEMUA") ? null : 
                          statusFilter.equals("BELUM BAYAR") ? "BELUM_BAYAR" : 
                          statusFilter.equals("SUDAH BAYAR") ? "SUDAH_BAYAR" : null;
        try (Connection conn = KoneksidB.getKoneksi();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT k.id, k.nomor_polisi, k.merk, k.jenis, k.tahun, k.harga_kendaraan, k.cc, MAX(p.status) as status " +
                     "FROM kendaraan k " +
                     "LEFT JOIN pajak p ON k.id = p.kendaraan_id " +
                     "WHERE (? IS NULL OR p.status = ? OR (p.status IS NULL AND ? = 'SEMUA')) " +
                     "GROUP BY k.id, k.nomor_polisi, k.merk, k.jenis, k.tahun, k.harga_kendaraan, k.cc")) {
            pstmt.setString(1, dbStatus);
            pstmt.setString(2, dbStatus);
            pstmt.setString(3, statusFilter);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = new Object[]{
                        rs.getInt("id"),
                        rs.getString("nomor_polisi"),
                        rs.getString("merk"),
                        rs.getString("jenis"),
                        rs.getInt("tahun"),
                        rs.getDouble("harga_kendaraan"),
                        rs.getString("cc"),
                        rs.getString("status") != null ? rs.getString("status") : "BELUM_BAYAR"
                    };
                    System.out.println("Retrieved row with status filter " + statusFilter + ": " + Arrays.toString(row));
                    list.add(row);
                }
            }
        }
        if (list.isEmpty()) {
            System.out.println("No kendaraan data found for status filter: " + statusFilter + " at " + new java.util.Date());
        }
        return list;
    }

    public Object[] getKendaraanById(int kendaraanId) throws SQLException {
        try (Connection conn = KoneksidB.getKoneksi();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM kendaraan WHERE id = ?")) {
            pstmt.setInt(1, kendaraanId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Object[]{
                        rs.getString("nomor_polisi"),
                        rs.getString("merk"),
                        rs.getString("jenis"),
                        rs.getInt("tahun"),
                        rs.getDouble("harga_kendaraan"),
                        rs.getString("cc")
                    };
                }
            }
        }
        System.out.println("No data found for kendaraanId: " + kendaraanId + " at " + new java.util.Date());
        return null;
    }

    public Object[] getKendaraanByIdAndUser(int kendaraanId, int userId) throws SQLException {
        try (Connection conn = KoneksidB.getKoneksi();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM kendaraan WHERE id = ? AND user_id = ?")) {
            pstmt.setInt(1, kendaraanId);
            pstmt.setInt(2, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Object[]{
                        rs.getString("nomor_polisi"),
                        rs.getString("merk"),
                        rs.getString("jenis"),
                        rs.getInt("tahun"),
                        rs.getDouble("harga_kendaraan"),
                        rs.getString("cc")
                    };
                }
            }
        }
        System.out.println("No data found for kendaraanId: " + kendaraanId + ", userId: " + userId + " at " + new java.util.Date());
        return null;
    }

    public boolean updateKendaraan(int kendaraanId, String nomorPolisi, String merk, String jenis, int tahun, double harga, String cc) throws SQLException {
        try (Connection conn = KoneksidB.getKoneksi();
             PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE kendaraan SET nomor_polisi = ?, merk = ?, jenis = ?, tahun = ?, harga_kendaraan = ?, cc = ? WHERE id = ?")) {
            pstmt.setString(1, nomorPolisi);
            pstmt.setString(2, merk);
            pstmt.setString(3, jenis);
            pstmt.setInt(4, tahun);
            pstmt.setDouble(5, harga);
            pstmt.setString(6, cc);
            pstmt.setInt(7, kendaraanId);
            int result = pstmt.executeUpdate();
            return result > 0;
        }
    }

    public boolean updateKendaraan(int kendaraanId, int userId, String nomorPolisi, String merk, String jenis, int tahun, double harga, String cc) throws SQLException {
        try (Connection conn = KoneksidB.getKoneksi();
             PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE kendaraan SET nomor_polisi = ?, merk = ?, jenis = ?, tahun = ?, harga_kendaraan = ?, cc = ? WHERE id = ? AND user_id = ?")) {
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

    public boolean deleteKendaraanAdmin(int kendaraanId) throws SQLException {
        try (Connection conn = KoneksidB.getKoneksi();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM kendaraan WHERE id = ?")) {
            pstmt.setInt(1, kendaraanId);
            int result = pstmt.executeUpdate();
            return result > 0;
        }
    }

    public boolean deletePajakByKendaraanId(int kendaraanId, int userId) throws SQLException {
        try (Connection conn = KoneksidB.getKoneksi();
             PreparedStatement pstmt = conn.prepareStatement(
                     "DELETE FROM pajak WHERE kendaraan_id = ? AND EXISTS (SELECT 1 FROM kendaraan k WHERE k.id = ? AND k.user_id = ?)")) {
            pstmt.setInt(1, kendaraanId);
            pstmt.setInt(2, kendaraanId);
            pstmt.setInt(3, userId);
            int result = pstmt.executeUpdate();
            return true; // Return true even if no rows are deleted
        }
    }

    public boolean deletePajakByKendaraanIdAdmin(int kendaraanId) throws SQLException {
        try (Connection conn = KoneksidB.getKoneksi();
             PreparedStatement pstmt = conn.prepareStatement(
                     "DELETE FROM pajak WHERE kendaraan_id = ?")) {
            pstmt.setInt(1, kendaraanId);
            int result = pstmt.executeUpdate();
            return true; // Return true even if no rows are deleted
        }
    }
}