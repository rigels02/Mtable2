
package mtable2.control;

import java.io.IOException;
import java.util.List;
import mtable2.model.MTable;
import mtable2.model.TableData;
import mtable2.model.TableInfo;

/**
 *
 * @author raitis
 */
public interface ITableControl {

    /**
     * Create new table and save in DataControl internal data
     * @param name
     * @param table
     * @throws Exception
     */
    void create(String name, MTable table) throws Exception;

     /**
     * update current selected table only
     * @param id
     * @throws Exception
     */
    void update(Long id) throws Exception;
    
    /**
     * delete  any table by id
     * @param id
     * @throws Exception
     */
    void delete(Long id) throws Exception;

    /**
     * Get selected table index in TableInfo list.
     * Selected table means - currently used table index in TableInfo list
     * @return 
     */
    int getSelectedTableIdx();
    /**
     * Get selected table File Index (Id) from TableInfo list.
     * @return 
     */
    long getSelectedTableId();
    
    long getTableIdByIdx(int idx);
    
    String getSelectedTableName();

   
    
    /**
     * Get list of TableInfo from persistence
     * @return
     * @throws Exception 
     */
    List<TableInfo> getTableInfoLst() throws Exception;

    List<MTable> getAllTables() throws Exception;
    
    public void saveAllTables(List<MTable> tables) throws Exception;
    
    
    /**
     * Get table by id select it and save in DataControl internal data
     * @param id
     * @throws Exception
     */
    void loadTableAndSelect(Long id) throws Exception;

    void renameSelectedTable(String name) throws IOException, Exception;

    void renameTableByid(long Id, String name) throws IOException, Exception;
   
}
