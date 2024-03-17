package HW_Try_4;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server_GUI {

    public static void main(String[] args) {
        new ManagerLogin();
    }
}

class ManagerLogin extends JFrame implements ActionListener, KeyListener {
    // 로그인 창
    JLabel Port_Label ;
    JLabel Port_Warning ;
    JLabel Start_Label;
    Server_ChatGUI Server_chat = null;
    JButton Start;
    JTextField Port_Text ;
    JButton Port_Enter ;

    public ManagerLogin() {

        setSize(500, 600);
        setResizable(false);
        getContentPane().setBackground(new Color(184,   208,   250));

        setTitle("서버 로그인 창");
        setLocationRelativeTo(null); // 프레임 화면을 가운데에 배치
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 닫았을 때 메모리에서 제거되도록 설정합니다.
        //Container c = frame.getContentPane();
        setLayout(null);

        Start_Label = new JLabel(" NUNSONG MESSENGER ",SwingConstants.CENTER);
        Start_Label.setFont(new Font("Microsoft GothicNeo", Font.BOLD,20));
        Start_Label.setBounds(113, 20, 250, 30);
        Start_Label.setOpaque(true);
        Start_Label.setBackground(Color.WHITE);
        add(Start_Label);

        Port_Label  = new JLabel("입력을 허용할 포트 번호를 입력하세요!",SwingConstants.CENTER);
        Port_Label.setBounds(80, 400, 300, 30);
        Port_Label.setForeground(Color.BLUE);
        Port_Label.setFont(new Font("한컴 말랑말랑 Regular",Font.PLAIN, 16));
        add(Port_Label);

        Port_Warning = new JLabel("(단, 포트 번호는 0 ~ 65535까지)",SwingConstants.CENTER) ;
        Port_Warning.setBounds(80, 420, 300, 30);
        Port_Warning.setFont(new Font("한컴 말랑말랑 Regular",Font.PLAIN, 15));
        add(Port_Warning);


        Port_Text = new JTextField("여기에 포트 번호를 입력해주세요 :)",20);
        Port_Text.setForeground(Color.GRAY);
        Port_Text.setFont(new Font("한컴 말랑말랑 Regular",Font.PLAIN, 15));
        Port_Text.setHorizontalAlignment(JTextField.CENTER);
        Port_Text.setBounds(80, 450, 300, 30);
        Port_Text.addKeyListener(this);

        Port_Text.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (Port_Text.getText().equals("여기에 포트 번호를 입력해주세요 :)")) {
                    Port_Text.setText("");
                    Port_Text.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (Port_Text.getText().isEmpty()) {
                    Port_Text.setForeground(Color.GRAY);
                    Port_Text.setText("여기에 포트 번호를 입력해주세요 :)");
                }
            }
        });

        add(Port_Text);

        Port_Enter = new JButton("접속!");
        Port_Enter.setFont(new Font("한컴 말랑말랑 Regular",Font.PLAIN, 15));
        Port_Enter.addActionListener(this);
        Port_Enter.setForeground(Color.BLACK);
        Port_Enter.setBounds(185, 500, 100, 30);
        Port_Enter.setBackground(new Color(250,226,184));
        add(Port_Enter);

        ImageIcon img = new ImageIcon("C:\\3학년 2학기\\NetWork_JAVA_CODE\\Network\\src\\HW_Try_4\\one.jpg");

        Start = new JButton(img);
        Start.setBounds(43, 80, 400, 300);
        Start.setBackground(Color.white);

        add(Start);
        setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {
        // "접속!" 버튼을 누르면 작동이 됩니다.
        try {
            int Port = Integer.parseInt(Port_Text.getText().trim());
            if (e.getSource() == Port_Enter) {
                Server_chat = new Server_ChatGUI(Port);
                setVisible(false);

            }
        } catch (Exception a) {
            JOptionPane.showMessageDialog(null, "올바르지 않은 입력입니다!");
        }
    }

    public void keyPressed(KeyEvent e) {
        // 텍스트필드에 값을 입력한 후 엔터키를 누르면 작동이 됩니다.
        try {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                int Port = Integer.parseInt(Port_Text.getText().trim());
                Server_chat = new Server_ChatGUI(Port);
                setVisible(false);

            }
        } catch (Exception a) {
            JOptionPane.showMessageDialog(null, "올바르지 않은 입력입니다!");
        }

    }

    public void keyTyped(KeyEvent e) { // 불필요
    }

    public void keyReleased(KeyEvent e) { // 불필요
    }

}

