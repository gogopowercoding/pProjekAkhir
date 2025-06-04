package model;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import javax.swing.JOptionPane;

public class PajakDAO {
    public DefaultTableModel getAllPajak() {
        String[] columns = {"ID Kendaraan", "Nomor Polisi", "Merk", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        try (Connection conn = KoneksidB.getKoneksi()) {
            String query = "SELECT k.id, k.nomor_polisi, k.merk, COALESCE(p.status, 'BELUM_BAYAR') AS status " +
                          "FROM kendaraan k " +
                          "LEFT JOIN pajak p ON k.id = p.kendaraan_id";
            ResultSet rs = conn.createStatement().executeQuery(query);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nomor_polisi"),
                    rs.getString("merk"),
                    rs.getString("status")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal memuat data kendaraan: " + e.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
        return model;
    }

    public DefaultTableModel getPajakByStatus(String status) {
        String[] columns = {"ID Kendaraan", "Nomor Polisi", "Merk", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        try (Connection conn = KoneksidB.getKoneksi()) {
            String query;
            if (status.equals("SEMUA")) {
                query = "SELECT k.id, k.nomor_polisi, k.merk, COALESCE(p.status, 'BELUM_BAYAR') AS status " +
                        "FROM kendaraan k " +
                        "LEFT JOIN pajak p ON k.id = p.kendaraan_id";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nomor_polisi"),
                        rs.getString("merk"),
                        rs.getString("status")
                    });
                }
            } else {
                query = "SELECT k.id, k.nomor_polisi, k.merk, p.status " +
                        "FROM kendaraan k " +
                        "JOIN pajak p ON k.id = p.kendaraan_id " +
                        "WHERE p.status = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, status.replace(" ", "_").toUpperCase()); // Ubah "SUDAH BAYAR" menjadi "SUDAH_BAYAR"
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nomor_polisi"),
                        rs.getString("merk"),
                        rs.getString("status")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal memfilter data: " + e.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
        return model;
    }
}