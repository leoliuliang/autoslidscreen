package com.mxkj.autoslidscreen;

import android.app.Application;

import com.tencent.bugly.Bugly;
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        CrashReport.initCrashReport(getApplicationContext(), "b6ca24ecd3", true);
        Bugly.init(getApplicationContext(), "b6ca24ecd3", false);
//        UMConfigure.init(this, "5fbcb007690bda19c789ee28", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
//        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
//        PlatformConfig.setWeixin("wxdc1e388c3822c80b", "3baf1193c85774b3fd9d18447d76cab0");//个人开发者申请不了微信开放平台
    }
}