class Server_ChatGUI extends JFrame implements ActionListener, KeyListener {
    // 서버용 채팅창
    JPanel ServerGUI_Panel = new JPanel();
    JLabel ServerLabel = new JLabel("Main Server");
    JLabel UserLabel = new JLabel("유저 목록");

    JTextField Chat = new JTextField(45);
    JButton Enter = new JButton(">");
    TextArea ServerChatList = new TextArea(30, 50);
    TextArea UserList = new TextArea(30, 15);
    Server_Back SB = new Server_Back();


    public Server_ChatGUI(int Port) {
        setTitle("메인 서버");
        setVisible(true);
        setLocationRelativeTo(null);
        setSize(550,520);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 닫았을 때 메모리에서 제거되도록 설정합니다.

        ServerChatList.setEditable(false);
        UserList.setEditable(false);
        Chat.addKeyListener(this);
        Enter.addActionListener(this);

        ServerChatList.setBounds(10, 20,380, 380);
        UserLabel.setBounds(400,10,90,30);
        UserLabel.setForeground(Color.WHITE);
        UserLabel.setFont(new Font("한컴 말랑말랑 Regular",Font.PLAIN, 17));

        UserList.setBounds(400,40,120,360);
        ServerLabel.setBounds(10,430,70,30);
        ServerLabel.setForeground(Color.BLACK);
        ServerLabel.setFont(new Font("한컴 말랑말랑 Regular",Font.PLAIN, 12));

        Chat.setBounds(85,430,330,30);
        Enter.setBounds(415, 430, 50, 30);


        ServerChatList.setBackground(Color.WHITE);
        UserList.setBackground(Color.WHITE);
        ServerGUI_Panel.setBackground(new Color(184,   208,   250));

        ServerGUI_Panel.setLayout(null);

        ServerGUI_Panel.add(ServerLabel);
        ServerGUI_Panel.add(ServerChatList);
        ServerGUI_Panel.add(UserLabel);
        ServerGUI_Panel.add(UserList);
        ServerGUI_Panel.add(Chat);
        ServerGUI_Panel.add(Enter);
        add(ServerGUI_Panel);

        UserList.append("서버 관리자\n"); // 실행과 동시에 서버주인(Admin)을 유저목록에 추가하도록 합니다.
        SB.setGUI(this);
        SB.Start_Server(Port);
        SB.start(); // 서버 채팅창이 켜짐과 동시에 서버소켓도 함께 켜집니다.
    }

    public void actionPerformed(ActionEvent e) { // 전송 버튼을 누르고, 입력값이 1이상일때만 전송되도록 합니다.
        String Message = Chat.getText().trim();
        if (e.getSource() == Enter && Message.length() > 0) {
            AppendMessage("[서버 관리자] >> " + Message + "\n");
            SB.Transmitall("[서버 관리자] >> " + Message + "\n");
            Chat.setText(null); // 채팅창 입력값을 초기화 시켜줍니다.
        }
    }

    public void keyPressed(KeyEvent e) { // 키보드 엔터키를 누르고, 입력값이 1이상일때만 전송되도록 합니다.
        String Message = Chat.getText().trim();
        if (e.getKeyCode() == KeyEvent.VK_ENTER && Message.length() > 0) {
            AppendMessage("[서버 관리자] >> " + Message + "\n");
            SB.Transmitall("[서버 관리자] >> " + Message + "\n");
            Chat.setText(null); // 채팅창 입력값을 초기화 시켜줍니다.
        }
    }

    public void AppendMessage(String Message) {
        ServerChatList.append(Message);
    }

    public void AppendUserList(ArrayList NickName) {
        String name;
        for (int i = 0; i < NickName.size(); i++) {
            name = (String) NickName.get(i);
            UserList.append(name + "\n");
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
}