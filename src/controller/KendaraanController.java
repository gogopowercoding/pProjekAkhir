package controller;

import model.KendaraanModel;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import model.BayarPajakModel;

public class KendaraanController {
    private KendaraanModel model;

    public KendaraanController() {
        model = new KendaraanModel();
    }

    public List<Object[]> getAllKendaraan(int userId, String statusFilter) throws SQLException {
        try {
            List<Object[]> kendaraanList;
            if (userId == 1) { // Admin (userId = 1) dapat melihat semua kendaraan dengan filter status
                kendaraanList = model.getKendaraanByStatus(statusFilter);
            } else {
                kendaraanList = model.getKendaraanByUser(userId); // Pengguna biasa hanya lihat kendaraan sendiri
            }
            if (kendaraanList.isEmpty()) {
                System.out.println("No kendaraan data found for userId: " + userId + " with status: " + statusFilter + " at " + new java.util.Date());
            } else {
                System.out.println("Retrieved " + kendaraanList.size() + " kendaraan records for userId: " + userId + " with status: " + statusFilter);
            }
            return kendaraanList;
        } catch (SQLException e) {
            System.out.println("SQL Error in getAllKendaraan for userId " + userId + " with status " + statusFilter + ": " + e.getMessage());
            throw e;
        }
    }

    public Object[] getKendaraanDetail(int kendaraanId, int userId) throws SQLException {
        try {
            Object[] kendaraan;
            if (userId == 1) { // Admin (userId = 1) dapat mengakses semua kendaraan
                kendaraan = model.getKendaraanById(kendaraanId);
            } else {
                kendaraan = model.getKendaraanByIdAndUser(kendaraanId, userId);
            }
            if (kendaraan == null) {
                System.out.println("No kendaraan data found for kendaraanId: " + kendaraanId + ", userId: " + userId);
            } else {
                System.out.println("Retrieved kendaraan detail for kendaraanId " + kendaraanId + ", userId: " + userId);
            }
            return kendaraan;
        } catch (SQLException e) {
            System.out.println("SQL Error in getKendaraanDetail for kendaraanId " + kendaraanId + ", userId " + userId + ": " + e.getMessage());
            throw e;
        }
    }

    public boolean update(int kendaraanId, int userId, String nomorPolisi, String merk, String jenis, int tahun, double harga, String cc) throws SQLException {
        try {
            boolean result;
            if (userId == 1) { // Admin dapat memperbarui semua kendaraan
                result = model.updateKendaraan(kendaraanId, nomorPolisi, merk, jenis, tahun, harga, cc);
            } else {
                result = model.updateKendaraan(kendaraanId, userId, nomorPolisi, merk, jenis, tahun, harga, cc);
            }
            System.out.println("Update result for kendaraanId " + kendaraanId + ": " + result);
            return result;
        } catch (SQLException e) {
            System.out.println("SQL Error in update for kendaraanId " + kendaraanId + ": " + e.getMessage());
            throw e;
        }
    }

    public boolean delete(int kendaraanId, int userId) throws SQLException {
        try {
            // Cek apakah ada entri pajak terkait
            List<Object[]> pajakList = new BayarPajakModel().getPajakBelumBayarByKendaraanId(kendaraanId);
            if (!pajakList.isEmpty()) {
                int confirm = JOptionPane.showConfirmDialog(null,
                    "Kendaraan ini memiliki " + pajakList.size() + " entri pajak yang belum dibayar. Hapus juga entri pajak?",
                    "Konfirmasi Penghapusan",
                    JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) {
                    return false;
                }
            }

            // Hapus entri pajak terkait
            boolean pajakDeleted = deletePajakByKendaraanId(kendaraanId, userId);
            if (!pajakDeleted && userId != 1) {
                System.out.println("No pajak entries to delete for kendaraanId: " + kendaraanId + ", userId: " + userId);
            }

            // Hapus kendaraan
            boolean result;
            if (userId == 1) {
                result = model.deleteKendaraanAdmin(kendaraanId);
            } else {
                result = model.deleteKendaraan(kendaraanId, userId);
            }
            System.out.println("Delete result for kendaraanId " + kendaraanId + ": " + result);
            return result;
        } catch (SQLException e) {
            System.out.println("SQL Error in delete for kendaraanId " + kendaraanId + ": " + e.getMessage());
            throw e;
        }
}

    public boolean deletePajakByKendaraanId(int kendaraanId, int userId) throws SQLException {
        try {
            boolean result;
            if (userId == 1) {
                result = model.deletePajakByKendaraanIdAdmin(kendaraanId);
            } else {
                result = model.deletePajakByKendaraanId(kendaraanId, userId);
            }
            System.out.println("Delete pajak result for kendaraanId " + kendaraanId + ": " + result);
            return result;
        } catch (SQLException e) {
            System.out.println("SQL Error in deletePajakByKendaraanId for kendaraanId " + kendaraanId + ": " + e.getMessage());
            throw e;
        }
    }
}