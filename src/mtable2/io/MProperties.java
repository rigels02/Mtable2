
package mtable2.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author raitis
 */
public class MProperties {

    public final static String PROPFILE="mproperties.properties";
    private final static String IP_PROP="IP";
    
    public static MProperties create() {
        return new MProperties();
    }
    
    private final Properties prop;

    private MProperties() {
        prop= new Properties();
    }

    public void createPropertyFile() throws Exception{
      FileOutputStream fos = new FileOutputStream(PROPFILE);
     try{
      prop.store(fos, "# Property file");
     } catch(IOException ex){
       fos.close();
       throw new Exception(ex.getMessage());
     }
      fos.close();
    }
    public void saveIpProperty(String strIp) throws Exception {
        
        FileOutputStream fos = new FileOutputStream(PROPFILE);
        
        prop.setProperty(IP_PROP, strIp);
        try{
            prop.store(fos,null);
        }catch(IOException ex){
          fos.close();
          throw new Exception(ex.getMessage());
        }
        fos.close();
    }

    public String readSavedIpProperty() throws Exception {
        FileInputStream fin=null;
       try{
           fin = new FileInputStream(PROPFILE);
       }catch(FileNotFoundException ex){
           return null;
       }
       try{
           prop.load(fin);
       }catch(IOException ex){
         fin.close();
        throw new Exception(ex.getMessage());
       }
        String Ip = prop.getProperty(IP_PROP);
        fin.close();
        return Ip;
    }
}
