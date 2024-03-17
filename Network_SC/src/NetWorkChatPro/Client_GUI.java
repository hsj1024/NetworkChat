package HW_Try_4;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Client_GUI {
    public static void main(String[] args) {
        LoginGUI LG = new LoginGUI();
    }
}

class LoginGUI extends JFrame implements ActionListener {
    // 유저의 로그인 창
    private JPanel Login_GUIPanel = new JPanel();
    private JTextField NickName_Text = new JTextField(20);
    private JTextField Port_Text = new JTextField("####", 20);
    private JTextField IPAddress_Text = new JTextField("###.###.###.###", 20);
    private JLabel NickName_Label = new JLabel("유저 입력");
    private JLabel Port_Label = new JLabel("포트 입력");
    private JLabel IPAddress_Label = new JLabel("주소 입력");
    private JButton Login_GUI_Button = new JButton("접속!");

    public LoginGUI() {
        setTitle("로그인 화면");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(300, 170);
        setResizable(false);
        setVisible(true);

        Port_Text.setForeground(Color.GRAY);
        Port_Text.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
                if (Port_Text.getText().equals("####")) {
                    Port_Text.setText("");
                    Port_Text.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (Port_Text.getText().isEmpty()) {
                    Port_Text.setForeground(Color.GRAY);
                    Port_Text.setText("####");
                }
            }
        });
        IPAddress_Text.setForeground(Color.GRAY);
        IPAddress_Text.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
                if (IPAddress_Text.getText().equals("###.###.###.###")) {
                    IPAddress_Text.setText("");
                    IPAddress_Text.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (IPAddress_Text.getText().isEmpty()) {
                    IPAddress_Text.setForeground(Color.GRAY);
                    IPAddress_Text.setText("###.###.###.###");
                }
            }
        });

        Login_GUI_Button.setPreferredSize(new Dimension(260, 40));
        Login_GUI_Button.addActionListener(this);
        Login_GUIPanel.add(NickName_Label);
        Login_GUIPanel.add(NickName_Text);
        Login_GUIPanel.add(Port_Label);
        Login_GUIPanel.add(Port_Text);
        Login_GUIPanel.add(IPAddress_Label);
        Login_GUIPanel.add(IPAddress_Text);
        Login_GUIPanel.add(Login_GUI_Button);
        add(Login_GUIPanel);
    }

    public void actionPerformed(ActionEvent e) {
        // 닉네임, 주소, 포트값을 버튼을 통해 입력받습니다.
        try {
            if (e.getSource() == Login_GUI_Button) {
                String NickName = NickName_Text.getText().trim();
                String IPAddress = IPAddress_Text.getText().trim();
                int Port = Integer.parseInt(Port_Text.getText().trim());
                SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        new Client_ChatGUI(NickName, IPAddress, Port);
                    }
                });
                setVisible(false);
            }
        } catch (Exception a) {
            // 만약 올바르지 않는 값이 입력되면 팝업창을 띄워줍니다.
            JOptionPane.showMessageDialog(null, "올바르지 않은 입력입니다!");
        }
    }
}

class Client_ChatGUI extends JFrame implements ActionListener, KeyListener {
    //클라이언트용 채팅창
    String NickName;
    Client_Back CB = new Client_Back();

    JTextPane ChatList = new JTextPane();
    JTextArea UserList = new JTextArea();

    JScrollPane sp_chat= new JScrollPane(ChatList);
    JScrollPane sp_user = new JScrollPane(UserList);

    JTextField Chat = new JTextField(15);

    JLabel user_list = new JLabel("유저 목록");
    JLabel la_msg;

    JLabel tip = new JLabel("<html><body>[Tip]<br>'@닉네임/'을 입력한 후 채팅을 보내면 해당 유저에게만 채팅을 보내는 '귓속말'기능을 사용할 수 있어요!</body></html>");

    JButton bt_exit = new JButton("나가기");
    JButton Enter = new JButton(">");

    JPanel ClientGUIPanel = new JPanel();

    StyledDocument doc = ChatList.getStyledDocument();

    public Client_ChatGUI(String NickName, String IPAddress, int Port) {
        this.NickName = NickName;

        setTitle("고객 창");
        la_msg = new JLabel(NickName);

        sp_chat.setBounds(10,20,380,390);
        user_list.setBounds(400,10,90,30);
        user_list.setForeground(Color.WHITE);
        user_list.setFont(new Font("한컴 말랑말랑 Regular",Font.PLAIN, 13));
        sp_user.setBounds(400,35,120,230);

        sp_user.setFont(new Font("한컴 말랑말랑 Regular",Font.PLAIN, 13));

        tip.setBounds(400,280,120,130);


        tip.setFont(new Font("한컴 말랑말랑 Regular",Font.PLAIN, 13));
        bt_exit.setBounds(400,430,120,30);
        bt_exit.setFont(new Font("한컴 말랑말랑 Regular",Font.PLAIN, 14));
        la_msg.setBounds(10,430,60,30);
        Chat.setBounds(70,430,270,30);
        Enter.setBounds(340, 430, 50, 30);

        ChatList.setEditable(false);
        UserList.setEditable(false);
        Chat.addKeyListener(this);
        Enter.addActionListener(this);
        bt_exit.addActionListener(this);

        ClientGUIPanel.setBackground(Color.GRAY);
        ClientGUIPanel.setLayout(null);

        ClientGUIPanel.add(sp_chat);
        ClientGUIPanel.add(user_list);
        ClientGUIPanel.add(sp_user);
        ClientGUIPanel.add(tip);

        ClientGUIPanel.add(la_msg);
        ClientGUIPanel.add(Chat);
        ClientGUIPanel.add(Enter);

        ClientGUIPanel.add(bt_exit);

        add(ClientGUIPanel);
        setVisible(true);
        setSize(550,520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CB.setGui(this);
        CB.getUserInfo(NickName, IPAddress, Port);
        CB.start(); // 채팅창이 켜짐과 동시에 접속을 실행해줍니다.
    }

    public void actionPerformed(ActionEvent e) {
        // 전송 버튼을 누르고, 입력값이 1이상일때만 전송되도록 합니다.
        String Message = Chat.getText().trim();
        if (e.getSource() == Enter && Message.length() > 0) {
            CB.Transmit("[" + NickName + "] >> " + Message + "\n");
            Chat.setText(null);
        }

        if (e.getSource() == bt_exit) {
            System.exit(0);
        }
    }

    public void keyPressed(KeyEvent e) {
        // 키보드 엔터키를 누르고, 입력값이 1이상일때만 전송되도록 합니다.
        String Message = Chat.getText().trim();
        if (e.getKeyCode() == KeyEvent.VK_ENTER && Message.length() > 0) {
            CB.Transmit("[" + NickName + "] >> " + Message + "\n");
            Chat.setText(null);
        }
    }

    public void AppendMessage(String Message) {
        try {
            doc.insertString(doc.getLength(), Message, null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void Whisper(String Message) {
        try {
            SimpleAttributeSet keyWord = new SimpleAttributeSet();
            StyleConstants.setForeground(keyWord, Color.GREEN);
            doc.insertString(doc.getLength(), Message, keyWord);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void AppendUserList(ArrayList NickName) {
        // 유저목록을 유저리스트에 띄워줍니다.
        String name;
        UserList.setText(null);
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