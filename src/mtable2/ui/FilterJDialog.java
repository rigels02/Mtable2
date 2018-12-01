/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mtable2.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import mtable2.model.TableData;
import mtable2.tools.TableDataFilter;

/**
 *
 * @author raitis
 */
public class FilterJDialog extends javax.swing.JDialog {
private final static String ANY="ANY";

    private TableDataFilter filter;
    private IFilterJDialog callBack;
    private SimpleDateFormat sf;
    private boolean enabledActions;
    

    /**
     * Creates new form FilterJDialog
     * @param parent
     * @param modal
     * @param filter
     * @param callBack
     */
    public FilterJDialog(java.awt.Frame parent, boolean modal,TableDataFilter filter, IFilterJDialog callBack) {
        super(parent, modal);
         if(filter==null ) throw new RuntimeException("Filter == NULL!");
        initComponents();
        
        sf = new SimpleDateFormat("dd/MM/yyyy");
        this.filter= filter;
        this.callBack = callBack;
        //while setting Controls disable actions (comboBox,CheckBox actions...)
        enabledActions= false;
        setControlsFromFilter();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtStartDate = new javax.swing.JTextField();
        btnStartDate = new javax.swing.JButton();
        txtEndDate = new javax.swing.JTextField();
        btnEndDate = new javax.swing.JButton();
        cbCat = new javax.swing.JComboBox<>();
        cbNotes = new javax.swing.JComboBox<>();
        btnDoFilter = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        chkStartDate = new javax.swing.JCheckBox();
        chkEndDate = new javax.swing.JCheckBox();
        chkCat = new javax.swing.JCheckBox();
        chkNotes = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Filter Settings");

        txtStartDate.setEditable(false);
        txtStartDate.setText("ANY");
        txtStartDate.setToolTipText("Selected Start Date");

        btnStartDate.setText("Start Date");
        btnStartDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartDateActionPerformed(evt);
            }
        });

        txtEndDate.setEditable(false);
        txtEndDate.setText("ANY");
        txtEndDate.setToolTipText("Selected End Date");

        btnEndDate.setText("End Date");
        btnEndDate.setMaximumSize(new java.awt.Dimension(103, 29));
        btnEndDate.setMinimumSize(new java.awt.Dimension(103, 29));
        btnEndDate.setPreferredSize(new java.awt.Dimension(103, 29));
        btnEndDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEndDateActionPerformed(evt);
            }
        });

        cbCat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ANY", " " }));
        cbCat.setToolTipText("Select Category");
        cbCat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbCatActionPerformed(evt);
            }
        });

        cbNotes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ANY", " " }));
        cbNotes.setToolTipText("Select Note");
        cbNotes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbNotesActionPerformed(evt);
            }
        });

        btnDoFilter.setText("Make Filter");
        btnDoFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDoFilterActionPerformed(evt);
            }
        });

        btnCancel.setText("FilterOff");
        btnCancel.setPreferredSize(new java.awt.Dimension(109, 29));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        chkStartDate.setSelected(true);
        chkStartDate.setText("Any");
        chkStartDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkStartDateActionPerformed(evt);
            }
        });

        chkEndDate.setSelected(true);
        chkEndDate.setText("Any");
        chkEndDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkEndDateActionPerformed(evt);
            }
        });

        chkCat.setSelected(true);
        chkCat.setText("Any");
        chkCat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCatActionPerformed(evt);
            }
        });

        chkNotes.setSelected(true);
        chkNotes.setText("Any");
        chkNotes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkNotesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(cbNotes, javax.swing.GroupLayout.Alignment.LEADING, 0, 136, Short.MAX_VALUE)
                                .addComponent(cbCat, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtStartDate, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                                    .addComponent(txtEndDate))
                                .addGap(47, 47, 47)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnStartDate)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(chkStartDate))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(chkCat)
                                            .addComponent(chkEndDate)
                                            .addComponent(chkNotes)))))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addComponent(btnDoFilter)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(65, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnStartDate)
                    .addComponent(chkStartDate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkEndDate))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbCat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkCat))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbNotes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkNotes))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 71, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDoFilter)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnStartDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartDateActionPerformed
       
        Date date = new DatePicker(null, 0, 0).setPickedDateAsDate();
        //System.out.println("Start Date: "+date);
        txtStartDate.setText(sf.format(date));
        filter.setStartDate(date);
    }//GEN-LAST:event_btnStartDateActionPerformed

    private void btnEndDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEndDateActionPerformed
        Date date = new DatePicker(null, 0, 0).setPickedDateAsDate();
       
        txtEndDate.setText(sf.format(date));
        filter.setEndDate(date);
    }//GEN-LAST:event_btnEndDateActionPerformed

    private void btnDoFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoFilterActionPerformed
        System.out.println(""+filter);
        System.out.println("Filter: "+filter.statusStr());
        //System.out.println("Result: "+filter.filter());
        if(callBack!=null){
         callBack.doFilter();
        }
    }//GEN-LAST:event_btnDoFilterActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        this.dispose();
        if(callBack!=null){
         callBack.filterOFF();
        }
        
    }//GEN-LAST:event_btnCancelActionPerformed

    private void cbCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbCatActionPerformed
        //this event is generated when comboBox changed: item added etc
        //therefore enabledActions is needed to disable unwanted data change
       if(enabledActions){ 
            System.out.println("selected: "+cbCat.getSelectedItem());
       filter.setCategory((String) cbCat.getSelectedItem());
       }
    }//GEN-LAST:event_cbCatActionPerformed

    private void chkStartDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkStartDateActionPerformed
        System.out.println("mtable2.ui.FilterJDialog.chkStartDateActionPerformed()"+chkStartDate.isSelected());
      if(chkStartDate.isSelected()){
        setStartDateAny();
      } else {
       setStartDateNotAny();
      }
       
    }//GEN-LAST:event_chkStartDateActionPerformed

    private void chkEndDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkEndDateActionPerformed
        if(chkEndDate.isSelected()){
        setEndDateAny();
      } else {
       setEndDateNotAny();
      }
    }//GEN-LAST:event_chkEndDateActionPerformed

    private void cbNotesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbNotesActionPerformed
        //this event is generated when comboBox changed: item added etc
        //therefore enabledActions is needed to disable unwanted data change
        if(enabledActions){
            System.out.println("mtable2.ui.FilterJDialog.cbNotesActionPerformed()");
            filter.setNote((String) cbNotes.getSelectedItem());
        }
    }//GEN-LAST:event_cbNotesActionPerformed

    private void chkCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCatActionPerformed
       if(chkCat.isSelected()){
        setCatAny();
       }else{
        setCatNotAny();
        filter.setCategory((String) cbCat.getSelectedItem());
       }
    }//GEN-LAST:event_chkCatActionPerformed

    private void chkNotesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkNotesActionPerformed
        if(chkNotes.isSelected()){
        setNoteAny();
        }else{
            setNoteNotAny();
            filter.setNote((String) cbNotes.getSelectedItem());
        }
    }//GEN-LAST:event_chkNotesActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FilterJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FilterJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FilterJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FilterJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        //-------------------------------------
                 List<TableData> data = new ArrayList<>();

                data.add(new TableData(new Date(117, 2, 14), "Cat1", 1.0, "Note1"));
                data.add(new TableData(new Date(117, 2, 15), "Cat2", 2.0, "Note2"));
                data.add(new TableData(new Date(117, 2, 16), "Cat3", 3.0, "Note3"));
                data.add(new TableData(new Date(117, 2, 17), "Cat4", 4.0, "Note4"));
                TableDataFilter mfilter = new TableDataFilter(null, null, null, null, data);
       
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FilterJDialog dialog = new FilterJDialog(new javax.swing.JFrame(), true, mfilter, null);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnDoFilter;
    private javax.swing.JButton btnEndDate;
    private javax.swing.JButton btnStartDate;
    private javax.swing.JComboBox<String> cbCat;
    private javax.swing.JComboBox<String> cbNotes;
    private javax.swing.JCheckBox chkCat;
    private javax.swing.JCheckBox chkEndDate;
    private javax.swing.JCheckBox chkNotes;
    private javax.swing.JCheckBox chkStartDate;
    private javax.swing.JTextField txtEndDate;
    private javax.swing.JTextField txtStartDate;
    // End of variables declaration//GEN-END:variables

