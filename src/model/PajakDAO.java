package model;

import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class PajakDAO {
    public DefaultTableModel getAllPajak() {
        String[] columns = {"Plat", "Merk", "Jatuh Tempo", "Status", "Total"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        try (Connection conn = KoneksidB.getKoneksi()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM pajak JOIN kendaraan ON pajak.kendaraan_id = kendaraan.id");
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("nomor_polisi"),
                    rs.getString("merk"),
                    rs.getDate("jatuh_tempo"),
                    rs.getString("status"),
                    rs.getDouble("total_bayar")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    public DefaultTableModel getPajakByStatus(String status) {
        if (status.equals("SEMUA")) return getAllPajak();

        String[] columns = {"Plat", "Merk", "Jatuh Tempo", "Status", "Total"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        try (Connection conn = KoneksidB.getKoneksi()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM pajak JOIN kendaraan ON pajak.kendaraan_id = kendaraan.id WHERE status = ?");
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("nomor_polisi"),
                    rs.getString("merk"),
                    rs.getDate("jatuh_tempo"),
                    rs.getString("status"),
                    rs.getDouble("total_bayar")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }
}