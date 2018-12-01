
package mtable2.io;

import java.io.IOException;
import java.util.List;




public class JpaRepositoryImpl<T> implements IJpaRepository<T> {

    private EntityManager em;
    private IJpaQueries<T> query;

    public JpaRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    public void setQuery(IJpaQueries<T> query) {
        this.query = query;
    }
    
    
    @Override
    public void saveList(List<T> ListOfObjects)  throws Exception {
     if (em != null) {
            em.getTransaction().begin();
            for (T ListOfObject : ListOfObjects) {
                em.persist(ListOfObject);
            }
            em.getTransaction().commit();
        }    
    }

    @Override
    public List<T> readListDB(Class<T> entityClass) {
    List<T> ListOfObjects=null;
        if (em != null && query != null) {
            em.getTransaction().begin();
           
            ListOfObjects= query.findAll(entityClass);
            em.getTransaction().commit();
        }

        
        return ListOfObjects;  
    }

    @Override
    public List<T> readListDB(Long Id, Class<T> entityClass) {
    List<T> ListOfObjects=null;
        if (em != null && query != null) {
            em.getTransaction().begin();
           
            ListOfObjects= query.findAll(Id, entityClass);
            em.getTransaction().commit();
        }

        
        return ListOfObjects;  
    }
    
    @Override
    public void create(T obj) throws Exception{
    if (em != null) {
            em.getTransaction().begin();
           
                em.persist(obj);
            
            em.getTransaction().commit();
        }      
    }

    @Override
    public <T> T update(Long id, T obj)  throws Exception {
          if (em != null) {
            em.getTransaction().begin();
           
              T robj = (T) em.merge(obj);
            
            em.getTransaction().commit();
            return robj;
        } 
          return null;
    }

    @Override
    public <T> T find(Class<T> entityClass, Long id) throws Exception {
          if (em != null) {
           
              T robj = (T) em.find(entityClass, id);
            
            return robj;
        }
          return null;
    }
    
    

    @Override
    public void delete(Object obj) throws Exception{
        
        if (em != null) {
            em.getTransaction().begin();
           
            em.remove(obj);
            
            em.getTransaction().commit();
            
        } 
    }

    @Override
    public void saveTablesInfoState(List<T> objs) {
        
        if(em != null){
            
            em.getTransaction().begin();
            for (T obj : objs) {
                em.merge(obj);
            }
            
            em.getTransaction().commit();
        }
        
    }

    
}
