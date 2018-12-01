package mtable2.control;



import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mtable2.impoexpo.IExportImport;
import mtable2.io.IPersistenceManager;
import mtable2.model.MTable;
import mtable2.model.TableData;
import mtable2.model.TableInfo;

/**
 * <pre>
 * This Datacontrol uses the following algorithm:
 * 1) to modify table it must be selected , selectedIdx > -1 and 
 * infoTables[selectedIdx]  = TableInfo of this table
 * 2) if table is created it becomes selected automatically.
 * 3) any table's name may be changed by Its Id (selection is not required)
 * 
 * Only tableData List modify methods are synchronized, but not read .
 * Exception: The exportData() is synchronized too.
 * </pre>
 * @author raitis
 * 
 */
public class DataControl implements ITDControl {

    private final static String NOTABLESELECTED="No table selected!";
    private final IPersistenceManager pm;
   
    
    private List<TableInfo> tablesInfo;
    //reference to table.getData()
    private List<TableData> rows;
    private IExportImport expImp;
    private int selectedTableIdx;
    
    
    public DataControl(IPersistenceManager pm) throws Exception {

        this.pm = pm;

        selectedTableIdx = -1;
        if ( loadTablesFromPersistence()){
        } else {

            tablesInfo = new ArrayList<>();
            TableInfo ti;
            tablesInfo.add(ti = new TableInfo("Dummy", new Date()));
            ti.setSelected(true);
            MTable table = new MTable();
            rows = table.getData();
        }
    }

    private boolean loadTablesFromPersistence() throws Exception{
    if (pm != null) {
            tablesInfo = pm.findAll(TableInfo.class);
            if (!tablesInfo.isEmpty()) {
                long id = findSelectedTableIdx();
                MTable table;
                if (id < 0) {
                    //none table is selected , set 1st (id=0) table as selected
                    table = (MTable) pm.find(MTable.class, tablesInfo.get(0).getId());
                    tablesInfo.get(0).setSelected(true);
                    selectedTableIdx= 0;
                } else {
                    //load selected table's data
                    table = (MTable) pm.find(MTable.class, id);
                }
                checkConsistency(table);
                //rows keeps selected table's data
                rows = table.getData();
            } else {
                //If table data file does not exist FileIO returns null
                //on TableData request. SO, just create empty list here.
                //rows != NULL
                rows = new ArrayList<>();
            }
            return true;
        }
      return false;
    }
    /**
     * Check table consistency after loading from persistence side
     * @param table
     * @throws Exception 
     */
    private void checkConsistency(MTable table) throws Exception{
        String msg=  "Internal Data Inconsistency:\n"+
                          table.toString()+"\n"+tablesInfo.toString();
        if(tablesInfo.isEmpty()) throw new Exception(msg);
        if(table.getId()> 0){
            for (TableInfo tableInfo : tablesInfo) {
                if (    tableInfo.getId()==table.getId()){
                    if( tableInfo.getModTime().equals(table.getModTime()) &&
                           (! tableInfo.getName().isEmpty()) &&
                           tableInfo.getName().equals(table.getName())
                    ){
                      return;
                   }
                }
            }
      }
      throw new Exception(msg);
    }
    
    private TableInfo findTableInfoById(long id){
      if(tablesInfo.isEmpty()) return null;
        for (TableInfo tableInfo : tablesInfo) {
            if(tableInfo.getId()== id){
              return tableInfo;
            }
        }
        return null;
    }
    
    /**
     * Compose table object for selected table
     * from TableInfo and rows.
     * @param tableInfo
     * @return 
     */
    private MTable composeTable(TableInfo tableInfo) {
        MTable table = null;

        table = new MTable();
        table.setId(tableInfo.getId());
        table.setModTime(tableInfo.getModTime());
        table.setName(tableInfo.getName());
        table.setSelected(tableInfo.isSelected());

        table.setData(rows);

        //  table  = getTable(tableInfo.getId());
        return table;
    }
    
