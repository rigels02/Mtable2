package mtable2.tcp.v1;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mtable2.model.MTable;
import mtable2.model.TableData;
import static mtable2.tcp.v1.TcpServer.MAGNUM;
import static mtable2.tcp.v1.TcpServer.MAGSTR;



/**
 * V1 use DataI/O Streams (DataInputStream, DataOutputStream)
 * @author Developer
 */
public class TcpClient {

    private int port;

    // received/sent mtables count
    private long counter;
    private List<MTable> mtables = null;
    // private InetAddress ipAddress;
    private String ipAddress;
    private Status status;
    private String error;
    private Socket socket;

    private DataInputStream din;

    private DataOutputStream dou;

    public TcpClient(String ipAddress, int port, Status status) {
        this.ipAddress = ipAddress;
        this.port = port;
        error = null;
        this.status = status;
        mtables = new ArrayList<>();
    }

    public void setPort(int port) {
        this.port = port;
    }

    public long getCounter() {
        return counter;
    }

    public void setMTables(List<MTable> mtables) {
        this.mtables = mtables;
        counter = mtables.size();
    }

    public List<MTable> getMTables() {
        return mtables;
    }

    public String getError() {
        return error;
    }

    public Status getStatus() {
        return status;
    }

    public Socket getSocket() {
        return socket;
    }

    private void sendError(String err) {

        error = err;
        // TODO call callback method to inform upper level
    }

    private void closeAll() {
        try {
            if (din != null) {
                din.close();
            }
            if (dou != null) {
                dou.close();
            }
            if (socket != null) {
                socket.close();
            }

        } catch (IOException ex) {
            Logger.getLogger(TcpServer.class.getName()).log(Level.SEVERE, null, ex);
            // sendError(ex.getMessage());

        }

    }

    private boolean isHeaderSend() {
        try {
            dou.writeUTF(MAGSTR);
            dou.writeLong(MAGNUM);
            dou.flush();
        } catch (IOException e) {
            String msg = "Handshaking problem! : ";
            Logger.getLogger(TcpClient.class.getName()).log(Level.SEVERE, null, msg + e.getMessage());
            return false;
        }

        return true;
    }

    private boolean isHeaderReceived() throws IOException {
        String magStr1 = din.readUTF();
        long magNum = din.readLong();
        if ((!magStr1.equals(MAGSTR)) || (magNum != MAGNUM)) {
            String msg = "Handshaking problem!";
            Logger.getLogger(TcpServer.class.getName()).log(Level.SEVERE, null,
                    String.format("%s : magStr1= %s, magNum= %d", msg, magStr1, magNum));
            return false;
        }
        return true;
    }

    private void returnError(String msg) {
        error = msg;
        status = Status.ERROR;
        closeAll();

    }

    private void returnCompleted() {
        closeAll();
        error = null;
        if (status == Status.RECEIVER) {
            System.out.println("Client reception completed");
        } else {
            System.out.println("Client sending completed");
        }
        status = Status.COMPLETED;

    }

    private void writeData(DataOutputStream dou) throws IOException {
        if (mtables.size() > 0) {
            Logger.getLogger(TcpClient.class.getName()).log(Level.INFO, "sendig Data");
        }
        dou.writeLong(mtables.size());
        dou.flush();
        for (int i = 0; i < mtables.size(); i++) {
            MTable.putToStream(dou, mtables.get(i));
            dou.flush();
        }

    }

    private boolean acknowledgeToSend(DataInputStream din, DataOutputStream dou) throws IOException {
        // dou.writeUTF("SEND2ME");
        String token = din.readUTF();
        if (!token.equals("SEND2ME")) {
            dou.writeUTF("NOTREADY");
            dou.flush();
            return false;
        }
        dou.writeUTF("READY");
        dou.flush();
        return true;
    }

    private void receiveData(DataInputStream din) throws IOException {
        this.counter = din.readLong();
        if (counter > 0) {
            Logger.getLogger(TcpClient.class.getName()).log(Level.INFO,"receiving Data");
        }
        while (counter > 0) {
            MTable good = MTable.getFromStream(din);
            mtables.add(good);
            counter--;
        }

    }

    private boolean acknowledgeReceive(DataInputStream din, DataOutputStream dou) throws IOException {
        // dou.readUTF("SEND2YOU");

        String token = din.readUTF();
        if (!token.equals("SEND2YOU")) {
            dou.writeUTF("NOTREADY");
            dou.flush();
            return false;
        }
        dou.writeUTF("READY");
        dou.flush();
        return true;
    }

    public void run() {

        try {

            this.socket = new Socket();

            // if timeout is reached and no response is received, it will throw
            // socket exception
            InetSocketAddress sockAddress = new InetSocketAddress(ipAddress, port);

            int timeOut = 0; // infinit 2000; //milisec
            System.out.println(String.format("TcpClient on port %d waiting for connection...", port));
            socket.connect(sockAddress, timeOut);
        } catch (Exception ex) {
            Logger.getLogger(TcpServer.class.getName()).log(Level.SEVERE, null, ex);
            //sendError(ex.getMessage());
            returnError(ex.getMessage());
            return;
        }

        try {

            din = new DataInputStream(socket.getInputStream());
            dou = new DataOutputStream(socket.getOutputStream());

            if ((!isHeaderSend()) || (!isHeaderReceived())) {
                String msg = "Handshaking problem!";
                returnError(msg);
                return;
            }

            // send or read actions
            if (status == Status.RECEIVER) {
                if (acknowledgeReceive(din, dou)) {
                    receiveData(din);
                } else {
                    returnError("The other side is not sender as expected!");
                    return;
                }

            } else if (status == Status.SENDER) {
                if (acknowledgeToSend(din, dou)) {
                    writeData(dou);
                } else {
                    returnError("The other side is not receiver as expected!");
                    return;
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(TcpServer.class.getName()).log(Level.SEVERE, null, ex);
            sendError(ex.getMessage());
            closeAll();
            return;
        }

        returnCompleted();

    }

    public static void main(String[] args) {
        List<MTable> data = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
           MTable table = new MTable();
            table.getData().add(new TableData(new Date(), "Table_"+i+"Cat1", 10.0, "Note_1"));
            table.getData().add(new TableData(new Date(), "Table_"+i+"Cat2", 11.0, "Note_2"));
            table.getData().add(new TableData(new Date(), "Table_"+i+"Cat3", 12.0, "Note_3"));
            data.add(table);
            
        }

        TcpClient client = new TcpClient("192.168.2.102", 63219, Status.SENDER);
        // TcpClient client = new TcpClient("localhost", 63219);
        client.setMTables(data);

        client.run();
        System.out.println("Client Status: " + client.getStatus());
        try {
            //Force to close from external source
            client.getSocket().close();
        } catch (IOException ex) {
            Logger.getLogger(TcpClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Client stopped....");
    }
}
