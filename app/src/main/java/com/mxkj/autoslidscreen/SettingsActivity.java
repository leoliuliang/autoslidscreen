package com.mxkj.autoslidscreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.mxkj.autoslidscreen.config.MyVariable;
import com.mxkj.autoslidscreen.simulatekey.AutoScrollAccessibilityService;

public class SettingsActivity extends BaseActivity {
    EditText etFrom;
    EditText etTo;
    CheckBox cbRandom;
    CheckBox cbFixed;
    EditText etFixed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                finish();
            }
        });
         etFrom = findViewById(R.id.etFrom);
         etTo = findViewById(R.id.etTo);
         cbRandom = findViewById(R.id.cbRandom);
         cbFixed = findViewById(R.id.cbFixed);
         etFixed = findViewById(R.id.etFixed);
        TextView tvSure = findViewById(R.id.tvSure);

        SharedPreferences sharedPreferences=this.getSharedPreferences("share",MODE_PRIVATE);
        int ef=sharedPreferences.getInt("ef", 3);
        int et=sharedPreferences.getInt("et", 12);
        int efi=sharedPreferences.getInt("efi", 10);
        boolean cbr=sharedPreferences.getBoolean("cbr", true);
        boolean cbf=sharedPreferences.getBoolean("cbf", false);
        etFrom.setText(""+ef);
        etTo.setText(""+et);
        etFixed.setText(""+efi);
        cbRandom.setChecked(cbr);
        cbFixed.setChecked(cbf);


        cbRandom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cbFixed.setChecked(false);
                }else{
                    cbFixed.setChecked(true);
                }
            }
        });
        cbFixed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cbRandom.setChecked(false);
                }else {
                    cbRandom.setChecked(true);

                }
            }
        });

        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        getData();
        super.onBackPressed();

    }


    protected void getData() {

        if(Integer.parseInt(etFrom.getText().toString())<=0){
            etFrom.setText("3");
        }
        if(Integer.parseInt(etTo.getText().toString())<=0){
            Log.d("", "onDestroy: "+etTo.getText().toString());
            etTo.setText("12");
        }
        if(Integer.parseInt(etFixed.getText().toString())<=0){
            etFixed.setText("10");
        }

        Intent data = new Intent();
        int ef = Integer.parseInt(etFrom.getText().toString());
        int et = Integer.parseInt(etTo.getText().toString());
        int efi =  Integer.parseInt(etFixed.getText().toString());
        boolean cbr = cbRandom.isChecked();
        boolean cbf = cbFixed.isChecked();
//        data.putExtra("etFrom",ef);
//        data.putExtra("etTo", et);
//        data.putExtra("etFixed",efi);
//        data.putExtra("cbRandom", cbr);
//        data.putExtra("cbFixed", cbf);
        AutoScrollAccessibilityService.fixedOrRandom=cbr;
        AutoScrollAccessibilityService.fixedTime=efi;
        AutoScrollAccessibilityService.etFrom=ef;
        AutoScrollAccessibilityService.etTo=et;
        MyVariable.isStartAuto = true;

        setResult(RESULT_OK, data);

        SharedPreferences sharedPreferences=this.getSharedPreferences("share",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("ef", ef);
        editor.putInt("et", et);
        editor.putInt("efi", efi);
        editor.putBoolean("cbr", cbr);
        editor.putBoolean("cbf", cbf);
        editor.commit();
    }
}