package controller;

import model.RiwayatPembayaranModel;
import view.RiwayatPembayaranView;
import view.PembayarDashboard;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class RiwayatPembayaranController {
    private int userId;
    private RiwayatPembayaranModel model;
    private RiwayatPembayaranView view;

    public RiwayatPembayaranController(int userId) {
        this.userId = userId;
        this.model = new RiwayatPembayaranModel();
        this.view = new RiwayatPembayaranView();

        loadRiwayatData();

        view.kembaliBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                view.dispose();
                new PembayarDashboard().setVisible(true);
            }
        });

        view.setVisible(true);
    }

    private void loadRiwayatData() {
        try {
            List<RiwayatPembayaranModel.Riwayat> data = model.getRiwayatByUser(userId);
            view.tableModel.setRowCount(0);
            for (RiwayatPembayaranModel.Riwayat item : data) {
                view.tableModel.addRow(new Object[]{
                    item.tanggal,
                    item.jumlah,
                    item.keterangan
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Gagal memuat data: " + e.getMessage());
        }
    }
}
