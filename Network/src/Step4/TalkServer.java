package Step4;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TalkServer {
    public static void main(String[] args) throws Exception
    {
        ServerSocket sersock = new ServerSocket(9995);
        System.out.println("Talk Server ready for chatting");
        Socket sock = sersock.accept();

        BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));

        OutputStream ostream = sock.getOutputStream();
        PrintWriter pwrite = new PrintWriter(ostream, true);

        InputStream istream = sock.getInputStream();
        BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));

        String receiveMessage, sendMessage;
        System.out.println("Server>> Message Waiting");
        while (true)
        {
            if ((receiveMessage = receiveRead.readLine()) != null)
            {
                System.out.println("Server>> Received Message from Client: " + receiveMessage);
            }
            System.out.print("Server>> Enter Sending Message: ");
            sendMessage = keyRead.readLine();
            pwrite.println(sendMessage);
            System.out.println("-> Over: Server Waiting");
            pwrite.flush();
        }
    }
}