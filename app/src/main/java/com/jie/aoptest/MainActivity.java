package com.jie.aoptest;

import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jie.aoptest.aop.Async;
import com.jie.aoptest.aop.CheckLogin;
import com.jie.aoptest.aop.CheckPermission;
import com.jie.aoptest.aop.DebugLog;
import com.jie.aoptest.aop.Safe;
import com.jie.aoptest.aop.SingleClick;

public class MainActivity extends AppCompatActivity {
    Button button;
    @DebugLog
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.btn_login);
        button.setOnClickListener(new View.OnClickListener() {
            @SingleClick
            @CheckLogin
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "登录测试", Toast.LENGTH_SHORT).show();
            }
        });
//        async();
//        safe();
        checkPhoneState();
    }

    @DebugLog
    @Async
    private void async() {
        try {
            Thread.sleep(10000);
            Log.d("async"," thread=" + Thread.currentThread().getName());
            Log.d("async", "ui thread=" + Looper.getMainLooper().getThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Safe()
    private void safe() {
        int a = 10 / 0;
    }

    @CheckPermission(declaredPermission="android.permission.READ_PHONE_STATE")
    private void checkPhoneState(){
        Log.d("CheckPermission","Read Phone State succeed");
    }
}
