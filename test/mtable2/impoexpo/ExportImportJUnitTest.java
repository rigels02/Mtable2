
package mtable2.impoexpo;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class ExportImportJUnitTest {
    
    public ExportImportJUnitTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
        File fi = new File("Exported.txt");
        fi.delete();
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
        
    }

    @Test
    public void testExport() throws Exception {
        System.out.println("mtable2.impoexpo.ExportImportJUnitTest.testExport()");
        OldExportImport expImpo = new OldExportImport();
        MTable table = new MTable();
        table.setName("Table_1");
        table.setId(1); //index is important!
        table.getData().add(new TableData(new Date(), "Table_1Cat1", 10.23, "Note_1"));
        table.getData().add(new TableData(new Date(), "Table_1Cat2", 11.23, "Note_1"));
        table.getData().add(new TableData(new Date(), "Table_1Cat3", 12.23, "Note_1"));
        MTable table1 = new MTable();
        table1.setName("Table_2");
        table1.setId(2);
        table1.getData().add(new TableData(new Date(), "Table_2Cat1", 20.23, "Note_2"));
        table1.getData().add(new TableData(new Date(), "Table_2Cat2", 21.23, "Note_2"));
        table1.getData().add(new TableData(new Date(), "Table_2Cat3", 22.23, "Note_2"));
        MTable table3 = new MTable();
        table3.setName("Table_3");
        table3.setId(3);
        table3.getData().add(new TableData(new Date(), "Table_3Cat1", 30.23, "Note_3"));
        table3.getData().add(new TableData(new Date(), "Table_3Cat2", 31.23, "Note_3"));
        table3.getData().add(new TableData(new Date(), "Table_3Cat3", 32.23, "Note_3"));
        ArrayList<MTable> lst = new ArrayList<>();
        lst.add(table);
        lst.add(table1);
        lst.add(table3);
        expImpo.exportData("Exported.txt", lst);
        File fi = new File("Exported.txt");
        assertTrue(fi.exists());
        System.out.println("mtable2.impoexpo.ExportImportJUnitTest.testExport() - OK");
    }
    
    @Test
    public void testImport() throws Exception{
        System.out.println("mtable2.impoexpo.ExportImportJUnitTest.testImport()");
        OldExportImport expImpo = new OldExportImport();
        MTable table = new MTable();
        List<MTable> result = expImpo.importData("Exported.txt");
        System.out.println("Result: "+result.toString());
        System.out.println("mtable2.impoexpo.ExportImportJUnitTest.testImport() - OK");
    }
}
