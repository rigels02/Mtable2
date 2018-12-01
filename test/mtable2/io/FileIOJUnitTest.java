
package mtable2.io;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mtable2.model.MTable;
import mtable2.model.TableData;
import mtable2.model.TableInfo;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author raitis
 */
public class FileIOJUnitTest {
    
    public FileIOJUnitTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @AfterClass
    public static void tearDownClass() {
        try {
            FileIOImpl<MTable> io = new FileIOImpl<>();
            io.deleteAll();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(FileIOJUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testCreate() throws IOException, ClassNotFoundException, Exception {
        System.out.println("mtable2.io.FileIOJUnitTest.testCreate()");
        FileIOImpl<MTable> io = new FileIOImpl<>();
        
        System.out.println("mtable2.io.FileIOJUnitTest.testCreate() - OK");
    }
    @Test(expected = IllegalArgumentException.class)
    public void testCreateExceptiom() throws IOException, ClassNotFoundException, Exception {
        System.out.println("mtable2.io.FileIOJUnitTest.testCreateExceptiom()");
        FileIOImpl<MTable> io = new FileIOImpl<>();
        io.create(null, new MTable());
        
        System.out.println("mtable2.io.FileIOJUnitTest.testCreateExceptiom() - OK");
    }
    @Test
    public void testDeleteAll() throws IOException, ClassNotFoundException{
        System.out.println("mtable2.io.FileIOJUnitTest.testDeleteAll()");
        FileIOImpl<MTable> io = new FileIOImpl<>();
        io.deleteAll();
        assertTrue(io.getTableInfoLst().isEmpty());
        System.out.println("mtable2.io.FileIOJUnitTest.testDeleteAll() - OK");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNullObject() throws IOException, ClassNotFoundException, Exception {
        System.out.println("mtable2.io.FileIOJUnitTest.testCreateWithNullObject()");
        
        FileIOImpl<MTable> io = new FileIOImpl<>();
        io.deleteAll();
        io.create("Table_1", null);
        
        System.out.println("mtable2.io.FileIOJUnitTest.testCreateWithNullObject() - OK");
    }
    
    @Test
    public void testCreateCreate() throws IOException, ClassNotFoundException, Exception {
        System.out.println("mtable2.io.FileIOJUnitTest.testCreateCreate()");
        
        FileIOImpl<MTable> io = new FileIOImpl<>();
        io.deleteAll();
        io.create("Table_1", new MTable());
        io.create("Table_2", new MTable());
        io.create("Table_3", new MTable());
        List<TableInfo> ilst = io.getTableInfoLst();
        assertTrue(ilst.size()==3);
        System.out.println("mtable2.io.FileIOJUnitTest.testCreateCreate() - OK");
    }
    
    @Test
    public void testCreateRead() throws IOException, ClassNotFoundException, Exception {
        System.out.println("mtable2.io.FileIOJUnitTest.testCreateRead()");
        
        FileIOImpl<MTable> io = new FileIOImpl<>();
        io.deleteAll();
        MTable t1= new MTable();
        MTable t2= new MTable();
        MTable t3= new MTable();
        t1.getData().add(new TableData(new Date(), "Table1Kat1", 10.0, "Note 1"));
        t2.getData().add(new TableData(new Date(), "Table2Kat1", 10.0, "Note 1"));
        t3.getData().add(new TableData(new Date(), "Table3Kat1", 10.0, "Note 1"));
        
        io.create("Table_1", t1);
        io.create("Table_2", t2);
        io.create("Table_3", t3);
        List<TableInfo> ilst = io.getTableInfoLst();
        assertTrue(ilst.size()==3);
        MTable rt1 = io.getObj(ilst.get(0).getId());
        MTable rt2 = io.getObj(ilst.get(1).getId());
        MTable rt3 = io.getObj(ilst.get(2).getId());
        assertEquals("Equal1", t1.toString(), rt1.toString());
        assertEquals("Equal2", t2.toString(), rt2.toString());
        assertEquals("Equal3", t3.toString(), rt3.toString());
        System.out.println("mtable2.io.FileIOJUnitTest.testCreateRead() - OK");
    }
    
    @Test
    public void testCreateReadUpdate() throws IOException, ClassNotFoundException, Exception {
        System.out.println("mtable2.io.FileIOJUnitTest.testCreateReadUpdate()");
        
        FileIOImpl<MTable> io = new FileIOImpl<>();
        io.deleteAll();
        MTable t1= new MTable();
        MTable t2= new MTable();
        MTable t3= new MTable();
        t1.getData().add(new TableData(new Date(), "Table1Kat1", 10.0, "Note 1"));
        t2.getData().add(new TableData(new Date(), "Table2Kat1", 10.0, "Note 1"));
        t3.getData().add(new TableData(new Date(), "Table3Kat1", 10.0, "Note 1"));
        
        io.create("Table_1", t1);
        io.create("Table_2", t2);
        io.create("Table_3", t3);
        List<TableInfo> ilst = io.getTableInfoLst();
        assertTrue(ilst.size()==3);
        MTable rt1 = io.getObj(ilst.get(0).getId());
        MTable rt2 = io.getObj(ilst.get(1).getId());
        MTable rt3 = io.getObj(ilst.get(2).getId());
        assertEquals("Equal1", t1.toString(), rt1.toString());
        assertEquals("Equal2", t2.toString(), rt2.toString());
        assertEquals("Equal3", t3.toString(), rt3.toString());
        rt2.getData().get(0).setCat("Table2KatChanged");
        rt2.setName("Table2Modified");
        io.update(ilst.get(1).getId(), rt2);
        MTable rrt2 = io.getObj(ilst.get(1).getId());
        assertEquals("tablesInfo", rt2.getName(), rrt2.getName());
        assertEquals("Equal22", rt2.toString(), rrt2.toString());
        System.out.println("mtable2.io.FileIOJUnitTest.testCreateReadUpdate() - OK");
    }
    
    @Test
    public void testDelete() throws IOException, ClassNotFoundException, Exception{
        System.out.println("mtable2.io.FileIOJUnitTest.testDelete()");
        FileIOImpl<MTable> io = new FileIOImpl<>();
        io.deleteAll();
        MTable t1= new MTable();
        MTable t2= new MTable();
        MTable t3= new MTable();
        t1.getData().add(new TableData(new Date(), "Table1Kat1", 10.0, "Note 1"));
        t2.getData().add(new TableData(new Date(), "Table2Kat1", 10.0, "Note 1"));
        t3.getData().add(new TableData(new Date(), "Table3Kat1", 10.0, "Note 1"));
        
        io.create("Table_1", t1);
        io.create("Table_2", t2);
        io.create("Table_3", t3);
        List<TableInfo> ilst = io.getTableInfoLst();
        assertTrue(ilst.size()==3);
        File fi = new File("mtable_2.dat");
        assertTrue(fi.exists());
        
        io.delete(ilst.get(1).getId());
        ilst = io.getTableInfoLst();
        assertTrue(ilst.size()==2);
        assertTrue(!fi.exists());
        //=================
        MTable rt3 = io.getObj(ilst.get(1).getId());
        assertEquals("Expected", t3.toString(), rt3.toString());
        System.out.println("mtable2.io.FileIOJUnitTest.testDelete() - OK");
    }
}
