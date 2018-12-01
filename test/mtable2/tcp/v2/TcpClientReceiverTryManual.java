package mtable2.tcp.v2;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mtable2.model.MTable;

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
        System.out.println("Received Tables: " + client.getMtables());
        try {
            //Force to close from external source
            client.getSocket().close();
        } catch (IOException ex) {
            Logger.getLogger(TcpClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Client stopped....");
    }
}
