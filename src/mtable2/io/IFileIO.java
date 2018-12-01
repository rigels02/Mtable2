
package mtable2.io;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import mtable2.model.TableInfo;

/**
 * Keep data in different files.
 * Every object is stored in individual file.
 * The indexes to every file is stored in index file.
 * The index file keep list<TableInfo> data, where
 * TableInfo.Id is index (Idx) of file.File name is
 * like <FileName>_Idx.
 * NOTE: AFter completion rename IIdxFileIO => IFileIO ?
 * @author raitis
 * @param <T>
 */
public  interface IFileIO<T extends TableInfo> {
    
    List<TableInfo> getTableInfoLst();

    List<T> getByTypeId(long Id,Class<T> type) throws Exception;
    
    List<T> getByType(Class<T> type)  throws Exception;
    
    void saveTablesInfoState() throws IOException;
    
    /**
     * Create and save new obj record
     * @param name name assigned for object.
     * @param obj obj must not be null
     * @return index of created record, or null if error
     * @throws java.lang.Exception
     */
    Long create(String name, T obj) throws Exception;
    
    /**
     * Update selected obj by id.
     * @param Id >0
     * @param obj
     * @return obj Id or null if error
     * @throws java.lang.Exception
     */
    Long update(Long Id, T obj) throws Exception;
    
    /**
     * Delete obj by id
     * @param Id
     * @return deleted obj id or null if error
     * @throws java.lang.Exception
     */
    Long delete(Long Id) throws Exception;
    
    /**
     * Get obj by index id (File index)
     * @param Id
     * @return T object or null if error
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    T getObj(Long Id) throws IOException, ClassNotFoundException;
    /**
     * Delete all files!
     */
    void deleteAll();
    /**
     * Get curent file's timestamp
     * @return timestamp java.​sql.Timestamp
     */
    public Timestamp getTimeStamp();
    
    void saveList(String fileName, List<T> ListOfObjects) throws IOException;
    
    List<T> readListStream(String fileName) throws IOException, ClassNotFoundException;
    
    /**
     * Read all tables by using index file.
     * Returned tables TableInfo fields are populated based on info from Index file.
     * @return
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    List<T> getTables() throws IOException, ClassNotFoundException;

    /**
     * Save tables. Rewrite old tables info.
     * Warning!: The new IndexFile is going to be generated for tables.
     * @param tables
     * @throws IOException 
     */
    void saveTables(List<T> tables) throws Exception;

}
