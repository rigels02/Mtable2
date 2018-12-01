package mtable2.reports;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

public class Utils {



/**
 * Check that txt is convertible to float 	
 * @param txt - txt string to convert to float
 * @return - true if ok, false if nok.
 */
public static boolean isFloat(String txt){
	try {
		Float.parseFloat(txt);
	} catch (NumberFormatException e){
		return false;
	}
	return true;
}
/***
public static boolean isDouble(String txt){
	try {
		Double.parseDouble(txt);
	} catch (NumberFormatException e){
		return false;
	}
	return true;
}
 ***/

/**
 * Column valid types: "text","float","choice","date"
 */
private final static String[] types={"text","float","choice","date"};

private static String[] columnTitles;

public static String ErrorDetails(){
	return _DetErrMsg;
}

/**
 * Get info about column with error.
 * If there is columnTitles defined then get column name,
 * otherwise just index.
 * @param colidx column index in form where error occures 
 * @return string of message to be included for user
 */
private static String getColumnInfo(int colidx){
	if(columnTitles==null){
		
		return ": Column= "+(colidx+1);
	}
	String info=" in column: "+columnTitles[colidx];
	columnTitles=null;
	return info;
}
/**
 * Set column titles to be used in error report message
 * @param colTitles - column names or titles
 */
public static void setColumnTitles(String[] colTitles){
	columnTitles=colTitles;
}
private static String _DetErrMsg;
/**
 * 
 * Check that passed  table columns row contains valid values.
 * @see constant types
 * @param ColsType - String array of columns valid types, see constant types
 * @param cols2chk - String array of columns to be checked
 * @return - true if ok, false if is not valid
 * 
 */
public static boolean isValidRow(String[] ColsType,String[] cols2chk){
	_DetErrMsg=null;
	if(ColsType==null || cols2chk==null) return false;
	if(ColsType.length != cols2chk.length) return false;
	int sw=0;
	for(int i=0;i< cols2chk.length; i++){
		for(int k=0;k<types.length;k++){
			//find in types
			if(ColsType[i].equals(types[k]))
				sw=k;
		}
		switch(sw){
		case 0: //text
			 
			break;
		case 1: //float
			if(!isFloat(cols2chk[i])) {
			_DetErrMsg="Float value missing"+getColumnInfo(i);
				return false;
			}
			break;
		case 2: //choice
			if(cols2chk[i].trim().length()==0) {
				_DetErrMsg="Choice can not be blank"+getColumnInfo(i);
				return false; //cannot be blank
			}
			break;
		case 3: //date
			if(!IsDate(cols2chk[i])) {
				_DetErrMsg="Date Format:\ndd/mm/yyyy"+getColumnInfo(i);
				return false;
			}
			break;

		
		default:
			_DetErrMsg="Unknown Column type!";
			return false;
		}
	}
	return true;
}
	/**
	 * Add num spaces at the end of title
	 * @param title - string where spaces to be added to
	 * @param num - the number of spaces
	 * @return
	 */
public	static String AddTail(String title,int num){
		int maxln=num;
		String str=title;
		while(str.length()<maxln){
			str=" "+str+" ";
		}
		return str;
	}
/**
 * Add spaces to the end of text to make it of given length
 * @param txt - text to be formated
 * @param length - string length to be reached
 * @return - formated string
 */
public static String AddSpace(String txt,int length){
	int maxln=length;
	if(txt.length()>= length) return txt;
	String str=txt;
	while(str.length()<maxln){
		str=str+" ";
	}
	return str;
}

