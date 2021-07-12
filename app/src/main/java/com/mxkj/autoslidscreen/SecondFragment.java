package com.mxkj.autoslidscreen;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.mxkj.autoslidscreen.util.ToastUtil;

import static android.content.Context.MODE_PRIVATE;

public class SecondFragment extends Fragment {
    private View view;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        if(null==view) {
            view = inflater.inflate(R.layout.fragment_second, container, false);

        }
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvName = view.findViewById(R.id.tvName);
        int i = (int)((Math.random()*9+1)*100000);
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("share",MODE_PRIVATE);
        int tn=sharedPreferences.getInt("tvName", 666666);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        if(tn==666666) {
            editor.putInt("tvName", i);
            editor.commit();
        }
        tvName.setText(""+tn);
        view.findViewById(R.id.relAboutUs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),AboutActivity.class));
            }
        });
        view.findViewById(R.id.relContact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),ContactActivity.class));
            }
        });
        view.findViewById(R.id.relCheck).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.toastShort(getActivity(),"当前是最新版本");
            }
        });
        view.findViewById(R.id.relShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=23){
                    String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                    ActivityCompat.requestPermissions(getActivity(),mPermissionList,123);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==123){
            ToastUtil.toastShort(getActivity(),"没有相关权限");
        }
    }
}