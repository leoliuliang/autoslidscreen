package com.mxkj.autoslidscreen.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class IntentUtil {
    public static void intentPackage(Context context,String packageName){
        Intent intent = new Intent();
        PackageManager packageManager = context.getPackageManager();
        intent = packageManager.getLaunchIntentForPackage(packageName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
