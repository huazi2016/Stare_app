package com.blueocean.stare_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(5000);
                    SplashActivity.this.startActivity(new Intent(SplashActivity.this,MainActivity.class));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    long oldBackTime = 0;
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - oldBackTime > 2000) {
            oldBackTime = System.currentTimeMillis();
            Toast.makeText(this, "按二次退出应用", Toast.LENGTH_SHORT).show();
        } else {
            finish();
        }
    }

    public void ClosdActivity(){
        finish();
    }
}
