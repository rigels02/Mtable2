package mtable2.impoexpo.v1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Tables header record class.
 * Contains main info for every table.
 * @author rbrodezh
 *@version 1.00
 */
public class HRec {

	public final static int cDefaultTable=0;
    public final static int cCustomTable=1;
	
	//Constants to access to Header table record's field in J2ME.
	//Has to be considered in case of tables export/import  between Android and J2ME applications
    //To export to J2ME must be used fields in the following order  started from cTableTitle onward.
    public final static int cRmsName=0,cTableTitle=1,cTableType=2,cSumColNum=3,
             cLColSize=4,cLCol0=5,cLCol1=6,cLCol2=7,cLCol3=9,cLCol4=9,cLCol5=10;
    
    
    
    //record itself to be stored in file
    /**
     * Table's file index to be used as the ending part in the tables filename
     */
    public long Idx; //Table's file index to be used as the ending part in the tables filename
    /**
     * Table's title
     */
    public String title;
    /**
     * Table's type, usually default
     */
    public int type;
    /**
     * Column number to be used for sum operation. Reserved
     */
    public int sumColNum;
    /**
     * LCols describes what columns are displayed on the list screen. In some tables it is useful do 
     * not show all columns in the list, but just some of them.
     * LCols[0] - int - count of useful elements .
     * LCols[1]...LCols[6]- element's values in LCol[] array. To be converted from String to int type.
     */
    public String[] LCols;
    
    private static final String[] defLCols={"4","0","1","2","3","",""};
	private static final String[] emptyCols={"","","","","","",""};
    
	public HRec(long idx, String title, int type, int sumColNum, String[] lCols) {
		
		Idx = idx;
		this.title = title;
		this.type = type;
		this.sumColNum = sumColNum;
		LCols = lCols;
	}
	/**
	 * Create h-record 
	 * @param idx - table's file index
	 * @param title - table's title
	 */
	public HRec(long idx, String title) {
		
		this(idx,title,cDefaultTable,2,defLCols.clone());
		
	}

	public HRec(){
	this(0,"",-1,-1,emptyCols.clone());
	}

	public void parseHRec(int idx,List<String> Hrecord) throws Exception {
	  this.Idx= idx;
		this.title= Hrecord.get(0);
		String op;
		int sz;
		try {
			this.type = Integer.parseInt(Hrecord.get(1));
			this.sumColNum = Integer.parseInt(Hrecord.get(2));
			sz = Integer.parseInt(Hrecord.get(3));
		}catch (NumberFormatException e){
			op="LCol size, table:"+this.title;
			if(this.type==-1) {op="Table:"+this.title+ " type";}
			else if(this.sumColNum==-1) {op="sumColNum, table:"+this.title;}
			throw new Exception("parseHRec:Wrong "+op);
		}
		//this.LCols= new String[sz+1];
		//this.LCols= new String[defLCols.length];
		int colv;
		//fill in LCols values from Hrecord string
		for(int i=0,k=3;i<=sz;i++,k++){
			this.LCols[i]= Hrecord.get(k);
			//check for digit
			try {
				Integer.parseInt(LCols[i]);
			}catch (NumberFormatException e){
			 throw new Exception("parseHRec:Wrong LCols value idx="+i+" ,must be digit string!, Table:"+this.title);
			}

		}
		//sz can be smaller then default defLCols size
		//fill in the rest elements with ""
		// for(int i=sz+1; i<defLCols.length;i++){
		//	this.LCols[i]="";
		// }


	}

	public static void saveRecord(DataOutputStream os, HRec rec) throws IOException{
    	os.writeLong(rec.Idx);
    	os.writeUTF(rec.title);
    	os.writeInt(rec.type);
    	os.writeInt(rec.sumColNum);
    	for(int i=0;i<rec.LCols.length;i++){
    		os.writeUTF(rec.LCols[i]);
    		
    	}
    	
    }
    public static HRec readRecord(DataInputStream is) throws IOException{
    	HRec rec= new HRec(0, "");
    	rec.Idx=is.readLong();
    	rec.title=is.readUTF();
    	rec.type=is.readInt();
    	rec.sumColNum=is.readInt();
		// !!LCols.length == defLCols.length !!
    	for(int i=0;i<rec.LCols.length;i++){
    		rec.LCols[i]= is.readUTF();
    		
    	}
    	return rec;
    	
    }
    
