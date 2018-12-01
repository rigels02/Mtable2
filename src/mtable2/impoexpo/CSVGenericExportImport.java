package mtable2.impoexpo;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import mtable2.io.TxtReaderWriter;

/**
 *
 * @author raitis
 */
public class CSVGenericExportImport<T extends ICSV> implements IExportImport<T> {

    private final String delimiter;
    private Class<T> clazz;

    public CSVGenericExportImport(Class<T> clazz) {
        this.delimiter = ",";
        this.clazz = clazz;

    }

    public CSVGenericExportImport(Class<T> clazz, String delimiter) {
        this.delimiter = delimiter;
        this.clazz = clazz;
    }

    @Override
    public void exportData(String filePath, List<T> table) throws Exception {
        TxtReaderWriter rw = new TxtReaderWriter(filePath);
        StringBuilder sb = new StringBuilder();
        for (T mtable : table) {
            sb.append(mtable.formatHeader()).append("\n");
            sb.append(mtable.writeCSVString(delimiter));
        }

        rw.writeToFile(sb.toString());
    }

    @Override
    public List<T> importData(String filePath) throws IOException, Exception {
        TxtReaderWriter rw = new TxtReaderWriter(filePath);
        String imported = rw.ReadFromFile();
        String[] lines = imported.split("\n");
        //find comment indexes
        //Comment is a table header and keeps table's name
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < lines.length; i++) {

            if (lines[i].startsWith(clazz.newInstance().getHeaderMarker())) {
                indexes.add(i);
            }
        }
        List<T> tables = new ArrayList<>();

        for (int i = 0; i < indexes.size(); i++) {
            Integer idx1 = indexes.get(i);
            Integer idx2 = (i < indexes.size() - 1) ? indexes.get(i + 1) : null;

            //get table name
            // by parsing header
            T table = clazz.newInstance();
            table.parseHeader(lines[idx1]);
            //Build rows string for current table with name 'name'
            StringBuilder sb = new StringBuilder();
            if (idx2 != null) //not last table
            {
                for (int s = idx1 + 1; s < idx2; s++) {
                    sb.append(lines[s]).append("\n");
                }
            } else {
                for (int s = idx1 + 1; s < lines.length; s++) {
                    sb.append(lines[s]).append("\n");
                }
            }
            try {
                table.readCSVString(sb.toString(), delimiter);
            } catch (Exception ex) {
                throw new Exception("Error after line#: "+indexes.get(i)+"\n"+ex.getMessage());
            }
            tables.add(table);
        }

        return tables;
    }

   

}
