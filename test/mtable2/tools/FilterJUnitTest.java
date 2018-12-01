
package mtable2.tools;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class FilterJUnitTest {
    
    public FilterJUnitTest() {
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
    public void testFilterBYNoFilter() throws Exception {
        System.out.println("mtable2.tools.FilterJUnitTest.testFilterBYNoFilter()");
        
        List<TableData> data= new ArrayList<>();
        data.add(new TableData(new Date(), "Cat1", 1.0, "Note1"));
        data.add(new TableData(new Date(), "Cat2", 2.0, "Note2"));
        data.add(new TableData(new Date(), "Cat3", 3.0, "Note3"));
        data.add(new TableData(new Date(), "Cat4", 4.0, "Note4"));
        TableDataFilter filter = new TableDataFilter(null, null, null, null, data);
        List<TableData> result = filter.filter(data);
        assertTrue(result.size()==4);
        System.out.println("mtable2.tools.FilterJUnitTest.testFilterBYNoFilter() - OK");
    }
    @Test
    public void testFilterBYDateOne() throws Exception {
        System.out.println("mtable2.tools.FilterJUnitTest.testFilterBYDateOne()");
        
        List<TableData> data= new ArrayList<>();
        TableData data1; TableData data2; TableData data3;TableData data4;
        data.add(data1=new TableData(new Date(117,2,14), "Cat1", 1.0, "Note1"));
        data.add(data2=new TableData(new Date(117,2,15), "Cat2", 2.0, "Note2"));
        data.add(data3=new TableData(new Date(117,2,16), "Cat3", 3.0, "Note3"));
        data.add(data4=new TableData(new Date(117,2,17), "Cat4", 4.0, "Note4"));
        TableDataFilter filter = new TableDataFilter(new Date(117,2,16), new Date(117,2,16), null, null, data);
        List<TableData> result = filter.filter(data);
        assertTrue(result.size()==1);
        assertEquals(result.get(0), data3);
        
        System.out.println("mtable2.tools.FilterJUnitTest.testFilterBYDateOne() - OK");
    }
    @Test
    public void testFilterBYDateRange() throws Exception {
        System.out.println("mtable2.tools.FilterJUnitTest.testFilterBYDateRange()");
        
        List<TableData> data= new ArrayList<>();
        TableData data1; TableData data2; TableData data3;TableData data4;
        data.add(data1=new TableData(new Date(117,2,14), "Cat1", 1.0, "Note1"));
        data.add(data2=new TableData(new Date(117,2,15), "Cat2", 2.0, "Note2"));
        data.add(data3=new TableData(new Date(117,2,16), "Cat3", 3.0, "Note3"));
        data.add(data4=new TableData(new Date(117,2,17), "Cat4", 4.0, "Note4"));
        TableDataFilter filter = new TableDataFilter(new Date(117,2,15), new Date(117,2,16), null, null, data);
        List<TableData> result = filter.filter(data);
        assertTrue(result.size()==2);
        assertEquals(result.get(0), data2);
        assertEquals(result.get(1), data3);
        
        System.out.println("mtable2.tools.FilterJUnitTest.testFilterBYDateRange() - OK");
    }
    @Test
    public void testFilterBYDateUntil() throws Exception {
        System.out.println("mtable2.tools.FilterJUnitTest.testFilterBYDateUntil()");
        
        List<TableData> data= new ArrayList<>();
        TableData data1; TableData data2; TableData data3;TableData data4;
        data.add(data1=new TableData(new Date(117,2,14), "Cat1", 1.0, "Note1"));
        data.add(data2=new TableData(new Date(117,2,15), "Cat2", 2.0, "Note2"));
        data.add(data3=new TableData(new Date(117,2,16), "Cat3", 3.0, "Note3"));
        data.add(data4=new TableData(new Date(117,2,17), "Cat4", 4.0, "Note4"));
        TableDataFilter filter = new TableDataFilter(null, new Date(117,2,16), null, null, data);
        List<TableData> result = filter.filter(data);
        assertTrue(result.size()==3);
        assertEquals(result.get(0), data1);
        assertEquals(result.get(1), data2);
        assertEquals(result.get(2), data3);
        
        System.out.println("mtable2.tools.FilterJUnitTest.testFilterBYDateUntil() - OK");
    }
    @Test
    public void testFilterBYDateAfter() throws Exception {
        System.out.println("mtable2.tools.FilterJUnitTest.testFilterBYDateAfter()");
        
        List<TableData> data= new ArrayList<>();
        TableData data1; TableData data2; TableData data3;TableData data4;
        data.add(data1=new TableData(new Date(117,2,14), "Cat1", 1.0, "Note1"));
        data.add(data2=new TableData(new Date(117,2,15), "Cat2", 2.0, "Note2"));
        data.add(data3=new TableData(new Date(117,2,16), "Cat3", 3.0, "Note3"));
        data.add(data4=new TableData(new Date(117,2,17), "Cat4", 4.0, "Note4"));
        TableDataFilter filter = new TableDataFilter(new Date(117,2,16), null, null, null, data);
        List<TableData> result = filter.filter(data);
        assertTrue(result.size()==2);
        assertEquals(result.get(0), data3);
        assertEquals(result.get(1), data4);
        
        
        System.out.println("mtable2.tools.FilterJUnitTest.testFilterBYDateAfter() - OK");
    }
    
    @Test
    public void testFilterBYCat() throws Exception {
        System.out.println("mtable2.tools.FilterJUnitTest.testFilterBYCat()");
        
        List<TableData> data= new ArrayList<>();
        TableData data1; TableData data2; TableData data3;TableData data4;
        data.add(data1=new TableData(new Date(117,2,14), "Cat1", 1.0, "Note1"));
        data.add(data2=new TableData(new Date(117,2,15), "Cat2", 2.0, "Note2"));
        data.add(data3=new TableData(new Date(117,2,16), "Cat3", 3.0, "Note3"));
        data.add(data4=new TableData(new Date(117,2,17), "Cat4", 4.0, "Note4"));
        TableDataFilter filter = new TableDataFilter(null, null, "Cat3", null, data);
        List<TableData> result = filter.filter(data);
        assertTrue(result.size()==1);
        assertEquals(result.get(0), data3);
        
        System.out.println("mtable2.tools.FilterJUnitTest.testFilterBYCat() - OK");
    }
    
     @Test
    public void testFilterBYNote() throws Exception {
        System.out.println("mtable2.tools.FilterJUnitTest.testFilterBYNote()");
        
        List<TableData> data= new ArrayList<>();
        TableData data1; TableData data2; TableData data3;TableData data4;
        data.add(data1=new TableData(new Date(117,2,14), "Cat1", 1.0, "Note1"));
        data.add(data2=new TableData(new Date(117,2,15), "Cat2", 2.0, "Note2"));
        data.add(data3=new TableData(new Date(117,2,16), "Cat3", 3.0, "Note3"));
        data.add(data4=new TableData(new Date(117,2,17), "Cat4", 4.0, "Note4"));
        TableDataFilter filter = new TableDataFilter(null, null, null, "Note3", data);
        List<TableData> result = filter.filter(data);
        assertTrue(result.size()==1);
        assertEquals(result.get(0), data3);
        
        System.out.println("mtable2.tools.FilterJUnitTest.testFilterBYNote() - OK");
    }
    @Test
    public void testFilterBYCatNote() throws Exception {
        System.out.println("mtable2.tools.FilterJUnitTest.testFilterBYCatNote()");
        
        List<TableData> data= new ArrayList<>();
        TableData data1; TableData data2; TableData data3;TableData data4;
        data.add(data1=new TableData(new Date(117,2,14), "Cat1", 1.0, "Note1"));
        data.add(data2=new TableData(new Date(117,2,15), "Cat2", 2.0, "Note2"));
        data.add(data3=new TableData(new Date(117,2,16), "Cat3", 3.0, "Note3"));
        data.add(data4=new TableData(new Date(117,2,17), "Cat4", 4.0, "Note4"));
        TableDataFilter filter = new TableDataFilter(null, null, "Cat3", "Note3", data);
        List<TableData> result = filter.filter(data);
        assertTrue(result.size()==1);
        assertEquals(result.get(0), data3);
        
        System.out.println("mtable2.tools.FilterJUnitTest.testFilterBYCatNote() - OK");
    }
    @Test
    public void testFilterEmptyNotesList() throws Exception{
        System.out.println("mtable2.tools.FilterJUnitTest.testFilterEmptyNotesList()");
        List<TableData> data= new ArrayList<>();
        TableData data1; TableData data2; TableData data3;TableData data4;
        data.add(data1=new TableData(new Date(117,2,14), "Cat1", 1.0, "Note1"));
        data.add(data2=new TableData(new Date(117,2,15), "Cat2", 2.0, ""));
        data.add(data3=new TableData(new Date(117,2,16), "Cat3", 3.0, "Note3"));
        data.add(data4=new TableData(new Date(117,2,17), "Cat4", 4.0, ""));
        TableDataFilter filter = new TableDataFilter(null, null, "Cat3", "Note3", data);
        List<TableData> result = filter.filter(data);
        assertTrue(result.size()==1);
        assertEquals(result.get(0), data3);
        List<String> notes = filter.getNotes();
        assertTrue(notes.size()==2);
        System.out.println("mtable2.tools.FilterJUnitTest.testFilterEmptyNotesList() - OK");
    }
}
