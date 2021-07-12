package com.mxkj.autoslidscreen.simulatekey;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.mxkj.autoslidscreen.config.MyVariable;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by finup_zander on 2019-11-18.
 */
public class AutoScrollAccessibilityService extends BaseAccessibilityService {

    private long lastTime;
    private String TAG = "AutoScrollAccessibilityService";
    public static boolean fixedOrRandom;
    public static int etTo= 3 ;
    public static int etFrom= 12 ;
    public static int fixedTime= 10 ;
    public static  int i=1;
    Random random;
    Handler handler;

    @RequiresApi(api = VERSION_CODES.N)
    @Override
    public void onAccessibilityEvent(final AccessibilityEvent event) {
        super.onAccessibilityEvent(event);
        if(MyVariable.isStartAuto) {
            Log.e("AutoScroll", event.toString());
            MyVariable.isStartAuto = false;
            if(handler!=null){
                handler.removeMessages(0);
                handler=null;
                random=null;
            }
            random = new Random();
             handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if(fixedOrRandom) {
                        i = random.nextInt(etTo - etFrom) + etFrom;
                    }else{
                        i = fixedTime;
                    }
                    System.out.println("我爱你中国  : "+i);
                    handler.sendEmptyMessageDelayed(0,i*1000);
                    if (!MyVariable.isPause) {
                        scroll();
                    }
                }
            };
            if(fixedOrRandom) {
                i = random.nextInt(etTo - etFrom) + etFrom;
            }else{
                i = fixedTime;
            }
            handler.sendEmptyMessageDelayed(0,i*1000);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void scroll(){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int middleYValue = displayMetrics.heightPixels/2;
        final int originYSideOfScreen = displayMetrics.heightPixels * 4/5;
        final int destYSizeOfScreen = displayMetrics.heightPixels * 1 / 8;

        Log.e("AutoScroll", "originYSideOfScreen:" + originYSideOfScreen + "--destYSizeOfScreen:" + destYSizeOfScreen);

        Point origin = new Point(middleYValue,originYSideOfScreen);
        Point dest = new Point(middleYValue,destYSizeOfScreen);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path p = new Path();
        p.moveTo(origin.x, origin.y);
        p.lineTo(dest.x, dest.y);
//        p.moveTo(200, 2000);//设置Path的起点
//        p.quadTo(450,1036,90,864);
//        p.lineTo(dest.x, dest.y);
        builder.addStroke(new GestureDescription.StrokeDescription(p, 200L, 500L));
        GestureDescription gesture = builder.build();
        boolean isDispatched = dispatchGesture(gesture, new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                Log.d("AutoScroll","===============Gesture Completed================");// originYSideOfScreen:1862--destYSizeOfScreen:291,2400x1176 //2640x1200,1999 312
                super.onCompleted(gestureDescription);
            }

            @Override
            public void onCancelled(final GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                Log.d("AutoScroll","===============Gesture Cancelled================");
            }
        },null);
    }

}
