package com.mxkj.autoslidscreen;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mxkj.autoslidscreen.util.ToastUtil;

public class MainActivity extends BaseActivity {
    private long mExitTime =0L;
    public BottomNavigationView mNavigationView;
    private FirstFragment firstFrg;
    private SecondFragment secondFrg;
    private Fragment[] fragments;
    private FrameLayout frameLayout;
    private int lastfragment = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    private void initView() {
        firstFrg = new FirstFragment();
        secondFrg = new SecondFragment();
        fragments = new Fragment[]{firstFrg, secondFrg};
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        //设置fragment到布局
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, firstFrg).show(firstFrg).commit();

        mNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);

        //这里是bottomnavigationview的点击事件
        mNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    if (lastfragment != 0) {
                        switchFragment(lastfragment, 0);
                        lastfragment = 0;
                    }
                    return true;
                case R.id.my:
                    if (lastfragment != 1) {
                        switchFragment(lastfragment, 1);
                        lastfragment = 1;
                    }
                    return true;
                default:
                    break;
            }
            return false;
        }
    };

    /**
     *切换fragment
     */
    private void switchFragment(int lastfragment, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //隐藏上个Fragment
        transaction.hide(fragments[lastfragment]);
        if (fragments[index].isAdded() == false) {
            transaction.add(R.id.frameLayout, fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        exitApp();
    }

    public void exitApp(){
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            ToastUtil.toastShort(this,"再按一次退出程序");
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
            super.onBackPressed();
        }
    }
}