    private TableInfo findTableInfoForSeletcedTable(){
      if(selectedTableIdx < 0) return null;
      return tablesInfo.get(selectedTableIdx);
    }
    private boolean isSelectedTable(long Id){
        TableInfo tableInfo = findTableInfoById(Id);
        return tableInfo.isSelected();
    }
    /**
     * Find selected table's Idx in tablesInfo and set selectedTableIdx.
     * 
     * @return selected table Id (id in DB or File Index in File Storage) 
     */
    private long findSelectedTableIdx(){
      
        for (int i=0; i<tablesInfo.size();i++) {
           if( tablesInfo.get(i).isSelected()){
               selectedTableIdx= i;
             return tablesInfo.get(i).getId();
           }
        }
      return -1;
    }
    public void setExpImp(IExportImport expImp) {
        this.expImp = expImp;
    }

    /***
    private void saveAndUpdateView() throws Exception {
        if (pm != null) {
            if(table!=null){
            pm.update(table.getId(), table);
            
            }
        }
        if (lv != null) {
            //TODO: Remove ??
        lv.updateListView(rows); 
        }
    }
    ****/
    
    //----------------Table methods
    @Override
    public void create(String name, MTable table)throws Exception{
         
        if(pm != null){
            MTable ntable = (MTable) pm.create(name, table);
            tablesInfo = pm.findAll(TableInfo.class);
            loadTableAndSelect(ntable.getId());
        }
       
    }
    
    

    @Override
     public void loadTableAndSelect(Long id) throws Exception{
        //getTableInfoLst();
        MTable table = getTable(id);
           checkConsistency(table);
           if(table !=null){
             rows= table.getData();
            
               for (int i=0; i< tablesInfo.size();i++) {
                 TableInfo tableInfo = tablesInfo.get(i);
                   if(tableInfo.getId() == id){
                    tableInfo.setSelected(true);
                    selectedTableIdx= i;
                   }else{
                    tableInfo.setSelected(false);
                   }
               }
             pm.saveTablesInfoState(tablesInfo);
           }
         
     }
    
    @Override
    public List<TableInfo> getTableInfoLst() throws Exception{
       List<TableInfo> ninfo= new ArrayList<>();
        for (TableInfo info : tablesInfo) {
            ninfo.add(info.mkCloaning());
        }
       return ninfo;
    }
    @Override
    public int  getSelectedTableIdx(){
      return selectedTableIdx;
    }
    @Override
    public String getSelectedTableName(){
      if(selectedTableIdx> -1){
        return tablesInfo.get(selectedTableIdx).getName();
      }
      return null;
    }
    @Override
    public long getSelectedTableId() {
        if(selectedTableIdx> -1){
        return tablesInfo.get(selectedTableIdx).getId();
      }
      return -1;
    }

    @Override
    public long getTableIdByIdx(int idx){
       if(idx<0 || idx>tablesInfo.size()-1) return -1; 
      return tablesInfo.get(idx).getId();
    }
    @Override
    public void renameSelectedTable(String name) throws IOException, Exception{
      
        if(selectedTableIdx > -1){
          tablesInfo.get(selectedTableIdx).setName(name);
         MTable table = composeTable(tablesInfo.get(selectedTableIdx));
          
          if(pm != null){
              pm.update(table.getId(), table);
            pm.saveTablesInfoState(tablesInfo);
          }
        }
        
    }
    
    @Override
    public void renameTableByid(long Id, String name) throws IOException, Exception{
        TableInfo tableInfo=null;
        if((tableInfo=findTableInfoById(Id)) == null) {
         throw new RuntimeException("Table with Id not found!");
        }
        tableInfo.setName(name);
       if(pm != null){
              
            pm.saveTablesInfoState(tablesInfo);
          }
    }
    
    
    
     
    
    private MTable getTable(Long id) throws Exception{
        MTable obj =null;
          if(pm != null){
           obj = (MTable) pm.find(MTable.class, id);
           
          }
          return obj;
    }
    
