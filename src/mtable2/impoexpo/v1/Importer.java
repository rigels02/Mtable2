package mtable2.impoexpo.v1;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Imported data are accessible via TManager method call:
 * {@link TManager#getMTables() }
 * <br>
 * Import started by call of {@linkplain #execImport(java.lang.String) }.
 * If error occurs then error text can be retrieved by call of 
 * {@linkplain #errorTxt() }.
 * <br>
 * Created by raitis on 15.8.9.
 *
 * @see TManager TManager
 */
public class Importer {

    private final TManager tmgr;
    private boolean OK;
    private String Errtxt;

    private final String Delim = "|";
    private Mtable mtable=null;
    private Htable htable=null;

    private int idx1,idxn,idx2,tType;
    private List<String> colnames,coltypes;
    private int helmidx;
    private String impTxt;

    public Importer(TManager tmgr){

        this.tmgr= tmgr;
        Errtxt="";
        OK= true;

        //this.mtable= tmgr.getMtable();
        mtable = new Mtable();
    }


    /**
     * Returns error text.
     * @return error text in case of an error.
     */
    
    public String errorTxt(){
        String err= Errtxt;
        Errtxt="";
        OK= true;
        return err;
    }
    private void error(String etxt){

        OK=false;
        Errtxt= etxt;
    }

    /**
     * Get data to be imported: header and table data.
     * In 1st pass store=false, because data must be checked.
     * In 2nd pass store=true, we may save given data.
     * @param store  true - if to be stored, false - not to be stored
     * @return - false if any error
     */
    private boolean get2Import(boolean store){
        //main loop
        while (idx1<idx2){
            if(!readHeader()) return false;
            //end of string reached, never be true, because string was trimmed
            //in execImport()
            if(idx1>= idx2) break;
            tType=getTableType();
            if(tType==HRec.cCustomTable){
               String tn= htable.getElmAt(htable.getSize()-1).getTitle();
                error("Custom Table Not supported!\nTable name is: "+tn);
                return false;
            }else if(tType==HRec.cDefaultTable){
                //default table, set default columns
                colnames= MRec.getDefTitlesList();
                coltypes=MRec.getDefColTypesList();
            }else {
                String tn= htable.getElmAt(htable.getSize()-1).getTitle();
                error("Wrong Table type!\nTable name is: "+tn);
                return false;
            }
            //after previous storing reset mtable
            mtable.clear();

            if(!getData()) return false;
            //DebugDump(); //for debug
            //add new  htable record to the existing table manager
            if(store) {
                if(!htable.hlist.isEmpty())
                    add2TableHeader();
            }
            if(store){
                if(!mtable.mlist.isEmpty())
                    tmgr.settModified(true);

                if(!StoreData()) {
                    tmgr.settModified(false);
                    return false;
                }
                tmgr.settModified(false);
            }



        }//while
        if(store){
            if(!htable.hlist.isEmpty()){
                tmgr.noneTables=false;
                /**
                * No tables in tmgr, so, just adding new tables
                 * If there is already any table (CurrentTableIdx != -1) , then we keep current table as it is
                 */

                if(tmgr.getCurrentTableIdx()== -1) {
                    tmgr.setCurrentTableFileIdx(htable.getElmAt(htable.getSize() - 1).Idx);
                    tmgr.setMtable(this.mtable);
                }
            }
        }
        return true;
    }

    private void FAULT(){
        //if(tmgr.noneTables){
            mtable.clear();
            tmgr.getHtable().clear();
        //}
    }


    /**
     * The main import Exec method.
     * The result is kept in TManager and can be retrieved 
     * by {@linkplain TManager#getMTables() }
     * @param imptxt input text to be imported
     * @return - true if Ok, false  if fails
     */
  
    public boolean execImport(String imptxt){

        htable= new Htable();
        mtable= new Mtable();
        helmidx= -1;
        if(imptxt==null|| imptxt.trim().length()==0) {
            error("Nothing to import!\n");
            return false;
        }
        this.impTxt= imptxt.trim();
        idx1=0;idx2=impTxt.length()-1;
        //idxn=impTxt.indexOf('\n');
        if(idx1==idx2 ) return true;

        //if there is no tables at all then do nothing.
        /***
        if(mlist->tmgr->htable->size()==0){
            mlist->tmgr->addReservedRecords();
        }
         ***/

        //pass 1, do not store data
        if(!get2Import(false)) {
            FAULT();
            return false;
        }
        //reset header and indexes to make pass2 with storing
        helmidx=-1;
        htable.clear();
        idx1=0;idx2=impTxt.length()-1;
        //pass 2 with saving
        if(!get2Import(true)){ //!!!
            //file saving error
            //remove last table's header
            tmgr.getHtable().delElm(tmgr.getHtable().getSize() - 1);

            //try to save this header
            StoreTablesHeader();
            FAULT();
            return false;
        }
        //we make import, it means adding of new tables headers into existing now
        //in the meantime the checking is done on duplicated tables names.


        if(!StoreTablesHeader()) {
            //saved tables , but can not save header!?!

            //TODO
            //====
            //delete all tables and header!
            //this function should be put in mlist class
            FAULT();
            return false;
        }

            return true;

    }

    private boolean StoreTablesHeader(){
        
        tmgr.saveHtable();
        
        tmgr.sethModified(false);
        return true;
    }

  private void add2TableHeader(){
      if(tType== HRec.cDefaultTable){
          HRec elm = htable.getElmAt(helmidx);
          long idx = tmgr.getHtable().getNewFileIdx();
          elm.Idx=idx;
          tmgr.getHtable().addElm(elm);
      }

  }

    private boolean StoreData() {

        if (tType == HRec.cDefaultTable) {
            //QString filename=mlist->tmgr->getLastStorageName();
           // String filename = tmgr.getTableFileName(tmgr.getHtable().getSize() - 1);
            
            tmgr.saveMtable(mtable, tmgr.getHtable().getSize() - 1);
            
            return true;
        }
        error("StoreData:Only default table type is supported!");
        return false;
    }
    private int rowsnumber(String rows){
        int count=0,fromIndex=0;
        int idx=0;
        while(idx != -1){
            idx=rows.indexOf('\n', fromIndex);
            if(idx!= -1) count++;
            fromIndex=idx+1;
        }
        return count;
    }
    private int colsnumber(String row){
        int count=0,fromIndex=0;
        int idx=0;
        while(idx != -1){
            idx=row.indexOf(Delim, fromIndex);
            if(idx!= -1) count++;
            fromIndex=idx+1;
        }
        return count;
    }
    private int hcolsnumber(String row){
        int count=0,fromIndex=0;
        int idx=0;
        while(idx != -1){
            idx=row.indexOf(',', fromIndex);
            if(idx!= -1) count++;
            fromIndex=idx+1;
        }
        return count;
    }
    private List<String> getHCols(String row){
        //QStringList *Cols= new QStringList();
        List<String> Cols=new ArrayList<>();
        int fromIndex=0,idx=0;

        for(int i=0;i<hcolsnumber(row);i++){
            idx=row.indexOf(',', fromIndex);
            Cols.add(row.substring(fromIndex, idx ).trim());
            //Cols.add(row.substring(fromIndex, idx - fromIndex).trim());
            fromIndex=idx+1;
        }
        return Cols;
    }
    private boolean  getCols(String row){

        List<String> Cols=new ArrayList<>();
        int fromIndex=0,idx=0;

        for(int i=0;i<colsnumber(row);i++){
            idx=row.indexOf(Delim, fromIndex);
            Cols.add(row.substring(fromIndex, idx ).trim());
            fromIndex=idx+1;
        }
        //check types
        String[] acoltypes= new String[coltypes.size()];
        String[] aCols= new String[Cols.size()];
        coltypes.toArray(acoltypes);
        Cols.toArray(aCols);
        if(Utils.isValidRow(acoltypes, aCols)){
            MRec mrec=null;
            try {
                mrec= new MRec(Cols.get(0),Cols.get(1),Cols.get(2),Cols.get(3));
            } catch (Exception e) {
                error("getCols:"+e.getMessage());
                return false;
            }
            mtable.addElm(mrec);

            return true;
        }else {
            error("Wrong column type!\nrow: "+row+"\n"+Utils.ErrorDetails());
        }
        return false;
    }
    private boolean getColType(String row){
        int fromIndex=0,idx=0;
     // List<String> colnames=new ArrayList<String>();
       // List<String> coltypes=new ArrayList<String>();

        idx=row.indexOf(',', fromIndex);
        if(idx== -1) return false;
        colnames.add(row.substring(fromIndex, idx ).trim());
        //colnames.add(row.substring(fromIndex, idx - fromIndex).trim());
        fromIndex=idx+1;
        //idx=row.indexOf('\n', fromIndex);
        //if(idx== -1) return false;
        coltypes.add(row.substring(fromIndex).trim());

        return true;
    }

    private boolean getData(){
        String rows= getBlock();
        if(rows==null) return false;
        if(rows.isEmpty()) return true; //no data, empty table
        int endIndex=rows.indexOf('\n', 0);
        int beginIndex=0;
        String row;
        int rowcount=rowsnumber(rows);
        for(int i=0;i<rowcount;i++){
            row=rows.substring(beginIndex, endIndex ).trim();
            //row=rows.substring(beginIndex, endIndex - beginIndex);

            //skip empty row
            if(row.isEmpty()) {
                beginIndex=endIndex+1;
                endIndex=rows.indexOf('\n', beginIndex);
                continue;
            }
            //check row
            if(colsnumber(row)!= colnames.size()) {
                String tn= htable.getElmAt(htable.getSize()-1).getTitle();
                error("Wrong columns number!\nTable: "+tn+" ,Check delimiters.\nrow:"+row);
                return false;
            }
            if(!getCols(row)) return false;
            //next
            beginIndex=endIndex+1;
            endIndex=rows.indexOf('\n', beginIndex);
        }

        return true;
    }

    private static final String BlkOpen="{";
    private static final String BlkClose="}";

    private String getBlock(){
        //find '{' and '}' positions
        idx1=impTxt.indexOf(BlkOpen, idx1);
        if(idx1== -1) return null;
        idx1++;
        int idx_1=idx1;//startpoint
        int idx_2;
        idx_2=impTxt.indexOf(BlkClose, idx_1);
        idxn=impTxt.indexOf('\n', idx_1);
        if(idx_2== -1) return null;
        idx1=idx_2+1; //next block startpoint
        //idx_2--; //end point
        //get col rows block
        String rows = impTxt.substring(idx_1, idx_2 );
        //String rows = impTxt.substring(idx_1, idx_2 - idx_1);
        return rows;
    }

    private boolean getColumns(){
        String rows= getBlock();
        if(rows==null) return false;
        int endIndex=rows.indexOf('\n', 0);
        int beginIndex=0;
        String row;
        int rowcount=rowsnumber(rows);
        //create fields
        colnames=new ArrayList<>();
        coltypes=new ArrayList<>();

        for(int i=0;i<rowcount;i++){

            row=rows.substring(beginIndex, endIndex);
            //row=rows.substring(beginIndex, endIndex - beginIndex);

            if(!getColType(row)) return false;
            //next
            beginIndex=endIndex+1;
            endIndex=rows.indexOf('\n', beginIndex);
        }
        //get next '\n' position
        //idx1=idx_2+1;

        return true;
    }
    private int getTableType(){
        HRec elm = htable.getElmAt(helmidx);

        return elm.getTypeInt();
    }

    private boolean readHeader(){
        //get header row
        idxn=impTxt.indexOf('\n', idx1);
        if(idxn==-1){
            error("Bad header!\n");
            return false;
        }
        String sh= impTxt.substring(idx1, idxn );
        while(sh.trim().length()==0){
            //skip spaces and \n
            idx1=idxn+1;
            //get next '\n' position
            idxn=impTxt.indexOf('\n', idx1);
            if(idxn== -1) return true; // 09Okt. false;
            sh= impTxt.substring(idx1, idxn );
        }
        //unpack header

        List<String> header=getHCols(sh);

        //We don't use storage name in import string
       // List<String> Header=new ArrayList<String>();
        //for import is used header without resource storage name ,

        //so, check gotten header size
        if(header.size()!= HRec.cLCol5) {
            error("Wrong header size! Check delimiters! Table:"+header.get(0));
            return false;
        }
        //copy new header
        /**
        for(int i=0,k=1;i<header.size();i++,k++){
            //Header[k]=header[i];
            Header.add(k, header.get(i));
        }
         ***/
        if(header.get(0).trim().isEmpty()) {
            error("Empty Table name not allowed!");
            return false;
        }
        if(isDuplicateTableName(header.get(0))){
            error("Duplicate or Empty Table name not allowed!: "+header.get(0));
            return false;
        }
        /**
         * the getNewFileIdx() part is moved into add2TableHeader()
         * if tmgr.htable.size = 0 then new Idx calculation here would be valid,
         * but in opposite case we have to consider tmgr.htable
         */
        //int idx = htable.getNewFileIdx();
        int idx=0;
        HRec hrec= new HRec(); //empty HRec
        try {
            hrec.parseHRec(idx,header);//fill it
        } catch (Exception e) {
            error("Error:"+e.getMessage());
            return false;
        }
        //HRec hrec= new HRec(idx,Header);
        htable.addElm(hrec);
        helmidx++;
        return true;
    }

    /**
     * avoid dublicated table name in case of import new tables into existing now.
     * @param title table title name
     * @return - true if duplicated name , false - if not duplicated
     */
    private boolean isDuplicateTableName(String title){
        //find in loaded headers via hmngr
        if(tmgr.isTitleExists(title)) return true; //Duplicated!
        //find in imported array
        for(int i=0;i< htable.getHlist().size();i++)
            if(htable.getElmAt(i).getTitle().compareTo(title)==0)
                          return true;//Duplicated !
        return false;
    }
}