    //------ Getters/Setters for export/import feature------//
    //-----For export/import all fields must be represented as String values.
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return String.valueOf(type);
	}

	/**
	 * Get table type integer
	 * @return - int table type
	 */
	public int getTypeInt() {
		return this.type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String stype) {
		this.type = Integer.parseInt(stype);
	}
	/**
	 * @return the sumColNum
	 */
	public String getSumColNum() {
		return String.valueOf(sumColNum);
	}
	/**
	 * @param sumColNum the sumColNum to set
	 */
	public void setSumColNum(String ssumColNum) {
		this.sumColNum = Integer.parseInt(ssumColNum);
	}
	/**
	 * @return the lCols
	 */
	public String[] getLCols() {
		return LCols;
	}
	/**
	 * @param lCols the lCols to set
	 */
	public void setLCols(String[] lCols) {
		LCols = lCols;
	}
    
	/**
	 * Useful for export feature to get HRec item's string representation
	 */
    public String toString(){
       StringBuilder bstr= new StringBuilder();
       bstr.append(getTitle() + "," + getType() + "," + getSumColNum());
    	int msz= defLCols.length; // -1;
    	//int csz= this.LCols.length;
    	//bstr.append(csz);
		int csz= Integer.parseInt(this.LCols[0]);
    	for(int i=0;i<=csz;i++){
    		bstr.append(',');
    		bstr.append(LCols[i]);
    	}
    	for(int i=0;i<=msz-csz-1;i++){
    		bstr.append(',');
    	}
    	bstr.append('\n');
    	
    	return bstr.toString();
    }


	/**
	 * Useful for import feature to get HRec item from string representation
	 *string format:
	 * <pre>
	 *tableTitle,tableType,SumCol,LColsSize(3),LCols[0],LCols[1],LCols[2],,,,, LCols[5]\n
	 * </pre>
	 *if LCols size <6, then the rest of LCols[] are empty, like above, where size=3
	 *ex: Benzins,0,2,3,0,1,2,,,,
	 *
	 * @param Idx the real table's file index (can be 0 if not important)
	 * @param recHRec HRec string  (format see above)
	 * @param delimiters delimeters for fields, generaly char ','
	 * @return - HRec object instance
	 * @throws Exception
	 */
    public static HRec parseString(int Idx,String recHRec,String delimiters) throws Exception{
    //the HRec idx must be the real table's file index!!!

		HRec hrec= new HRec();
		hrec.Idx=Idx;
        hrec.setLCols(emptyCols.clone());
    	 StringTokenizer strTkn = new StringTokenizer(recHRec, delimiters);
    	 ArrayList<String> arrLis = new ArrayList<String>();
    	 int cnt=strTkn.countTokens();
    	 if(cnt>cLCol5){
    		 throw new Exception("HREC:Too many fields in HRec!");
    	 }
    	 while(strTkn.hasMoreElements()){
    		 arrLis.add(strTkn.nextToken());
    	 }
    	 hrec.setTitle(arrLis.get(0));
    	 if( ! arrLis.get(1).trim().equals(String.valueOf(cDefaultTable))){
    		 throw new Exception("HREC:Wrong table type!");
    	 }
    	 hrec.setType(arrLis.get(1).trim());
    	 int sumcol=0;
    	 try {
    		 sumcol=Integer.parseInt(arrLis.get(2));
    	 }catch(NumberFormatException e){
    		 throw new Exception("HREC:Sum Column value error!\n"+e.getMessage());
    	 }
    	 hrec.setSumColNum(arrLis.get(2));

		 int LColsSz=0;
		try {
			LColsSz=Integer.parseInt(arrLis.get(3));
		}catch(NumberFormatException e){
			throw new Exception("HREC:LCols size value error!\n"+e.getMessage());
		}
		if(LColsSz>6)
			  throw  new Exception("HREC: LCols size >6 !");
		hrec.LCols[0]= arrLis.get(3);//LcolsSz string
		for(int i=1,j=4;i<=LColsSz;i++,j++){
			String v = arrLis.get(j);
			if(!v.matches("\\d+")){
				throw new Exception("HREC: LCol value must be digit string!(no spaces around");
			}
			hrec.LCols[i]= arrLis.get(j);
		}
    	 
    return hrec;
    }


}
