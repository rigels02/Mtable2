
package mtable2.impoexpo.v1;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mtable2.control.DataControl;
import mtable2.control.DataControlJUnitTest;
import mtable2.io.FileIOImpl;
import mtable2.io.FilePersistenceManager;
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
public class ExporterJUnitTest {
    
    public ExporterJUnitTest() {
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
    public void testForThreeTables() throws IOException, ClassNotFoundException, Exception {
        System.out.println("mtable2.impoexpo.v1.ExporterJUnitTest.testForThreeTables()");
        FileIOImpl<MTable> io = new FileIOImpl<>();
        io.deleteAll();
        FilePersistenceManager<MTable> pm = new FilePersistenceManager<>(io);
        DataControl ctrl = new DataControl(pm);
        ctrl.create("Table_1", new MTable());
        TableData tdata = new TableData(new Date(117,2,17), "Table_1Cat1", 12.126, "Note_1");
        ctrl.addItem(tdata);
        int Id1 = ctrl.getSelectedTableIdx();
        assertTrue(Id1==0);
        assertTrue(ctrl.getTableInfoLst().size()==1);
        assertTrue(ctrl.getItems().get(0).toString().equals(tdata.toString()));
        //----Table_2
        ctrl.create("Table_2", new MTable());
        TableData tdata1 = new TableData(new Date(117,2,17), "Table_2Cat1", 12.555, "Note_1");
        ctrl.addItem(tdata1);
        int Id2 = ctrl.getSelectedTableIdx();
        assertTrue(Id2==1);
        assertTrue(ctrl.getTableInfoLst().size()==2);
        assertTrue(ctrl.getItems().get(0).toString().equals(tdata1.toString()));
        
        //----Table_3
        ctrl.create("Table_3", new MTable());
        TableData tdata2 = new TableData(new Date(117,2,17), "Table_3Cat1", 12.0, "Note_1");
        ctrl.addItem(tdata2);
        int Id3 = ctrl.getSelectedTableIdx();
        assertTrue(Id3==2);
        assertTrue(ctrl.getTableInfoLst().size()==3);
        assertTrue(ctrl.getItems().get(0).toString().equals(tdata2.toString()));
        //try to export to dump string
        //List<MTable> tlst = ctrl.getTables();
        List<MTable> tlst = pm.findAll(MTable.class);
        Exporter exp = new Exporter(new TManager(tlst));
         exp.dumpAll();
       
        String expected = "\nTable_1,0,2,4,0,1,2,3,,,\n" +
                "{17/03/2017|Table_1Cat1|12.13|Note_1|\n" +
                "}\n" +
                "Table_2,0,2,4,0,1,2,3,,,\n" +
                "{17/03/2017|Table_2Cat1|12.55|Note_1|\n" +
                "}\n" +
                "Table_3,0,2,4,0,1,2,3,,,\n" +
                "{17/03/2017|Table_3Cat1|12|Note_1|\n" +
                "}\n";
        String res = exp.getDumptxt().toString();
        assertEquals(expected, exp.getDumptxt().toString());
        System.out.println("Expoerted:\n"+exp.getDumptxt().toString());
        System.out.println("mtable2.impoexpo.v1.ExporterJUnitTest.testForThreeTables() - OK");
        
    
    }
    
    @Test
    public void testDecimalFormater(){
    //DecimalFormat df = new DecimalFormat("0.00");
    DecimalFormat df = new DecimalFormat("#.##");
     //to get always two digits after point.
    //df.setMinimumFractionDigits(2);
    double v1= 0.0;
    double v2= 12.10;
    double v3= 1112.00;
    double v4 = 12.34;
    double v5= -12.34;
        System.out.println(
                String.format("v1=%s : v2=%s : v3=%s : v4=%s : v5=%s ",
                df.format(v1),df.format(v2),df.format(v3),df.format(v4),df.format(v5)
                )
        );
    }
}
