
package mtable2.io;

import java.util.List;

/**
 *
 * @author raitis
 * @param <T>
 */
public interface IJpaQueries<T> {
    
     List<T> findAll(Class<T> entityClass);
     
     List<T> findAll(Long Id, Class<T> entityClass);
}
