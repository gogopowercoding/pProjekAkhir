package model;

import java.sql.*;

public class NotifikasiDAO {
    public void kirimPesanOtomatis() {
        String sql = "INSERT INTO notifikasi (id_user, pesan, tanggal_kirim, status_baca)\n" +
                     "SELECT u.id, CONCAT('Pajak kendaraan ', k.nomor_polisi, ' akan jatuh tempo!'), NOW(), 'belum'\n" +
                     "FROM pajak p\n" +
                     "JOIN kendaraan k ON p.kendaraan_id = k.id\n" +
                     "JOIN users u ON k.user_id = u.id\n" +
                     "WHERE DATEDIFF(p.jatuh_tempo, NOW()) <= 7 AND p.status = 'BELUM BAYAR'";

        try (Connection conn = KoneksidB.getKoneksi()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
