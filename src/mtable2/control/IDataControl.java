
package mtable2.control;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import mtable2.model.TableData;

/**
 *
 * @author raitis
 */
public interface IDataControl {
    
    
    //------ TableData methods
    /**
     * Get row by index id
     * Method returns clone of item.
     * Immutable
     * @param id
     * @return row item
     */
    TableData getItem(int id)  throws IndexOutOfBoundsException;
    /**
     * new row is ready to be added
     * @param row  the new object
     * @throws Exception
     */
    void addItem(TableData row) throws Exception;
    
    /**
     * Modify  selected table's row .
     * @param id row's id
     * @param row data
     * @throws Exception
     */
    void editItem(int id, TableData row) throws Exception;

    /**
     * Update rows data for selected table
     * @param rows
     * @throws Exception 
     */
    void updateItems(List<TableData> rows)throws Exception;
    
    /**
     * the selected row by id is ready to be deleted
     * @param id 
     * @throws Exception 
     */
    
    void delItem(int id) throws Exception;
    
    /**
     * We do not need always to read rows list from persistence (file,DB).
     * For better performance we can use rows list kept in DataControl
     * memory, but BE careful with direct access
     * @return 
     */
     List<TableData> getItemsFromDataControlMemory();
   
    /**
     * Get tables rows (Items) from controller memory
     * Returned items are cloned 
     * @return 
     */
    List<TableData> getItems();
    
     
   
    
    //Below for Simulation
    
    /**
     * TEst persistence thread startup
     */
     void startPersistDataControlMemoryThread();
    
     /**
      * TEST method
      * @throws FileNotFoundException
      * @throws UnsupportedEncodingException 
      */
     void persistDataControlMemory() throws FileNotFoundException, UnsupportedEncodingException;
   
    /**
     * TEst persistence thread
     */
    //private void persistDataControlMemory();
    /**
     * Test ui access to locked DataControlMemory
     */
    
     TableData simulateAccessToDataControlMemoryTableData(String txt) throws Exception;
}
