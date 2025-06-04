package model;

import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class PajakDAO {
    public DefaultTableModel getAllPajak() {
        String[] columns = {"NIK", "Plat", "Jatuh Tempo", "Status", "Total"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        try (Connection conn = KoneksidB.getKoneksi()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT u.nik, k.nomor_polisi, p.jatuh_tempo, p.status, p.total_bayar FROM pajak  p JOIN kendaraan k ON p.kendaraan_id = k.id JOIN users u ON u.id = k.user_id ");
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("nik"),
                    rs.getString("nomor_polisi"),
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

    /**
     * @param status
     * @return
     */
    public DefaultTableModel getPajakByStatus(String status) {
        if (status.equals("SEMUA")) return getAllPajak();

        String[] columns = {"NIK", "Plat", "Jatuh Tempo", "Status", "Total"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        try (Connection conn = KoneksidB.getKoneksi()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT u.nik, k.nomor_polisi, p.jatuh_tempo, p.status, p.total_bayar FROM pajak  p JOIN kendaraan k ON p.kendaraan_id = k.id JOIN users u ON u.id = k.user_id WHERE status = ?");
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("nik"),
                    rs.getString("nomor_polisi"),
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