
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;


public class KoneksidB {
    private static final String URL = "jdbc:mysql://localhost:3306/perpajakan?useSSL=false";
    private static final String USER = "root";
    private static final String PASS = "";

    // Mendapatkan koneksi ke database
    public static Connection getKoneksi() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Koneksi database berhasil");
            return conn;
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                "Driver MySQL tidak ditemukan: " + e.getMessage(),
                "Error Driver", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Gagal koneksi ke database: " + e.getMessage(),
                "Error Koneksi", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
}
