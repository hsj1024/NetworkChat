package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String NickName, IPAddress;
    private int Port;
    private Socket socket;
    private String Message;
    private DataInputStream in;
    private DataOutputStream out;

    private Handler handler;

    ArrayList<String> NickNameList = new ArrayList<String>();

    TextView UserList;
    TextView ChatList;

    EditText Chat;
    Button chatbutton;

    @Override
    protected void onStop() {
        super.onStop();
        try {
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UserList = (TextView) findViewById(R.id.client_list);
        ChatList = (TextView) findViewById(R.id.chatView);
        Chat = (EditText) findViewById(R.id.message);

        Intent intent = getIntent();
        NickName = intent.getStringExtra("username");
        IPAddress = intent.getStringExtra("ip");
        Port = Integer.parseInt(intent.getStringExtra("port"));

        chatbutton = (Button) findViewById(R.id.chatbutton);

        handler = new Handler();

        new Thread() {
            public void run() {
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
                            userNull();
                            NickNameList.add(Message.substring(12));
                            AppendUserList(NickNameList);
                        } else if (Message.contains("님이 입장하셨습니다.")) {
                            // ~~ 님이 입장하셨습니다. 라는 식별자를 받으면 기존의 닉네임 리스트 초기화 후 새로 입력시킵니다.
                            NickNameList.clear();
                            userNull();
                            AppendMessage(Message);
                        } else if (Message.contains("님이 퇴장하셨습니다.")) {
                            // ~~ 님이 퇴장하셨습니다. 라는 식별자를 받으면 기존의 닉네임 리스트 초기화 후 새로 입력시킵니다.
                            NickNameList.clear();
                            userNull();
                            AppendMessage(Message);
                        } else {
                            // 위 모든 값이 아닐 시엔 일반 메세지로 간주합니다.
                            AppendMessage(Message);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } }}.start();

        chatbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Message = Chat.getText().toString();
                if (Message.length() > 0) {
                    Transmit(NickName + " : " + Message + "\n");
                    Chat.getText().clear();
                }
            }
        });
    }

    public void Transmit(String Message) {
        // 전달받은 값(Message)를 각 클라이언트의 쓰레드에 맞춰 전송합니다.
        new Thread(){
            public void run(){
                try {
                    out.writeUTF(Message);
                    out.flush();
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        }.start();
    }

    public void AppendMessage(String Message) {
        ChatList.append(Message);
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

    public void userNull(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                UserList.setText(null);
            }
        });
    }
}