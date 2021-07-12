package com.mxkj.autoslidscreen.simulatekey;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.mxkj.autoslidscreen.R;
import com.mxkj.autoslidscreen.config.MyVariable;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

/**
 * Created by dongzhong on 2018/5/30.
 */

public class FloatingButtonService extends Service {
    Handler handler;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;

    private CheckBox checkBox;

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        Display defaultDisplay = windowManager.getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int x = point.x;
        int y = point.y;
        layoutParams.width = 200;
        layoutParams.height = 200;
        layoutParams.x = x/2;
        layoutParams.y = y/2;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showFloatingWindow();
        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showFloatingWindow() {
        if (Settings.canDrawOverlays(this)) {
            if (checkBox==null) {
                checkBox = new CheckBox(getApplicationContext());
                checkBox.setButtonDrawable(0);
                checkBox.setBackground(getResources().getDrawable(R.drawable.selector_bf));
                checkBox.setChecked(true);
                windowManager.addView(checkBox, layoutParams);

//            Handler.Callback hc = new Handler.Callback() {
//                @Override
//                public boolean handleMessage(@NonNull Message msg) {
//                    if (msg.what==0) {
//
//                    }
//                    return false;
//                }
//            };
//            handler = new Handler(getMainLooper(),hc);
//            handler.sendEmptyMessageDelayed(0,1000);
                if (MyVariable.isStartAuto) {
                    checkBox.setChecked(true);
                } else {
                    checkBox.setChecked(false);
                }
//            handler.sendEmptyMessageDelayed(0, 1000);
                checkBox.setOnTouchListener(new FloatingOnTouchListener());
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Log.e(TAG, "onCheckedChanged: " + isChecked);
                        if (isChecked) {
                            MyVariable.isAutoCut = true;
                            MyVariable.isStartAuto = true;
                            MyVariable.isPause = false;
                        } else {
                            MyVariable.isStartAuto = false;
                            MyVariable.isAutoCut = false;
                            MyVariable.isPause = true;
                        }
                    }
                });
            }
        }
    }

    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    Log.e(TAG, "onTouch: "+ x+"  "+y );
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    layoutParams.x = layoutParams.x + movedX;
                    layoutParams.y = layoutParams.y + movedY;
                    windowManager.updateViewLayout(view, layoutParams);
                    break;
                default:
                    break;
            }
            return false;
        }
    }
}
