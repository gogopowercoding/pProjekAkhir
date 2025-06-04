package controller;

import model.KendaraanModel;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class KendaraanController {
    private KendaraanModel model;
    

    public KendaraanController() {
        model = new KendaraanModel();
    }

    public List<Object[]> getAllKendaraan(int userId) throws SQLException {
        return model.getKendaraanByUser(userId);
    }

    public Object[] getKendaraanDetail(int kendaraanId, int userId) throws SQLException {
        return model.getKendaraanById(kendaraanId, userId);
    }

    public boolean update(int kendaraanId, int userId, String nomorPolisi, String merk, String jenis, int tahun, double harga, String cc) throws SQLException {
        return model.updateKendaraan(kendaraanId, userId, nomorPolisi, merk, jenis, tahun, harga, cc);
    }

    
    
        public boolean delete(int kendaraanId, int userId) {
        try {
            return model.deleteKendaraan(kendaraanId, userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
 

}