	/**
	 * Check in that date in sText is in valid format dd/mm/yyyy
	 * @param sText - string to be checked
	 * @return
	 */
public	static boolean IsDate(String sText)
	{
	  String ValidChars = "0123456789";
	  String dd="",mm="",yyyy="";
	  char Char;
	  if(sText.length()!=10) return false;

	  for (int i = 0; i < 2; i++)
	  {
	    Char = sText.charAt(i);
	    if (ValidChars.indexOf(Char) == -1)
	    {
	       return false;
	   }
	    dd+= String.valueOf(Char);
	  }
	  int nn=Integer.parseInt(dd);
	  if(nn>31 || nn<1) return false;

	  if(sText.charAt(2)!='/') return false;

	  for (int i = 3; i < 5; i++)
	  {
	    Char = sText.charAt(i);
	    if (ValidChars.indexOf(Char) == -1)
	    {
	       return false;
	   }
	    mm+= String.valueOf(Char);
	  }
	  nn=Integer.parseInt(mm);
	  
	  if(nn>12 || nn<1) return false;
	  
	  if(sText.charAt(5)!='/') return false;
	  for(int i = 6;i < 10; i++){
		  Char = sText.charAt(i);
		    if (ValidChars.indexOf(Char) == -1)
		    {
		       return false;
		   }  
	   yyyy+= String.valueOf(Char);
	  }
	  return true;
	}

