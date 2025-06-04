package view;

import controller.KendaraanController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.sql.SQLException; // Tambahkan impor ini

public class EditKendaraanForm extends JFrame {
    private int userId;
    private JTable kendaraanTable;
    private DefaultTableModel tableModel;
    private KendaraanController controller;

    public EditKendaraanForm(int userId) {
        this.userId = userId;
        controller = new KendaraanController();
        initComponents();
        loadKendaraanData();
    }

    private void initComponents() {
        setTitle("Edit Data Kendaraan");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel headerLabel = new JLabel("Daftar Kendaraan Anda", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Nomor Polisi", "Merk", "Jenis", "Tahun", "Harga", "CC"};
        tableModel = new DefaultTableModel(columnNames, 0);
        kendaraanTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(kendaraanTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton editBtn = new JButton("Edit");
        JButton hapusBtn = new JButton("Hapus");
        JButton kembaliBtn = new JButton("Kembali");

        editBtn.addActionListener(e -> editKendaraan());
        hapusBtn.addActionListener(e -> hapusKendaraan());
        kembaliBtn.addActionListener(e -> kembaliToDashboard());

        buttonPanel.add(editBtn);
        buttonPanel.add(hapusBtn);
        buttonPanel.add(kembaliBtn);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadKendaraanData() {
        try {
            tableModel.setRowCount(0);
            List<Object[]> kendaraanList = controller.getAllKendaraan(userId);
            if (kendaraanList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Tidak ada data kendaraan untuk user ID: " + userId,
                                              "Informasi", JOptionPane.INFORMATION_MESSAGE);
            }
            for (Object[] row : kendaraanList) {
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal load data kendaraan: " + e.getMessage(),
                                          "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan tidak terduga: " + e.getMessage(),
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editKendaraan() {
        int selectedRow = kendaraanTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih kendaraan yang akan diedit!");
            return;
        }
        int kendaraanId = (int) tableModel.getValueAt(selectedRow, 0);
        new EditKendaraanDetailForm(userId, kendaraanId).setVisible(true);
        this.dispose();
    }

    private void hapusKendaraan() {
        int selectedRow = kendaraanTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih kendaraan yang akan dihapus!");
            return;
        }

        int kendaraanId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus kendaraan ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = controller.delete(kendaraanId, userId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Kendaraan berhasil dihapus!");
                loadKendaraanData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus kendaraan.");
            }
        }
    }

    private void kembaliToDashboard() {
        new AdminDashboard().setVisible(true);
        this.dispose();
    }
}