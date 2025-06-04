package controller;

import model.KendaraanModel;
import java.sql.SQLException;
import java.util.List;

public class KendaraanController {
    private KendaraanModel model;

    public KendaraanController() {
        model = new KendaraanModel();
    }

    public List<Object[]> getAllKendaraan(int userId) throws SQLException {
        try {
            List<Object[]> kendaraanList = model.getKendaraanByUser(userId);
            if (kendaraanList.isEmpty()) {
                System.out.println("No kendaraan data found for userId: " + userId + " at " + new java.util.Date());
            } else {
                System.out.println("Retrieved " + kendaraanList.size() + " kendaraan records for userId: " + userId);
            }
            return kendaraanList;
        } catch (SQLException e) {
            System.out.println("SQL Error in getAllKendaraan for userId " + userId + ": " + e.getMessage());
            throw e;
        }
    }

    public Object[] getKendaraanDetail(int kendaraanId, int userId) throws SQLException {
        try {
            Object[] kendaraan = model.getKendaraanById(kendaraanId, userId);
            if (kendaraan == null) {
                System.out.println("No kendaraan data found for kendaraanId: " + kendaraanId + ", userId: " + userId);
            } else {
                System.out.println("Retrieved kendaraan detail for kendaraanId: " + kendaraanId + ", userId: " + userId);
            }
            return kendaraan;
        } catch (SQLException e) {
            System.out.println("SQL Error in getKendaraanDetail for kendaraanId " + kendaraanId + ", userId " + userId + ": " + e.getMessage());
            throw e;
        }
    }

    public boolean update(int kendaraanId, int userId, String nomorPolisi, String merk, String jenis, int tahun, double harga, String cc) throws SQLException {
        try {
            boolean result = model.updateKendaraan(kendaraanId, userId, nomorPolisi, merk, jenis, tahun, harga, cc);
            System.out.println("Update result for kendaraanId " + kendaraanId + ": " + result);
            return result;
        } catch (SQLException e) {
            System.out.println("SQL Error in update for kendaraanId " + kendaraanId + ": " + e.getMessage());
            throw e;
        }
    }

    public boolean delete(int kendaraanId, int userId) {
        try {
            boolean result = model.deleteKendaraan(kendaraanId, userId);
            System.out.println("Delete result for kendaraanId " + kendaraanId + ": " + result);
            return result;
        } catch (SQLException e) {
            System.out.println("SQL Error in delete for kendaraanId " + kendaraanId + ": " + e.getMessage());
            return false;
        }
    }
}