package view;

import controller.UserController;
import model.User;

import javax.swing.*;
import java.awt.*;

public class RegisterView extends JFrame {
    private JTextField nikField, namaField, alamatField, telpField;
    private JPasswordField passwordField;
    private UserController controller = new UserController();

    public RegisterView() {
        setTitle("Registrasi Pengguna");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("NIK:")); nikField = new JTextField(); panel.add(nikField);
        panel.add(new JLabel("Nama:")); namaField = new JTextField(); panel.add(namaField);
        panel.add(new JLabel("Password:")); passwordField = new JPasswordField(); panel.add(passwordField);
        panel.add(new JLabel("Alamat:")); alamatField = new JTextField(); panel.add(alamatField);
        panel.add(new JLabel("No. Telp:")); telpField = new JTextField(); panel.add(telpField);
        

        JButton daftarBtn = new JButton("Daftar");
        daftarBtn.addActionListener(e -> handleRegister());
        panel.add(daftarBtn);

        add(panel);
    }

    private void handleRegister() {
        String nik = nikField.getText();
        String nama = namaField.getText();
        String pass = new String(passwordField.getPassword());
        String alamat = alamatField.getText();
        String telp = telpField.getText();

        if (nik.isEmpty() || nama.isEmpty() || pass.isEmpty() || alamat.isEmpty() || telp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua data wajib diisi!");
            return;
        }

        if (!nik.matches("\\d{16}")) {
            JOptionPane.showMessageDialog(this, "NIK harus terdiri dari 16 digit angka!");
            return;
        }

        if (pass.length() < 6) {
        JOptionPane.showMessageDialog(this, "Password minimal 6 karakter!");
        return;
        }

        
        if (telp.length() < 10) {
        JOptionPane.showMessageDialog(this, "Nomor Telepon minimal 10 karakter!");
        return;
        }

        User user = new User(nik, nama, pass, alamat, telp, "pembayar");
        if (controller.register(user)) {
            JOptionPane.showMessageDialog(this, "Registrasi berhasil!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal registrasi! Periksa kembali data.");
        }

    }
}
