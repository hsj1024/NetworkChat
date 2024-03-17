package Network_Practice;

import java.net.*;
import java.io.*;
import java.util.concurrent.ExecutionException;

public class MyServer2 {
    public static void main(String args[]) throws Exception
    {
        ServerSocket sersock = new ServerSocket(8888);
        System.out.println("Server ready ...........");
        Socket sock = sersock.accept ( );

        OutputStream ostream = sock.getOutputStream();
        BufferedWriter bw1 = new BufferedWriter(new OutputStreamWriter(ostream));
        String s2 = "Thanks client for your calling on " + new java.util.Date();
        bw1.write(s2);

        bw1.close();
        ostream.close();
        sock.close();
        sersock.close();
    }
}
