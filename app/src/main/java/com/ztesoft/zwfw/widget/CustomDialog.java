package com.ztesoft.zwfw.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

import com.ztesoft.zwfw.R;

/**
 * Created by BaoChengchen on 2017/8/7.
 */

public class CustomDialog extends Dialog{

    TextView dialogText;

    public CustomDialog(Context context, int themeResId) {
        super(context, themeResId);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_progress_loading);
        getWindow().setGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(false);
        dialogText = (TextView) findViewById(R.id.dialog_text);
    }

    public void setContent(String text){
        if(dialogText != null){
            dialogText.setText(text);
        }
    }
}
