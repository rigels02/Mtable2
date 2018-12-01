package mtable2.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.IIOException;
import mtable2.model.TableInfo;

/**
 *
 * @author raitis
 * Implementation of IFileIO<T> for class what is
 * extension of TableInfo.
 * @param <T extends TableInfo> working object
 */
public class FileIOImpl<T extends TableInfo> implements IFileIO<T> {

    private final static String IDXFILE = "mtable.idx";
    private final static String DATAFILE = "mtable_";
    private final static String DATAEXT = ".dat";
    private final static String MAGICSIGN = "11-03-2017@#$%";
    private Timestamp timeStamp;

    private List<TableInfo> tablesInfo;

    
    /**
     * Default constructor with exceptions...
     * Case when factory could be useful
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public FileIOImpl() throws IOException, ClassNotFoundException {

        tablesInfo = new ArrayList<>();
        File file = new File(IDXFILE);
        if (!file.exists()) {
            file.createNewFile();
            List<T> ListOfObjects = new ArrayList<>();
            saveEmptyList(IDXFILE, ListOfObjects);
        }
        tablesInfo = (List<TableInfo>) readListStream(IDXFILE);

    }

    private void saveEmptyList(String fileName, List<T> ListOfObjects) throws IOException {
        saveList(fileName, ListOfObjects);
    }

    @Override
    public List<TableInfo> getTableInfoLst() {

        return tablesInfo;
    }
    
    @Override
    public void saveTablesInfoState() throws IOException {
        saveList(IDXFILE, (List<T>) tablesInfo);
    }

    @Override
    public Long create(String name, T obj) throws Exception {

        if (name == null || name.isEmpty()) {
            String msg = "Name must be defined!";
            logError(Level.SEVERE, msg, null);

            throw new IllegalArgumentException(msg);
        }
        if (obj == null) {
            String msg = "Obj must not be NULL!";
            logError(Level.SEVERE, msg, null);

            throw new IllegalArgumentException(msg);
        }
        String newFile;
        TableInfo ti = createTablesInfo(name);
        long newIdx = ti.getId();
        obj.setModTime(ti.getModTime());
        obj.setName(name);
        obj.setId(newIdx);
        newFile = composeIdxFileName(newIdx);
        write2File(newFile, obj);
        write2IdxFile();
        return newIdx;
    }

    @Override
    public Long update(Long Id, T obj) throws IOException {
        if (Id == null || obj == null) {
            String msg = "Argument must not be NULL!";
            logError(Level.SEVERE, msg, null);
            throw new IllegalArgumentException(msg);
        }
        Long idx = null;
        if (tablesInfo.isEmpty()) {
            return null;
        }
        for (TableInfo tableInfo : tablesInfo) {
            if (tableInfo.getId() == Id) {
                tableInfo.setModTime(new Date());
                obj.setId(tableInfo.getId());
                obj.setModTime(tableInfo.getModTime());
                obj.setName(tableInfo.getName());
                obj.setSelected(tableInfo.isSelected());
              
                String fn = composeIdxFileName(Id);
                write2File(fn, obj);
                saveTablesInfoState(); //added 17.Mar.2017
                return Id;

            }
        }
        return null;
    }

    @Override
    public Long delete(Long Id) throws Exception {
        if (Id == null) {
            String mag = "Argument must not be NULL!";
            logError(Level.SEVERE, mag, null);
            throw new IllegalArgumentException(mag);
        }
        for (int i = 0; i < tablesInfo.size(); i++) {
            TableInfo tableInfo = tablesInfo.get(i);
            if (tableInfo.getId() == Id) {
                tablesInfo.remove(i);
                String fn = composeIdxFileName(Id);
                File fi = new File(fn);
                fi.delete();
                write2IdxFile();
                return Id;
            }
        }
        return null;
    }

    @Override
    public T getObj(Long Id) throws IOException, ClassNotFoundException {
        T obj = null;
        if (tablesInfo.isEmpty()) {
            return null;
        }
        for (TableInfo tableInfo : tablesInfo) {
            if (tableInfo.getId() == Id) {
                String fn = composeIdxFileName(Id);
                obj = readFile(fn);
                copyTablesInfo(obj, tableInfo);
                return obj;

            }
        }
        return null;
    }

    @Override
    public void deleteAll() {

        for (TableInfo tableInfo : tablesInfo) {
            long idx = tableInfo.getId();
            String filename = composeIdxFileName(idx);
            File fi = new File(filename);
            if (fi.exists()) {
                fi.delete();
            }
        }
        File file = new File(IDXFILE);
        if (file.exists()) {
            file.delete();
        }
        tablesInfo.clear();
    }

    @Override
    public Timestamp getTimeStamp() {

        return timeStamp;
    }

    @Override
    public void saveList(String fileName, List<T> ListOfObjects) throws IOException {
        ObjectOutputStream oos = null;

        oos = openOutStream(fileName);
        if (oos != null) {
            oos.writeObject(ListOfObjects);

        }
        if (oos != null) {
            try {
                oos.flush();
                oos.close();
            } catch (IOException ex) {
                logError(Level.SEVERE, null, ex);
            }
        }

    }

    @Override
    public List<T> readListStream(String fileName) throws IOException, ClassNotFoundException {
        List<T> ListOfObjects = null;

        try (ObjectInputStream ois = openInStream(fileName)) {
            if (ois != null) {
                ListOfObjects = (List<T>) ois.readObject();
            }
            ois.close();
        }

        return ListOfObjects;
    }

    @Override
    public void saveTables(List<T> tables) throws Exception {
        if (tables.isEmpty()) {
            return;
        }
        //Check tables names
        for (TableInfo tableInfo : tables) {
            if(tableInfo == null || tableInfo.getName()== null || tableInfo.getName().isEmpty()){
              throw new RuntimeException("Wrong tableInfo or missing table name!");
            }
        }
        List<TableInfo> tinfo = new ArrayList<>();
        for (int i = 0; i < tables.size(); i++) {
            T table = tables.get(i);
            TableInfo tableInfo = (TableInfo) table;
            tableInfo.setId(arrangeNewIdx(tinfo));
            //Keep existing table ModTime from tables if it is not NULL
            //useful in case of import from JSON
            if(tableInfo.getModTime()==null){
            tableInfo.setModTime(new Date());
            }
            table.setModTime(tableInfo.getModTime());
            table.setId(tableInfo.getId());
            tinfo.add(tableInfo);
        }

        for (T table : tables) {

            String newFile = composeIdxFileName(table.getId());
            write2File(newFile, table);
        }
        saveList(IDXFILE, (List<T>) tinfo);
        //reload fresh index file
        tablesInfo = (List<TableInfo>) readListStream(IDXFILE);
    }
    @Override
    public List<T> getTables() throws IOException, ClassNotFoundException{
      List<TableInfo> tinfo= getTableInfoLst();
      List<T> tables= new ArrayList<>();
        for (TableInfo tableInfo : tinfo) {
          String fi = composeIdxFileName(tableInfo.getId());
          T table = readFile(fi);
          TableInfo rtableInfo = (TableInfo)table;
          rtableInfo.setId(tableInfo.getId());
          rtableInfo.setName(tableInfo.getName());
          rtableInfo.setModTime(tableInfo.getModTime());
          rtableInfo.setSelected(tableInfo.isSelected());
            tables.add(table);
        }
        return tables;
    }
    /**
     * Write file's heade info magicsign and timestamp
     *
     * @param oos
     * @throws IOException
     */
    private void writeHeader(ObjectOutputStream oos) throws IOException {
        oos.writeUTF(MAGICSIGN);
        //TimeStamp is in format 2016-11-16 06:43:19.77
        oos.writeObject(timeStamp = new Timestamp(System.currentTimeMillis()));

    }

