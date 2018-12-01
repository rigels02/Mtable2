
package mtable2.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author raitis
 */

public class TableData implements Serializable,Cloneable{
static final long  serialVersionUID = 1L;

    public static int FCOUNT= 4;
    
    private long Id;
    private Date cdate;
    private String cat;
    private Double amount;
    private String note;

    public TableData() {
    }

    public TableData(Date cdate, String cat, Double amount, String note) {
        this.cdate = cdate;
        this.cat = cat;
        this.amount = amount;
        this.note = note;
    }

    
    private TableData(TableData other) {
        this.Id = other.Id;
        this.cdate = other.cdate;
        this.cat = other.cat;
        this.amount = other.amount;
        this.note = other.note;  
    }

    

    public Date getCdate() {
        return cdate;
    }

    public void setCdate(Date cdate) {
        this.cdate = cdate;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getId() {
        return Id;
    }

    public void setId(long Id) {
        this.Id = Id;
    }

    public static void putToStream(DataOutputStream dou, TableData data) throws IOException {
        dou.writeLong(data.getId());
        dou.writeLong(data.getCdate().getTime());
        dou.writeUTF(data.getCat());
        dou.writeDouble(data.getAmount());
        dou.writeUTF(data.getNote());
    }

    public static TableData getFromStream(DataInputStream din) throws IOException {
    TableData data = new TableData();
    data.setId(din.readLong());
    data.setCdate(new Date(din.readLong()));
    data.setCat(din.readUTF());
    data.setAmount(din.readDouble());
    data.setNote(din.readUTF());
    return data;
    }
    
     /**
     * Make shallow copy 
     * Should be overriden in subclasses
     * @return 
     */
    public TableData mkCopy(){
        return new TableData(this);
     }
    /**
     * Make deep copy
     *  Should be overriden in subclasses
     * @return
     * @throws Exception 
     */
    public TableData deepCopy() throws Exception {
        //Check if T is instance of Serializeble other throw CloneNotSupportedException
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        //Serialize it
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(this);

        byte[] bytes = bos.toByteArray();
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));

        //Deserialize it and return the new instance
        return (TableData) ois.readObject();
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone(); 
    }
    public TableData mkCloaning() throws CloneNotSupportedException{
        TableData data = (TableData) this.clone();
        data.setCdate((Date) this.getCdate().clone());
       return data;
    }
    public String formated(){
      return ""+"cdate=" + cdate + ", cat=" + cat + ", amount=" + amount + ", note=" + note;
    }
    @Override
    public String toString() {
        return "TableData{" + "Id=" + Id + ", cdate=" + cdate + ", cat=" + cat + ", amount=" + amount + ", note=" + note + '}';
    }

    
}
