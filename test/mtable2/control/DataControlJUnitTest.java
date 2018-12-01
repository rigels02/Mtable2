
package mtable2.control;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mtable2.io.FileIOImpl;
import mtable2.io.FilePersistenceManager;

import mtable2.model.MTable;
import mtable2.model.TableData;
import mtable2.model.TableInfo;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author raitis
 */
public class DataControlJUnitTest {
    
    public DataControlJUnitTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
        try {
            FileIOImpl<MTable> io = new FileIOImpl<>();
            io.deleteAll();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(DataControlJUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    
    @Test
    public void testControlCreate() throws  Exception {
        System.out.println("mtable2.control.DataControlJUnitTest.testControlCreate()");
        FileIOImpl<MTable> io = new FileIOImpl<>();
        io.deleteAll();
        FilePersistenceManager<MTable> pm = new FilePersistenceManager<>(io);
        DataControl ctrl = new DataControl(pm);
        ctrl.create("Table_1", new MTable());
        int idx = ctrl.getSelectedTableIdx();
        assertTrue(idx == 0);
        List<TableData> data = ctrl.getItemsFromDataControlMemory();
        assertTrue(data.isEmpty());
        File fidx = new File("mtable.idx");
        File fdat = new File("mtable_1.dat");
        assertTrue(fidx.exists());
         assertTrue(fdat.exists());
        System.out.println("mtable2.control.DataControlJUnitTest.testControlCreate() - OK");
    }
    @Test
    public void testControlNewInstance() throws IOException, ClassNotFoundException, Exception{
        System.out.println("mtable2.control.DataControlJUnitTest.testControlNewInstance()");
        FileIOImpl<MTable> io = new FileIOImpl<>();
        io.deleteAll();
        FilePersistenceManager<MTable> pm = new FilePersistenceManager<>(io);
        DataControl ctrl = new DataControl(pm);
        ctrl.create("Table_1", new MTable());
        int idx = ctrl.getSelectedTableIdx();
        long Id = ctrl.getSelectedTableId();
        assertTrue(idx == 0);
        assertTrue(Id==1);
        assertTrue(ctrl.getSelectedTableName().equals("Table_1"));
        List<TableInfo> tinfo = ctrl.getTableInfoLst();
        assertTrue(tinfo.size()==1);
        
        //MTable table = ctrl.getTable(ctrl.getTableInfoLst().get(idx).getId());
        MTable table = pm.find(MTable.class, ctrl.getTableInfoLst().get(idx).getId());
        assertTrue(tinfo.get(idx).getId()==table.getId());
        //new Instances the same table is reloaded
        io = new FileIOImpl<>();
        pm = new FilePersistenceManager<>(io);
        ctrl = new DataControl(pm);
         idx = ctrl.getSelectedTableIdx();
         Id = ctrl.getSelectedTableId();
         assertTrue(idx == 0);
         assertTrue(Id==1);
        assertTrue(ctrl.getSelectedTableName().equals("Table_1"));
        System.out.println("mtable2.control.DataControlJUnitTest.testControlNewInstance() - OK");
    }
    @Test
    public void testAddItem() throws IOException, ClassNotFoundException, Exception{
        System.out.println("mtable2.control.DataControlJUnitTest.testAddItem()");
        FileIOImpl<MTable> io = new FileIOImpl<>();
        io.deleteAll();
        FilePersistenceManager<MTable> pm = new FilePersistenceManager<>(io);
        DataControl ctrl = new DataControl(pm);
        ctrl.create("Table_1", new MTable());
        int idx = ctrl.getSelectedTableIdx();
        long Id = ctrl.getSelectedTableId();
        List<TableData> data = ctrl.getItemsFromDataControlMemory();
        List<TableData> data1 = ctrl.getItems();
        assertTrue(data.isEmpty());
        assertTrue(data.size() == data1.size());
        
        //add item, update table is checked too
        TableData tdata = new TableData(new Date(), "Cat1", 12.0, "Note_1");
        ctrl.addItem(tdata);
        data = ctrl.getItemsFromDataControlMemory();
        //data1 = ctrl.getList(Id);
        data1 = ctrl.getItems();
        assertTrue(data.size() == data1.size());
        TableData dataItem = data.get(0);
         TableData dataItem1 = data1.get(0);
        assertEquals( tdata.toString(), dataItem.toString());
        assertEquals( tdata.toString(), dataItem1.toString());
        System.out.println("mtable2.control.DataControlJUnitTest.testAddItem() - OK");
    }
    
    @Test
    public void testAddSomeItems() throws IOException, ClassNotFoundException, Exception{
        System.out.println("mtable2.control.DataControlJUnitTest.testAddSomeItems()");
        FileIOImpl<MTable> io = new FileIOImpl<>();
        io.deleteAll();
        FilePersistenceManager<MTable> pm = new FilePersistenceManager<>(io);
        DataControl ctrl = new DataControl(pm);
        ctrl.create("Table_1", new MTable());
        int idx = ctrl.getSelectedTableIdx();
        long Id = ctrl.getSelectedTableId();
        List<TableData> data = ctrl.getItemsFromDataControlMemory();
        List<TableData> data1 = ctrl.getItems();
        assertTrue(data.isEmpty());
        assertTrue(data.size() == data1.size());
        Date tim0= ctrl.getTableInfoLst().get(ctrl.getSelectedTableIdx()).getModTime();
        //add items , these are added to the table and rows are updated in
        //controller memory. Table's timestamp has to be updated too.
        TableData tdata = new TableData(new Date(), "Cat1", 12.0, "Note_1");
        Thread.sleep(500);
        ctrl.addItem(tdata);
        Date tim1= ctrl.getTableInfoLst().get(ctrl.getSelectedTableIdx()).getModTime();
        assertTrue(tim1.after(tim0));
        assertTrue(ctrl.getItems().size()==1);
        assertEquals(tdata.toString(),ctrl.getItem(0).toString());
        //add next
        TableData tdata1 = new TableData(new Date(), "Cat2", 22.0, "Note_2");
        Thread.sleep(500);
        ctrl.addItem(tdata1);
        Date tim2= ctrl.getTableInfoLst().get(ctrl.getSelectedTableIdx()).getModTime();
        assertTrue(tim2.after(tim1));
        assertTrue(ctrl.getItems().size()==2);
        assertEquals(tdata1.toString(),ctrl.getItem(1).toString());
        //add next
         TableData tdata2 = new TableData(new Date(), "Cat3", 22.0, "Note_3");
        Thread.sleep(500);
        ctrl.addItem(tdata2);
        Date tim3= ctrl.getTableInfoLst().get(ctrl.getSelectedTableIdx()).getModTime();
        assertTrue(tim3.after(tim2));
        assertTrue(ctrl.getItems().size()==3);
        assertEquals(tdata2.toString(),ctrl.getItem(2).toString());
       
        System.out.println("mtable2.control.DataControlJUnitTest.testAddSomeItems() - OK");
    }
    
    @Test
    public void testAddEdit() throws IOException, ClassNotFoundException, Exception{
        System.out.println("mtable2.control.DataControlJUnitTest.testAddEdit()");
        FileIOImpl<MTable> io = new FileIOImpl<>();
        io.deleteAll();
        FilePersistenceManager<MTable> pm = new FilePersistenceManager<>(io);
        DataControl ctrl = new DataControl(pm);
        ctrl.create("Table_1", new MTable());
        TableData tdata = new TableData(new Date(), "Cat1", 12.0, "Note_1");
        ctrl.addItem(tdata);
        Date tim1 = ctrl.getTableInfoLst().get(0).getModTime();
        tdata.setCat("Cat1Changed");
        Thread.sleep(2000);
        ctrl.editItem(0, tdata);
        Date tim2 = ctrl.getTableInfoLst().get(0).getModTime();
        assertTrue(tim2.after(tim1));
        
        List<TableData> data = ctrl.getItemsFromDataControlMemory();
        Long Id = ctrl.getSelectedTableId();
        //Date tim = ctrl.getTable(Id).getModTime();
         Date tim = pm.find(MTable.class,Id).getModTime();
        assertTrue(tim.equals(tim2));
        List<TableData> data1 = ctrl.getItems();
        assertTrue(data.size() == data1.size());
        TableData dataItem = data.get(0);
         TableData dataItem1 = data1.get(0);
        assertEquals( tdata.toString(), dataItem.toString());
        assertEquals( tdata.toString(), dataItem1.toString());
        System.out.println("mtable2.control.DataControlJUnitTest.testAddEdit() - OK");
    }
    @Test
    public void testAddDel() throws IOException, ClassNotFoundException, Exception{
        System.out.println("mtable2.control.DataControlJUnitTest.testAddDel()");
        FileIOImpl<MTable> io = new FileIOImpl<>();
        io.deleteAll();
        FilePersistenceManager<MTable> pm = new FilePersistenceManager<>(io);
        DataControl ctrl = new DataControl(pm);
        ctrl.create("Table_1", new MTable());
        TableData tdata = new TableData(new Date(), "Cat1", 12.0, "Note_1");
        ctrl.addItem(tdata);
        Date tim1 = ctrl.getTableInfoLst().get(0).getModTime();
        Thread.sleep(2000);
        ctrl.delItem(0);
        Date tim2 = ctrl.getTableInfoLst().get(0).getModTime();
        assertTrue(tim2.after(tim1));
        List<TableData> data = ctrl.getItemsFromDataControlMemory();
        Long Id = ctrl.getSelectedTableId();
        List<TableData> data1 = ctrl.getItems();
        assertTrue(data.size() == data1.size());
        assertTrue(data.size()==0);
        System.out.println("mtable2.control.DataControlJUnitTest.testAddDel() - OK");
    }
    @Test
    public void testAddRenameTable() throws IOException, ClassNotFoundException, Exception{
        System.out.println("mtable2.control.DataControlJUnitTest.testAddRenameTable()");
        FileIOImpl<MTable> io = new FileIOImpl<>();
        io.deleteAll();
        FilePersistenceManager<MTable> pm = new FilePersistenceManager<>(io);
        DataControl ctrl = new DataControl(pm);
        ctrl.create("Table_1", new MTable());
        TableData tdata = new TableData(new Date(), "Cat1", 12.0, "Note_1");
        ctrl.addItem(tdata);
        Date tim1 = ctrl.getTableInfoLst().get(0).getModTime();
        String nname= "Table_1Modified";
        Thread.sleep(2000);
        ctrl.renameSelectedTable(nname);
        Date tim2 = ctrl.getTableInfoLst().get(0).getModTime();
        assertTrue(ctrl.getSelectedTableName().equals(nname));
        assertTrue(tim2.after(tim1));
        //MTable table = ctrl.getTable(ctrl.getSelectedTableId());
        MTable table = pm.find(MTable.class,ctrl.getSelectedTableId());
        assertTrue(table.getName().equals(nname));
        assertTrue(table.getModTime().equals(tim2));
        System.out.println("mtable2.control.DataControlJUnitTest.testAddRenameTable() - OK");
    }
    
    @Test
    public void testAddDeleteTableLastTable() throws IOException, ClassNotFoundException, Exception{
        System.out.println("mtable2.control.DataControlJUnitTest.testAddDeleteTableLastTable()");
        FileIOImpl<MTable> io = new FileIOImpl<>();
        io.deleteAll();
        FilePersistenceManager<MTable> pm = new FilePersistenceManager<>(io);
        DataControl ctrl = new DataControl(pm);
        ctrl.create("Table_1", new MTable());
        TableData tdata = new TableData(new Date(), "Cat1", 12.0, "Note_1");
        ctrl.addItem(tdata);
        long Id = ctrl.getSelectedTableId();
        //delete table
        ctrl.delete(Id);
        int idx = ctrl.getSelectedTableIdx();
        List<TableInfo> infoLst = ctrl.getTableInfoLst();
        List<TableData> rows = ctrl.getItemsFromDataControlMemory();
        //If it was last table then 
        assertTrue(infoLst.isEmpty());
        assertTrue(rows.isEmpty());
       File fidx = new File("mtable.idx");
        File fdat = new File("mtable_1.dat");
        assertTrue(fidx.exists());
        assertTrue(!fdat.exists());
        //delete with no any table!!!
        Id = ctrl.getSelectedTableId();
        assertTrue(Id==-1);
        ctrl.delete(Id);
        System.out.println("mtable2.control.DataControlJUnitTest.testAddDeleteTableLastTable() - OK");
    
    }
    @Test
    public void testAddSomeTablesDeleteSelectedTable() throws IOException, ClassNotFoundException, Exception{
        System.out.println("mtable2.control.DataControlJUnitTest.testAddSomeTablesDeleteSelectedTable()");
        FileIOImpl<MTable> io = new FileIOImpl<>();
        io.deleteAll();
        FilePersistenceManager<MTable> pm = new FilePersistenceManager<>(io);
        DataControl ctrl = new DataControl(pm);
        ctrl.create("Table_1", new MTable());
        TableData tdata = new TableData(new Date(), "Table_1Cat1", 12.0, "Note_1");
        ctrl.addItem(tdata);
        int Id = ctrl.getSelectedTableIdx();
        assertTrue(Id==0);
        assertTrue(ctrl.getTableInfoLst().size()==1);
        assertTrue(ctrl.getItems().get(0).toString().equals(tdata.toString()));
        //----Table_2
        ctrl.create("Table_2", new MTable());
        TableData tdata1 = new TableData(new Date(), "Table_2Cat1", 12.0, "Note_1");
        ctrl.addItem(tdata1);
        Id = ctrl.getSelectedTableIdx();
        assertTrue(Id==1);
        assertTrue(ctrl.getTableInfoLst().size()==2);
        assertTrue(ctrl.getItems().get(0).toString().equals(tdata1.toString()));
        
        //----Table_3
        ctrl.create("Table_3", new MTable());
        TableData tdata2 = new TableData(new Date(), "Table_3Cat1", 12.0, "Note_1");
        ctrl.addItem(tdata2);
        Id = ctrl.getSelectedTableIdx();
        assertTrue(Id==2);
        assertTrue(ctrl.getTableInfoLst().size()==3);
        assertTrue(ctrl.getItems().get(0).toString().equals(tdata2.toString()));
        //delete Table_3
        long ID = ctrl.getSelectedTableId();
        ctrl.delete(ID);
         Id = ctrl.getSelectedTableIdx();
         //if selected table is deleted then table with Idx=0 should be selected
         assertTrue(Id==0);
        assertTrue(ctrl.getTableInfoLst().size()==2);
        assertTrue(ctrl.getItems().get(0).toString().equals(tdata.toString()));
        
        System.out.println("mtable2.control.DataControlJUnitTest.testAddSomeTablesDeleteSelectedTable() - OK");
    }
    @Test
    public void testAddSomeTablesDeleteNonSelectedTable() throws IOException, ClassNotFoundException, Exception{
        System.out.println("mtable2.control.DataControlJUnitTest.testAddSomeTablesDeleteNonSelectedTable()");
        FileIOImpl<MTable> io = new FileIOImpl<>();
        io.deleteAll();
        FilePersistenceManager<MTable> pm = new FilePersistenceManager<>(io);
        DataControl ctrl = new DataControl(pm);
        ctrl.create("Table_1", new MTable());
        TableData tdata = new TableData(new Date(), "Table_1Cat1", 12.0, "Note_1");
        ctrl.addItem(tdata);
        int Id = ctrl.getSelectedTableIdx();
        assertTrue(Id==0);
        assertTrue(ctrl.getTableInfoLst().size()==1);
        assertTrue(ctrl.getItems().get(0).toString().equals(tdata.toString()));
        //----Table_2
        ctrl.create("Table_2", new MTable());
        TableData tdata1 = new TableData(new Date(), "Table_2Cat1", 12.0, "Note_1");
        ctrl.addItem(tdata1);
        Id = ctrl.getSelectedTableIdx();
        assertTrue(Id==1);
        assertTrue(ctrl.getTableInfoLst().size()==2);
        assertTrue(ctrl.getItems().get(0).toString().equals(tdata1.toString()));
        
        //----Table_3
        ctrl.create("Table_3", new MTable());
        TableData tdata2 = new TableData(new Date(), "Table_3Cat1", 12.0, "Note_1");
        ctrl.addItem(tdata2);
        Id = ctrl.getSelectedTableIdx();
        assertTrue(Id==2);
        assertTrue(ctrl.getTableInfoLst().size()==3);
        assertTrue(ctrl.getItems().get(0).toString().equals(tdata2.toString()));
        //table idx=2 is selected. Delete table Table_2(idx=1)
        //Idx should be decremented. Table_2 must be deleted.
        //selected table should be Table_3
        ctrl.delete(ctrl.getTableIdByIdx(1));
        int Id1 = ctrl.getSelectedTableIdx();
        assertTrue(Id1==Id-1);
        assertTrue(ctrl.getTableInfoLst().size()==2);
        assertTrue(ctrl.getItems().get(0).toString().equals(tdata2.toString()));
        for (TableInfo tinfo : ctrl.getTableInfoLst()) {
            if(tinfo.getName().equals("Table_2")){
                fail("Table : Table_2 is not deleted");
            }
        }
        System.out.println("mtable2.control.DataControlJUnitTest.testAddSomeTablesDeleteNonSelectedTable() - OK");
    }
    @Test
    public void testAddSomeTablesSelectOtherTable() throws IOException, ClassNotFoundException, Exception{
        System.out.println("mtable2.control.DataControlJUnitTest.testAddSomeTablesSelectOtherTable()");
        FileIOImpl<MTable> io = new FileIOImpl<>();
        io.deleteAll();
        FilePersistenceManager<MTable> pm = new FilePersistenceManager<>(io);
        DataControl ctrl = new DataControl(pm);
        ctrl.create("Table_1", new MTable());
        TableData tdata = new TableData(new Date(), "Table_1Cat1", 12.0, "Note_1");
        ctrl.addItem(tdata);
        int Id1 = ctrl.getSelectedTableIdx();
        assertTrue(Id1==0);
        assertTrue(ctrl.getTableInfoLst().size()==1);
        assertTrue(ctrl.getItems().get(0).toString().equals(tdata.toString()));
        //----Table_2
        ctrl.create("Table_2", new MTable());
        TableData tdata1 = new TableData(new Date(), "Table_2Cat1", 12.0, "Note_1");
        ctrl.addItem(tdata1);
        int Id2 = ctrl.getSelectedTableIdx();
        assertTrue(Id2==1);
        assertTrue(ctrl.getTableInfoLst().size()==2);
        assertTrue(ctrl.getItems().get(0).toString().equals(tdata1.toString()));
        
        //----Table_3
        ctrl.create("Table_3", new MTable());
        TableData tdata2 = new TableData(new Date(), "Table_3Cat1", 12.0, "Note_1");
        ctrl.addItem(tdata2);
        int Id3 = ctrl.getSelectedTableIdx();
        assertTrue(Id3==2);
        assertTrue(ctrl.getTableInfoLst().size()==3);
        assertTrue(ctrl.getItems().get(0).toString().equals(tdata2.toString()));
        //table_3 is selected
        //And now select table_1
        //Selected Index must be 0
        //rows must be updated for selected table
        ctrl.loadTableAndSelect(ctrl.getTableIdByIdx(Id1));
        int idx= ctrl.getSelectedTableIdx();
            assertTrue(idx==0);
            assertTrue(ctrl.getItems().size()==1);
            assertEquals(ctrl.getItem(0).toString(),tdata.toString());
        //select table_2
        ctrl.loadTableAndSelect(ctrl.getTableIdByIdx(Id2));
        idx= ctrl.getSelectedTableIdx();
            assertTrue(idx==1);
            assertTrue(ctrl.getItems().size()==1);
            assertEquals(ctrl.getItem(0).toString(),tdata1.toString());
        //selecte table_3
        ctrl.loadTableAndSelect(ctrl.getTableIdByIdx(Id3));
        idx= ctrl.getSelectedTableIdx();
            assertTrue(idx==2);
            assertTrue(ctrl.getItems().size()==1);
            assertEquals(ctrl.getItem(0).toString(),tdata2.toString());
        
        System.out.println("mtable2.control.DataControlJUnitTest.testAddSomeTablesSelectOtherTable() - OK");
    }
    
    
    
    @Test
    public void testAddOneTableRunNewController() throws IOException, ClassNotFoundException, Exception{
        System.out.println("mtable2.control.DataControlJUnitTest.testAddOneTableRunNewController()");
        
        FileIOImpl<MTable> io = new FileIOImpl<>();
        io.deleteAll();
        FilePersistenceManager<MTable> pm = new FilePersistenceManager<>(io);
        DataControl ctrl = new DataControl(pm);
        ctrl.create("Table_1", new MTable());
        TableData tdata = new TableData(new Date(), "Table_1Cat1", 12.0, "Note_1");
        ctrl.addItem(tdata);
        int Id1 = ctrl.getSelectedTableIdx();
        assertTrue(Id1==0);
        assertTrue(ctrl.getTableInfoLst().size()==1);
        assertTrue(ctrl.getItems().get(0).toString().equals(tdata.toString()));
        io = new FileIOImpl<>();
        
        pm = new FilePersistenceManager<>(io);
        ctrl = new DataControl(pm);
         Id1 = ctrl.getSelectedTableIdx();
        assertTrue(Id1==0);
        assertTrue(ctrl.getTableInfoLst().size()==1);
        assertTrue(ctrl.getItems().get(0).toString().equals(tdata.toString()));
        
        
        System.out.println("mtable2.control.DataControlJUnitTest.testAddOneTableRunNewController() - OK");
    }
    @Test
    public void testAddSomeTablesRunNewController() throws IOException, ClassNotFoundException, Exception{
        System.out.println("mtable2.control.DataControlJUnitTest.testAddSomeTablesRunNewController()");
        FileIOImpl<MTable> io = new FileIOImpl<>();
        io.deleteAll();
        FilePersistenceManager<MTable> pm = new FilePersistenceManager<>(io);
        DataControl ctrl = new DataControl(pm);
        ctrl.create("Table_1", new MTable());
        TableData tdata = new TableData(new Date(), "Table_1Cat1", 12.0, "Note_1");
        ctrl.addItem(tdata);
        int Id1 = ctrl.getSelectedTableIdx();
        assertTrue(Id1==0);
        assertTrue(ctrl.getTableInfoLst().size()==1);
        assertTrue(ctrl.getItems().get(0).toString().equals(tdata.toString()));
        //----Table_2
        ctrl.create("Table_2", new MTable());
        TableData tdata1 = new TableData(new Date(), "Table_2Cat1", 12.0, "Note_1");
        ctrl.addItem(tdata1);
        int Id2 = ctrl.getSelectedTableIdx();
        assertTrue(Id2==1);
        assertTrue(ctrl.getTableInfoLst().size()==2);
        assertTrue(ctrl.getItems().get(0).toString().equals(tdata1.toString()));
        
        //----Table_3
        ctrl.create("Table_3", new MTable());
        TableData tdata2 = new TableData(new Date(), "Table_3Cat1", 12.0, "Note_1");
        ctrl.addItem(tdata2);
        int Id3 = ctrl.getSelectedTableIdx();
        assertTrue(Id3==2);
        assertTrue(ctrl.getTableInfoLst().size()==3);
        assertTrue(ctrl.getItems().get(0).toString().equals(tdata2.toString()));
        //table_3 is selected
        //run new controller
        //the same table has to be selected
        io = new FileIOImpl<>();
        //Loading : internall iconsistency !!!
        pm = new FilePersistenceManager<>(io);
        ctrl = new DataControl(pm);
        int idx = ctrl.getSelectedTableIdx();
        long Id = ctrl.getSelectedTableId();
        String name = ctrl.getSelectedTableName();
        
        System.out.println("mtable2.control.DataControlJUnitTest.testAddSomeTablesRunNewController() - OK");
    }
    
    
    
    @Test
    public void testDeleteLastTable() throws IOException, ClassNotFoundException, Exception{
        System.out.println("mtable2.control.DataControlJUnitTest.testDeleteLastTable()");
        FileIOImpl<MTable> io = new FileIOImpl<>();
        io.deleteAll();
        FilePersistenceManager<MTable> pm = new FilePersistenceManager<>(io);
        DataControl ctrl = new DataControl(pm);
        ctrl.create("Table_1", new MTable());
        TableData tdata = new TableData(new Date(), "Table_1Cat1", 12.0, "Note_1");
        ctrl.addItem(tdata);
        int Id1 = ctrl.getSelectedTableIdx();
        assertTrue(Id1==0);
        assertTrue(ctrl.getTableInfoLst().size()==1);
        assertTrue(ctrl.getItems().get(0).toString().equals(tdata.toString()));
       //delete table
       //selected table is -1
       //table tableInfo list is empty
       //rows are empty
       //files: mtable.idx exists, table_1.dat deleted?
       ctrl.delete(ctrl.getSelectedTableId());
       int idx= ctrl.getSelectedTableIdx();
        assertTrue(idx == -1);
        List<TableInfo> infoLst = ctrl.getTableInfoLst();
        List<TableData> rows = ctrl.getItems();
       
        assertTrue(infoLst.isEmpty());
        assertTrue(rows.isEmpty());
        File fidx= new File("mtable.idx");
        assertTrue(fidx.exists());
        File f1= new File("mtable_1.dat");
        assertTrue(!f1.exists());
        
        //create new table again and delete
       ctrl.create("Table_1", new MTable());
        tdata = new TableData(new Date(), "Table_1Cat1", 12.0, "Note_1");
        ctrl.addItem(tdata);
        Id1 = ctrl.getSelectedTableIdx();
        assertTrue(Id1==0);
        assertTrue(ctrl.getTableInfoLst().size()==1);
        assertTrue(ctrl.getItems().get(0).toString().equals(tdata.toString()));
        fidx= new File("mtable.idx");
        assertTrue(fidx.exists());
         f1= new File("mtable_1.dat");
        assertTrue(f1.exists());
        //delete again
        ctrl.delete(ctrl.getSelectedTableId());
       idx= ctrl.getSelectedTableIdx();
       assertTrue(idx == -1);
        infoLst = ctrl.getTableInfoLst();
        rows = ctrl.getItems();
        assertTrue(infoLst.isEmpty());
        assertTrue(rows.isEmpty());
        fidx= new File("mtable.idx");
        assertTrue(fidx.exists());
        f1= new File("mtable_1.dat");
        assertTrue(!f1.exists());
        
//create new controller instance on case when no any table exists
        io = new FileIOImpl<>();
        
        pm = new FilePersistenceManager<>(io);
        ctrl = new DataControl(pm);
        //check condition, should be the same
        idx= ctrl.getSelectedTableIdx();
        assertTrue(idx == -1);
        infoLst = ctrl.getTableInfoLst();
        rows = ctrl.getItems();
        assertTrue(infoLst.isEmpty());
        assertTrue(rows.isEmpty());
        fidx= new File("mtable.idx");
        assertTrue(fidx.exists());
        f1= new File("mtable_1.dat");
        assertTrue(!f1.exists());
        //create table and check
        ctrl.create("Table_1", new MTable());
        tdata = new TableData(new Date(), "Table_1Cat1", 12.0, "Note_1");
        ctrl.addItem(tdata);
        Id1 = ctrl.getSelectedTableIdx();
        assertTrue(Id1==0);
        assertTrue(ctrl.getTableInfoLst().size()==1);
        assertTrue(ctrl.getItems().get(0).toString().equals(tdata.toString()));
       
        System.out.println("mtable2.control.DataControlJUnitTest.testDeleteLastTable() - OK");
    }
    /*@Test
    public void testClone() throws CloneNotSupportedException, InterruptedException{
    TableInfo t1 = new TableInfo("Table_1", new Date());
    TableInfo t2 =  t1.mkCopy();
    //TableInfo t2= t1;
    System.out.println("t1= "+t1.toString());
    System.out.println("t2= "+t2.toString());
    t2.setName("TableModified");
    Thread.sleep(1000);
    t2.setModTime(new Date());
    System.out.println("t1= "+t1.toString());
    System.out.println("t2= "+t2.toString());
    t2.getModTime().setYear(116);
    System.out.println("\n---------------\nt1= "+t1.toString());
    System.out.println("t2= "+t2.toString());
    }*/
    
    @Test
    public void testGetItemAndModify() throws IOException, ClassNotFoundException, Exception{
    
        FileIOImpl<MTable> io = new FileIOImpl<>();
        io.deleteAll();
        FilePersistenceManager<MTable> pm = new FilePersistenceManager<>(io);
        DataControl ctrl = new DataControl(pm);
        ctrl.create("Table_1", new MTable());
        TableData tdata = new TableData(new Date(), "Table_1Cat1", 12.0, "Note_1");
        ctrl.addItem(tdata);
        int Id1 = ctrl.getSelectedTableIdx();
        assertTrue(Id1==0);
        assertTrue(ctrl.getTableInfoLst().size()==1);
        assertTrue(ctrl.getItems().get(0).toString().equals(tdata.toString()));
        //make shallow copy of item - can not be modified just by setter
        //immutable in DataControl memory
        ctrl.getItem(0).setCat("Table_1Changed");
        TableData it = ctrl.getItem(0);
        //--------------
        //getItems make a copy of list but keeps elements' references,
        // items are mutable in Datacontrol
        List<TableData> ilst= ctrl.getItems();
        ilst.get(0).setCat("Table_1Cganged_2");
        it = ctrl.getItem(0);
        ilst = ctrl.getItems();
        
    }
    
    @Test
    public void testItemsDeepCopy() throws IOException, ClassNotFoundException, Exception{
        FileIOImpl<MTable> io = new FileIOImpl<>();
        io.deleteAll();
        FilePersistenceManager<MTable> pm = new FilePersistenceManager<>(io);
        DataControl ctrl = new DataControl(pm);
        ctrl.create("Table_1", new MTable());
        long tim11= System.currentTimeMillis();
        for(int i=1;i<=100;i++){
        TableData tdata = new TableData(new Date(), "Table_1Cat"+i, 12.0, "Note_"+i);
        ctrl.addItem(tdata);
        }
        System.out.println("AddItems time: "+(System.currentTimeMillis()-tim11));
        int Id1 = ctrl.getSelectedTableIdx();
        assertTrue(Id1==0);
        assertTrue(ctrl.getTableInfoLst().size()==1);
        long tim1 = System.currentTimeMillis();
        List<TableData> items = ctrl.getItems();
        System.out.println("ETime= "+ (System.currentTimeMillis()-tim1));
        //assertTrue(items.size()==100);
        assertTrue(ctrl.getItems().get(99).getCat().equals("Table_1Cat100"));
    
    }
    @Test
    public void testSaveItems() throws IOException, ClassNotFoundException, Exception{
        FileIOImpl<MTable> io = new FileIOImpl<>();
        io.deleteAll();
        FilePersistenceManager<MTable> pm = new FilePersistenceManager<>(io);
        DataControl ctrl = new DataControl(pm);
        ctrl.create("Table_1", new MTable());
        
        List<TableData> items = ctrl.getItemsFromDataControlMemory();
        for(int i=1;i<=100;i++){
        TableData tdata = new TableData(new Date(), "Table_1Cat"+i, 12.0, "Note_"+i);
        items.add(tdata);
        }
        long tim11= System.currentTimeMillis();
        ctrl.saveTable();
        System.out.println("Save Time: "+(System.currentTimeMillis()-tim11));
        
        int Id1 = ctrl.getSelectedTableIdx();
        assertTrue(Id1==0);
        assertTrue(ctrl.getTableInfoLst().size()==1);
        
        //assertTrue(items.size()==100);
        assertTrue(ctrl.getItems().get(99).getCat().equals("Table_1Cat100"));
    
    }
    @Test
    public void test3Tables() throws IOException, ClassNotFoundException, Exception{
    FileIOImpl<MTable> io = new FileIOImpl<>();
        io.deleteAll();
        FilePersistenceManager<MTable> pm = new FilePersistenceManager<>(io);
        DataControl ctrl = new DataControl(pm);
        ctrl.create("Table_1", new MTable());
        TableData tdata = new TableData(new Date(), "Table_1Cat1", 10.0, "Note_1");
        ctrl.addItem(tdata);
        tdata = new TableData(new Date(), "Table_1Cat2", 20.0, "Note_2");
        ctrl.addItem(tdata);
        tdata = new TableData(new Date(), "Table_1Cat3", 30.0, "Note_3");
        ctrl.addItem(tdata);
        
        int Id = ctrl.getSelectedTableIdx();
        assertTrue(Id==0);
        assertTrue(ctrl.getTableInfoLst().size()==1);
        assertTrue(ctrl.getItems().get(2).toString().equals(tdata.toString()));
        //----Table_2
        ctrl.create("Table_2", new MTable());
        TableData tdata1 = new TableData(new Date(), "Table_2Cat1", 10.0, "Note_21");
        ctrl.addItem(tdata1);
        tdata1 = new TableData(new Date(), "Table_2Cat2", 20.0, "Note_21");
        ctrl.addItem(tdata1);
        tdata1 = new TableData(new Date(), "Table_2Cat3", 30.0, "Note_21");
        ctrl.addItem(tdata1);
        
        Id = ctrl.getSelectedTableIdx();
        assertTrue(Id==1);
        assertTrue(ctrl.getTableInfoLst().size()==2);
        assertTrue(ctrl.getItems().get(2).toString().equals(tdata1.toString()));
        
        //----Table_3
        ctrl.create("Table_3", new MTable());
        TableData tdata2 = new TableData(new Date(), "Table_3Cat1", 10.0, "Note_31");
        ctrl.addItem(tdata2);
        tdata2 = new TableData(new Date(), "Table_3Cat2", 20.0, "Note_31");
        ctrl.addItem(tdata2);
        tdata2 = new TableData(new Date(), "Table_3Cat3", 30.0, "Note_31");
        ctrl.addItem(tdata2);
        Id = ctrl.getSelectedTableIdx();
        assertTrue(Id==2);
        assertTrue(ctrl.getTableInfoLst().size()==3);
        assertTrue(ctrl.getItems().get(2).toString().equals(tdata2.toString()));
        
    }
}
