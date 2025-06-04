package model;

import java.sql.*;
import java.util.*;

public class KendaraanModel {

    public List<Object[]> getKendaraanByUser(int userId) throws SQLException {
        List<Object[]> list = new ArrayList<>();
        Connection conn = KoneksidB.getKoneksi();
        String sql = "SELECT * FROM kendaraan WHERE user_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, userId);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            list.add(new Object[]{
                rs.getInt("id"),
                rs.getString("nomor_polisi"),
                rs.getString("merk"),
                rs.getString("jenis"),
                rs.getInt("tahun"),
                rs.getDouble("harga_kendaraan"),
                rs.getString("cc")
            });
        }

        rs.close();
        pstmt.close();
        conn.close();
        return list;
    }

    public Object[] getKendaraanById(int kendaraanId, int userId) throws SQLException {
        Connection conn = KoneksidB.getKoneksi();
        String sql = "SELECT * FROM kendaraan WHERE id = ? AND user_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, kendaraanId);
        pstmt.setInt(2, userId);
        ResultSet rs = pstmt.executeQuery();

        Object[] data = null;
        if (rs.next()) {
            data = new Object[]{
                rs.getString("nomor_polisi"),
                rs.getString("merk"),
                rs.getString("jenis"),
                rs.getInt("tahun"),
                rs.getDouble("harga_kendaraan"),
                rs.getString("cc")
            };
        }

        rs.close();
        pstmt.close();
        conn.close();
        return data;
    }

    public boolean updateKendaraan(int kendaraanId, int userId, String nomorPolisi, String merk, String jenis, int tahun, double harga, String cc) throws SQLException {
        Connection conn = KoneksidB.getKoneksi();
        String sql = "UPDATE kendaraan SET nomor_polisi=?, merk=?, jenis=?, tahun=?, harga_kendaraan=?, cc=? WHERE id=? AND user_id=?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, nomorPolisi);
        pstmt.setString(2, merk);
        pstmt.setString(3, jenis);
        pstmt.setInt(4, tahun);
        pstmt.setDouble(5, harga);
        pstmt.setString(6, cc);
        pstmt.setInt(7, kendaraanId);
        pstmt.setInt(8, userId);

        int result = pstmt.executeUpdate();
        pstmt.close();
        conn.close();
        return result > 0;
    }

    public boolean deleteKendaraan(int kendaraanId, int userId) throws SQLException {
        Connection conn = KoneksidB.getKoneksi();
        String sql = "DELETE FROM kendaraan WHERE id = ? AND user_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, kendaraanId);
        pstmt.setInt(2, userId);
        int result = pstmt.executeUpdate();
        pstmt.close();
        conn.close();
        return result > 0;
    }
} 