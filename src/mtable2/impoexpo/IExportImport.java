
package mtable2.impoexpo;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 *
 * @author raitis
 * @param <T>
 */
public interface IExportImport<T> {

    /**
     * Export data to file filePath
     * @param filePath
     * @param tables
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException 
     */
     void exportData(String filePath,List<T> tables) throws Exception;

    /**
     * Import tables from file filePath
     * @param filePath
     * @return List of MTable
     * @throws IOException
     * @throws Exception 
     */
    List<T> importData(String filePath) throws IOException, Exception;
    
}
