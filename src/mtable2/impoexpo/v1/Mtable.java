package mtable2.impoexpo.v1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;



public class Mtable {
//public boolean firstTime=true;
	
	public ArrayList<MRec> mlist=null;

	
	
	public Mtable(ArrayList<MRec> mlist) {
	
	this.mlist = mlist;
}

	public Mtable() {
	this(new ArrayList<MRec>());
}

	public ArrayList<MRec> getMlist() {
		return mlist;
	}

	public void setMlist(ArrayList<MRec> mlist) {
		this.mlist = mlist;
	}
	public void addElm(MRec t){
		mlist.add(t);
		
	}
	public void delElm(int idx){
		
		if(mlist.size()>0){
			mlist.remove(idx);
		}
	}
	public MRec getElmAt(int idx){
		if(mlist.size()>0){
		   return mlist.get(idx);
		}
		return null;
	}
	public void setElmAt(int idx,MRec rec){
		if(mlist.size()>0){
			mlist.set(idx, rec);
		}
	}
	public ArrayList<String> getArrayList(){
		
		ArrayList<String> larr= new ArrayList<String>();
		if(mlist.size()==0) return larr;
		for(int i=0;i<mlist.size();i++){
			MRec elm= mlist.get(i);
			String dstr= elm.getStringDate()+","+elm.cat+","+elm.Amount+","+elm.note;
			larr.add(dstr);
			
		}
		return larr;
	}
	public Vector<String[]> getAsVector(){
		Vector<String[]> vect = new Vector<String[]>();
		if(mlist.size()==0) return vect;
		for(int i=0;i<mlist.size();i++){
			MRec elm= mlist.get(i);
			String[] ve= new String[4];
			ve[0]=elm.getStringDate();
			ve[1]=elm.cat;
			ve[2]=elm.Amount;
			ve[3]=elm.note;
			
			vect.add(ve);
			
			
		}
		return vect;
		
	}
	public void getSortedList(){
		//ArrayList<MRec> sorted= new ArrayList<MRec>();
		Collections.sort(mlist);
		
	}

	/**
	 * Make deep copy /clone of Mtable object
	 * @return
	 */
	public Mtable makeCopy(){
		Mtable ntable= new Mtable();
		ntable.mlist= new ArrayList<MRec>();
		for(MRec elm: mlist){
			MRec nelm= new MRec();
			nelm.dd=elm.dd;
			nelm.mm=elm.mm;
			nelm.yy=elm.yy;
			
			nelm.cat=new String(elm.cat);
			nelm.Amount= new String(elm.Amount);
			nelm.note= new String(elm.note);
			ntable.mlist.add(nelm);
		}
		return  ntable;
		
	}
	public ArrayList<String> sortLstByColumn(int columnIdx){
		ArrayList<String> arr= new ArrayList<String>();
		if(this.getSize()==0)
			return arr;
		for(int i=0;i<this.getSize();i++){
			arr.add(i, this.getElmAt(i).getValByCol(columnIdx));
		} 
		//String[] slist= new String[arr.size()];
		Collections.sort(arr);
		 
		return arr;
	}

	public String getLastStringElement() {
		if(mlist.size()==0)
				return "";
		return getArrayList().get(getArrayList().size()-1);
	}
	public String getStringElmAt(int idx){
		if(mlist.size()==0)
			return "";
		return getArrayList().get(idx);
		
	}
	public int getSize(){
		
		return mlist.size();
	}
	public void clear(){
		mlist.clear();
	}
	
    public double getSum(){
    	double sum= 0.0;
    	for(int i=0;i<mlist.size();i++){
    		sum=sum+Double.parseDouble(getElmAt(i).Amount);
    	}
    	
    	return sum;
    	
    }

	
}
