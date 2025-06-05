package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class RiwayatPembayaranView extends JFrame {
    public JTable table;
    public DefaultTableModel tableModel;
    public JButton kembaliBtn;

    public RiwayatPembayaranView() {
        setTitle("Riwayat Pembayaran");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel header = new JLabel("Riwayat Pembayaran", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(header, BorderLayout.NORTH);

        String[] columnNames = {"Tanggal", "Jumlah", "Keterangan"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        kembaliBtn = new JButton("Kembali");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(kembaliBtn);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
}