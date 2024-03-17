package Network_Practice;

import java.net.Socket;
import java.io.OutputStream;
import java.io.DataOutputStream;

public class MyClient {
    public static void main(String args[]) throws Exception
    {
        Socket sock = new Socket("192.168.0.21", 8888);
        String message1 = "Accept Best Wishes, My Server";

        OutputStream ostream = sock.getOutputStream();
        DataOutputStream dos = new DataOutputStream(ostream);
        dos.writeBytes(message1);
        dos.close();
        ostream.close();
        sock.close();
    }
}
