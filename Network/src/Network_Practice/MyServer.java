package Network_Practice;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStream;
import java.io.DataInputStream;

public class MyServer
{
    public static void main(String args[]) throws Exception
    {
        ServerSocket sersock = new ServerSocket(8888);
        System.out.println("server is ready");

        Socket sock = sersock.accept();

        InputStream istream = sock.getInputStream();
        DataInputStream dstream = new DataInputStream(istream);

        String message2 = dstream.readLine();
        System.out.println(message2);
        dstream .close();
        istream.close();
        sock.close();
        sersock.close();
    }

}