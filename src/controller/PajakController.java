package controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.PajakDAO;
import model.NotifikasiDAO;

public class PajakController {
    private PajakDAO pajakDAO = new PajakDAO();
    private NotifikasiDAO notifikasiDAO = new NotifikasiDAO();

    public void loadAllData(JTable table) {
        DefaultTableModel model = pajakDAO.getAllPajak();
        table.setModel(model);
    }

    public void filterStatus(JTable table, String status) {
        DefaultTableModel model = pajakDAO.getPajakByStatus(status);
        table.setModel(model);
    }

    public void kirimNotifikasiJatuhTempo() {
        notifikasiDAO.kirimPesanOtomatis();
        JOptionPane.showMessageDialog(null, "Notifikasi dikirim ke user yang hampir jatuh tempo.");
    }
}