package com.example.administrator.myapplication1.socket;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.myapplication1.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketActivity extends AppCompatActivity {
    //接收到了消息
    private static final int MSG_RECEIVE = 1;
    //连接服务端
    private static final int MSG_CONNECTED = 2;

    private TextView msgTextView;
    private AutoCompleteTextView input;
    private Button sendBtn;

    private PrintWriter printWriter;
    private Socket mClientSocket;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_RECEIVE:
                    msgTextView.setText(msgTextView.getText() + "\n" + String.valueOf(msg.obj));
                    break;
                case MSG_CONNECTED:
                    sendBtn.setEnabled(true);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        msgTextView = (TextView) findViewById(R.id.textView);
        input = (AutoCompleteTextView) findViewById(R.id.input);
        sendBtn = (Button) findViewById(R.id.button);

        Intent service = new Intent(this, TCPServerService.class);
        startService(service);
        new Thread() {
            @Override
            public void run() {
                connectTCPServer();
            }
        }.start();
    }


    private void connectTCPServer() {
        Socket socket = null;
        while (socket == null) {
            try {
                socket = new Socket("localhost", 8688);
                mClientSocket = socket;
                printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                handler.sendEmptyMessage(MSG_CONNECTED);
                System.out.println("连接成功！");
            } catch (IOException e) {
                e.printStackTrace();
                SystemClock.sleep(1000);
                System.out.println("连接失败！重试中。。。");
            }
        }

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!SocketActivity.this.isFinishing()){//当前activity没有关闭
                String msg = bufferedReader.readLine();
                System.out.println("接收:" + msg);
                if (msg != null) {
                    handler.obtainMessage(MSG_RECEIVE, msg).sendToTarget();
                }
            }
            System.out.println("quit...");
            printWriter.close();
            bufferedReader.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.button:
                final String msg = input.getText().toString();
                if(!TextUtils.isEmpty(msg) && printWriter != null){
                    printWriter.println(msg);
                    input.setText("");
                    msgTextView.setText(msgTextView.getText() + "\n" + String.valueOf(msg));
                }
        }
    }

    @Override
    protected void onDestroy() {
        if (mClientSocket != null) {
            try {
                mClientSocket.shutdownInput();
                mClientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }
}
