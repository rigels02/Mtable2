package mtable2.impoexpo.v1;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import mtable2.control.ITDControl;
import mtable2.model.MTable;
import mtable2.model.TableData;
import mtable2.model.TableInfo;

/**
 * This TManager is used as data manager between old Importer/ Exporter
 * and Data Controller ITDControl. Its purpose is to make data converting
 * from old to the new data models during data importing and from new to old model
 * during data exporting.
 * Importer/Exporter uses old text file format similar to CSV format. 
 * The new version of Importer/Exporter supports txt JSON file format.
 * 21Jul2017, Changed: convertToMtable()
 *      - Force to use dot '.' as decimal separator!
 * @author raitis
 * @version 1.2
 */
public class TManager {

    public boolean firstTime = true;
    public boolean noneTables = true;

    private List<MTable> tables;
    private Htable htable = null;
    private Mtable mtable = null;

    private boolean tModified = false;
    private boolean hModified = false;

    private List<MTable> inputTables;
    private long currentTableFileIdx;

    public TManager(List<MTable> input) {
        
        this.inputTables = input;
        htable = new Htable();
        mtable = new Mtable();
        tables = new ArrayList<>();
    }

    /**
     * Imported Tables are exposed by this method
     * @return 
     */
    public List<MTable> getMTables() {
        return tables;
    }

    void settModified(boolean tModified) {
        this.tModified = tModified;
    }

    void sethModified(boolean hModified) {
        this.hModified = hModified;
    }

    int getCurrentTableIdx() {
        if (currentTableFileIdx == -1) {
            return -1;
        }
        for (int i = 0; i < htable.getSize(); i++) {
            if (htable.getElmAt(i).Idx == currentTableFileIdx) {
                return i;
            }

        }
        return -1;
    }

    void setCurrentTableFileIdx(long Idx) {
        this.currentTableFileIdx = Idx;
    }

    void setMtable(Mtable mtable) {
        this.mtable = mtable;
    }

    Htable getHtable()  {
        // this.htable=  convertToHtable(ctrl.getTableInfoLst());
        return htable;
    }

    void saveHtable()  {
        //NOthing to do    
    }

    /**
     * Convert to MTable model and keep in Tmanager's memory
     *
     * @param mtable
     * @param htableIdx
     */
    void saveMtable(Mtable mtable, int htableIdx)  {
        MTable table = convertToMTable(mtable);
        composeTableInfo(table, htable.getElmAt(htableIdx));
        tables.add(table);

    }

    boolean isTitleExists(String title) {
        if (htable.getSize() == 0) {
            return false;
        }
        for (int i = 0; i < htable.getSize(); i++) {
            if (htable.getElmAt(i).title.compareTo(title) == 0) {
                return true;
            }

        }
        return false;
    }

    /**
     * Called by Exporter.
     * calls data controller method to get data. 
     * @param mtable data loaded from dataController
     * @param tableIdx table file Index
     * @throws IOException
     * @throws Exception 
     */
    void loadMtable(Mtable mtable, long tableIdx) throws Exception{
        checkTablesIds();
        for (MTable inputTable : inputTables) {
            if (inputTable.getId() == tableIdx) {
                 convertToMtable(mtable, inputTable.getData());
            }
        
           
        }
    }

     private void checkTablesIds() {
         if(inputTables == null) throw new RuntimeException("inputTables == NULL!");
         Set<Long> ids= new HashSet();
       //for different tables ids must be different
         for (MTable table : inputTables) {
            if( ! ids.contains(table.getId()) ){
              ids.add(table.getId());
            }else{
             throw new RuntimeException("Internal Inconsistency: Different tables must have different Id!");
            }
         }
     }
     
    private Htable convertToHtable(List<TableInfo> tableInfoLst) {
        Htable nhtable = new Htable();
        for (TableInfo tableInfo : tableInfoLst) {
            nhtable.hlist.add(new HRec(tableInfo.getId(), tableInfo.getName()));
        }
        return nhtable;
    }

    /**
     * CHanges : Forced to use '.' as decimal separator 
     * @param mtable
     * @param list
     * @throws Exception 
     */
    private void convertToMtable(Mtable mtable, List<TableData> list) throws Exception {
        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
        //Warning !!! separator depends from Locale config
        //Force to use dot '.' as decimal separator!!!
        DecimalFormatSymbols mySymbols = new DecimalFormatSymbols();
        mySymbols.setDecimalSeparator('.');
        
        DecimalFormat df = new DecimalFormat("#.##",mySymbols);
        //df.setGroupingUsed(false);
        
        //Warning !!! separator depends from Locale Config
        //System.out.println("Decimal separator: "+df.getDecimalFormatSymbols().getDecimalSeparator());
        for (TableData tableData : list) {
            MRec rec = new MRec(sf.format(tableData.getCdate()),
                    tableData.getCat(),
                    df.format(tableData.getAmount()), tableData.getNote());
            mtable.addElm(rec);
        }

    }

    private MTable convertToMTable(Mtable mtable) {
        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();

        MTable ntable = new MTable();

        for (MRec rec : mtable.getMlist()) {
            TableData row = new TableData();
            cal.set(rec.yy, rec.mm-1, rec.dd);
            Date date = cal.getTime();
            row.setCdate(date);
            row.setCat(rec.getCat());
            row.setAmount(Double.parseDouble(rec.getAmount()));
            row.setNote(rec.getNote());
            ntable.getData().add(row);
        }

        return ntable;
    }

    private void composeTableInfo(MTable table, HRec helm) {
        table.setId(helm.Idx + 1);
        table.setName(helm.getTitle());
        //there is no date kept in HRec
        table.setModTime(null);
    }

    /**
     * Called by Exporter.
     * Calls Data Controller method to get data
     * @return
     * @throws Exception 
     */
    Htable loadHtable()  {
        if(inputTables == null) throw new RuntimeException("inputTables == NULL!");
        List<TableInfo> infoLst = new ArrayList<>();
        for (MTable table : inputTables) {
            infoLst.add((TableInfo) table);
        }
        this.htable = convertToHtable(infoLst);
        return htable;
    }

   
}
