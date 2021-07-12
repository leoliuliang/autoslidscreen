package com.mxkj.autoslidscreen.simulatekey;

import android.app.Application;
import android.app.Service;
import android.content.Intent;
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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.mxkj.autoslidscreen.R;
import com.mxkj.autoslidscreen.config.MyVariable;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class FloatingImageDisplayService extends Service {
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;

    private View displayView;
    ImageView imageView;
    private int[] images;
    private int imageIndex = 0;
    private Handler changeImageHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        MyVariable.isFloating=true;
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
        layoutParams.width = 100;
        layoutParams.height = 1000;
        Display defaultDisplay = windowManager.getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int x = point.x;
        int y = point.y;
        layoutParams.x = x/2;
        layoutParams.y = y/2;

        images = new int[] {
                R.mipmap.jt,
                0
        };
        changeImageHandler = new Handler(this.getMainLooper(), changeImageCallback);
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
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            displayView = layoutInflater.inflate(R.layout.image_display, null);
//            displayView.setOnTouchListener(new FloatingOnTouchListener());
            imageView = displayView.findViewById(R.id.image_display_imageview);
            imageView.setImageResource(images[imageIndex]);
            windowManager.addView(displayView, layoutParams);

            changeImageHandler.sendEmptyMessageDelayed(0, 200);
        }
    }

    private Handler.Callback changeImageCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0) {
                imageIndex++;
                if (imageIndex >= 2) {
                    imageIndex = 0;
                    changeImageHandler.sendEmptyMessageDelayed(0, 200);
                }else {
                    changeImageHandler.sendEmptyMessageDelayed(0, AutoScrollAccessibilityService.i*1000-200);
                }
                if (!MyVariable.isPause) {
                    if (displayView != null) {
                        ((ImageView) displayView.findViewById(R.id.image_display_imageview)).setImageResource(images[imageIndex]);
                    }
                }

            }
            return false;
        }
    };

//    private class FloatingOnTouchListener implements View.OnTouchListener {
//        private int x;
//        private int y;
//
//        @Override
//        public boolean onTouch(View view, MotionEvent event) {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    x = (int) event.getRawX();
//                    y = (int) event.getRawY();
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    int nowX = (int) event.getRawX();
//                    int nowY = (int) event.getRawY();
//                    int movedX = nowX - x;
//                    int movedY = nowY - y;
//                    x = nowX;
//                    y = nowY;
//                    layoutParams.x = layoutParams.x + movedX;
//                    layoutParams.y = layoutParams.y + movedY;
//                    windowManager.updateViewLayout(view, layoutParams);
//                    break;
//                default:
//                    break;
//            }
//            return false;
//        }
//    }
}
