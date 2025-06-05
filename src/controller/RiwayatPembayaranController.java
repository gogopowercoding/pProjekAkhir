package controller;

import model.RiwayatPembayaranModel;
import view.RiwayatPembayaranView;
import view.PembayarDashboard;
import javax.swing.*;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class RiwayatPembayaranController {
    private int userId;
    private RiwayatPembayaranModel model;
    private RiwayatPembayaranView view;

    public RiwayatPembayaranController(int userId) {
        this.userId = userId;
        try {
            this.model = new RiwayatPembayaranModel();
            this.view = new RiwayatPembayaranView();
        } catch (Exception e) {
            System.out.println("Error inisialisasi model atau view pada " + new Date() + ": " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Gagal menginisialisasi: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        loadRiwayatData();

        view.kembaliBtn.addActionListener(e -> kembaliToDashboard());

        view.setVisible(true);
    }

    private void loadRiwayatData() {
        if (model == null || view == null) {
            System.out.println("Model atau view belum diinisialisasi pada " + new Date());
            return;
        }

        try {
            List<RiwayatPembayaranModel.Riwayat> data = model.getRiwayatByUser(userId);
            view.tableModel.setRowCount(0);
            if (data.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Tidak ada riwayat pembayaran untuk user ID: " + userId + " pada " + new Date());
            } else {
                for (RiwayatPembayaranModel.Riwayat item : data) {
                    view.tableModel.addRow(new Object[]{
                        item.tanggal,
                        item.jumlah,
                        item.keterangan
                    });
                }
                System.out.println("Berhasil memuat " + data.size() + " riwayat pembayaran untuk userId: " + userId + " pada " + new Date());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Gagal memuat data: Terjadi kesalahan database - " + e.getMessage());
            System.out.println("SQL Error in loadRiwayatData untuk userId " + userId + " pada " + new Date() + ": " + e.getMessage());
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(view, "Gagal memuat data: Komponen tidak diinisialisasi dengan benar - " + e.getMessage());
            System.out.println("NullPointerException in loadRiwayatData untuk userId " + userId + " pada " + new Date() + ": " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Gagal memuat data: Error tidak terduga - " + e.getMessage());
            System.out.println("Unexpected error in loadRiwayatData untuk userId " + userId + " pada " + new Date() + ": " + e.getMessage());
        }
    }

    private void kembaliToDashboard() {
        try {
            view.dispose();
            PembayarDashboard dashboard = new PembayarDashboard();
            dashboard.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Gagal kembali ke dashboard: " + e.getMessage());
            System.out.println("Error in kembaliToDashboard untuk userId " + userId + " pada " + new Date() + ": " + e.getMessage());
        }
    }
}