    @Override
    public List<MTable> getAllTables() throws Exception {
      List<MTable> mtables= null;
      if(pm != null){
       mtables = pm.findAll(MTable.class);
      }
      return mtables;
    }

    
    /*public List<MTable> getTables() throws Exception {
    List<MTable> objs =null;
    if(pm != null){
    objs =  pm.findAll(MTable.class);
    
    }
    return objs;
    }*/
    
    
    @Override
    public void update(Long id) throws Exception {
        if (pm != null) {
            if(isSelectedTable(id)){
                MTable table = composeTable(findTableInfoForSeletcedTable());
                pm.update(id, table);
            }

        }
    }

    @Override
    public void delete(Long id) throws Exception {
        if (pm != null) {
            if(tablesInfo.isEmpty()) return;
            //If selected table is going to be deleted
            if(findSelectedTableIdx()==id){
              selectedTableIdx = (tablesInfo.size() > 1)? 0: -1; 
             
              }else{
                selectedTableIdx = (selectedTableIdx > 0)? selectedTableIdx-1:0; 
              
            }
            
            //MTable table = composeTable(findTableInfoById(id));
            MTable table = getTable(id);
            pm.delete(id, table);
           
            //reload fresh tablesInfo
            tablesInfo = pm.findAll(TableInfo.class);
            if (!tablesInfo.isEmpty()) {

                int idx = getSelectedTableIdx();
                long nid = tablesInfo.get(idx).getId();
                loadTableAndSelect(nid);

            }else{
            rows.clear();
            }

        }
    }

    //----------------TableData methods works with only selected Table
    
