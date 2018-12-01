
package mtable2.io;

import java.io.IOException;
import java.util.List;
import mtable2.model.TableInfo;


//TODO: to be deleted
/**
 *
 * @author raitis
 * @param <T>
 */
public interface IPersistenceManager<T> {

    T create(String name,T obj) throws Exception;
    
    T update(Long id, T obj) throws Exception;
    
    T find(Class<T> entityClass, Long id) throws Exception;
    
    List<T> findAll(Class<T> entityClass) throws Exception;
    
    List<?> findAllSuper(Class<? super T> entityClass) throws Exception;
    
    List<T> findAllById(Long Id,Class<T> entityClass) throws Exception;
    
    void delete(Long id, T obj) throws Exception;
    
    void saveTablesInfoState(List<TableInfo> objs) throws IOException;
    
    void saveTables(List<T> tables) throws Exception;

    List<T> getTables(Class<T> entityClass) throws Exception;
}
