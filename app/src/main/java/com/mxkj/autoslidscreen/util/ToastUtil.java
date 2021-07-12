package com.mxkj.autoslidscreen.util;

import android.content.Context;
import android.widget.Toast;

import com.mxkj.autoslidscreen.MainActivity;

public class ToastUtil {
    public static void toastShort(Context context,String str){
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }
}
