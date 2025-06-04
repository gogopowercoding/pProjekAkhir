package view;

import controller.KendaraanControllerTambah;
import model.KendaraanTambah;

import javax.swing.*;
import java.awt.*;

public class TambahKendaraanForm extends JFrame {
    private int userId;
    private JTextField nomorPolisiField, merkField, jenisField, tahunField, hargaField;
    private JComboBox<String> ccComboBox;
    private KendaraanControllerTambah controller = new KendaraanControllerTambah();

    public TambahKendaraanForm(int userId) {
        this.userId = userId;
        initComponents();
    }

    private void initComponents() {
        setTitle("Tambah Data Kendaraan");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel header = new JLabel("Tambah Data Kendaraan Baru");
        header.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(header, gbc);

        gbc.gridwidth = 1;

        int row = 1;

        // Nomor Polisi
        panel.add(new JLabel("Nomor Polisi:"), createGbc(0, row));
        nomorPolisiField = new JTextField(15);
        panel.add(nomorPolisiField, createGbc(1, row++));

        // Merk
        panel.add(new JLabel("Merk:"), createGbc(0, row));
        merkField = new JTextField(15);
        panel.add(merkField, createGbc(1, row++));

        // Jenis
        panel.add(new JLabel("Jenis:"), createGbc(0, row));
        jenisField = new JTextField(15);
        panel.add(jenisField, createGbc(1, row++));

        // Tahun
        panel.add(new JLabel("Tahun:"), createGbc(0, row));
        tahunField = new JTextField(15);
        panel.add(tahunField, createGbc(1, row++));

        // Harga
        panel.add(new JLabel("Harga Kendaraan:"), createGbc(0, row));
        hargaField = new JTextField(15);
        panel.add(hargaField, createGbc(1, row++));

        // CC
        panel.add(new JLabel("CC:"), createGbc(0, row));
        ccComboBox = new JComboBox<>(new String[]{"50-250", ">250"});
        panel.add(ccComboBox, createGbc(1, row++));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton simpanBtn = new JButton("Simpan");
        JButton kembaliBtn = new JButton("Kembali");

        simpanBtn.addActionListener(e -> simpanKendaraan());
        kembaliBtn.addActionListener(e -> kembaliToDashboard());

        buttonPanel.add(simpanBtn);
        buttonPanel.add(kembaliBtn);

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        add(panel);
    }

    private GridBagConstraints createGbc(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        return gbc;
    }

    private void simpanKendaraan() {
        try {
            String nomorPol = nomorPolisiField.getText().trim();
            String merk = merkField.getText().trim();
            String jenis = jenisField.getText().trim();
            String tahunStr = tahunField.getText().trim();
            String hargaStr = hargaField.getText().trim();

            if (nomorPol.isEmpty() || merk.isEmpty() || jenis.isEmpty() || tahunStr.isEmpty() || hargaStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
                return;
            }

            int tahun = Integer.parseInt(tahunStr);
            double harga = Double.parseDouble(hargaStr);

            KendaraanTambah kendaraan = new KendaraanTambah(
                userId, nomorPol, merk, jenis, tahun, harga,
                ccComboBox.getSelectedItem().toString()
            );

            boolean success = controller.tambahKendaraan(kendaraan); // perbaikan nama metode

            if (success) {
                JOptionPane.showMessageDialog(this, "Data kendaraan berhasil disimpan!");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan data kendaraan.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Tahun dan Harga harus berupa angka!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage());
        }
    }

    private void clearFields() {
        nomorPolisiField.setText("");
        merkField.setText("");
        jenisField.setText("");
        tahunField.setText("");
        hargaField.setText("");
        ccComboBox.setSelectedIndex(0);
    }

    private void kembaliToDashboard() {
        new AdminDashboard().setVisible(true);
        this.dispose();
    }
}
