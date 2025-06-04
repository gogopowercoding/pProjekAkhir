package controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.PajakDAO;


public class PajakController {
    private PajakDAO pajakDAO = new PajakDAO();
    

    public void loadAllData(JTable table) {
        DefaultTableModel model = pajakDAO.getAllPajak();
        table.setModel(model);
    }

    public void filterStatus(JTable table, String status) {
        DefaultTableModel model = pajakDAO.getPajakByStatus(status);
        table.setModel(model);
    }

}