package view;

import controller.KendaraanController;
import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class EditKendaraanDetailForm extends JFrame {
    private int userId, kendaraanId;
    private JTextField nomorPolisiField, merkField, jenisField, tahunField, hargaField;
    private JComboBox<String> ccComboBox;
    private KendaraanController controller;

    public EditKendaraanDetailForm(int userId, int kendaraanId) {
        this.userId = userId;
        this.kendaraanId = kendaraanId;
        controller = new KendaraanController();
        initComponents();
        loadKendaraanDetail();
    }

    private void initComponents() {
        setTitle("Edit Detail Kendaraan");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        nomorPolisiField = addLabeledField(panel, gbc, "Nomor Polisi:", 0);
        merkField = addLabeledField(panel, gbc, "Merk:", 1);
        jenisField = addLabeledField(panel, gbc, "Jenis:", 2);
        tahunField = addLabeledField(panel, gbc, "Tahun:", 3);
        hargaField = addLabeledField(panel, gbc, "Harga:", 4);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("CC:"), gbc);
        gbc.gridx = 1;
        ccComboBox = new JComboBox<>(new String[]{"50-250", ">250"});
        panel.add(ccComboBox, gbc);

        JPanel buttonPanel = new JPanel();
        JButton updateBtn = new JButton("Update");
        JButton kembaliBtn = new JButton("Kembali");

        updateBtn.addActionListener(e -> updateKendaraan());
        kembaliBtn.addActionListener(e -> kembali());

        buttonPanel.add(updateBtn);
        buttonPanel.add(kembaliBtn);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        add(panel);
    }

    private JTextField addLabeledField(JPanel panel, GridBagConstraints gbc, String label, int y) {
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        JTextField field = new JTextField(15);
        panel.add(field, gbc);
        return field;
    }

    private void loadKendaraanDetail() {
        try {
            Object[] kendaraan = controller.getKendaraanDetail(kendaraanId, userId);
            if (kendaraan == null) {
                JOptionPane.showMessageDialog(this, "Data kendaraan tidak ditemukan untuk kendaraan ID: " + kendaraanId + " dan user ID: " + userId,
                                              "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            nomorPolisiField.setText(kendaraan[0] != null ? kendaraan[0].toString() : "");
            merkField.setText(kendaraan[1] != null ? kendaraan[1].toString() : "");
            jenisField.setText(kendaraan[2] != null ? kendaraan[2].toString() : "");
            tahunField.setText(kendaraan[3] != null ? kendaraan[3].toString() : "");
            hargaField.setText(kendaraan[4] != null ? kendaraan[4].toString() : "");
            ccComboBox.setSelectedItem(kendaraan[5] != null ? kendaraan[5].toString() : "50-250");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error memuat detail kendaraan: " + e.getMessage(),
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateKendaraan() {
        try {
            String nomorPol = nomorPolisiField.getText().trim();
            String merk = merkField.getText().trim();
            String jenis = jenisField.getText().trim();
            int tahun = Integer.parseInt(tahunField.getText().trim());
            double harga = Double.parseDouble(hargaField.getText().trim());
            String cc = ccComboBox.getSelectedItem().toString();

            boolean updated = controller.update(kendaraanId, userId, nomorPol, merk, jenis, tahun, harga, cc);
            if (updated) {
                JOptionPane.showMessageDialog(this, "Kendaraan berhasil diupdate.");
                kembali();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal update kendaraan.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Tahun dan Harga harus berupa angka!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error update: " + e.getMessage());
        }
    }

    private void kembali() {
        new EditKendaraanForm(userId).setVisible(true);
        this.dispose();
    }
}