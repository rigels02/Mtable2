
package mtable2.ui;

import java.util.List;
import mtable2.control.ITableControl;
import mtable2.model.MTable;

/**
 * callback methods from ToolsJDialog ui
 * @author raitis
 */
public interface IToolsDialog {
    
    void makeOldExport(String filePath);
    
    void makeOldImport(String filePath);
    
    void makeJsonExport(String filePath);
    
    void makeJsonImport(String filePath);
    
    void makeCSVMTableExport(String filePath, List<MTable> selectedTables,String delimiter);
    
    void makeCSVMTableImport(String filePath,String delimiter);
    
    void tcpReceivingCompletedUpdateListView();
    
    ITableControl getDataControlInterface();
}
