
package mtable2.impoexpo;

/**
 *
 * @author raitis
 */
public interface ICSV {

    String formatHeader();

    String getHeaderMarker();

    void parseHeader(String headerString);

    void readCSVString(String csvRows, String delimiter) throws Exception;

    String writeCSVString(String delimiter);
    
}
