package view;

import controller.BayarPajakController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class BayarPajakForm extends JFrame {
    private int userId;
    private JTable pajakTable;
    private DefaultTableModel tableModel;
    private JButton bayarBtn;
    private JTextArea keteranganArea;
    private BayarPajakController controller;

    public BayarPajakForm(int userId) {
        this.userId = userId;
        try {
            controller = new BayarPajakController();
            initComponents();
            loadPajakData();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Koneksi gagal: " + e.getMessage());
        }
    }

    private void initComponents() {
        setTitle("Bayar Pajak Kendaraan");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel headerLabel = new JLabel("Daftar Pajak yang Belum Dibayar", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Sesuaikan kolom dengan data yang dikembalikan oleh BayarPajakController
        String[] columnNames = {"ID Pajak", "ID Kendaraan", "Nomor Polisi", "Jumlah Pajak", "Denda", "Total Bayar", "Jatuh Tempo"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        pajakTable = new JTable(tableModel);
        pajakTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(pajakTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Keterangan
        gbc.gridx = 0; gbc.gridy = 0;
        bottomPanel.add(new JLabel("Keterangan:"), gbc);
        gbc.gridx = 1;
        keteranganArea = new JTextArea(3, 20);
        keteranganArea.setBorder(BorderFactory.createLoweredBevelBorder());
        bottomPanel.add(new JScrollPane(keteranganArea), gbc);

        // Tombol
        JPanel buttonPanel = new JPanel(new FlowLayout());
        bayarBtn = new JButton("Bayar Pajak");
        JButton refreshBtn = new JButton("Refresh");
        JButton kembaliBtn = new JButton("Kembali");

        bayarBtn.addActionListener(e -> bayarPajak());
        refreshBtn.addActionListener(e -> loadPajakData());
        kembaliBtn.addActionListener(e -> kembaliToDashboard());

        buttonPanel.add(bayarBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(kembaliBtn);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        bottomPanel.add(buttonPanel, gbc);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH); // Perbaikan: Gunakan bottomPanel
        add(mainPanel);
    }

    private void loadPajakData() {
        try {
            tableModel.setRowCount(0);
            List<Object[]> data = controller.getDataPajak(userId);
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

            for (Object[] row : data) {
                tableModel.addRow(new Object[]{
                    row[0], // ID Pajak
                    row[1], // ID Kendaraan
                    row[2], // Nomor Polisi
                    currencyFormat.format((double) row[3]), // Jumlah Pajak
                    currencyFormat.format((double) row[4]), // Denda
                    currencyFormat.format((double) row[5]), // Total Bayar
                    row[6]  // Jatuh Tempo
                });
            }

            bayarBtn.setEnabled(tableModel.getRowCount() > 0);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal load data: " + e.getMessage());
        }
    }

    private void bayarPajak() {
        int selectedRow = pajakTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih pajak terlebih dahulu!");
            return;
        }

        int pajakId = (int) tableModel.getValueAt(selectedRow, 0);
        String keterangan = keteranganArea.getText().trim();

        try {
            if (controller.prosesBayar(pajakId, "Tunai", keterangan)) {
                JOptionPane.showMessageDialog(this, "Pembayaran berhasil!");
                loadPajakData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal melakukan pembayaran.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void clearForm() {
        keteranganArea.setText("");
        pajakTable.clearSelection();
    }

    private void kembaliToDashboard() {
        try {
            new PembayarDashboard().setVisible(true); // Gunakan userId
            this.dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: Tidak dapat membuka PembayarDashboard. " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}