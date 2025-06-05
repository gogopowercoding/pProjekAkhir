package controller;

import model.BayarPajakModel;
import java.sql.SQLException;
import java.util.List;
import java.util.Date;

public class BayarPajakController {
    private BayarPajakModel model;

    public BayarPajakController() throws SQLException {
        model = new BayarPajakModel();
    }

    public List<Object[]> getDataPajak(int userId) throws SQLException {
        try {
            List<Object[]> pajakList = model.getPajakBelumBayar(userId);
            if (pajakList.isEmpty()) {
                System.out.println("No unpaid pajak data found for userId: " + userId + " at " + new Date());
            } else {
                System.out.println("Retrieved " + pajakList.size() + " unpaid pajak records for userId: " + userId + " at " + new Date());
            }
            return pajakList;
        } catch (SQLException e) {
            System.out.println("SQL Error in getDataPajak for userId " + userId + ": " + e.getMessage());
            throw e;
        }
    }

    public boolean prosesBayar(int pajakId, String metode, String keterangan) throws SQLException {
        try {
            // Ambil total bayaran dan perbarui denda jika perlu
            double total = model.getTotalBayar(pajakId);
            // Proses pembayaran
            boolean success = model.bayarPajak(pajakId, total, metode, keterangan);
            if (success) {
                System.out.println("Pajak payment successful for pajakId: " + pajakId + " at " + new Date());
                // Optional: Buat entri pajak baru untuk periode berikutnya
                createNextPajakEntry(pajakId);
            } else {
                System.out.println("Pajak payment failed for pajakId: " + pajakId + " at " + new Date());
            }
            return success;
        } catch (SQLException e) {
            System.out.println("SQL Error in prosesBayar for pajakId " + pajakId + ": " + e.getMessage());
            throw e;
        }
    }

    private void createNextPajakEntry(int pajakId) throws SQLException {
        // Ambil informasi pajak saat ini untuk menentukan kendaraan_id dan jumlah_pajak
        Object[] pajakData = model.getPajakById(pajakId);
        if (pajakData == null) {
            System.out.println("Pajak data not found for pajakId: " + pajakId + " at " + new Date());
            return;
        }
        int kendaraanId = (int) pajakData[1]; // Asumsikan indeks 1 adalah kendaraan_id
        double jumlahPajak = (double) pajakData[2]; // Asumsikan indeks 2 adalah jumlah_pajak

        // Buat entri pajak baru untuk periode berikutnya (tahun berikutnya)
        model.createNewPajakEntry(kendaraanId, jumlahPajak, "2026-12-31"); // Jatuh tempo tahun berikutnya
        System.out.println("New pajak entry created for kendaraanId: " + kendaraanId + " for next period at " + new Date());
    }
}