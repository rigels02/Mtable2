
package mtable2.control;


import java.util.ArrayList;
import java.util.List;
import mtable2.model.TableData;

/**
 *
 * @author raitis
 */
class Utils {
    
    static List<String> getCatList(List<TableData> rows){
        List<String> list= new ArrayList<>();
        rows.stream()
             .filter((row) -> ( !list.contains(row.getCat())))
             .forEachOrdered((row) -> {
            list.add(row.getCat());
        });
        return list;
    }
    
    static List<String> getNotesList(List<TableData> rows){
        List<String> list= new ArrayList<>();
        for (TableData row : rows) {
            if( !list.contains(row.getNote())){
              list.add(row.getNote());
            }
        }
        return list;
    }
}
