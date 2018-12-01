
package mtable2.tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import mtable2.model.TableData;
/**
 *
 * @author raitis
 */
public class TableDataFilter {
    
    private Date startDate;
    private Date endDate;
    //Case sensitive
    private String category;
    //Case sensitive
    private String note;

    private List<String> categories;
    private List<String> notes;
    //private final List<TableData> filterData;
    private List<TableData> origData;
    
    public TableDataFilter(Date startDate, Date endDate, String category, String note,List<TableData> data) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.category = category;
        this.note = note;
        //filterData = new ArrayList<>();
        origData = data;
        prepareCategoryNoteLists();
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getCategory() {
        return category;
    }

    public String getNote() {
        return note;
    }

    public List<String> getCategories() {
        return categories;
    }

    public List<String> getNotes() {
        return notes;
    }

    private void prepareCategoryNoteLists(){
     if(origData==null) throw new RuntimeException("OrigData == NULL!");
     categories= new ArrayList<>();
     notes = new ArrayList<>();
        for (TableData el : origData) {
            if( !el.getCat().trim().isEmpty() &&!categories.contains(el.getCat()) ){
              categories.add(el.getCat());
            }
        }
        for (TableData el : origData) {
            if( !el.getNote().trim().isEmpty() && !notes.contains(el.getNote()) ){
              notes.add(el.getNote());
            }
        }
    }
    
    public List<TableData> getOrigData() {
        return origData;
    }

    public void setOrigData(List<TableData> origData) {
        this.origData = origData;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setNote(String note) {
        this.note = note;
    }
    
    public List<TableData> filterByDate(List<TableData> incomeData) throws Exception{
       if(incomeData==null) throw new RuntimeException("Input TableData is NULL!");
        List<TableData> fData= new ArrayList<>();
        //use any date
      if(startDate==null && endDate==null){
        return incomeData;
      }
      if(startDate!=null && endDate!=null){
          if(startDate.after(endDate)) throw new IllegalArgumentException("StartDate is after EndDate!");
      //only for selected date
      if( startDate.equals(endDate)){
          for (TableData tableData : incomeData) {
              if(tableData.getCdate().equals(startDate)){
                fData.add(tableData);
              }
          }
          return fData;
      }
      //date between startDate and EndDate
          for (TableData tableData : incomeData) {
              if(tableData.getCdate().compareTo(startDate)>=0 && 
                      tableData.getCdate().compareTo(endDate)<=0 ){
               fData.add(tableData);
              } 
          }
          return fData;
      }
      //all until endDate
      if(startDate==null && endDate!=null){
          for (TableData tableData : incomeData) {
              if(tableData.getCdate().compareTo(endDate)<= 0 ){
               fData.add(tableData);
              }
          }
          return fData;
      }
      //from startDate to TODAY included
      if(startDate!=null && endDate==null){
           Date today = new Date();
           for (TableData tableData : incomeData) {
              if(tableData.getCdate().compareTo(startDate)>= 0 &&
                      tableData.getCdate().compareTo(today)<= 0){
               fData.add(tableData);
              }
          }
           return fData;
      }
      return incomeData;
    }
    public List<TableData> filterByCategory(List<TableData> incomeData){
     if(incomeData==null) throw new RuntimeException("Input TableData is NULL!");
        List<TableData> fData= new ArrayList<>();
        //for ANY category
        if(category==null || category.isEmpty()) return incomeData;
       //check it
       if( !categories.contains(category) ) throw new RuntimeException("Illegal Category field!");
        for (TableData tableData : incomeData) {
            if(tableData.getCat().equals(category)){
             fData.add(tableData);
            }
        }
       return fData;
           
    }
    public List<TableData> filterByNote(List<TableData> incomeData){
     if(incomeData==null) throw new RuntimeException("Input TableData is NULL!");
        List<TableData> fData= new ArrayList<>();
        //for ANY note
        if(note==null || note.isEmpty()) return incomeData;
       //check it
       if( !notes.contains(note) ) throw new RuntimeException("Illegal Note field!");
        for (TableData tableData : incomeData) {
            if(tableData.getNote().equals(note)){
             fData.add(tableData);
            }
        }
       return fData;
           
    }
    /**
     * Make data filter by date, category, and notes
     * @param incomeData
     * @return
     * @throws Exception 
     */
    public List<TableData> filter(List<TableData> incomeData) throws Exception{
      return  filterByNote(filterByCategory(filterByDate(incomeData)));
    }

    public String statusStr(){
        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
        StringBuilder sb = new StringBuilder();
      sb.append("Filter: SDate: ").append((startDate==null)?"ANY":sf.format(startDate));
      sb.append(" EDate: ").append((endDate==null)?"ANY":sf.format(endDate));
      sb.append(" Cat: ").append((category==null)?"ANY":category);
      sb.append(" Note: ").append((note==null)?"ANY":note);
      return sb.toString();
    }
    @Override
    public String toString() {
        return "TableDataFilter{" + "startDate=" + startDate + ", endDate=" + endDate + ", category=" + category + ", note=" + note + '}';
    }
    
    
}