    @Override
    public TableData getItem(int id) throws IndexOutOfBoundsException{
        //checkIndex(id);
        if(selectedTableIdx < 0) throw new RuntimeException(NOTABLESELECTED);
        try {
            return rows.get(id).mkCloaning();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(DataControl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @Override
    public synchronized  void addItem(TableData row) throws Exception {
        if(selectedTableIdx < 0) throw new RuntimeException(NOTABLESELECTED);
        if(rows.size()>= (Integer.MAX_VALUE - 8)){
            throw new IOException("MAXIMUM alowed rows number has been reached!!!");
         }
        rows.add(row);
        sortByDate();
        saveTable();

    }

    @Override
    public synchronized void delItem(int id) throws Exception {
        if(selectedTableIdx < 0) throw new RuntimeException(NOTABLESELECTED);
        rows.remove(id);
        sortByDate();
        saveTable();
        
    }

    @Override
    public synchronized void editItem(int id, TableData row) throws Exception {
         if(selectedTableIdx < 0) throw new RuntimeException(NOTABLESELECTED);
        rows.set(id, row);
        sortByDate();
        saveTable();
    }
    public synchronized void updateItems(List<TableData> rows)throws Exception {
         if(selectedTableIdx < 0) throw new RuntimeException(NOTABLESELECTED);
        this.rows.clear();
        this.rows = rows;
        sortByDate();
        saveTable(); 
    }
    
    
    /**
     * We do not need always to read rows list from persistence (file,DB).
     * For better performance we can use rows list kept in DataControl
     * memory.
     * @return 
     */
    @Override
    public synchronized List<TableData> getItemsFromDataControlMemory(){
        
        /*try {
        List<TableData> copy = cloneList();
        } catch (CloneNotSupportedException ex) {
        Logger.getLogger(DataControl.class.getName()).log(Level.SEVERE, null, ex);
        }*/
      // if(selectedTableIdx < 0) throw new RuntimeException(NOTABLESELECTED);  
      return rows;
    }
    
   @Override
   public synchronized List<TableData> getItems(){
      //If there is no any table at all just return empty list
      if(tablesInfo.isEmpty()) return rows;
      if(selectedTableIdx < 0) throw new RuntimeException(NOTABLESELECTED);
       ArrayList<TableData> nrows = new ArrayList<>();
        for (TableData row : rows) {
           try {
               nrows.add(row.mkCloaning());
           } catch (CloneNotSupportedException ex) {
               Logger.getLogger(DataControl.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
     return nrows;
   }
   
  
    public synchronized void setListForDataControlMemory(List<TableData> lst){
       rows= lst;
    }
    public synchronized void saveTable() throws Exception{
       if(selectedTableIdx < 0) throw new RuntimeException(NOTABLESELECTED);  
      if (pm != null ) {
          MTable table = composeTable(findTableInfoForSeletcedTable());
            
            pm.update(table.getId(), table);
            //pm.saveTablesStateIO();
            
        }
    }
    
    
     private void sortByDate() {
        List<TableData> sorted = new ArrayList<>(rows);

        Collections.sort(sorted, new Comparator<TableData>() {
            @Override
            public int compare(TableData o1, TableData o2) {
                return o1.getCdate().compareTo(o2.getCdate());
            }
        });
        this.rows= sorted;
       
    }

     /**
      * Export List of TableData as CSV to file filePath
      * @param filePath
      * @throws FileNotFoundException
      * @throws UnsupportedEncodingException 
      */
    
    public void exportData(String filePath) throws Exception {
        if(this.expImp != null && pm != null){
            synchronized(this){
           expImp.exportData(filePath, pm.getTables(MTable.class));
            }
        }else {
            System.err.println("ERROR: control.DataControl.exportData(): ExpImp == NULL!");
        }
        
    }
    
    /**
     * Import List of TableDatas data from CSV file
     * @param filePath
     * @throws IOException
     * @throws Exception 
     */
   
    public void importData(String filePath) throws IOException, Exception{
       if(this.expImp!=null && pm != null){
            List<MTable> tables = expImp.importData(filePath);
           synchronized(this) {
              
               pm.saveTables(tables);
               loadTablesFromPersistence();
           }
       }else {
           System.err.println("control.DataControl.importData(): ExpImp == NULL!");
        } 
    }
    @Override
    public void saveAllTables(List<MTable> tables) throws Exception{
      if(pm != null){
         synchronized(this) {
              
               pm.saveTables(tables);
               loadTablesFromPersistence();
           }
      }
    }
    
     //--- below are test simulation methods -----------------------
    @Override
    public void startPersistDataControlMemoryThread() {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("PersistDataControlMemoryThread started: "
                        +Thread.currentThread().getId());
                try {
                    persistDataControlMemory();
                } catch (FileNotFoundException | UnsupportedEncodingException ex) {
                    Logger.getLogger(DataControl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        t1.start();
    }

    /*
    Simulate persist data
    */
    @Override
    public synchronized void persistDataControlMemory() throws FileNotFoundException, UnsupportedEncodingException{
        System.out.println("persistDataControlMemory() called by thread: "+
                Thread.currentThread().getId());
        long startTime = System.currentTimeMillis();
        try {
            //simulate persistance to DB
            Thread.sleep(7000);
        } catch (InterruptedException ex) {
            Logger.getLogger(DataControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("persistDataControlMemory(): Time = "+
                (System.currentTimeMillis()-startTime)+ " Done");
    }

    

    @Override
    public TableData simulateAccessToDataControlMemoryTableData(String txt) throws Exception {
      System.out.println("simulateAccessToDataControlMemoryTableData() called by dialog thread: "+
                Thread.currentThread().getId());
        System.out.println("Received: "+txt);
        if(txt.equals("Exception")){
          throw new Exception(" Generated Exception !!!");
        }
        long startTime = System.currentTimeMillis();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(DataControl.class.getName()).log(Level.SEVERE, null, ex);
        } 
        System.out.println("Delta Time: "+(System.currentTimeMillis()-startTime)/1000+" sec");
        return new TableData(new Date(), "Cat1",12.12, "Note1");
    }

    
    
}
