package mtable2.io;

import java.io.File;
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
public class MPropertiesJUnitTest {
    
    public MPropertiesJUnitTest() {
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
        File fi = new File(MProperties.PROPFILE);
        if(fi.exists()) fi.delete();
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void testSaveReadProp() throws Exception {
        System.out.println("mtable2.io.MPropertiesJUnitTest.testSaveProp()");
        String oip= "156.3.157.102";
        MProperties.create().saveIpProperty(oip);
        String sip = MProperties.create().readSavedIpProperty();
        assertEquals(oip, sip);
        System.out.println("mtable2.io.MPropertiesJUnitTest.testSaveProp() - OK");
    }
    @Test
    public void testReadPropNotFileExists() throws Exception{
        System.out.println("mtable2.io.MPropertiesJUnitTest.testReadProp()");
        String sip = MProperties.create().readSavedIpProperty();
        assertTrue(sip==null);
        System.out.println("mtable2.io.MPropertiesJUnitTest.testReadProp() - OK");
    }
    @Test
    public void testReadUnexistingProperty() throws Exception{
        System.out.println("mtable2.io.MPropertiesJUnitTest.testReadUnexistingProperty()");
        //create prop file
        MProperties prop = MProperties.create();
        prop.createPropertyFile();
        String sip= prop.readSavedIpProperty();
        assertTrue(sip==null);
        System.out.println("mtable2.io.MPropertiesJUnitTest.testReadUnexistingProperty() - OK");
    }
}
