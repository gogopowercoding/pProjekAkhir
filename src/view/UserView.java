package view;

import controller.UserController;
import model.User;

import javax.swing.*;
import java.awt.*;

public class UserView extends JFrame {
    private JTextField nikField;
    private JPasswordField passwordField;
    private UserController controller = new UserController();

    public UserView() {
        setTitle("Login Pengguna");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.add(new JLabel("NIK:"));
        nikField = new JTextField();
        panel.add(nikField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton loginBtn = new JButton("Login");
        loginBtn.addActionListener(e -> handleLogin());
        panel.add(loginBtn);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> new RegisterView().setVisible(true));
        panel.add(registerButton);

        add(panel);
    }

    private void handleLogin() {
    String nik = nikField.getText();
    String password = new String(passwordField.getPassword());

    User user = controller.login(nik, password);
    if (user != null) {
        JOptionPane.showMessageDialog(this, "Selamat datang, " + user.getNama());
        
        // Pengecekan role
        if ("admin".equalsIgnoreCase(user.getRole())) {
            new AdminDashboard().setVisible(true);
        } else {
            // Jika ada dashboard lain, misalnya untuk user biasa
            new PembayarDashboard().setVisible(true);
        }

        // Menutup form login
        dispose();
    } else {
        JOptionPane.showMessageDialog(this, "NIK atau password salah!");
    }
    }

}

