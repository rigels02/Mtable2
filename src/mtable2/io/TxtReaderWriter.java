
package mtable2.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author raitis
 */
public class TxtReaderWriter {
    
    private String filePath;
    private PrintStream out;
    

    public TxtReaderWriter(String filePath) {
        this.filePath = filePath;
    }
    
    private void openWriter() throws FileNotFoundException, UnsupportedEncodingException {
       
            out = new PrintStream(filePath, "UTF-8");
            
    }
    public void writeToFile(String txt) throws FileNotFoundException, UnsupportedEncodingException{
        openWriter();
        out.print(txt);
        out.close();
    }
    public String ReadFromFile() throws FileNotFoundException, IOException{
     
        Path path = Paths.get(filePath);
       
        if(Files.size(path)>= Integer.MAX_VALUE){
           throw new IOException("MAX size of text file reached!");
        }
        List<String> stringList = Files.readAllLines(path);
        StringBuilder sb = new StringBuilder();
        for (String line : stringList) {
            sb.append(line).append("\n");
        }
        
        return sb.toString();
    }
    
    public List<String> ReadFromFileAsStrList() throws FileNotFoundException, IOException{
     
        Path path = Paths.get(filePath);
        return Files.readAllLines(path);
        
    }
}
