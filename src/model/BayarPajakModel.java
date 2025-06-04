    package model;

    import java.sql.*;
    import java.util.ArrayList;
    import java.util.List;

    public class BayarPajakModel {
        private Connection conn;

        public BayarPajakModel() throws SQLException {
            conn = KoneksidB.getKoneksi();
        }

        public List<Object[]> getPajakBelumBayar(int userId) throws SQLException {
            List<Object[]> list = new ArrayList<>();
            String sql = "SELECT p.id, k.nomor_polisi, k.merk, p.jumlah_pajak, p.denda, p.total_bayar, p.jatuh_tempo " +
                         "FROM pajak p " +
                         "JOIN kendaraan k ON p.kendaraan_id = k.id " +
                         "WHERE k.user_id = ? AND p.status = 'BELUM_BAYAR'";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nomor_polisi"),
                    rs.getString("merk"),
                    rs.getDouble("jumlah_pajak"),
                    rs.getDouble("denda"),
                    rs.getDouble("total_bayar"),
                    rs.getDate("jatuh_tempo")
                });
            }
            return list;
        }

        public boolean bayarPajak(int pajakId, double totalBayar, String metode, String keterangan) throws SQLException {
            conn.setAutoCommit(false);

            String insert = "INSERT INTO pembayaran (pajak_id, tanggal_bayar, jumlah_bayar, metode, keterangan) VALUES (?, NOW(), ?, ?, ?)";
            String update = "UPDATE pajak SET status = 'SUDAH_BAYAR' WHERE id = ?";

            try (PreparedStatement insertStmt = conn.prepareStatement(insert);
                 PreparedStatement updateStmt = conn.prepareStatement(update)) {
                insertStmt.setInt(1, pajakId);
                insertStmt.setDouble(2, totalBayar);
                insertStmt.setString(3, metode);
                insertStmt.setString(4, keterangan);
                insertStmt.executeUpdate();

                updateStmt.setInt(1, pajakId);
                updateStmt.executeUpdate();

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }

        public double getTotalBayar(int pajakId) throws SQLException {
            String sql = "SELECT total_bayar FROM pajak WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, pajakId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getDouble("total_bayar");
            return 0;
        }
    }