package mtable2.impoexpo.v1;

import java.util.ArrayList;

/**
 * Export use TManager {@link TManager}.
 * Export is dun by call of {@linkplain #dumpAll() }
 * <br>
 * Created by raitis on 15.7.9.
 */
public class Exporter  {
    private final TManager tmgr;
    
    private StringBuilder Dumptxt;
    private final String Delim = "|";
    private Mtable mtable=null;


    public Exporter(TManager tmgr){
        this.tmgr= tmgr;
       
        Dumptxt= new StringBuilder();
        //this.mtable= tmgr.getMtable();
        this.mtable= new Mtable();
    }

    

    

    /**
     * 
     * @return exported files as string 
     */
  
    public StringBuilder getDumptxt() {
        return Dumptxt;
    }

    void dumpHeader( HRec hrec   ){

        Dumptxt.append("\n");
        Dumptxt.append(hrec.toString());
       // Dumptxt.append("\n");

    }

    void dumpData(){
        if(this.getMtable()==null) return;
        Dumptxt.append("{");
        for(MRec mrec: this.getMtable().mlist){
            Dumptxt.append(mrec.toString(Delim));
        }
        Dumptxt.append("}");
        if(this.getMtable().mlist.isEmpty()) Dumptxt.append("\n");

    }

    /*public boolean getMTable(String tableFileName){
    if(this.mtable!=null)
    this.mtable=null;
    this.mtable= new Mtable();
    try {
    tmgr.loadMtable(this.mtable,tableFileName);
    return true;
    } catch (IOException e) {
    this.error("Exporter: Table load error\n"+e.getMessage());
    }
    return false;
    }*/
    private Mtable getMtable() {
        return mtable;
    }

    /**
     * Dump all tables to string csv like format.
     * Result string may be get by call of {@linkplain #getDumptxt() }
     * @throws Exception 
     */
   
    public void dumpAll() throws Exception{

        if(Dumptxt.toString().length()>0) {
            Dumptxt = new StringBuilder();
        }
        String fileName;

        ArrayList<HRec> hlist = tmgr.loadHtable().hlist;
        for(HRec hrec: hlist){
            
            getMTable(hrec.Idx);
            dumpHeader(hrec);
            dumpData();
        }
        Dumptxt.append("\n");
       
    }

    private void getMTable(long Idx) throws Exception {
        if (this.mtable != null) {
            this.mtable = null;
        }
        this.mtable = new Mtable();

        tmgr.loadMtable(this.mtable, Idx);

    }
}
