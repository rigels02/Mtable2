
package mtable2.impoexpo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import mtable2.model.MTable;
import mtable2.model.TableData;



/**
 *
 * @author raitis
 */
public class ExportImportJSON implements IExportImport<MTable>{

   

    @Override
    public void exportData(String filePath, List<MTable> tables) throws Exception {
     
        JsonObject jsonTables = createMTableListJsonObject(tables);
        JsonWriter jwriter = Json.createWriter(new FileOutputStream(filePath));
        jwriter.writeObject(jsonTables);
        jwriter.close();
    }

    @Override
    public List<MTable> importData(String filePath) throws IOException, Exception {
        JsonReader jreader = Json.createReader(new FileInputStream(filePath));
        JsonObject jsonTables = jreader.readObject();
        return parseMTableListFromJson(jsonTables);
        
    }


private JsonObject createMTableListJsonObject(List<MTable> tables){
    JsonArrayBuilder tablesArrBld = Json.createArrayBuilder();
        JsonObjectBuilder tablesJsonObj = Json.createObjectBuilder();
      for(MTable table: tables){
       tablesArrBld.add(createMTableJsonObjectBuilder(table));
      }
      return tablesJsonObj.add("tables", tablesArrBld).build();
    }
    
    private JsonObjectBuilder createMTableJsonObjectBuilder(MTable table) {
    JsonArrayBuilder dataBld = Json.createArrayBuilder();
        for (TableData de : table.getData()) {
            JsonObjectBuilder dobj = Json.createObjectBuilder()
                    .add("date", de.getCdate().getTime())
                    .add("cat", de.getCat())
                    .add("amount", de.getAmount())
                    .add("note", de.getNote());
        dataBld.add(dobj);
        }
        
        JsonObjectBuilder tableBld = Json.createObjectBuilder()
                .add("data", dataBld)
                .add("name", table.getName())
                .add("mdate",table.getModTime().getTime());
                
        return tableBld;
           
    }
    
    private JsonObject createMTableJsonObject(MTable table) {
    JsonArrayBuilder dataBld = Json.createArrayBuilder();
        for (TableData de : table.getData()) {
            JsonObjectBuilder dobj = Json.createObjectBuilder()
                    .add("date", de.getCdate().getTime())
                    .add("cat", de.getCat())
                    .add("amount", de.getAmount())
                    .add("note", de.getNote());
        dataBld.add(dobj);
        }
        
        JsonObject tableBld = Json.createObjectBuilder()
                .add("data", dataBld)
                .add("name", table.getName())
                .add("mdate",table.getModTime().getTime())
                .build();
        return tableBld;
           
    }
     private List<MTable> parseMTableListFromJson(JsonObject tjobj){
     
         List<MTable> tables= new ArrayList<>();
         JsonArray tjarr = tjobj.getJsonArray("tables");
         if(tjarr.isEmpty()) return tables;
         
         for(int i=0; i< tjarr.size();i++){
           MTable table= parseMTableFromJson(tjarr.getJsonObject(i));
           tables.add(table);
         }
         return tables;
         
     }
    private MTable parseMTableFromJson(JsonObject jobj){
        MTable table = new MTable();
              
       table.setName(jobj.getString("name"));
       table.setModTime(new Date(jobj.getJsonNumber("mdate").longValue()));
        JsonArray jarr = jobj.getJsonArray("data");
        if(jarr.isEmpty())
            return table;
        for(int i=0; i< jarr.size();i++){
         TableData de= new TableData();
            JsonObject jsonData = jarr.getJsonObject(i);
            de.setCdate(new Date(jsonData.getJsonNumber("date").longValue()));
            de.setCat(jsonData.getString("cat"));
            de.setAmount(jsonData.getJsonNumber("amount").doubleValue());
            de.setNote(jsonData.getString("note"));
           table.getData().add(de);
        }
       return table; 
    }
        
}
