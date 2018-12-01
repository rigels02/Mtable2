package mtable2.tcp.v2;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        System.out.println("Received Tables: " + server.getMtables());
        try {
            //Force to close from external source
            server.getServerSocket().close();
        } catch (IOException ex) {
            Logger.getLogger(TcpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
