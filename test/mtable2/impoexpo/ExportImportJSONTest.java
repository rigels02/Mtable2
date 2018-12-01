
package mtable2.impoexpo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
public class ExportImportJSONTest {
    
    public ExportImportJSONTest() {
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

    
    //***********************************************//
    private List<MTable> createTables(int tn,int sid){
        List<MTable> tables= new ArrayList<>();
        for(int i=1; i<= tn; i++){
         MTable table= new MTable();
        table.setModTime(new Date());
        table.setName("Table_"+i);
        if(i != sid)
          for(int d=1; d<= 3; d++){
           table.getData().add(new TableData(new Date(), "Cat_"+i+""+d, 10.33d+d, "Note_"+i+""+d));
        
          }
        tables.add(table);
        }
        return tables;
    }
    private boolean isMtablesEQ(List<MTable> tables1,List<MTable> tables2){
        if(tables1.size()!= tables2.size()) return false;
        for(int i=0; i< tables1.size();i++){
            MTable table1 = tables1.get(i);
            MTable table2 = tables2.get(i);
            if(!table1.getName().equals(table2.getName())
                    || !table1.getModTime().equals(table2.getModTime())
                    || table1.isSelected()!= table2.isSelected())
                  return false;
            if(table1.getData().size() != table2.getData().size())
                  return false;
            for(int d=0; d< table1.getData().size(); d++){
                TableData data1 = table1.getData().get(d);
                TableData data2 = table2.getData().get(d);
                if( !data1.getCdate().equals(data2.getCdate())
                        || !data1.getCat().equals(data2.getCat())
                        ||( !Objects.equals(data1.getAmount(), data2.getAmount()))
                        || !data1.getNote().equals(data2.getNote()) )
                    return false;
            }
        }
        return true;
    }
    //***********************************************//
    /**
     * Test of exportData method, of class ExportImportJSON.
     */
    @Test
    public void testExportData() throws Exception {
        System.out.println("exportData");
        String filePath = "tablesJson.txt";
        List<MTable> tables = createTables(3, -1);
        ExportImportJSON instance = new ExportImportJSON();
        instance.exportData(filePath, tables);
        //----------//
        ExportImportJSON instance1 = new ExportImportJSON();
        
        List<MTable> result = instance1.importData(filePath);
        assertTrue(isMtablesEQ(tables, result));
         
    }

    
}
