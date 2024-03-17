package HW_Try_4;

import java.io.*;
import java.net.*;
import java.util.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client_Back extends Thread {
    private String NickName, IPAddress;
    private int Port;
    private Socket socket;
    private String Message;
    private DataInputStream in;
    private DataOutputStream out;
    private Client_ChatGUI chatgui;
    ArrayList<String> NickNameList = new ArrayList<String>(); // 유저목록을 저장합니다.

    // 프로토콜 문자열 분리
    StringTokenizer stringTokenizer;

    public void getUserInfo(String NickName, String IPAddress, int Port) {
        // Client_GUI로부터 닉네임, 아이피, 포트 값을 받아옵니다.
        this.NickName = NickName;
        this.IPAddress = IPAddress;
        this.Port = Port;
    }

    public void setGui(Client_ChatGUI chatgui) {
        // 실행했던 Client_GUI 그 자체의 정보를 들고옵니다.
        this.chatgui = chatgui;
    }

    public void run() {
        // 서버 접속을 실행합니다.
        try {
            socket = new Socket(IPAddress, Port);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());

            out.writeUTF(NickName);
            while (in != null) {
                // 임의의 식별자를 받아 닉네임 혹은 일반 메세지인지 등을 구분시킵니다.
                Message = in.readUTF();
                if (Message.contains("+++닉네임의시작+++")) {
                    // +++닉네임의시작+++이라는 수식어가 붙어있을 경우엔 닉네임으로 간주합니다.
                    chatgui.UserList.setText(null);
                    NickNameList.add(Message.substring(12));
                    chatgui.AppendUserList(NickNameList);
                } else if (Message.contains("님이 입장하셨습니다.")) {
                    // ~~ 님이 입장하셨습니다. 라는 식별자를 받으면 기존의 닉네임 리스트 초기화 후 새로 입력시킵니다.
                    NickNameList.clear();
                    chatgui.UserList.setText(null);
                    chatgui.AppendMessage(Message);
                } else if (Message.contains("님이 퇴장하셨습니다.")) {
                    // ~~ 님이 퇴장하셨습니다. 라는 식별자를 받으면 기존의 닉네임 리스트 초기화 후 새로 입력시킵니다.
                    NickNameList.clear();
                    chatgui.UserList.setText(null);
                    chatgui.AppendMessage(Message);
                } else if (Message.contains("@")) {
                    stringTokenizer = new StringTokenizer(Message, "@");

                    String whisperFrom = stringTokenizer.nextToken();
                    String sended = stringTokenizer.nextToken();

                    stringTokenizer = new StringTokenizer(sended, "/");

                    String whisperTo = stringTokenizer.nextToken();
                    String content = stringTokenizer.nextToken();
                    System.out.println(whisperFrom);

                    if(whisperTo.equals(NickName)||whisperFrom.equals(NickName+" : ")) {
                        chatgui.Whisper(whisperFrom+content);
                    }

                }else {
                    // 위 모든 값이 아닐 시엔 일반 메세지로 간주합니다.
                    chatgui.AppendMessage(Message);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void Transmit(String Message) {
        // 입력받은 값을 서버로 전송(out) 해줍니다.
        try {
            out.writeUTF(Message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}