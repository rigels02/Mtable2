
package mtable2.io;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import mtable2.model.MTable;
import mtable2.model.TableData;
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
public class PersistenceManagerJUnitTest {
    
    public PersistenceManagerJUnitTest() {
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
            Logger.getLogger(FileIOJUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
     public void testCreateWriteRead() throws IOException, ClassNotFoundException, Exception {
        System.out.println("mtable2.io.PersistenceManagerJUnitTest.testCreateWriteRead()");
        FileIOImpl<MTable> io = new FileIOImpl<>();
        
        FilePersistenceManager pm = new FilePersistenceManager(io); 
        MTable mtable1= new MTable();
        mtable1.getData().add(new TableData(new Date(), "Table1Cat1", 100.0, "Note_1"));
        mtable1.getData().add(new TableData(new Date(), "Table1Cat2", -10.10, "Note_2"));
        mtable1.getData().add(new TableData(new Date(), "Table1Cat3", -11.20, "Note_3"));
        //create
        pm.create("Table1", mtable1);
        //find
        MTable result = (MTable) pm.find(MTable.class, 1L);
        assertEquals("Should be equal", mtable1.getData().toString(), result.getData().toString());
       
        result.getData().get(2).setCat("Table1ModifietCat");
        //update
        pm.update(result.getId(), result);
        //find
        MTable result1 = (MTable) pm.find(MTable.class, 1L);
        assertEquals("Should be equal", result1.getData().get(2).getCat(),"Table1ModifietCat");
        //delete
        pm.delete(result1.getId(), result1);
        MTable result2 = (MTable) pm.find(MTable.class, 1L);
        assertTrue(result2==null);
        
        System.out.println("mtable2.io.PersistenceManagerJUnitTest.testCreateWriteRead() - OK");
     }
}
