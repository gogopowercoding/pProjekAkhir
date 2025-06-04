package controller;

import model.BayarPajakModel;
import java.sql.SQLException;
import java.util.List;

public class BayarPajakController {
    private BayarPajakModel model;

    public BayarPajakController() throws SQLException {
        model = new BayarPajakModel();
    }

    public List<Object[]> getDataPajak(int userId) throws SQLException {
        return model.getPajakBelumBayar(userId);
    }

    public boolean prosesBayar(int pajakId, String metode, String keterangan) throws SQLException {
        double total = model.getTotalBayar(pajakId);
        return model.bayarPajak(pajakId, total, metode, keterangan);
    }


}