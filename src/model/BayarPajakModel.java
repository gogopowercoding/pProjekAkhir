package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BayarPajakModel {
    public List<Object[]> getPajakBelumBayar(int userId) throws SQLException {
        List<Object[]> list = new ArrayList<>();
        try (Connection conn = KoneksidB.getKoneksi();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT p.id, p.kendaraan_id, k.nomor_polisi, p.jumlah_pajak, p.denda, p.total_bayar, p.jatuh_tempo, p.status " +
                     "FROM pajak p " +
                     "JOIN kendaraan k ON p.kendaraan_id = k.id " +
                     "WHERE k.user_id = ? AND p.status = 'BELUM_BAYAR' " +
                     "GROUP BY p.kendaraan_id")) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Date jatuhTempo = rs.getDate("jatuh_tempo");
                    double denda = calculateDenda(jatuhTempo, rs.getDouble("denda"));
                    double jumlahPajak = rs.getDouble("jumlah_pajak");
                    double totalBayar = jumlahPajak + denda;

                    updateDendaAndTotalBayar(rs.getInt("id"), denda, totalBayar);

                    Object[] row = new Object[]{
                        rs.getInt("id"),
                        rs.getInt("kendaraan_id"),
                        rs.getString("nomor_polisi"),
                        jumlahPajak,
                        denda,
                        totalBayar,
                        jatuhTempo,
                        rs.getString("status")
                    };
                    list.add(row);
                }
            }
        }
        return list;
    }

    private double calculateDenda(Date jatuhTempo, double currentDenda) {
        if (jatuhTempo == null) return currentDenda;
        Date today = new Date(); // 08:20 PM WIB, 05 Juni 2025
        if (today.after(jatuhTempo)) {
            long diffInMillies = today.getTime() - jatuhTempo.getTime();
            long diffInDays = diffInMillies / (1000 * 60 * 60 * 24);
            return currentDenda + (diffInDays * 0.01 * 50000); // Denda 1% per hari
        }
        return currentDenda;
    }

    private void updateDendaAndTotalBayar(int pajakId, double denda, double totalBayar) throws SQLException {
        try (Connection conn = KoneksidB.getKoneksi();
             PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE pajak SET denda = ?, total_bayar = ? WHERE id = ?")) {
            pstmt.setDouble(1, denda);
            pstmt.setDouble(2, totalBayar);
            pstmt.setInt(3, pajakId);
            pstmt.executeUpdate();
        }
    }

    public double getTotalBayar(int pajakId) throws SQLException {
        try (Connection conn = KoneksidB.getKoneksi();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT total_bayar FROM pajak WHERE id = ?")) {
            pstmt.setInt(1, pajakId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total_bayar");
                }
            }
        }
        throw new SQLException("Pajak not found for pajakId: " + pajakId);
    }

    public boolean bayarPajak(int pajakId, double totalBayar, String metode, String keterangan) throws SQLException {
        try (Connection conn = KoneksidB.getKoneksi()) {
            conn.setAutoCommit(false);
            try {
                // Perbarui status pajak
                try (PreparedStatement pstmt1 = conn.prepareStatement(
                        "UPDATE pajak SET status = 'SUDAH_BAYAR' WHERE id = ? AND status = 'BELUM_BAYAR'")) {
                    pstmt1.setInt(1, pajakId);
                    int updated = pstmt1.executeUpdate();
                    if (updated == 0) {
                        conn.rollback();
                        return false;
                    }
                }
                // Tambahkan entri pembayaran
                try (PreparedStatement pstmt2 = conn.prepareStatement(
                        "INSERT INTO pembayaran (pajak_id, tanggal_bayar, jumlah_bayar, metode, keterangan) " +
                        "VALUES (?, NOW(), ?, ?, ?)")) {
                    pstmt2.setInt(1, pajakId);
                    pstmt2.setDouble(2, totalBayar);
                    pstmt2.setString(3, metode);
                    pstmt2.setString(4, keterangan);
                    pstmt2.executeUpdate();
                }
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public Object[] getPajakById(int pajakId) throws SQLException {
        try (Connection conn = KoneksidB.getKoneksi();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT * FROM pajak WHERE id = ?")) {
            pstmt.setInt(1, pajakId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Object[]{
                        rs.getInt("id"),
                        rs.getInt("kendaraan_id"),
                        rs.getDouble("jumlah_pajak"),
                        rs.getDouble("denda"),
                        rs.getDouble("total_bayar"),
                        rs.getDate("jatuh_tempo"),
                        rs.getString("status")
                    };
                }
            }
        }
        return null;
    }

    public void createNewPajakEntry(int kendaraanId, double jumlahPajak, String jatuhTempo) throws SQLException {
        try (Connection conn = KoneksidB.getKoneksi();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO pajak (kendaraan_id, jumlah_pajak, denda, total_bayar, jatuh_tempo, status) " +
                     "VALUES (?, ?, 0.00, ?, ?, 'BELUM_BAYAR')")) {
            pstmt.setInt(1, kendaraanId);
            pstmt.setDouble(2, jumlahPajak);
            pstmt.setDouble(3, jumlahPajak);
            pstmt.setString(4, jatuhTempo);
            pstmt.executeUpdate();
        }
    }

    public List<Object[]> getPajakBelumBayarByKendaraanId(int kendaraanId) throws SQLException {
        List<Object[]> list = new ArrayList<>();
        try (Connection conn = KoneksidB.getKoneksi();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT p.id, p.kendaraan_id, k.nomor_polisi, p.jumlah_pajak, p.denda, p.total_bayar, p.jatuh_tempo, p.status " +
                     "FROM pajak p " +
                     "JOIN kendaraan k ON p.kendaraan_id = k.id " +
                     "WHERE p.kendaraan_id = ? AND p.status = 'BELUM_BAYAR'")) {
            pstmt.setInt(1, kendaraanId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Object[]{
                        rs.getInt("id"),
                        rs.getInt("kendaraan_id"),
                        rs.getString("nomor_polisi"),
                        rs.getDouble("jumlah_pajak"),
                        rs.getDouble("denda"),
                        rs.getDouble("total_bayar"),
                        rs.getDate("jatuh_tempo"),
                        rs.getString("status")
                    });
                }
            }
        }
        return list;
    }
}