private void setStartDateFromFilter(){
  if(filter.getStartDate()==null){
    txtStartDate.setText(ANY);
    btnStartDate.setEnabled(false);
    chkStartDate.setSelected(true);
    return;
  }
  txtStartDate.setText(sf.format(filter.getStartDate()));
  btnStartDate.setEnabled(true);
  chkStartDate.setSelected(false);
}
private void setEndDateFromFilter(){
  if(filter.getEndDate()==null){
    txtEndDate.setText(ANY);
    btnEndDate.setEnabled(false);
    chkEndDate.setSelected(true);
    return;
  }
  txtEndDate.setText(sf.format(filter.getEndDate()));
  btnEndDate.setEnabled(true);
  chkEndDate.setSelected(false);
}


    private void setCatFromFilter() {
        List<String> _mcategories = filter.getCategories();
        cbCat.removeAllItems();
        for (String el : _mcategories) {
            cbCat.addItem(el);
        }
        if(filter.getCategory()==null){
         cbCat.setEnabled(false);
         chkCat.setSelected(true);
         return;
        }
        cbCat.setEnabled(true);
        chkCat.setSelected(false);
        cbCat.setSelectedItem(filter.getCategory());
    }
    private void setNotesFromFilter() {
        List<String> notes = filter.getNotes();
        cbNotes.removeAllItems();
        for (String el : notes) {
            cbNotes.addItem(el);
        }
      if(filter.getNote()==null){ //ANY
         cbNotes.setEnabled(false);
         chkNotes.setSelected(true);
         return;
        }
        cbNotes.setEnabled(true);
        chkNotes.setSelected(false);
        cbNotes.setSelectedItem(filter.getNote());
    }
    private void setControlsFromFilter(){
      setStartDateFromFilter();
      setEndDateFromFilter();
      setCatFromFilter();
      setNotesFromFilter();
      enabledActions = true;
    }
    private void setStartDateNotAny() {

        //txtStartDate.setText(sf.format(filter.getStartDate()));
        btnStartDate.setEnabled(true);

    }
    private void setEndDateNotAny() {

        //txtStartDate.setText(sf.format(filter.getStartDate()));
        btnEndDate.setEnabled(true);

    }
    private boolean isStartDateValidState(){
     return chkStartDate.isSelected() && txtStartDate.getText().contains(ANY);
    }
    private boolean isEndDateValidState(){
     return chkEndDate.isSelected() && txtEndDate.getText().contains(ANY);
    }

    private void setStartDateAny() {
      btnStartDate.setEnabled(false);
      txtStartDate.setText(ANY);
      filter.setStartDate(null);
    }
    private void setEndDateAny(){
     btnEndDate.setEnabled(false);
     txtEndDate.setText(ANY);
     filter.setEndDate(null);
    }
    private void setCatAny(){
     cbCat.setEnabled(false);
     filter.setCategory(null);
    }
    private void setCatNotAny(){
     cbCat.setEnabled(true);
    }
    
    private void setNoteAny(){
     cbNotes.setEnabled(false);
     filter.setNote(null);
    }
    private void setNoteNotAny(){
     cbNotes.setEnabled(true);
    }
}
