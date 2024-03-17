package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EnterActivity extends AppCompatActivity {
    EditText NickName;
    EditText IPAddress;
    EditText Port;
    Button enterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        enterButton = (Button)findViewById(R.id.enterButton);
        NickName = (EditText)findViewById(R.id.NickName);
        IPAddress = (EditText)findViewById(R.id.IPAddress);
        Port = (EditText)findViewById(R.id.Port);

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                String username = NickName.getText().toString();
                String ip = IPAddress.getText().toString();
                String port = Port.getText().toString();

                intent.putExtra("username",username);
                intent.putExtra("ip",ip);
                intent.putExtra("port",port);
                startActivity(intent);
            }
        });

    }

}