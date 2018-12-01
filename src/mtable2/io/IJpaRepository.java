package mtable2.io;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author raitis
 * @param <T>
 */
public interface IJpaRepository<T> {

    void create(T obj) throws Exception;

    <T> T update(Long id, T obj) throws Exception;

    <T> T find(Class<T> entityClass, Long id) throws Exception;

     
    void delete(Object obj) throws Exception;

    /**
     * Save List of entities into DB (Jpa)
     *
     * @param ListOfObjects
     * @throws IOException
     */
    public void saveList(List<T> ListOfObjects) throws Exception;

    /**
     * Read entities from DB (JPA)
     *
     * @param entityClass
     * @return
     */
    public List<T> readListDB(Class<T> entityClass);
    /**
     * Get list of entity class by id
     * @param Id
     * @param entityClass
     * @return 
     */
    public List<T> readListDB(Long Id, Class<T> entityClass);

    public void saveTablesInfoState(List<T> objs);
    
    
}
