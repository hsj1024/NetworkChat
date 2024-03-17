package HW_Try_4;

import java.net.*;
import java.io.*;
import java.util.*;

public class Server_Back extends Thread {
    Vector<ReceiveInfo> ClientList = new Vector<ReceiveInfo>(); // 클라이언트의 쓰레드를 저장해줍니다.
    ArrayList<String> NickNameList = new ArrayList<String>(); // 클라이언트의 닉네임을 저장해줍니다.
    ServerSocket serversocket;
    Socket socket;
    private Server_ChatGUI serverchatgui;

    public void setGUI(Server_ChatGUI serverchatgui) {
        this.serverchatgui = serverchatgui;
    }

    public void Start_Server(int Port) {
        try {
            Collections.synchronizedList(ClientList); // 교통정리를 해준다.( clientList를 네트워크 처리해주는것 )
            serversocket = new ServerSocket(Port); // 서버에 입력된 특정 Port만 접속을 허가하기 위해 사용했습니다.
            System.out.println("현재 아이피와 포트넘버는 [" + InetAddress.getLocalHost() + "], [" + Port + "] 입니다.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void run() {
        try {
            NickNameList.add("Admin"); // 유저목록의 첫 번째 서버(Admin)을 추가합니다.
            while (true) {
                System.out.println("새 접속을 대기합니다...");
                socket = serversocket.accept(); // 포트 번호와 일치한 클라이언트의 소켓을 받습니다.
                System.out.println("[" + socket.getInetAddress() + "]에서 접속하셨습니다.");
                ReceiveInfo receive = new ReceiveInfo(socket);
                ClientList.add(receive);
                receive.start();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void Transmitall(String Message) {
        // 모든 클라이언트들에게 메세지를 전송해줍니다.
        for (int i = 0; i < ClientList.size(); i++) {
            try {
                ReceiveInfo ri = ClientList.elementAt(i);
                ri.Transmit(Message);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void removeClient(ReceiveInfo Client, String NickName) {
        // 퇴장한 유저 발생시 목록에서 삭제하는 역할을 합니다.
        ClientList.removeElement(Client);
        NickNameList.remove(NickName);
        System.out.println(NickName + "을 삭제 완료했습니다.");
        serverchatgui.UserList.setText(null);
        serverchatgui.AppendUserList(NickNameList);
    }

    class ReceiveInfo extends Thread {
        // 각 네트워크(클라이언트)로부터 소켓을 받아 다시 내보내는 역할을 합니다.
        private DataInputStream in;
        private DataOutputStream out;
        String NickName;
        String Message;

        public ReceiveInfo(Socket socket) {
            try {
                out = new DataOutputStream(socket.getOutputStream()); // Output
                in = new DataInputStream(socket.getInputStream()); // Input
                NickName = in.readUTF();
                NickNameList.add(NickName);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        public void run() {
            try {
                // 새로운 유저 발생시 유저목록을 초기화 합니다.
                // 후에 새롭게 유저목록을 입력해줍니다.
                // 또한, 새로운 유저가 입장했음을 모든 클라이언트에게 전송합니다.
                serverchatgui.UserList.setText(null);
                serverchatgui.AppendUserList(NickNameList);
                Transmitall(NickName + "님이 입장하셨습니다.\n");
                for (int i = 0; i < NickNameList.size(); i++) {
                    // +++닉네임의시작+++은 해당 값이 닉네임임을 알게해주는 식별자이며
                    // 실전에서는 더욱 암호화된 값을 적용시켜 혼동 발생을 막아줍니다.
                    Transmitall("+++닉네임의시작+++" + NickNameList.get(i));
                }
                serverchatgui.AppendMessage(NickName + "님이 입장하셨습니다.\n");
                while (true) {
                    Message = in.readUTF();
                    serverchatgui.AppendMessage(Message);
                    Transmitall(Message);
                }
            } catch (Exception e) {
                // 유저가 접속을 종류하면 여기서 오류가 발생합니다.
                // 따라서 발생한 값을 다시 모든 클라이언트 들에게 전송시켜줍니다.
                System.out.println(NickName + "님이 퇴장하셨습니다.");
                removeClient(this, NickName);
                Transmitall(NickName + "님이 퇴장하셨습니다.\n");
                for (int i = 0; i < NickNameList.size(); i++) {
                    Transmitall("+++닉네임의시작+++" + NickNameList.get(i));
                }
                serverchatgui.AppendMessage(NickName + "님이 퇴장하셨습니다.\n");
            }
        }

        public void Transmit(String Message) {
            // 전달받은 값(Message)를 각 클라이언트의 쓰레드에 맞춰 전송합니다.
            try {
                out.writeUTF(Message);
                out.flush();
            } catch (Exception e) {
                e.getStackTrace();
            }

        }
    }
}