package com.ztesoft.zwfw.user;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.base.BaseActivity;

public class MeInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_info);
        TextView csTitile = (TextView) findViewById(R.id.cs_title);
        csTitile.setText(getString(R.string.edit_meInfo));
        findViewById(R.id.cs_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
}
