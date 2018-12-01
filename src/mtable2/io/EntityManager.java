
package mtable2.io;

import java.util.List;

/**
 *THIS IS a STUB Interface.
 * Should be removed if JPA provider lib is included
 * @author raitis
 * @param <T>
 */
public interface EntityManager <T> {
    
    interface EntityTransaction{
        void begin();
        void commit();
    }
    
    void persist(Object entity);
    T merge(T entity);
    void remove(Object entity);
    EntityTransaction getTransaction();
   T find(Class<T> entityClass, Long id);
}
