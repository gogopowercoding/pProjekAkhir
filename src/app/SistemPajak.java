
package app;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import view.UserView;

public class SistemPajak {

     public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new UserView().setVisible(true));
    }
    
}
