package mtable2.tcp.v1;


import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mtable2.model.MTable;
import mtable2.model.TableData;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Developer
 */
public class TcpServerReceiverTryManual {

    
    
    public static void main(String[] args) throws UnknownHostException {

        TcpServer server = new TcpServer(63219, Status.RECEIVER);
        InetAddress addr = server.getServerHostAddress();
        String saddr = server.getServerHostAddressAsString();
        int port = TcpServer.getRandomPortNumber();

        server.run();
        System.out.println("Status: " + server.getStatus());
        if(server.getStatus()==Status.ERROR){
             System.out.println("Error: "+server.getError());  
        }
        if(server.getStatus()==Status.COMPLETED){
        System.out.println("Received Tables: " + server.getMTables());
        //Check received data
            List<MTable> tables1 = server.getMTables();
            List<MTable> tables0 = TcpClientSenderTryManual.makeTables(-1);
            
            for (int i=0; i<tables0.size();i++) {
                assertEquals(tables0.get(i).getName(), tables1.get(i).getName());
                assertEquals(tables0.get(i).isSelected(), tables1.get(i).isSelected());
                List<TableData> dd = tables0.get(i).getData();
                List<TableData> dd1 = tables1.get(i).getData();
                assertEquals(dd.toString(), dd1.toString());
                
            }
        }
        try {
            //Force to close from external source
            server.getServerSocket().close();
        } catch (IOException ex) {
            Logger.getLogger(TcpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