    /**
     * Read file's header info magicsign and timestamp
     *
     * @param ois
     */
    private void readHeader(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        String magic = ois.readUTF();
        if (!magic.equals(MAGICSIGN)) {
            throw new IIOException("Bad input file's header!");
        }
        timeStamp = (Timestamp) ois.readObject();

    }

    private ObjectOutputStream openOutStream(String fileName) throws IOException {
        ObjectOutputStream oos = null;
        if (fileName != null) {

            try {
                oos = new ObjectOutputStream(new FileOutputStream(fileName));
                writeHeader(oos);
            } catch (FileNotFoundException ex) {
                logError(Level.SEVERE, null, ex);
                throw new IOException(ex.getMessage());
            } catch (IOException ex) {
                logError(Level.SEVERE, null, ex);
                throw new IOException(ex.getMessage());
            }
        }
        return oos;
    }

    private ObjectInputStream openInStream(String fileName) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = null;
        if (fileName != null) {

            try {
                ois = new ObjectInputStream(new FileInputStream(fileName));
                readHeader(ois);
            } catch (FileNotFoundException ex) {
                logError(Level.SEVERE, null, ex);
                throw new IOException(ex.getMessage());
            } catch (IOException ex) {
                logError(Level.SEVERE, null, ex);
                throw new IOException(ex.getMessage());
            }
        }
        return ois;
    }

    private long arrangeNewIdx(List<TableInfo> tinfo) {
        ArrayList<Long> idxLst = new ArrayList<>();
        long Idx = -1;
        if (tinfo.isEmpty()) {
            return 1;
        }
        for (int i = 0; i < tinfo.size(); i++) {
            idxLst.add(tinfo.get(i).getId());
        }
        Collections.sort(idxLst);
        Idx = 1;
        for (Long idx : idxLst) {
            if (Idx != idx) {
                return Idx;
            }
            Idx++;
        }
        return Idx;
    }

    private String composeIdxFileName(long newIdx) {
        return DATAFILE + newIdx + DATAEXT;
    }

    private void logError(Level level, String msg, Object ex) {
        Logger.getLogger(FileIOImpl.class.getName()).log(level, msg, ex);
    }

    private void write2File(String filePath, T obj) throws IOException {
        ObjectOutputStream oos = null;

        oos = openOutStream(filePath);
        if (oos != null) {
            oos.writeObject(obj);

        }

        if (oos != null) {
            try {
                oos.flush();
                oos.close();
            } catch (IOException ex) {
                logError(Level.SEVERE, null, ex);

            }
        }
    }

    private T readFile(String fn) throws IOException, ClassNotFoundException {
        ObjectInputStream iis = null;
        T obj = null;
        iis = openInStream(fn);
        if (iis != null) {
            obj = (T) iis.readObject();

        }
        if (iis != null) {
            try {

                iis.close();
            } catch (IOException ex) {
                logError(Level.SEVERE, null, ex);

            }
        }
        return obj;
    }

    private void write2IdxFile() throws IOException {
        ObjectOutputStream oos = null;

        oos = openOutStream(IDXFILE);
        if (oos != null) {
            oos.writeObject(tablesInfo);

        }

        if (oos != null) {
            try {
                oos.flush();
                oos.close();
            } catch (IOException ex) {
                logError(Level.SEVERE, null, ex);

            }
        }
    }

    private TableInfo createTablesInfo(String name) {
        long newIdx = -1;
        newIdx = arrangeNewIdx(tablesInfo);

        TableInfo ti = new TableInfo(name, new Date());
        ti.setId(newIdx);
        tablesInfo.add(ti);
        return ti;
    }

    private void copyTablesInfo(T table, TableInfo tableInfo){
        if(table == null || tableInfo == null)
            throw new RuntimeException("Null pointer...");
        TableInfo ttableInfo = (TableInfo) table;
        ttableInfo.setId(tableInfo.getId());
        ttableInfo.setModTime(tableInfo.getModTime());
        ttableInfo.setName(tableInfo.getName());
        ttableInfo.setSelected(tableInfo.isSelected());
    }
    private TableInfo findTableInfoByFileID(long Id){
        for (TableInfo tableInfo : tablesInfo) {
            if(tableInfo.getId() == Id)
                return tableInfo;
        }
        return null;
      }
    
    @Override
    public List<T> getByTypeId(long Id, Class<T> type) throws Exception {
       List<T> obj=  new ArrayList<>();
        if(type == TableInfo.class){
          
           obj.add((T) tablesInfo.get((int)Id));
           return obj;
        }else{
           String fi = composeIdxFileName(Id);
           T table = readFile(fi);
            copyTablesInfo(table, findTableInfoByFileID(Id));
           obj.add(table);
            return obj;
        }
       
    }

    @Override
    public List<T> getByType(Class<T> type) throws Exception {
        List<T> objs = new ArrayList<>();
        if (type == TableInfo.class) {

            return (List<T>) tablesInfo;
        } else {
            for (TableInfo tableInfo : tablesInfo) {
                long id = tableInfo.getId();
                String fi = composeIdxFileName(id);
                T obj = readFile(fi);
                copyTablesInfo(obj, tableInfo);
                objs.add(obj);

            }

            return objs;
        }
    }

    /**
     * *
     * public static void main(String[] args) throws IOException,
     * ClassNotFoundException { IdxFileIOImpl<MTable> io = new
     * IdxFileIOImpl<MTable>(); TableInfo t1 = new TableInfo("table_1", new
     * Date()); TableInfo t2 = new TableInfo("table_2", new Date()); TableInfo
     * t3 = new TableInfo("table_2", new Date()); t1.setId(3); t2.setId(2);
     * t3.setId(5); io.tablesInfo.add(t1); io.tablesInfo.add(t2);
     * io.tablesInfo.add(t3); long nIdx = io.arrangeNewIdx(); }
    **
     */
}
