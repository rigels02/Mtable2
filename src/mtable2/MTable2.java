
package mtable2;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import mtable2.control.DataControl;
import mtable2.io.FileIOImpl;
import mtable2.io.FilePersistenceManager;
import mtable2.model.MTable;
import mtable2.ui.TablesList;

/**
 * V 1.0
 * @author raitis
 */
public class MTable2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TablesList.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>
        
 /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FileIOImpl<MTable> io = null;
                try {
                    io = new FileIOImpl<>();
                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(TablesList.class.getName()).log(Level.SEVERE, null, ex);
                }
                //io.deleteAll();
                FilePersistenceManager<MTable> pm = new FilePersistenceManager<>(io);
                DataControl ctrl = null;
                try {
                    ctrl = new DataControl(pm);
                } catch (Exception ex) {
                    Logger.getLogger(TablesList.class.getName()).log(Level.SEVERE, null, ex);
                }
                TablesList tv = new TablesList(ctrl);
                tv.setVisible(true);
                tv.getListAndUpdateView();
                tv.setTitle(tv.getTitle()+"   V"+TablesList.Version);
            }
        });

    }
    
}
