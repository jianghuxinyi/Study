package com.linanqing.usbsocket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private ImageView ivReceiveImg;
    private TextView tvImgSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivReceiveImg = findViewById(R.id.ivReceiveImg);
        tvImgSize = findViewById(R.id.tvImgSize);
        Thread thread = new Thread(new SocketThread());
        thread.run();
    }

    private Socket socket;
    public class SocketThread implements Runnable{

        @Override
        public void run() {
            socket = new Socket();

        }
    }
}