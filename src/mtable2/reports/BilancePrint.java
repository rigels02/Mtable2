package mtable2.reports;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Vector;
import mtable2.model.TableData;



/**
 * Get balance data from default Table.
 * Table must be default table.
 * @author rbrodezh
 * @version 1.01
 */
public class BilancePrint {

	private Vector mtable;
	private String tableName;
	private Vector categoryLst;
	private Vector monthsLst;
	
	
	public boolean isForBilancePrint;
	private float TotalRemainSum;
	
	public BilancePrint(String tableTitle, List<TableData> data) {
		//super();
			
		
		this.mtable= getAsVector(data);
		if(this.mtable==null) return;
		
		this.isForBilancePrint =true;
		
		
		this.tableName= tableTitle;
		if(tableName.isEmpty()||tableName.equalsIgnoreCase("Non Table")){
			return;
		}
		this.isForBilancePrint= true;	
		this.categoryLst = new Vector();
		this.monthsLst= new Vector();
		//--------------------//
		getCategories();
		getMonths();
		//this.categoryLst = categoryLst;
		//this.monthsLst = monthsLst;
	}
	private boolean isInList(Vector Lst,String txt){
		for(int i=0;i<Lst.size();i++){
		String lstTxt = (String) Lst.elementAt(i);
		if(lstTxt.equals(txt))
			 return true;
		
		}
		return false;
	}
	private void getCategories(){
		int sz= mtable.size();
		String cat;
		for(int i=0;i<sz;i++){
			cat= ((String[])mtable.elementAt(i))[1]; //get Category
			if(! isInList(categoryLst, cat))
				   categoryLst.addElement(cat);
		}
	}
	private void getMonths(){
		//date is in format dd/mm/yyyy
		int sz= mtable.size();
		String date;
		for(int i=0;i<sz;i++){
			date= ((String[])mtable.elementAt(i))[0]; //get Date
			//30.11.2012 was: String mm =date.substring(3, 5);
                        String mm =date.substring(3);
			if(! isInList(monthsLst, mm))
				   monthsLst.addElement(mm);
		}
	}
	
	private float getTotalByCategory(String Cat){
		float Sum=0.0f;
		if(!isInList(categoryLst, Cat))
			 return 0.0f;
		//Sum
		for(int i=0;i<mtable.size();i++){
			String[] elm= (String[]) mtable.elementAt(i);
			if(elm[1].equals(Cat))
				  Sum+=Float.parseFloat(elm[2]); //get amount
		}
		return Sum;
	}
	
	private String printTotalForCategories(){
		float Sum=0.0f;
		float TotalSum=0.0f;
		float TotalMin=0.0f;
		String Cat;
		String txt="";
		if(categoryLst.size()==0) return "";
		txt+="Balance by categories:<br>";
		for(int i=0;i<categoryLst.size();i++){
			Cat=(String)categoryLst.elementAt(i);
			Sum = getTotalByCategory(Cat); 
		if(Sum>0){
			txt+=Utils.AddSpace(Utils.AddSpace(Cat, 10)+" "+Utils.toStrWithStrip(Sum),17)+" :<br>";
		}else{
			TotalMin+=Sum;
			txt+=Utils.AddSpace(Cat, 17)+" : "+Utils.toStrWithStrip(Sum)+"<br>";
		}
	     TotalSum+=Sum;
	     //Sum=0;
		}
		//String sTotalSum= Float.toString(TotalSum);
		//String sTotalMin;
		String sTotalSum= Utils.toStrWithStrip(TotalSum);
		String sTotalMin= Utils.toStrWithStrip(TotalMin);
		txt+="<hr>"+sTotalSum+"  :  "+sTotalMin+"<br><br>";
		return txt;
	}
	

	
	private float getTotalPerMonthByCategory(String mm,String Cat){
		float Sum=0.0f;
		if(!isInList(monthsLst, mm))
			 return 0.0f;
		//Sum
		for(int i=0;i<mtable.size();i++){
			String[] elm= (String[]) mtable.elementAt(i);
                        //30.11.2012 was: elm[0].substring(3, 5).equals(mm)
			if(elm[0].substring(3).equals(mm) && elm[1].equals(Cat))
				  Sum+=Float.parseFloat(elm[2]); //get amount
		}
		return Sum;
	}
	
	private String printTotalPerMonth(String mm){
		float Sum=0.0f;
		float TotalSum=0.0f;
		float TotalMin=0.0f;
		String txt="";
		if(!isInList(monthsLst, mm)) 
			    return "";
		//Sum
		txt+="<font color=\"blue\">Total per month: "+mm+"</font><br>";
		for(int i=0; i< categoryLst.size();i++){
			String Cat= (String) categoryLst.elementAt(i);
			Sum= getTotalPerMonthByCategory(mm, Cat);
			if(Sum>0){
			 txt+=Utils.AddSpace(Utils.AddSpace(Cat, 10)+" "+Utils.toStrWithStrip(Sum),17)+" :<br>";	
			}else {
			 TotalMin+=Sum;	
			 txt+=Utils.AddSpace(Cat, 17)+" : "+Utils.toStrWithStrip(Sum)+"<br>";	
			}
		TotalSum+=Sum;
		
		}
		TotalRemainSum += TotalSum;
		String sTotalSum= Utils.toStrWithStrip(TotalSum);
		String sTotalMin= Utils.toStrWithStrip(TotalMin);
		txt+="<hr>"+sTotalSum+"  :  "+sTotalMin+"<br>";
		txt+="<font color=\"red\">Remain. Sum= "+Utils.toStrWithStrip(TotalRemainSum)+"</font><br><br>";
		
		return txt;
	}
	private String printTotalForMonths(){
		if(monthsLst.size()==0) return "";
		String txt="";
		for(int i=0;i< monthsLst.size();i++){
			txt+=printTotalPerMonth((String)monthsLst.elementAt(i));
		}
		return txt;
	}
	
	public String printBilance(){
		String txt="";
		if(!this.isForBilancePrint) return txt;
                txt+="<html><head></head><body>";
		txt+="<pre>Balance for table: <font color=\"red\">"+this.tableName+"</font><br><br>";
		txt+=printTotalForCategories();
		txt+=printTotalForMonths()+"</pre></body></html>";
		//txt+="Remain. Sum= "+midlet.sgetSum()+"<br>";
		return txt;
	}

    private Vector getAsVector(List<TableData> data) {
        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
        //Warning !!! separator depends from Locale config
        //Force to use dot '.' as decimal separator!!!
        DecimalFormatSymbols mySymbols = new DecimalFormatSymbols();
        mySymbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#.##",mySymbols);
        Vector<String[]> vect = new Vector<>();
        if (data.isEmpty()) {
            return vect;
        }
        for (TableData tableData : data) {
            String[] ve = new String[4];
            ve[0] = sf.format(tableData.getCdate());
            ve[1] = tableData.getCat();
            ve[2] = df.format(tableData.getAmount());
            ve[3] = tableData.getNote();

            vect.add(ve);
        }
        return vect;
    }
}

