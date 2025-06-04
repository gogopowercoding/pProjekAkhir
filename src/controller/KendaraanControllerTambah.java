package controller;

import model.KendaraanTambah;
import model.KendaraanDAO;

public class KendaraanControllerTambah {
    private KendaraanDAO kendaraanDAO = new KendaraanDAO();

    public boolean tambahKendaraan(KendaraanTambah kendaraan) throws Exception {
        return kendaraanDAO.insert(kendaraan);
    }

}
