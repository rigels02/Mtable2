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



/**
 *
 * @author Developer
 */
public class TcpServerSenderTryManual {

    public static List<MTable> makeTables(int v){
        List<MTable> mtables = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            MTable table = new MTable();
            table.setId(i);
            table.setName("Table_"+i);
            table.setModTime(new Date());
            if(i != v){
            table.getData().add(new TableData(new Date(117,1,10), "Table_" + i + "_Cat1", 10.0, "Note_1"));
            table.getData().add(new TableData(new Date(117,2,20), "Table_" + i + "_Cat2", 20.0, "Note_2"));
            table.getData().add(new TableData(new Date(117,3,21), "Table_" + i + "_Cat3", 30.0, "Note_3"));
            }
            mtables.add(table);
        }
        return mtables;
}
    
    
  
    public static void main(String[] args) throws UnknownHostException {
        
        List<MTable> tables = makeTables(-1);
                
        TcpServer server = new TcpServer(63219, Status.SENDER);
        server.setMTables(tables);
        InetAddress addr = server.getServerHostAddress();
        String saddr = server.getServerHostAddressAsString();
        int port = TcpServer.getRandomPortNumber();

        server.run();
        System.out.println("Status: " + server.getStatus());
        if(server.getStatus()==Status.ERROR){
             System.out.println("Error: "+server.getError());  
        }
        //System.out.println("Received goods: " + server.getGoods());
        try {
            //Force to close from external source
            server.getServerSocket().close();
        } catch (IOException ex) {
            Logger.getLogger(TcpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
