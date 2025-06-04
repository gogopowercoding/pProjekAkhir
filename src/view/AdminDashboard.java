package view;

import javax.swing.*;
import java.awt.*;
import controller.PajakController;

public class AdminDashboard extends JFrame {
    private JTable table;
    private JComboBox<String> filterCombo;
    private PajakController controller;

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        controller = new PajakController();
        initComponents();
    }

    private void initComponents() {
        JPanel topPanel = new JPanel();
        filterCombo = new JComboBox<>(new String[]{"SEMUA", "SUDAH BAYAR", "BELUM BAYAR"});
        filterCombo.addActionListener(e -> controller.filterStatus(table, (String) filterCombo.getSelectedItem()));
        topPanel.add(new JLabel("Filter Status: "));
        topPanel.add(filterCombo);

        table = new JTable();
        controller.loadAllData(table);

        JButton kirimPesanBtn = new JButton("Kirim Notifikasi Jatuh Tempo");
        kirimPesanBtn.addActionListener(e -> controller.kirimNotifikasiJatuhTempo());
        topPanel.add(kirimPesanBtn);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}