	/**
	 * Gives current date in dd/mm/yyyy format
	 * @return - string in format dd/mm/yyyy
	 */
public	static String GetCurDate(){
		String dd,mm,yyyy;
		Calendar c=Calendar.getInstance();
		Date d=new Date();
		c.setTime(d);
		//dd/mm/yyyy format
		dd=""+c.get(Calendar.DATE);
		mm=""+(c.get(Calendar.MONTH)+1);
		yyyy=""+c.get(Calendar.YEAR);
		dd=dd.length()<2?"0"+dd:dd;
		mm=mm.length()<2?"0"+mm:mm;
		
		return dd+"/"+mm+"/"+yyyy;
		}

/**
 * Gives current date in dd/mm format
 * @return - string in format dd/mm
 */
public	static String GetCurDateDDMM(){
	String dd,mm;
	Calendar c=Calendar.getInstance();
	Date d=new Date();
	c.setTime(d);
	//dd/mm format
	dd=""+c.get(Calendar.DATE);
	mm=""+(c.get(Calendar.MONTH)+1);
	dd=dd.length()<2?"0"+dd:dd;
	mm=mm.length()<2?"0"+mm:mm;
	
	return dd+"/"+mm;
	}

/**
 * Gives current date's month part.
 * 
 * @return Month's number string in format mm
 */
public static String GetCurMonthMM(){
	String smm= GetCurDateDDMM();
	//select month part
	String s= smm.substring(smm.indexOf("/")+1);
	return s;
}
/**
 * Gives current date in dd/mm format
 * @param d - Date , type Date
 * @return - string in format dd/mm/yyyy
 */
public	static String Date2Str(Date d){
	String dd,mm,yyyy;
	Calendar c=Calendar.getInstance();
	//Date d=new Date();
	c.setTime(d);
	//dd/mm format
	dd=""+c.get(Calendar.DATE);
	mm=""+(c.get(Calendar.MONTH)+1);
	yyyy=""+(c.get(Calendar.YEAR));
	dd=dd.length()<2?"0"+dd:dd;
	mm=mm.length()<2?"0"+mm:mm;
	
	return dd+"/"+mm+"/"+yyyy;
	}

/**
 * Convert String dd/mm/yyyy to Date
 * @param sText - string in format dd/mm/yyyy
 * @return - Date type, or null if error<br>
 * <b>Note:</b><i>This method check only '/' position and length, but not do full validation.</i>
 * See isDate() for full validation.
 */
public static Date Str2Date(String sText){
	String dd="",mm="",yyyy="";
	  char Char;
	  if(sText.length()!=10) return null;

	  for (int i = 0; i < 2; i++)
	  {
	    Char = sText.charAt(i);
	    dd+= String.valueOf(Char);
	  }
	  int idd=Integer.parseInt(dd);
	  
	  if(sText.charAt(2)!='/') return null;

	  for (int i = 3; i < 5; i++)
	  {
	    Char = sText.charAt(i);
	    mm+= String.valueOf(Char);
	  }
	  int imm=Integer.parseInt(mm);
	  
	  if(sText.charAt(5)!='/') return null;
	  for(int i = 6;i < 10; i++){
		  Char = sText.charAt(i);
		  yyyy+= String.valueOf(Char);
	  }
	  int iyyyy=Integer.parseInt(yyyy);
	  Calendar c=Calendar.getInstance();
	  c.set(Calendar.DATE, idd);
	  c.set(Calendar.MONTH, imm-1);
	  c.set(Calendar.YEAR, iyyyy);
	  Date d=c.getTime();
	  return d;
}

/**
 * Sort String[] elements of vector by Date field in String[] element.
 * @param vec - vector of String[] elements to be sorted
 * @return - true , if sorting has being happened
 */
public static boolean SortStrVector(Vector vec){
	if(vec==null || vec.size()<2) return false; //Nothing to sort 
	boolean res=false;
	for(int i=0;i<vec.size()-1;i++)
		for(int j=i+1;j<vec.size();j++)
			if(compr_date(((String [])vec.elementAt(i))[0],((String[])vec.elementAt(j))[0])>0){
				//swap elements
				String[] t=(String [])vec.elementAt(i);
				vec.setElementAt(vec.elementAt(j), i);
				vec.setElementAt(t, j);
		     res=true;
				
			}
	return res;
}
/**
 * Compare String date in format dd/mm/yyyy	
 * @param a - date 1 in format "dd/mm/yyyy"
 * @param b - date 2 in format "dd/mm/yyyy"
 * @return - 0 if equals, 
 *           Negative if a is smaller then b,
 *           Positive if a is bigger then b
 */
public static int compr_date(String a,String b){
//first validate format
	if(!Utils.IsDate(a)) return 0;
	if(!Utils.IsDate(b)) return 0;
	//convert and compare
	Date da= Utils.Str2Date(a);
	Date db= Utils.Str2Date(b);
	long lda= da.getTime();
	long ldb= db.getTime();
	
	if(lda==ldb) return 0;
	if(lda>ldb) return 1;
	return -1;
}

private static int type2int(String coltype){
  for(int i=0;i<types.length;i++){
	  if(coltype.equals(types[i]))
		    return i;
  }
  return -1;
}

public final static int cText=0,cFloat=1,cDate=3,cChoice=2;
/**
 * Sort String[] elements of vector by column with given colnum and type.
 * @param vec - vector of String[] elements to be sorted
 * @param colnum - column number to be sorted by
 * @param coltype - the column's with number colnum type.
 * @return - true, if sorting has being taken place
 *  The valid types are: cText=0,cFloat=1,cDate=3,cChoice=2
 */
public static boolean SortStrVector(Vector vec,int colnum,String scoltype){
	if(vec==null || vec.size()<2) return false; //Nothing to sort 
	int coltype= type2int(scoltype);
	boolean res=false;
	
	for(int i=0;i<vec.size()-1;i++)
		for(int j=i+1;j<vec.size();j++){
		  switch(coltype){
		  case cDate:
			if(compr_date(((String [])vec.elementAt(i))[colnum],((String[])vec.elementAt(j))[colnum])>0){
				//swap elements
				String[] t=(String [])vec.elementAt(i);
				vec.setElementAt(vec.elementAt(j), i);
				vec.setElementAt(t, j);
				res=true;
				
			}
			break;
		  case cText:
			  if(((String[])vec.elementAt(i))[colnum].compareTo(
					  ((String[])vec.elementAt(j))[colnum])>0	  ){
				//swap elements
					String[] t=(String [])vec.elementAt(i);
					vec.setElementAt(vec.elementAt(j), i);
					vec.setElementAt(t, j);
					res=true;
			  }
				  
			  break;
		  case cFloat:
			  String elm1= ((String[])vec.elementAt(i))[colnum];
			  String elm2= ((String[])vec.elementAt(j))[colnum];
			  if(Integer.parseInt(elm1)> Integer.parseInt(elm2)){
				//swap elements
					String[] t=(String [])vec.elementAt(i);
					vec.setElementAt(vec.elementAt(j), i);
					vec.setElementAt(t, j);
					res=true;
			  }
			  break;
		  case cChoice:
			  if(((String[])vec.elementAt(i))[colnum].compareTo(
					  ((String[])vec.elementAt(j))[colnum])>0	  ){
				//swap elements
					String[] t=(String [])vec.elementAt(i);
					vec.setElementAt(vec.elementAt(j), i);
					vec.setElementAt(t, j);
					res=true;
			  }
			  break;
		  }	
		}
	return res;
}

/**
 * Prepares packed item to be displayed in mainScreen
 * @param field - String[3] of fields to be packed
 * @param maxChars - max. amount of chars are able to fit in screen horizontally
 * @return - packed String item
 */
public static String PackItem(String[] field, int maxChars) {
	int len=field.length;
	String res="";
	String[] f=new String[len];
	int itemlen=0;
	
	for(int i=0;i<len;i++){
		f[i]=field[i]+" ";
		itemlen+=f[i].length();
	}
	while(itemlen<=maxChars){
	itemlen=0;
		for(int i=0;i<len;i++){
			f[i]+=" ";
			itemlen+=f[i].length();
		}
	if(itemlen>=maxChars) {
		
		for(int i=0;i<len;i++)
			res+=f[i];
		f=null;
		return	res;
	}
	}
	
	for(int i=0;i<len;i++){
		f[i]=field[i]+" ";
		res+=f[i];
	}
	f=null;
	return res;
}

/**
 * Convert float to string and strip the last decimal digits until
 * 3 digits.
 * @param v - float value to be converted
 * @return - String result
 */
public static String toStrWithStrip(float v){
	String res="";
	String sv=Float.toString(v);
	String mp,dp;
	int pp;
	//get point position
	pp=sv.indexOf(".");
	//get main part
	mp=sv.substring(0, pp);
	//get decimal part
	dp=sv.substring(pp+1, sv.length());
	//if decimal part longer then 2 digits,
	//take only 3 digits
	if(dp.length()>=3) dp=dp.substring(0, 3);
	res=mp+"."+dp;
	return res;
	}

/**
 * 
 * Get choices list for given column number.
 * The column must be choice type.
 * The table is vector of String[] array .
 * @param mtable table of String[] array
 * @param colnum the column number 
 * @return String[] array of choices.
 */
public static String[] getChoiceList(Vector mtable,int colnum){
	String str;
	boolean more;
	String[] st0= {"None"};
	
	
	if(mtable.size()==0)
		 return st0;
	//String vector
	Vector vec= new Vector();
	//add choices from the table mtable if any
	for(int i=0;i<mtable.size();i++){
		str=((String[]) mtable.elementAt(i))[colnum]; //get cat column
		more=true;
		for(int j=0;j<vec.size()&&more;j++){
			if(vec.indexOf(str)!= -1) //already exists in cat list,skip it 
				   more=false;
		}
	if(more) vec.addElement(str); //new category found in mtable, add it	
	}
	int sz=vec.size();
	String[] choicelist=new String[sz];
	vec.copyInto(choicelist);
	return choicelist;
}
/**
 * Get vector of words from string of words separated by delimiters.
 * @param str string of words containing separated words
 * @param sep separator used to separate words in the string str
 * @return vector of words
 */
public static Vector getVectorFromString(String str, String sep){
Vector vec= new Vector();
String str1= str.trim();
int idx0=0;
int idx=0;
if (str1.length()==0)
	return null;
int idxlast=str1.length()-1;

//check for 1st and last separator
if(str1.charAt(0)==sep.charAt(0)){
	idx0++;
	idx++;
}
if(str1.charAt(idxlast)==sep.charAt(0)){
	idxlast--;
}
//get words

while(idx<idxlast){
	//get next sep position
idx=str1.indexOf(sep, idx);
if(idx== -1){
	//no next separator
	idx=idxlast+1;
}
String word=str1.substring(idx0, idx);
idx0=idx+1;
idx++;
if(word.trim().length()>0){
	vec.addElement(word.trim());
}
}
return vec;
}

}

