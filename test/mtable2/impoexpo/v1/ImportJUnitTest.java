package mtable2.impoexpo.v1;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import mtable2.control.DataControl;
import mtable2.io.FileIOImpl;
import mtable2.io.FilePersistenceManager;
import mtable2.model.MTable;
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
public class ImportJUnitTest {

    public ImportJUnitTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    String impoStr1 = "\nTable_1,0,2,4,0,1,2,3,,,\n"
            + "{17/03/2017|Table_1Cat1|12.13|Note_1|\n"
            + "}\n"
            + "Table_2,0,2,4,0,1,2,3,,,\n"
            + "{17/03/2017|Table_2Cat1|12.55|Note_1|\n"
            + "}\n"
            + "Table_3,0,2,4,0,1,2,3,,,\n"
            + "{17/11/2017|Table_3Cat1|12.0|Note_1|\n"
            + "}\n";

    String impoStrError = "\nTable_1,0,2,4,0,1,2,3,,,\n"
            + "{17/03/2017|Table_1Cat1|12.13|Note_1|\n"
            + "}\n"
            + "Table_2,0,2,4,0,1,2,3,,,\n"
            + "{17/03/2017|Table_2Cat1|12.55|Note_1|\n"
            + "}\n"
            + "Table_3,0,2,4,0,1,2,3,,,\n"
            + "{17/03/2017|Table_3Cat1|1a.0|Note_1|\n"
            + "}\n";

    @Test
    public void testImport() throws IOException, ClassNotFoundException, Exception {
        System.out.println("mtable2.impoexpo.v1.ImportJUnitTest.testImport()");

        FileIOImpl<MTable> io = new FileIOImpl<>();
        io.deleteAll();
        FilePersistenceManager<MTable> pm = new FilePersistenceManager<>(io);
        DataControl ctrl = new DataControl(pm);
        TManager tmgr;
        Importer impo = new Importer(tmgr = new TManager(null));
        boolean ok = impo.execImport(impoStr1);

        assertTrue(ok);
        List<MTable> result = tmgr.getMTables();
        String sresult = result.toString();
        
        System.out.println("Mtables:\n" + result.toString());
        assertTrue(result.size() == 3);
        assertTrue(result.get(0).getName().equals("Table_1"));
        assertTrue(result.get(1).getName().equals("Table_2"));
        assertTrue(result.get(2).getName().equals("Table_3"));
        assertTrue(result.get(0).getData().get(0).getCat().equals("Table_1Cat1"));
        assertTrue(result.get(1).getData().get(0).getCat().equals("Table_2Cat1"));
        assertTrue(result.get(2).getData().get(0).getCat().equals("Table_3Cat1"));
        //check dates
        
        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
        assertTrue(sf.format(result.get(0).getData().get(0).getCdate()).equals("17/03/2017"));
        assertTrue(sf.format(result.get(1).getData().get(0).getCdate()).equals("17/03/2017"));
        assertTrue(sf.format(result.get(2).getData().get(0).getCdate()).equals("17/11/2017"));
        //check amount fields
        DecimalFormat df = new DecimalFormat("#.##");
        assertTrue(df.format(result.get(0).getData().get(0).getAmount()).equals("12.13"));
        assertTrue(df.format(result.get(1).getData().get(0).getAmount()).equals("12.55"));
        assertTrue(df.format(result.get(2).getData().get(0).getAmount()).equals("12"));
        
        System.out.println("mtable2.impoexpo.v1.ImportJUnitTest.testImport() - OK");

    }

     @Test
    public void testImportError1() throws IOException, ClassNotFoundException, Exception {
        System.out.println("mtable2.impoexpo.v1.ImportJUnitTest.testImportError1()");

        FileIOImpl<MTable> io = new FileIOImpl<>();
        io.deleteAll();
        FilePersistenceManager<MTable> pm = new FilePersistenceManager<>(io);
        DataControl ctrl = new DataControl(pm);
        TManager tmgr;
        Importer impo = new Importer(tmgr = new TManager(null));
        boolean ok = impo.execImport(impoStrError);

        assertTrue(!ok);
        String err = impo.errorTxt();
         assertTrue(!err.isEmpty());
         System.out.println("Error text received: "+err);
        List<MTable> result = tmgr.getMTables();
        //If error then returned MTable list is empty!
         assertTrue(result.isEmpty());
        
        System.out.println("mtable2.impoexpo.v1.ImportJUnitTest.testImportError1() - OK");

    }

}
