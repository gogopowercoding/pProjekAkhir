package model;

import java.sql.*;

public class UserDAO {
    public static User login(String nik, String password) {
        try (Connection conn = KoneksidB.getKoneksi()) {
            String sql = "SELECT * FROM users WHERE nik = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nik);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User(
                    rs.getString("nik"),
                    rs.getString("nama"),
                    rs.getString("password"),
                    rs.getString("alamat"),
                    rs.getString("no_telp"),
                    rs.getString("role")
                );
                user.setId(rs.getInt("id"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
}

public static boolean register(User user) {
    try (Connection conn = KoneksidB.getKoneksi()) {

        // Cek apakah NIK sudah terdaftar
        String checkSql = "SELECT id FROM users WHERE nik = ?";
        PreparedStatement checkStmt = conn.prepareStatement(checkSql);
        checkStmt.setString(1, user.getNik());
        ResultSet rs = checkStmt.executeQuery();
        if (rs.next()) {
            // NIK sudah ada
            return false;
        }

        // Jika belum ada, lanjutkan insert
        String insertSql = "INSERT INTO users (nik, nama, password, alamat, no_telp, role) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement insertStmt = conn.prepareStatement(insertSql);
        insertStmt.setString(1, user.getNik());
        insertStmt.setString(2, user.getNama());
        insertStmt.setString(3, user.getPassword());
        insertStmt.setString(4, user.getAlamat());
        insertStmt.setString(5, user.getNoTelp());
        insertStmt.setString(6, user.getRole());

        return insertStmt.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}


}
