
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
public class CSVJUnitTest {

    private static String csvString;
    private static ArrayList expected;
    private static String fileName = "csvtest.txt";
    private ArrayList<MTable> tables;
    
    public CSVJUnitTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
     File fi = new File(fileName);    
     if(fi.exists())
          fi.delete();
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    private List<MTable> makeTables_10(int emptyIndex){
        ArrayList<MTable> tables = new ArrayList<MTable>();
       for(int i=1 ; i<= 3;i++){
           MTable table = new MTable();
           table.setId(i);
           table.setName("Table_"+i);
           table.setModTime(new Date());
           if(i!=emptyIndex){
           table.getData().add(new TableData(new Date(117,1,10), "Table_"+i+"Cat1", 10.10, "Note_1"));
           table.getData().add(new TableData(new Date(117,2,11), "Table_"+i+"Cat2", 20.10, "Note_2"));
           table.getData().add(new TableData(new Date(117,3,12), "Table_"+i+"Cat3", 30.10, "Note_3"));
           }
         tables.add(table);
       }
       return tables;
    }
    private List<MTable> makeTables_11(int emptyIndex){
        ArrayList<MTable> tables = new ArrayList<MTable>();
       for(int i=1 ; i<= 3;i++){
           MTable table = new MTable();
           table.setId(i);
           table.setName("Table_"+i);
           table.setModTime(new Date());
           if(i!=emptyIndex){
           table.getData().add(new TableData(new Date(117,1,10), "Table_"+i+"Cat1", 10.10, ""));
           table.getData().add(new TableData(new Date(117,2,11), "Table_"+i+"Cat2", 20.10, ""));
           table.getData().add(new TableData(new Date(117,3,12), "Table_"+i+"Cat3", 30.10, ""));
           }
         tables.add(table);
       }
       return tables;
    }
    private List<MTable> makeTables_12(int emptyIndex){
        ArrayList<MTable> tables = new ArrayList<MTable>();
       for(int i=1 ; i<= 3;i++){
           MTable table = new MTable();
           table.setId(i);
           table.setName("Table_"+i);
           table.setModTime(new Date());
           if(i!=emptyIndex){
           table.getData().add(new TableData(new Date(117,1,10), "Table_"+i+"Cat1", 10.10, "Note_1|Cipa"));
           table.getData().add(new TableData(new Date(117,2,11), "Table_"+i+"Cat2", 20.10, "Note_2|Cipa"));
           table.getData().add(new TableData(new Date(117,3,12), "Table_"+i+"Cat3", 30.10, "Note_3|Cipa"));
           }
         tables.add(table);
       }
       return tables;
    }
    @Test
    public void testWriteReadCSVString() throws Exception {
        System.out.println("csvmodel.impoexpo.CSVJUnitTest.testWriteReadCSVString()");
        List<MTable> tables = makeTables_10(-1);
        MTable table = tables.get(0);
        String marker = table.getHeaderMarker();
        String header = table.formatHeader();
        String csv = table.writeCSVString(",");
        MTable table1 = new MTable();
        table1.parseHeader(header);
        table1.readCSVString(csv, ",");
        System.out.println("table1 : "+table1.toString());
        System.out.println("csvmodel.impoexpo.CSVJUnitTest.testWriteReadCSVString() - OK");
    }
    @Test
    public void testExportCSVFile() throws Exception{
        System.out.println("csvmodel.impoexpo.CSVJUnitTest.testExportCSVFile()");
         List<MTable> tables = makeTables_10(-1);
        CSVGenericExportImport<MTable> expo = new CSVGenericExportImport<>(MTable.class);
        expo.exportData(fileName, tables);
        
        System.out.println("csvmodel.impoexpo.CSVJUnitTest.testExportCSVFile() - OK");
    }
    @Test
    public void testImportCSVFile() throws Exception{
        System.out.println("csvmodel.impoexpo.CSVJUnitTest.testImportCSVFile()");
         List<MTable> tables = makeTables_10(-1);
        CSVGenericExportImport expo = new CSVGenericExportImport<>(MTable.class);
        expo.exportData(fileName, tables);
        CSVGenericExportImport impo = new CSVGenericExportImport<>(MTable.class);
        List<MTable> result = impo.importData(fileName);
        for(int i=0 ; i<tables.size();i++){
            assertEquals(tables.get(i).getName(), result.get(i).getName());
            assertEquals(tables.get(i).getData().size(), result.get(i).getData().size());
            List<TableData> data1 = tables.get(i).getData();
            List<TableData> data2 = result.get(i).getData();
            for(int k=0; k <tables.get(i).getData().size(); k++){
                assertEquals(data1.get(k).toString(), data2.get(k).toString());
            }
        }
        
        System.out.println("csvmodel.impoexpo.CSVJUnitTest.testImportCSVFile() - OK");
    }
    @Test
    public void testExpoImpoCSVGenFile3() throws Exception{
        System.out.println("csvmodel.impoexpo.CSVJUnitTest.testExpoImpoCSVGenFile3()");
         List<MTable> tables = makeTables_10(3);
        CSVGenericExportImport<MTable> expo = new CSVGenericExportImport<>(MTable.class);
        expo.exportData(fileName, tables);
        CSVGenericExportImport<MTable> impo = new CSVGenericExportImport<>(MTable.class);
        List<MTable> result = impo.importData(fileName);
        for(int i=0 ; i<tables.size();i++){
            assertEquals(tables.get(i).getName(), result.get(i).getName());
            assertEquals(tables.get(i).getData().size(), result.get(i).getData().size());
            List<TableData> data1 = tables.get(i).getData();
            List<TableData> data2 = result.get(i).getData();
            for(int k=0; k <tables.get(i).getData().size(); k++){
                assertEquals(data1.get(k).toString(), data2.get(k).toString());
            }
        }
        
        System.out.println("csvmodel.impoexpo.CSVJUnitTest.testExpoImpoCSVGenFile3() - OK");
    }
    @Test
    public void testExpoImpoCSVGenFile1() throws Exception{
        System.out.println("csvmodel.impoexpo.CSVJUnitTest.testExpoImpoCSVGenFile1()");
         List<MTable> tables = makeTables_10(1);
        CSVGenericExportImport<MTable> expo = new CSVGenericExportImport<>(MTable.class);
        expo.exportData(fileName, tables);
        CSVGenericExportImport<MTable> impo = new CSVGenericExportImport<>(MTable.class);
        List<MTable> result = impo.importData(fileName);
        for(int i=0 ; i<tables.size();i++){
            assertEquals(tables.get(i).getName(), result.get(i).getName());
            assertEquals(tables.get(i).getData().size(), result.get(i).getData().size());
            List<TableData> data1 = tables.get(i).getData();
            List<TableData> data2 = result.get(i).getData();
            for(int k=0; k <tables.get(i).getData().size(); k++){
                assertEquals(data1.get(k).toString(), data2.get(k).toString());
            }
        }
        
        System.out.println("csvmodel.impoexpo.CSVJUnitTest.testExpoImpoCSVGenFile1() - OK");
    }
    @Test
    public void testExpoImpoCSVGenFile2() throws Exception{
        System.out.println("csvmodel.impoexpo.CSVJUnitTest.testExpoImpoCSVGenFile2()");
         List<MTable> tables = makeTables_10(2);
        CSVGenericExportImport<MTable> expo = new CSVGenericExportImport<>(MTable.class);
        expo.exportData(fileName, tables);
        CSVGenericExportImport<MTable> impo = new CSVGenericExportImport<>(MTable.class);
        List<MTable> result = impo.importData(fileName);
        for(int i=0 ; i<tables.size();i++){
            assertEquals(tables.get(i).getName(), result.get(i).getName());
            assertEquals(tables.get(i).getData().size(), result.get(i).getData().size());
            List<TableData> data1 = tables.get(i).getData();
            List<TableData> data2 = result.get(i).getData();
            for(int k=0; k <tables.get(i).getData().size(); k++){
                assertEquals(data1.get(k).toString(), data2.get(k).toString());
            }
        }
        
        System.out.println("csvmodel.impoexpo.CSVJUnitTest.testExpoImpoCSVGenFile2() - OK");
    }
    private void makeTables(){
       tables = new ArrayList<MTable>();
       for(int i=1 ; i<= 3;i++){
           MTable table = new MTable();
           table.setId(i);
           table.setName("Table_"+i);
           table.setModTime(new Date());
           table.getData().add(new TableData(new Date(117,1,10), "Table_"+i+"Cat1", 10.10, "Note_1"));
           table.getData().add(new TableData(new Date(117,2,11), "Table_"+i+"Cat2", 20.10, "Note_2"));
           table.getData().add(new TableData(new Date(117,3,12), "Table_"+i+"Cat3", 30.10, "Note_3"));
         tables.add(table);
       }
    }
    private void makeTables_1(int emptyIndex){
       tables = new ArrayList<MTable>();
       for(int i=1 ; i<= 3;i++){
           MTable table = new MTable();
           table.setId(i);
           table.setName("Table_"+i);
           table.setModTime(new Date());
           if(i!=emptyIndex){
           table.getData().add(new TableData(new Date(117,1,10), "Table_"+i+"Cat1", 10.10, "Note_1"));
           table.getData().add(new TableData(new Date(117,2,11), "Table_"+i+"Cat2", 20.10, "Note_2"));
           table.getData().add(new TableData(new Date(117,3,12), "Table_"+i+"Cat3", 30.10, "Note_3"));
           }
         tables.add(table);
       }
    }
    @Test
    public void testCSVMTableExport() throws Exception{
        System.out.println("mtable2.impoexpo.CSVJUnitTest.testCSVMTableExport()");
        makeTables();
        CSVGenericExportImport expo = new CSVGenericExportImport<>(MTable.class);
        expo.exportData(fileName, tables);
        
        System.out.println("mtable2.impoexpo.CSVJUnitTest.testCSVMTableExport() - OK");
    }
    @Test
    public void testCSVTableImport() throws Exception{
        System.out.println("mtable2.impoexpo.CSVJUnitTest.testCSVTableImport()");
      makeTables();
        CSVGenericExportImport expo = new CSVGenericExportImport<>(MTable.class);
        expo.exportData(fileName, tables);
        CSVGenericExportImport impo = new CSVGenericExportImport<>(MTable.class);
        List<MTable> result = impo.importData(fileName);
        System.out.println("Result: \n"+result.toString());
        for (MTable table : tables) {
            for (int i=0; i<tables.size();i++) {
                List<TableData> data = tables.get(i).getData();
                List<TableData> data1 = result.get(i).getData();
                assertEquals(data.toString(), data1.toString());
                
            }
            
        }
        
        System.out.println("mtable2.impoexpo.CSVJUnitTest.testCSVTableImport() - OK");
    }
    @Test
    public void testCSVTableImport_1() throws Exception{
        System.out.println("mtable2.impoexpo.CSVJUnitTest.testCSVTableImport_1()");
      makeTables_1(1);
        CSVGenericExportImport expo = new CSVGenericExportImport<>(MTable.class);
        expo.exportData(fileName, tables);
        CSVGenericExportImport impo = new CSVGenericExportImport<>(MTable.class);
        List<MTable> result = impo.importData(fileName);
        System.out.println("Result: \n"+result.toString());
        for (MTable table : tables) {
            for (int i=0; i<tables.size();i++) {
                List<TableData> data = tables.get(i).getData();
                List<TableData> data1 = result.get(i).getData();
                assertEquals(data.toString(), data1.toString());
                
            }
            
        }
        
        System.out.println("mtable2.impoexpo.CSVJUnitTest.testCSVTableImport_1() - OK");
    }
    
