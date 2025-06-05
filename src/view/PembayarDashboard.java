package view;

import controller.RiwayatPembayaranController;
import javax.swing.*;
import java.awt.*;


public class PembayarDashboard extends JFrame {
    private int userId;

    public PembayarDashboard() {
        this.userId = userId;
        initComponents();
    }

    private void initComponents() {
        setTitle("Dashboard Pembayar");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel header = new JLabel("Selamat datang di Dashboard Pembayar", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(header, BorderLayout.NORTH);

        JPanel menuPanel = new JPanel(new GridLayout(3, 1, 15, 15));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50));

        JButton bayarBtn = new JButton("Bayar Pajak");
        JButton riwayatBtn = new JButton("Riwayat Pembayaran");
        JButton logoutBtn = new JButton("Logout");

        Font buttonFont = new Font("Arial", Font.PLAIN, 12);
        bayarBtn.setFont(buttonFont);
        riwayatBtn.setFont(buttonFont);
        logoutBtn.setFont(buttonFont);

        bayarBtn.addActionListener(e -> openBayarPajak());
        riwayatBtn.addActionListener(e -> openRiwayatPembayaran());
        logoutBtn.addActionListener(e -> logout());

        menuPanel.add(bayarBtn);
        menuPanel.add(riwayatBtn);
        menuPanel.add(logoutBtn);

        mainPanel.add(menuPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void openBayarPajak() {
        try {
            BayarPajakForm bayarForm = new BayarPajakForm(userId);
            bayarForm.setVisible(true);
            this.dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error membuka form bayar pajak: " + e.getMessage());
        }
    }

    private void openRiwayatPembayaran() {
        try {
            new RiwayatPembayaranController(userId);
            this.dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error membuka riwayat pembayaran: " + e.getMessage());
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Apakah Anda yakin ingin logout?",
            "Konfirmasi Logout",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                new UserView().setVisible(true);
                this.dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error kembali ke login: " + e.getMessage());
            }
        }
    }
}