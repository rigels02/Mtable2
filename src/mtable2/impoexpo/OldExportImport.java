
package mtable2.impoexpo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mtable2.impoexpo.v1.Exporter;
import mtable2.impoexpo.v1.Importer;
import mtable2.impoexpo.v1.TManager;
import mtable2.io.TxtReaderWriter;
import mtable2.model.MTable;

/**
 *
 * @author raitis
 */
public class OldExportImport implements IExportImport<MTable>{

    @Override
    public void exportData(String filePath, List<MTable> tables) throws Exception {
    Exporter export = new Exporter(new TManager(tables));
                 {
                    try {
                        export.dumpAll();
                        String result = export.getDumptxt().toString();
                        TxtReaderWriter txtWriter = new TxtReaderWriter(filePath);
                        txtWriter.writeToFile(result);

                    } catch (Exception ex) {
                        Logger.getLogger(OldExportImport.class.getName()).log(Level.SEVERE, null, ex);
                        throw new Exception(ex.getMessage());
                    }
                }   
    }

    @Override
    public List<MTable> importData(String filePath) throws IOException, Exception {
    TManager tmgr;
                Importer imp = new Importer(tmgr = new TManager(new ArrayList<>()));
                TxtReaderWriter reader = new TxtReaderWriter(filePath);

                boolean ok = imp.execImport(reader.ReadFromFile());
                if (!ok) {
                    throw new Exception("Import Error:\n" + imp.errorTxt());
                }
                return tmgr.getMTables();   
    }
    
}
