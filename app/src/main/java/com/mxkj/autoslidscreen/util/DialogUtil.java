package com.mxkj.autoslidscreen.util;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.mxkj.autoslidscreen.MainActivity;
import com.mxkj.autoslidscreen.R;
import com.mxkj.autoslidscreen.SplashActivity;

public class DialogUtil {
    Dialog securityDialog;

    onDialogUtilLinster mOnLinster;
    public interface onDialogUtilLinster{
        public void isOk(boolean cancelOrOk);
    }
    public void setmOnLinster(onDialogUtilLinster onLinster){
        this.mOnLinster = onLinster;
    }

    public void showDialog(Context context,String content) {
        //TODO 显示提醒对话框
        securityDialog = new Dialog(context, R.style.DialogTheme);
        securityDialog.setCancelable(false);//返回键也会屏蔽
        securityDialog.setCanceledOnTouchOutside(false);
        View view = View.inflate(context, R.layout.dialog_util, null);
        TextView tv_msg = view.findViewById(R.id.tv_msgnotice);
        tv_msg.setText(content);
        TextView tv_cancel = view.findViewById(R.id.tv_cancel);
        TextView tv_positive = view.findViewById(R.id.tv_positive);

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                securityDialog.dismiss();
                mOnLinster.isOk(false);
            }
        });
        tv_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnLinster.isOk(true);
                securityDialog.dismiss();
            }
        });

        securityDialog.setContentView(view);
        securityDialog.show();
    }
}
