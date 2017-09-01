package com.ztesoft.zwfw.moudle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ztesoft.zwfw.Config;
import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.base.BaseActivity;
import com.ztesoft.zwfw.utils.APPPreferenceManager;

public class SplashActivity extends BaseActivity {

    Handler handler;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;
        handler = new Handler();
        handler.postDelayed(runnable,3000);
    }


    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(APPPreferenceManager.getInstance().getBool(mContext, Config.IS_LOGIN)){
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
            }else{
                startActivity(new Intent(SplashActivity.this,LoginActivity.class));
            }

            finish();
        }
    };
}
