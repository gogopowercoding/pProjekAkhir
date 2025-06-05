package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import controller.KendaraanController;

public class AdminDashboard extends JFrame {
    private JTable table;
    private JComboBox<String> filterCombo;
    private KendaraanController controller;

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        controller = new KendaraanController();
        initComponents();
    }

    private void initComponents() {
        JPanel topPanel = new JPanel();
        filterCombo = new JComboBox<>(new String[]{"SEMUA", "BELUM BAYAR", "SUDAH BAYAR"});
        filterCombo.addActionListener(e -> {
            try {
                loadKendaraanData((String) filterCombo.getSelectedItem());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal memfilter data: " + ex.getMessage());
            }
        });
        topPanel.add(new JLabel("Filter Status Pajak: "));
        topPanel.add(filterCombo);

        // Definisikan kolom tabel dengan status pajak
        String[] columnNames = {"ID Kendaraan", "Nomor Polisi", "Merk", "Status Pajak"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        try {
            loadKendaraanData("SEMUA"); // Muat semua data saat pertama kali
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage());
        }

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton tambahBtn = new JButton("Tambah Kendaraan");
        JButton editBtn = new JButton("Edit Kendaraan");
        JButton logoutBtn = new JButton("Logout");

        tambahBtn.addActionListener(e -> openTambahKendaraan());
        editBtn.addActionListener(e -> openEditKendaraan());
        logoutBtn.addActionListener(e -> logout());

        bottomPanel.add(tambahBtn);
        bottomPanel.add(editBtn);
        bottomPanel.add(logoutBtn);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadKendaraanData(String statusFilter) {
        try {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);
            int adminUserId = 1; // Gunakan user_id admin yang valid
            List<Object[]> kendaraanList = controller.getAllKendaraan(adminUserId, statusFilter);
            for (Object[] row : kendaraanList) {
                model.addRow(new Object[]{
                    row[0], // ID Kendaraan
                    row[1], // Nomor Polisi
                    row[2], // Merk
                    row[7] != null ? row[7].toString().replace("_", " ") : "BELUM BAYAR" // Status Pajak (ganti _ dengan spasi)
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(),
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openTambahKendaraan() {
        try {
            int adminUserId = 1; // Gunakan user_id admin yang valid
            TambahKendaraanForm form = new TambahKendaraanForm(adminUserId);
            form.setVisible(true);
            form.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent windowEvent) {
                    try {
                        loadKendaraanData((String) filterCombo.getSelectedItem()); // Segarkan tabel setelah form ditutup
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(AdminDashboard.this, "Gagal memuat ulang data: " + e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal membuka form tambah kendaraan: " + e.getMessage());
        }
    }

    private void openEditKendaraan() {
        try {
            int adminUserId = 1; // Gunakan user_id admin yang valid (sesuai dengan data di tabel users)
            EditKendaraanForm form = new EditKendaraanForm(adminUserId);
            form.setVisible(true);
            form.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent windowEvent) {
                    try {
                        loadKendaraanData((String) filterCombo.getSelectedItem()); // Segarkan tabel setelah form ditutup
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(AdminDashboard.this, "Gagal memuat ulang data: " + e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal membuka form edit kendaraan: " + e.getMessage());
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