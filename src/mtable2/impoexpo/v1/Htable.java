package mtable2.impoexpo.v1;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Htable {
	public final static int HVersion=100;
	public final static int TVersion=100;
    public final static String HName="Header";
    public final static String TName="MTable";
    
	public ArrayList<HRec> hlist=null;
	
	
	public Htable(ArrayList<HRec> hlist) {
		
		this.hlist = hlist;
	}
	public Htable(){
		this(new ArrayList<HRec>());
		
	}

	//----methods---//
	public ArrayList<HRec> getHlist() {
		return hlist;
	}

	public void setHlist(ArrayList<HRec> list) {
		this.hlist = list;
	}
	public void addElm(HRec t){
		hlist.add(t);
		
	}
	public void setElmAtIdx(int idx,HRec rec){
		hlist.set(idx, rec);
	}
	public void addElm(String title){
		if(hlist.size()==0){
			hlist.add(new HRec(0, title));
		return;
		}
		long idx= getNewFileIdx();
		hlist.add(new HRec(idx, title));
	}
	/**
	 * Modified: 30.08.2015
	 * Find new table's file index. Take next maximum or minimum (>= 0) value from unused indexes.
	 * @return new table's file index
	 */
	public long getNewFileIdx() {

		int sz= hlist.size();
		int newidx=sz;
		if(sz==0) return 0;

		List<Long> lst=new ArrayList<Long>();
		for(int i=0;i<sz;i++){

			lst.add(hlist.get(i).Idx);
		}
		Collections.sort(lst);

		for(int i=0; i<sz;i++){
			long idx=lst.get(i);
			if(i< idx){

				return i;
			}
		}

		return newidx;

	}
	public void delElm(int idx){
		
		if(hlist.size()>0){
			hlist.remove(idx);
		}
	}
	public HRec getElmAt(int idx){
		if(hlist.size()>0){
		   return hlist.get(idx);
		}
		return null;
	}
	public void setElmAt(int idx,HRec rec){
		if(hlist.size()>0){
			hlist.set(idx, rec);
		}
	}
	public ArrayList<String> getArrayList(){
		
		ArrayList<String> larr= new ArrayList<String>();
		if(hlist.size()==0) return larr;
		
		for(int i=0;i<hlist.size();i++){
			HRec elm= hlist.get(i);
			String dstr= TName+elm.Idx+","+elm.title+","+elm.type+","+elm.sumColNum;
			for(int j=0;j<elm.LCols.length;j++){
				dstr+=","+elm.LCols[j];
				
			}
			larr.add(dstr);
			
		}
		return larr;
	}
	public ArrayList<String> getTitleList(){
		ArrayList<String> larr= new ArrayList<String>();
		if(hlist.size()==0) return larr;
		
		for(int i=0;i<hlist.size();i++){
			HRec elm= hlist.get(i);
						
			larr.add(elm.title);
			
		}
		return larr;
		
	}
	

	public String getLastStringElement() {
		if(hlist.size()==0)
				return "";
		return getArrayList().get(getArrayList().size()-1);
	}
	public String getStringElmAt(int idx){
		if(hlist.size()==0)
			return "";
		return getArrayList().get(idx);
		
	}
	public int getSize(){
		
		return hlist.size();
	}
	public ArrayList<String> getTableTitles(){
		ArrayList<String> list= new ArrayList<String>();
		if(hlist.size()==0)
			return list;
		for(int i=0;i<hlist.size();i++){
		list.add(hlist.get(i).title);	
			
		}
		return list;
		
	}
	public String getTableTitle(int idx){
		if(hlist.size()==0){
			return "";
		}
		return hlist.get(idx).title;
	}
	public void clear() {
		hlist.clear();
		
		
	}
}
