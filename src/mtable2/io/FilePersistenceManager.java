package mtable2.io;

import java.io.IOException;
import java.util.List;
import mtable2.model.TableInfo;

/**
 *
 * @author raitis
 * @param <T>
 */
public class FilePersistenceManager<T  extends TableInfo> implements IPersistenceManager<T> {

    private IFileIO<T> fileIO;

    public FilePersistenceManager() {
    }

    public FilePersistenceManager(IFileIO<T> fileIO) {
        this.fileIO = fileIO;
    }

    @Override
    public T create(String name, T obj) throws Exception {
        if (obj == null) {
            throw new IllegalArgumentException("obj==NULL not allowed!");
        }

        if (fileIO != null) {
            //obj setting are done in fileIO
            Long id = fileIO.create(name, obj);
            obj.setId(id);
        }
        return obj;
    }

    @Override
    public T update(Long id, T obj) throws Exception {
        if (obj == null || id == null) {
            throw new IllegalArgumentException("obj,id == NULL not allowed!");
        }

        if (fileIO != null) {
            fileIO.update(id, obj);
        }
        return obj;
    }

    @Override
    public T find(Class<T> entityClass, Long id) throws Exception {
        T obj = null;
        if (id == null) {
            throw new IllegalArgumentException("id == NULL not allowed!");
        }
        if (fileIO != null) {
            obj = fileIO.getObj(id);
        }

        return obj;
    }

    @Override
    public List<T> findAll(Class<T> entityClass) throws Exception {
        List<T> obj = null;

        if (fileIO != null) {
            obj = fileIO.getByType(entityClass);
        }

        return obj;
    }

    @Override
    public List<?> findAllSuper(Class<? super T> entityClass) throws Exception {
        List<?> obj = null;

        if (fileIO != null) {
            obj = fileIO.getByType((Class<T>) entityClass);
        }

        return obj;
    }

    @Override
    public List<T> findAllById(Long Id, Class<T> entityClass) throws Exception {
        List<T> obj = null;

        if (fileIO != null) {
            obj = fileIO.getByTypeId(Id, entityClass);
        }

        return obj;
    }

    @Override
    public void delete(Long id, T obj) throws Exception {
        if (id == null) {
            throw new IllegalArgumentException("id == NULL not allowed!");
        }

        if (fileIO != null) {
            fileIO.delete(id);
        }
    }

    @Override
    public void saveTablesInfoState(List<TableInfo> objs) throws IOException {

        if (fileIO != null) {
            fileIO.saveTablesInfoState();
        }
    }

    @Override
    public void saveTables(List<T> tables) throws Exception {
      fileIO.saveTables(tables);
    }

    @Override
    public List<T> getTables(Class<T> entityClass) throws Exception {
      return fileIO.getTables();
    }

}
