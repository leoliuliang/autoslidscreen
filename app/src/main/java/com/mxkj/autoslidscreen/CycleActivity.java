package com.mxkj.autoslidscreen;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mxkj.autoslidscreen.config.MyVariable;
import com.mxkj.autoslidscreen.simulatekey.AutoScrollAccessibilityService;
import com.mxkj.autoslidscreen.simulatekey.BaseAccessibilityService;
import com.mxkj.autoslidscreen.simulatekey.FloatingButtonService;
import com.mxkj.autoslidscreen.simulatekey.FloatingImageDisplayService;
import com.mxkj.autoslidscreen.simulatekey.FloatingTextService;
import com.mxkj.autoslidscreen.util.AUtil;
import com.mxkj.autoslidscreen.util.DialogUtil;
import com.mxkj.autoslidscreen.util.IntentUtil;
import com.mxkj.autoslidscreen.util.JsonUtil;
import com.mxkj.autoslidscreen.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CycleActivity extends AppCompatActivity {
    int index=0;
    Handler mHandler;
    JSONObject jsonObject;
    String packagename = null;
    String toastStr = null;
    JSONArray jsonArray;
    int time;
    Handler.Callback hc;
    Intent intent_floatingImage;
    Intent intent_floatingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent_floatingImage = new Intent(CycleActivity.this, FloatingImageDisplayService.class);
        intent_floatingText = new Intent(CycleActivity.this, FloatingButtonService.class);
        setContentView(R.layout.activity_cycle);
        final CheckBox cb1 = findViewById(R.id.cb1);
        final CheckBox cb2 = findViewById(R.id.cb2);
        final CheckBox cb3 = findViewById(R.id.cb3);
        final CheckBox cb4 = findViewById(R.id.cb4);
        final EditText etTime = findViewById(R.id.etTime);
        TextView tvSure = findViewById(R.id.tvSure);
        jsonArray = JsonUtil.getJStr(this);
//        List<String> listPaks = new ArrayList<>();
        final HashSet<String> hashSet = new HashSet<>();
        cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    boolean isInstalled = AUtil.checkAppInstalled(CycleActivity.this,getPackagename(0));
                    if (!isInstalled){
                        ToastUtil.toastShort(CycleActivity.this,"没有安装此应用!");
                        cb1.setChecked(false);
                    }else{
                        hashSet.add(getPackagename(0));
                    }
                }else{
                    hashSet.remove(getPackagename(0));
                }
            }
        });
        cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    boolean isInstalled = AUtil.checkAppInstalled(CycleActivity.this,getPackagename(1));
                    if (!isInstalled){
                        ToastUtil.toastShort(CycleActivity.this,"没有安装此应用!");
                        cb2.setChecked(false);
                    }else{
                        hashSet.add(getPackagename(1));
                    }
                }else{
                    hashSet.remove(getPackagename(1));
                }
            }
        });
        cb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    boolean isInstalled = AUtil.checkAppInstalled(CycleActivity.this,getPackagename(2));
                    if (!isInstalled){
                        ToastUtil.toastShort(CycleActivity.this,"没有安装此应用!");
                        cb3.setChecked(false);
                    }else{
                        hashSet.add(getPackagename(2));
                    }
                }else{
                    hashSet.remove(getPackagename(2));
                }
            }
        });
        cb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    boolean isInstalled = AUtil.checkAppInstalled(CycleActivity.this,getPackagename(3));
                    if (!isInstalled){
                        ToastUtil.toastShort(CycleActivity.this,"没有安装此应用!");
                        cb4.setChecked(false);
                    }else{
                        hashSet.add(getPackagename(3));
                    }
                }else{
                    hashSet.remove(getPackagename(3));
                }
            }
        });


        tvSure.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                if (BaseAccessibilityService.getInstance().isAccessibilitySettingsOn(CycleActivity.this)) {
                    if (!Settings.canDrawOverlays(CycleActivity.this)) {
                        ToastUtil.toastShort(CycleActivity.this,"请授权悬浮窗权限");
                        startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" +getPackageName())), 123);
                        return;
                    }
                    if (hashSet.size()<2){
                        ToastUtil.toastShort(CycleActivity.this,"至少选择两个！");
                        return;
                    }
                    time = Integer.parseInt(etTime.getText().toString());
                    if ( time> 60){
                        ToastUtil.toastShort(CycleActivity.this,"不能超过60分钟！");
                        return;
                    }

                    DialogUtil dialogUtil = new DialogUtil();
                    dialogUtil.showDialog(CycleActivity.this,"将按照每"+etTime.getText()+"分钟间隔，循环启动你所选中的程序，进行自动切换并自动滑屏？");
                    dialogUtil.setmOnLinster(new DialogUtil.onDialogUtilLinster() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void isOk(boolean cancelOrOk) {
                            if(cancelOrOk){
                                sureService();
                                Iterator<String> iterator = hashSet.iterator();
                                final List<String> list = new ArrayList<>();
                                while (iterator.hasNext()){
                                    String s = iterator.next();
                                    Log.e("TAG", "isOk2: "+s);
                                    list.add(s);
                                }

                                IntentUtil.intentPackage(CycleActivity.this,list.get(0));

                                if (hc!=null){
                                    hc = null;
                                }
                                 hc = new Handler.Callback() {
                                    @RequiresApi(api = Build.VERSION_CODES.M)
                                    @Override
                                    public boolean handleMessage(@NonNull Message msg) {
                                        if(MyVariable.isAutoCut){
                                            if(msg.what==0) {
                                                index++;
                                                if (index >= list.size()) {
                                                    index = 0;
                                                }
                                                String pakg = list.get(index);
                                                Log.e("TAG", "handleMessage: " + pakg);
                                                IntentUtil.intentPackage(CycleActivity.this, pakg);
                                            }
                                        }
                                        mHandler.sendEmptyMessageDelayed(0,time*6000);
                                        return false;
                                    }
                                };
                                if (mHandler!=null) {
                                    mHandler.removeMessages(0);
                                    mHandler=null;
                                }
                                mHandler = new Handler(getMainLooper(),hc);
                                mHandler.sendEmptyMessageDelayed(0,time*6000);

                            }
                        }
                    });
                }else{
                    diaHint();
                }

            }
        });

    }

    private void diaHint(){
        final DialogUtil dialogUtil = new DialogUtil();
        dialogUtil.showDialog(CycleActivity.this,"必须先开启无障碍权限！");
        dialogUtil.setmOnLinster(new DialogUtil.onDialogUtilLinster() {
            @Override
            public void isOk(boolean cancelOrOk) {
                if(cancelOrOk){
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivity(intent);

                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void sureService(){
        MyVariable.isStartAuto=true;
//        if (MyVariable.isFloating) {
//            stopService(intent_floatingImage);
//            stopService(intent_floatingText);
//        }
        if (MyVariable.isFirstStart) {
            startService(intent_floatingImage);
            startService(intent_floatingText);
        }
            Toast.makeText(CycleActivity.this,"服务已开启",Toast.LENGTH_LONG).show();

    }

    private String getPackagename(int index){
        try {
            jsonObject = jsonArray.getJSONObject(index);
            packagename = jsonObject.getString("packagename");
            toastStr = jsonObject.getString("toastStr");
            return packagename;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}