    @Test
    public void testCSVTableImport_2() throws Exception{
        System.out.println("mtable2.impoexpo.CSVJUnitTest.testCSVTableImport_2()");
      makeTables_1(3);
        CSVGenericExportImport expo = new CSVGenericExportImport<>(MTable.class);
        expo.exportData(fileName, tables);
        CSVGenericExportImport impo = new CSVGenericExportImport<>(MTable.class);
        List<MTable> result = impo.importData(fileName);
        System.out.println("Result: \n"+result.toString());
        for (MTable table : tables) {
            for (int i=0; i<tables.size();i++) {
                List<TableData> data = tables.get(i).getData();
                List<TableData> data1 = result.get(i).getData();
                assertEquals(data.toString(), data1.toString());
                
            }
            
        }
        
        System.out.println("mtable2.impoexpo.CSVJUnitTest.testCSVTableImport_2() - OK");
    }
    @Test
    public void testCSVImpoSpecial() throws Exception{
        System.out.println("mtable2.impoexpo.CSVJUnitTest.testCSVImpoSpecial()");
        List<MTable> tables0 = makeTables_11(-1);
        CSVGenericExportImport expo = new CSVGenericExportImport<>(MTable.class);
        expo.exportData(fileName, tables0);
      
        CSVGenericExportImport<MTable> impo = new CSVGenericExportImport<>(MTable.class);
        List<MTable> result = impo.importData(fileName);
       System.out.println("Result: \n"+result.toString());
        for (MTable table : tables0) {
            for (int i=0; i<tables0.size();i++) {
                List<TableData> data = tables0.get(i).getData();
                List<TableData> data1 = result.get(i).getData();
                assertEquals(data.toString(), data1.toString());
                
            }
            
        }
        System.out.println("mtable2.impoexpo.CSVJUnitTest.testCSVImpoSpecial() - OK");
    }
    
    @Test
    public void testCSVWithDelimiters() throws Exception{
        System.out.println("mtable2.impoexpo.CSVJUnitTest.testCSVWithDelimiters()");
        List<MTable> tables0 = makeTables_11(-1);
        CSVGenericExportImport expo = new CSVGenericExportImport<>(MTable.class,"|"); //"|" , \u007C
        expo.exportData(fileName, tables0);
      
        CSVGenericExportImport<MTable> impo = new CSVGenericExportImport<>(MTable.class,"\\|"); //"\\|"
        List<MTable> result = impo.importData(fileName);
       System.out.println("Result: \n"+result.toString());
        for (MTable table : tables0) {
            for (int i=0; i<tables0.size();i++) {
                List<TableData> data = tables0.get(i).getData();
                List<TableData> data1 = result.get(i).getData();
                assertEquals(data.toString(), data1.toString());
                
            }
            
        }
        System.out.println("mtable2.impoexpo.CSVJUnitTest.testCSVWithDelimiters() - OK");
    }
}
