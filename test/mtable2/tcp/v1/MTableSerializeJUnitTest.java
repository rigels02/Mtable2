package mtable2.tcp.v1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
public class MTableSerializeJUnitTest {
    
    public MTableSerializeJUnitTest() {
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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void testMTableSerialize() throws IOException {
        System.out.println("mtable2.tcp.v1.MTableSerializeJUnitTest.testMTableSerialize()");

        MTable table = new MTable();
        table.setId(1);
        table.setName("Table_1");
        table.setModTime(new Date());
        table.getData().add(new TableData(new Date(), "Table_1" + "Cat1", 10.0, "Note_1"));
        table.getData().add(new TableData(new Date(), "Table_1" + "Cat2", 11.0, "Note_2"));
        table.getData().add(new TableData(new Date(), "Table_1" + "Cat3", 12.0, "Note_3"));

        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        DataOutputStream dou = new DataOutputStream(ba);
        
        MTable.putToStream(dou, table);
        byte[] buf = ba.toByteArray();
        
        ByteArrayInputStream bi = new ByteArrayInputStream(buf);
        
        DataInputStream din = new DataInputStream(bi);
        MTable table1= MTable.getFromStream(din);
        assertEquals(table1.toString(), table.toString());
        System.out.println("mtable2.tcp.v1.MTableSerializeJUnitTest.testMTableSerialize() - OK");
    }
    
    @Test
    public void testMTableSerialize1() throws IOException {
        System.out.println("mtable2.tcp.v1.MTableSerializeJUnitTest.testMTableSerialize1()");

        MTable table = new MTable();
        table.setId(1);
        table.setName("Table_1");
        table.setModTime(new Date());
        table.getData().add(new TableData(new Date(), "Table_1" + "Cat1", 10.0, ""));
        table.getData().add(new TableData(new Date(), "Table_1" + "Cat2", 11.0, ""));
        table.getData().add(new TableData(new Date(), "Table_1" + "Cat3", 12.0, ""));

        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        DataOutputStream dou = new DataOutputStream(ba);
        
        MTable.putToStream(dou, table);
        byte[] buf = ba.toByteArray();
        
        ByteArrayInputStream bi = new ByteArrayInputStream(buf);
        
        DataInputStream din = new DataInputStream(bi);
        MTable table1= MTable.getFromStream(din);
        assertEquals(table1.toString(), table.toString());
        System.out.println("mtable2.tcp.v1.MTableSerializeJUnitTest.testMTableSerialize1() - OK");
    }
}
