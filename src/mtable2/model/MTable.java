
package mtable2.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import mtable2.impoexpo.ICSV;



/**
 *
 * @author raitis
 */
public class MTable extends TableInfo implements Serializable, ICSV{
    static final long  serialVersionUID = 1L;

    public final static int VERSION= 1;
    
    public static void putToStream(DataOutputStream dou, MTable table) throws IOException {
     dou.writeInt(VERSION);
     dou.writeLong(table.getId());
     dou.writeUTF(table.getName());
     dou.writeLong(table.getModTime().getTime());
     dou.writeBoolean(table.isSelected());
     dou.writeInt(table.getData().size());
        for (TableData row : table.getData()) {
            TableData.putToStream(dou, row);
        }
    }

    public static MTable getFromStream(DataInputStream din) throws IOException {
        if( din.readInt() != VERSION)
              throw new IOException("Wrong MTable VERSION number received!");
        MTable table = new MTable();
        table.setId(din.readLong());
        table.setName(din.readUTF());
        table.setModTime(new Date(din.readLong()));
        table.setSelected(din.readBoolean());
        int sz = din.readInt();
        List<TableData> data= new ArrayList<>();
        
        for(int i=0; i< sz; i++){
         data.add(TableData.getFromStream(din));
        }
        table.setData(data);
        return table;
    }
  
    
    private List<TableData> data= new ArrayList<>();

    public MTable() {
    }

    

    public List<TableData> getData() {
        return data;
    }

    public void setData(List<TableData> data) {
        this.data = data;
    }

    
    @Override
    public String toString() {
        return "MTable{"+super.toString()+", data=" + data + '}';
    }

    //-----For CSV -------
     @Override
    public String getHeaderMarker() {
        return "#MTable ";
    }

    @Override
    public String formatHeader() {
        return getHeaderMarker() + this.getName();
    }

    @Override
    public void parseHeader(String headerString) {
        String name = headerString.substring(getHeaderMarker().length()).trim();
        this.setId(0);
        //there is no date kept in CSV
        this.setModTime(null);
        this.setSelected(false);
        this.setName(name);
    }

    private final static String DFORMAT="dd/MM/yyyy";
    @Override
    public String writeCSVString(String delimiter) {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sf = new SimpleDateFormat(DFORMAT);
        for (TableData row : data) {
            sb.append(sf.format(row.getCdate())).append(delimiter);
            sb.append(row.getCat()).append(delimiter).append(row.getAmount()).append(delimiter);
            sb.append(row.getNote().replace(delimiter, " ")).append("\n");
        }
        return sb.toString();
    }

    @Override
    public void readCSVString(String csvRows, String delimiter) throws Exception {
        if (csvRows.isEmpty()) {
            return;
        }

        SimpleDateFormat sf = new SimpleDateFormat(DFORMAT);
        String[] rows = csvRows.split("\n");
        for (String row : rows) {
            String[] fields = row.split(delimiter,4);
            for (int i=0; i<fields.length;i++) {
                fields[i]= fields[i].trim();
            }
            TableData dataEl = new TableData();
            dataEl.setCdate(sf.parse(fields[0]));
            dataEl.setCat(fields[1]);
            dataEl.setAmount(Double.parseDouble(fields[2]));
            dataEl.setNote(fields[3]);
            dataEl.setId(0);
            data.add(dataEl);
        }
    }
}
