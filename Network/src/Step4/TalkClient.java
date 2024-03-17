package Step4;

import java.net.*;
import java.io.*;
import java.nio.Buffer;

public class TalkClient
{
    public static void main(String[] args) throws Exception
    {
        Socket sock = new Socket("192.168.49.107", 9997);

        // reading from keyboard (keyRead object)
        BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));

        // sending to client (pwrite object)
        OutputStream ostream = sock.getOutputStream();
        PrintWriter pwrite = new PrintWriter(ostream, true);

        // receiving from server (receiveRead object)
        InputStream istream = sock.getInputStream();
        BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));

        System.out.println("Start the chitchat, type and press Enter key");

        String receiveMessage, sendMessage;
        while(true)
        {
            System.out.println("Client>> Enter Sending Message: ");
            sendMessage = keyRead.readLine();   // keyboard reading
            pwrite.println(sendMessage);        // sending to server
            System.out.println("-> Over: Client Waiting");
            pwrite.flush();     // flush the data
            if((receiveMessage = receiveRead.readLine()) != null)       // receive from server
            {
                System.out.println("Client>> Received Message from Server : " + receiveMessage);
            }
        }
    }
}
