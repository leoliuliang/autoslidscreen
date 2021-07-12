package com.mxkj.autoslidscreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kuaiyou.open.AdManager;
import com.kuaiyou.open.InitSDKManager;
import com.kuaiyou.open.SpreadManager;
import com.kuaiyou.open.interfaces.AdViewSpreadListener;
import com.mxkj.autoslidscreen.config.AdViewConfig;

import java.util.List;

public class SplashActivity extends BaseActivity implements AdViewSpreadListener {
    Dialog securityDialog;
    SharedPreferences.Editor editor;
    private SpreadManager spreadManager = null;
    public String[] permissions = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //下载类广告默认弹出二次确认框，如需关闭提示请设置如下；设置后对全部广告生效
//        InitSDKManager.setDownloadNotificationEnable(false);
//        //在调用SDK之前，如果您的App的targetSDKVersion >= 23，那么一定要把"READ_PHONE_STATE"、"WRITE_EXTERNAL_STORAGE"这几个权限申请到
//        permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE};
//
//        if (Build.VERSION.SDK_INT >= 23) {
//            requestRunTimePermission(permissions, new PermissionListener() {
//
//                @Override
//                public void onGranted(List<String> grantedPermission) {
//                    this.onGranted();
//                }
//
//                @Override
//                public void onGranted() {
//                    requestSpreadAd();
//                }
//
//                @Override
//                public void onDenied(List<String> deniedPermission) {
//                    Toast.makeText(SplashActivity.this, deniedPermission.get(0) + "权限被拒绝了", Toast.LENGTH_SHORT).show();
//                }
//            });
//        } else {
//            requestSpreadAd();
//        }

        SharedPreferences sharedPreferences=this.getSharedPreferences("share",MODE_PRIVATE);
        boolean isFirstRun=sharedPreferences.getBoolean("isFirstRun", true);
        editor=sharedPreferences.edit();
        if(isFirstRun){
//            Toast.makeText(SplashActivity.this, "第一次运行", Toast.LENGTH_SHORT).show();
            showSecurityDialog();

        }else{
//            Toast.makeText(SplashActivity.this, "不是第一次运行", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    finish();
                }
            },1500);
        }


    }

    private void requestSpreadAd() {
        spreadManager = AdManager.createSpreadAd();
        spreadManager.loadSpreadAd(this, AdViewConfig.APPID, AdViewConfig.SPREADPOSID,
                (RelativeLayout) findViewById(R.id.layoutSplash));
        spreadManager.setBackgroundColor(Color.WHITE);
        spreadManager.setSpreadNotifyType(AdManager.NOTIFY_COUNTER_NUM);
        spreadManager.setSpreadListener(this);
    }

    private void showSecurityDialog() {
        //TODO 显示提醒对话框
        securityDialog = new Dialog(this, R.style.DialogTheme);
        securityDialog.setCancelable(false);//返回键也会屏蔽
        securityDialog.setCanceledOnTouchOutside(false);
        View view = View.inflate(this, R.layout.dialog_activity_sercurity, null);
        TextView tv_msg = view.findViewById(R.id.sercurity_tv_msgnotice);
        TextView tv_cancel = view.findViewById(R.id.sercurity_tv_cancel);
        TextView tv_positive = view.findViewById(R.id.sercurity_tv_positive);
        SpannableStringBuilder spannable = new SpannableStringBuilder(tv_msg.getText());
//        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#C89C3C")), 50, 56, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_msg.setMovementMethod(LinkMovementMethod.getInstance());
        spannable.setSpan(new TextClick(), 50, 56, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_msg.setText(spannable);
        tv_msg.setHighlightColor(Color.parseColor("#00ffffff"));

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putBoolean("isFirstRun", true);
                editor.commit();
                finish();
                securityDialog.dismiss();
            }
        });
        tv_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putBoolean("isFirstRun", false);
                editor.commit();
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                finish();
                securityDialog.dismiss();
                //TODO 进入主界面
            }
        });

        securityDialog.setContentView(view);
        securityDialog.show();
    }


    private class TextClick extends ClickableSpan {
        @Override
        public void onClick(View widget) { //在此处理点击事件
            startActivity(new Intent(SplashActivity.this,PrivacyPolicyActiviy.class));
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            ds.setColor(Color.parseColor("#C89C3C"));
        }
    }
    @Override
    public void onAdClicked() {
        Log.i("AdViewDemo", "onAdClicked");
    }

    @Override
    public void onAdClosed() {
        Log.i("AdViewDemo", "onAdClosedAd");
        jump();
    }

    @Override
    public void onAdClosedByUser() {
        Log.i("AdViewDemo", "onAdClosedByUser");
        jump();
    }

    @Override
    public void onRenderSuccess() {

    }

    @Override
    public void onAdDisplayed() {
        Log.i("AdViewDemo", "onAdDisplayed");
    }

    @Override
    public void onAdFailedReceived(String arg1) {
        Log.i("AdViewDemo", "onAdRecieveFailed");
        jump();
    }

    @Override
    public void onAdReceived() {
        Log.i("AdViewDemo", "onAdRecieved");
    }

    @Override
    public void onAdSpreadPrepareClosed() {
        Log.i("AdViewDemo", "onAdSpreadPrepareClosed");
        jump();
    }

    private void jump() {
//        Intent intent = new Intent();
//        intent.setClass(this, MainActivity.class);
//        startActivity(intent);
//        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != spreadManager)
            spreadManager.destroy();
    }
}