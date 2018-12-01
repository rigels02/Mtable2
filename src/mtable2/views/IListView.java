
package mtable2.views;


import java.util.List;
import mtable2.model.TableData;

/**
 *
 * @author raitis
 */
public interface IListView {
    
    /**
     * Update the list view
     * @param rows 
     */
    void updateListView( List<TableData> rows);
}
