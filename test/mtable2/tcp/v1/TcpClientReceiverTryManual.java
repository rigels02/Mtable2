package mtable2.tcp.v1;


import java.io.IOException;
import java.util.ArrayList;
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
public class TcpClientReceiverTryManual {

    public static void main(String[] args) {
        List<MTable> data = new ArrayList<>();
        

        //TcpClient client = new TcpClient("192.168.2.104", 63219, Status.RECEIVER);
        TcpClient client = new TcpClient("localhost", 63219, Status.RECEIVER);
        // TcpClient client = new TcpClient("localhost", 65414);
       // client.setGoods(data);

        client.run();
        System.out.println("Client Status: " + client.getStatus());
        if (client.getStatus() == Status.ERROR) {
            System.out.println("Error: " + client.getError());

        }
        if(client.getStatus() == Status.COMPLETED){
        System.out.println("Received Tables: " + client.getMTables());
            //Check received data
            List<MTable> tables1 = client.getMTables();
            List<MTable> tables0 = TcpServerSenderTryManual.makeTables(-1);
            
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
            client.getSocket().close();
        } catch (IOException ex) {
            Logger.getLogger(TcpClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Client stopped....");
    }
}
