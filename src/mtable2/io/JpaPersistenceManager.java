package mtable2.io;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import mtable2.model.TableInfo;

/**
 *
 * @author raitis
 * @param <T>
 */
public class JpaPersistenceManager<T extends TableInfo> implements IPersistenceManager<T> {

    private IJpaRepository<T> jpaRepo;

    public JpaPersistenceManager() {
    }

    public JpaPersistenceManager(IJpaRepository<T> jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public T create(String name, T obj) throws Exception {
        if (obj == null) {
            throw new IllegalArgumentException("obj==NULL not allowed!");
        }

        if (jpaRepo != null) {
            //set obj modification time and name
            obj.setModTime(new Date());
            obj.setName(name);
            jpaRepo.create(obj);
        }

        return obj;
    }

    @Override
    public T update(Long id, T obj) throws Exception {
        if (obj == null || id == null) {
            throw new IllegalArgumentException("obj,id == NULL not allowed!");
        }
        if (jpaRepo != null) {
            obj.setModTime(new Date());

            jpaRepo.update(id, obj);
        }

        return obj;
    }

    @Override
    public T find(Class<T> entityClass, Long id) throws Exception {
        T obj = null;
        if (id == null) {
            throw new IllegalArgumentException("id == NULL not allowed!");
        }
        if (jpaRepo != null) {

            obj = jpaRepo.find(entityClass, id);
        }
        return obj;
    }

    @Override
    public List<T> findAll(Class<T> entityClass) throws Exception {
        List<T> obj = null;
        if (jpaRepo != null) {

            obj = jpaRepo.readListDB(entityClass);
        }
        return obj;
    }

    @Override
    public List<?> findAllSuper(Class<? super T> entityClass) throws Exception {
        List<?> obj = null;
        if (jpaRepo != null) {

            obj = jpaRepo.readListDB((Class<T>) entityClass);
        }
        return obj;
    }

    @Override
    public List<T> findAllById(Long Id, Class<T> entityClass) throws Exception {
        List<T> obj = null;
        if (jpaRepo != null) {

            obj = jpaRepo.readListDB(Id, entityClass);
        }
        return obj;
    }

    @Override
    public void delete(Long id, T obj) throws Exception {
        if (id == null) {
            throw new IllegalArgumentException("id == NULL not allowed!");
        }
        if (jpaRepo != null) {

            jpaRepo.delete(obj);
        }
    }

    @Override
    public void saveTablesInfoState(List<TableInfo> objs) throws IOException {
        if (jpaRepo != null) {
            jpaRepo.saveTablesInfoState((List<T>) objs);
        }
    }

    @Override
    public void saveTables(List<T> tables) throws Exception {
          if (jpaRepo != null) {
            jpaRepo.saveList(tables);
          
         }
    }

    
    public List<T> getTables(Class<T> entityClass) throws Exception {
        if (jpaRepo != null) {
           
            return jpaRepo.readListDB(entityClass);
          
         }
        return null;
    }

    
}
