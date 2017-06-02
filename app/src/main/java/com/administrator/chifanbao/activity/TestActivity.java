package com.administrator.chifanbao.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.administrator.chifanbao.R;

public class TestActivity extends AppCompatActivity {
    MessageHandler messageHandler;
    EditText editText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        editText = (EditText) findViewById(R.id.editText1);
//        Button button = (Button) findViewById(R.id.button1);
//        button.setOnClickListener((OnClickListener) this);
        Looper looper = Looper.myLooper();
        messageHandler = new MessageHandler(looper);
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //No.1
                //开始写代码，请在下面实现利用Message和messageHandler发送任意数据,实现线程间的通讯。




                //end_code
            }
        }.start();
    }

    public void onClick(View v) {

    }

    class MessageHandler extends Handler {
        public MessageHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
//            editText.setText((String) msg.obj);
            Log.d("hangdler", "handleMessage: msg" + msg.obj);
        }
    }
}
