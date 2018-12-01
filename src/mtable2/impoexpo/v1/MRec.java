package mtable2.impoexpo.v1;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;


public class MRec implements Comparable<MRec>{

	private static final String[] DefTitles = new String[]{"Date", "Category", "Amount", "Note"};
	public static final String[]  DefColTypes= new String[]{"date","choice","float","text"};
//public String date;
public int dd;
public int mm;
public int yy;
public String cat;
public String Amount;
public String note;

public MRec(){
	
	this(1,1,2012,"Karte","0.0","");
}
public MRec(int dd,int mm,int yy, String cat, String amount, String note) {
	//super();
	this.dd=dd;
	this.mm=mm;
    this.yy=yy;
	this.cat = cat;
	Amount = amount;
	this.note = note;
}


	/**
	 * Create MRec instance from string parameters
	 * @param date string in format dd/mm/yyyy
	 * @param cat category string
	 * @param amount amount string
	 * @param note note string
	 * @throws Exception
	 */
public MRec(String date,String cat, String amount, String note) throws Exception{
	
	
	try {
		this.dd=Integer.parseInt(date.substring(0, 2));
		this.mm=Integer.parseInt(date.substring(3, 5));
		this.yy=Integer.parseInt(date.substring(6));
		//Check for upper boundaries, but not for exactly date correctness
		if(dd<=0 || dd>=32 || mm<=0 ||mm>=13 ||yy< 1000){
			throw new Exception("MRec: date out of boundaries!");
		}
	} catch (Exception e) {
		throw new Exception(e.getMessage());
	}
	this.cat = cat;
	Amount = amount;
	this.note = note;
	
}

	public static String[] getDefTitles() {
		return DefTitles;
	}

	public static List<String> getDefTitlesList(){
		List<String> titles=new ArrayList<String>();
		for(int i=0;i<DefTitles.length;i++){
			titles.add(DefTitles[i]);
		}
				return titles;
	}
	public static List<String> getDefColTypesList(){
		List<String> types=new ArrayList<String>();
		for(int i=0;i<DefColTypes.length;i++){
			types.add(DefColTypes[i]);
		}
		return types;
	}

	public int getDd() {
	return dd;
}
public void setDd(int dd) {
	this.dd = dd;
}
public int getMm() {
	return mm;
}
public void setMm(int mm) {
	this.mm = mm;
}
public int getYy() {
	return yy;
}
public void setYy(int yy) {
	this.yy = yy;
}
public int getColNum(){
	return 6;
}

	public String getCat() {
		return cat;
	}

	public void setCat(String cat) {
		this.cat = cat;
	}

	public String getAmount() {
		return Amount;
	}

	public void setAmount(String amount) {
		Amount = amount;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * Returns date in format dd/MM/yyyy with leading zero if needed
 	 * @return
	 */
public String getStringDate(){

	String ss= String.format("%02d/%02d/%04d",this.dd,this.mm,this.yy);
	//return ""+this.dd+"/"+this.mm+"/"+this.yy;
	return ss;
}

public String getValByCol(int columnIdx){
	if(columnIdx<0 || (columnIdx>getColNum()-1))
		return "";
	String sval="";
	
	switch(columnIdx){
	case 0:
		sval= getStringDate();
		break;
	case 1:
		sval= this.cat;
		break;
	case 2:
		sval= this.Amount;
		break;
	case 3:
		sval= this.note;
		break;
	
	}
	return sval;
}
public static void saveRecord(DataOutputStream os, MRec rec) throws IOException{
	os.writeInt(rec.dd);
	os.writeInt(rec.mm);
	os.writeInt(rec.yy);
	os.writeUTF(rec.cat);
	os.writeUTF(rec.Amount);
	os.writeUTF(rec.note);
}
public static MRec readRecord(DataInputStream is) throws IOException{
	MRec rec= new MRec(0,0,0,"","","");
	rec.dd= is.readInt();
	rec.mm= is.readInt();
	rec.yy= is.readInt();
	rec.cat= is.readUTF();
	rec.Amount= is.readUTF();
	rec.note= is.readUTF();
	return rec;
	
}
@Override
public int compareTo(MRec other) {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String sdThis=String.format("%04d",this.yy)+String.format("%02d", this.mm)+
			String.format("%02d", this.dd);
	String sdOther= String.format("%04d",other.yy)+String.format("%02d", other.mm)+
			String.format("%02d", other.dd);
	try {
		Date dateThis = sdf.parse(sdThis);
		Date dateOther = sdf.parse(sdOther);
		Calendar calThis = Calendar.getInstance();
    	Calendar calOther = Calendar.getInstance();
    	calThis.setTime(dateThis);
    	calOther.setTime(dateOther);
    	return calThis.compareTo(calOther);
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return 0;
}

/**
 * Useful for export feature to get MREc item's string representation
 * @param delimiter - delimiter char to be used
 * @return
 */
public String toString(String delimiter){
	StringBuilder bstr= new StringBuilder();
	bstr.append(getStringDate()).append(delimiter).
	append(this.cat).append(delimiter).
	append(this.Amount).append(delimiter).append(this.note).append(delimiter).append('\n');
	return bstr.toString();
}

	/**
	 * Parse table's string row and returns MRec object
	 * @param recMRec table's row string presentation
	 * @param fieldDelimiter table string row's column delimiter,usually char '|'
	 *
	 * @return - MRec obkect
	 * @throws Exception
	 */
	public static MRec parseString(String recMRec,String fieldDelimiter) throws Exception{
     //Make delimiters  including line spaces like space char, next line char etc.
		String delim= fieldDelimiter+"\n";
		MRec mrec= new MRec(0,0,0,"","","");
		StringTokenizer strTkn = new StringTokenizer(recMRec, delim);
		ArrayList<String> arrLis = new ArrayList<String>();
		int cnt=strTkn.countTokens();
		if(cnt!= DefTitles.length) {
			throw new Exception("Wrong number of Columns!");
		}
		while(strTkn.hasMoreElements()){
			arrLis.add(strTkn.nextToken());
		}
		//is date?
		int col=0;
		int dd,mm,yy;
		String date= arrLis.get(0).trim();
		try {
			dd=Integer.parseInt(date.substring(0, 2));
			mm=Integer.parseInt(date.substring(3, 5));
			yy=Integer.parseInt(date.substring(6));

		} catch (Exception e) {
			throw new Exception("Date error Col="+col);
		}
		mrec.setDd(dd);mrec.setMm(mm);mrec.setYy(yy);
		col++;
		mrec.setCat(arrLis.get(col));
		col++;
		mrec.setAmount(arrLis.get(col));
		col++;
		mrec.setNote(arrLis.get(col));
		return mrec;

	}
}
