package mtable2.model;

import java.io.Serializable;
import java.util.Date;


/**
 *
 * @author raitis
 */
public class TableInfo implements Serializable, Cloneable {
static final long  serialVersionUID = 1L;

private long Id;
private String name;
private Date modTime;
private boolean selected;

    public TableInfo() {
    }

    
    public TableInfo(String name, Date modTime) {
        this.name = name;
        this.modTime = modTime;
    }

    public TableInfo(long Id, String name, Date modTime) {
        this.Id = Id;
        this.name = name;
        this.modTime = modTime;
       
    }

    
    /*private TableInfo(TableInfo other) {
    this.Id = other.Id;
    this.modTime = other.modTime;
    this.name = other.name;
    this.selected= other.selected;
    }*/


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getModTime() {
        return modTime;
    }

    public void setModTime(Date modTime) {
        this.modTime = modTime;
    }

    public long getId() {
        return Id;
    }

    public void setId(long Id) {
        this.Id = Id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    
    /*public TableInfo mkCopy()  {
    return new TableInfo(this);
    }*/

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone(); 
    }

    public TableInfo mkCloaning() throws CloneNotSupportedException{
        TableInfo data = (TableInfo) this.clone();
        data.setModTime((Date) this.getModTime().clone());
       return data;
    }
   

    public String formated(){
      return " "+"Id=" + Id + ", name=" + name + ", modTime=" + modTime; 
    }
    @Override
    public String toString() {
        return "TableInfo{" + "Id=" + Id + ", name=" + name + ", modTime=" + modTime + '}';
    }

    



}
