
package mtable2.tcp.v2;


import java.io.IOException;
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
public class TcpClientSenderTryManual {
    
    public static void main(String[] args) {
		 List<MTable> tables = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            MTable table = new MTable();
            table.getData().add(new TableData(new Date(), "Table_" + i + "_Cat1", 10.0, "Note_1"));
            table.getData().add(new TableData(new Date(), "Table_" + i + "_Cat2", 20.0, "Note_2"));
            table.getData().add(new TableData(new Date(), "Table_" + i + "_Cat3", 30.0, "Note_3"));
            tables.add(table);
        }

		TcpClient client = new TcpClient("192.168.2.104", 63219, Status.SENDER);
		// TcpClient client = new TcpClient("localhost", 63219);
		client.setMtables(tables);
		
		client.run();
		System.out.println("Client Status: "+client.getStatus());
                if(client.getStatus()==Status.ERROR){
             System.out.println("Error: "+client.getError());  
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
