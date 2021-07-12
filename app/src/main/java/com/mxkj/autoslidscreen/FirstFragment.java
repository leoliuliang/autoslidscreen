package com.mxkj.autoslidscreen;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mxkj.autoslidscreen.config.MyVariable;
import com.mxkj.autoslidscreen.simulatekey.AutoScrollAccessibilityService;
import com.mxkj.autoslidscreen.simulatekey.BaseAccessibilityService;
import com.mxkj.autoslidscreen.simulatekey.FloatingImageDisplayService;
import com.mxkj.autoslidscreen.simulatekey.FloatingTextService;
import com.mxkj.autoslidscreen.util.AUtil;
import com.mxkj.autoslidscreen.util.DialogUtil;
import com.mxkj.autoslidscreen.util.IntentUtil;
import com.mxkj.autoslidscreen.util.JsonUtil;
import com.mxkj.autoslidscreen.util.ToastUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class FirstFragment extends Fragment {
    private static String TAG="FirstFragment";
    TextView btnAccredit;

    int REQUEST_SELECT=1;
//    int etFrom=3;
//    int etTo=12;
//    int etFixed=10;
//    boolean cbRandom=true;
//    boolean cbFixed=false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setView(view);
    }

    private void setView(View view){

        Banner banner = (Banner) view.findViewById(R.id.banner);
        setBanner(banner);
        btnAccredit = view.findViewById(R.id.btnAccredit);
        TextView btnStartService = view.findViewById(R.id.btnStartService);
        final CheckBox rbBF = view.findViewById(R.id.rbBF);
        TextView tvHelp = view.findViewById(R.id.tvHelp);
        TextView tvSet = view.findViewById(R.id.tvSet);

        btnAccredit.setVisibility(View.GONE);
        BaseAccessibilityService.getInstance().init(getActivity());

        tvSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(),SettingsActivity.class),REQUEST_SELECT);
            }
        });
        if(!BaseAccessibilityService.getInstance().isAccessibilitySettingsOn(getActivity())){
            btnAccredit.setVisibility(View.VISIBLE);
            btnAccredit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivity(intent);
                }
            });

        }

//        final BottomNavigationView mNavigationView = ((MainActivity)getActivity()).mNavigationView;
//        ViewTreeObserver vto = mNavigationView.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) rbBF.getLayoutParams();
//                lp.bottomMargin = mNavigationView.getHeight();
//                rbBF.setLayoutParams(lp);
//            }
//        });

        rbBF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Log.e(TAG,"正在播放。。");
                }else {
                    Log.e(TAG,"暂停。。");
                }
            }
        });

        btnStartService.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Log.d(TAG, "run: -------1-");
                if (BaseAccessibilityService.getInstance().isAccessibilitySettingsOn(getActivity())) {
                    sureService();

                }else{
                    diaHint();
                }



            }
        });
        tvHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),HelpActivity.class));

            }
        });
        view.findViewById(R.id.tvDY).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = "确定打开抖音极速版app并且自动播放？";
                String jsKs = "com.ss.android.ugc.aweme.lite";
                String toastStr = "没有安装抖音";
                showMyDialog(content,jsKs,toastStr);
            }
        });

        view.findViewById(R.id.tvKS).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = "确定打开快手极速版app并且自动播放？";
                String jsKs = "com.kuaishou.nebula";
                String toastStr = "没有安装快手";
                showMyDialog(content,jsKs,toastStr);
            }
        });

        view.findViewById(R.id.tvHS).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = "确定打开火山极速版app并且自动播放？";
                String jsKs = "com.ss.android.ugc.livelite";
                String toastStr = "没有安装火山极速版";
                showMyDialog(content,jsKs,toastStr);
            }
        });

        view.findViewById(R.id.tvDYHS).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = "确定打开抖音火山版app并且自动播放？";
                String jsKs = "com.ss.android.ugc.live";
                String toastStr = "没有安装抖音火山版";
                showMyDialog(content,jsKs,toastStr);
            }
        });
        view.findViewById(R.id.tvCycle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(getActivity(),CycleActivity.class));

            }
        });
    }

    private void setBanner(Banner banner){
        List<String> listImg = new ArrayList<>();
        listImg.add("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3762848093,2881616330&fm=26&gp=0.jpg");
        listImg.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1486271666,307393547&fm=26&gp=0.jpg");
//        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(getActivity()).load(path).into(imageView);
            }
        });
        banner.setImages(listImg);
        banner.setBannerAnimation(Transformer.DepthPage);
        banner.isAutoPlay(true);
        banner.setDelayTime(2000);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
//                ToastUtil.toastShort(getActivity(),"你点击了第" + (position + 1) + "张轮播图");
            }
        });
        banner.start();
    }

    private void showMyDialog(String content,final String pkgName, final String toastStr){
        if (MyVariable.isFloating) {
            String jsKs = pkgName;
            boolean isJsks = AUtil.checkAppInstalled(getActivity(),jsKs);
            if(!isJsks){
                ToastUtil.toastShort(getActivity(),toastStr);
            }else{
                IntentUtil.intentPackage(getActivity(),jsKs);
            }
            return;
        }

        if (BaseAccessibilityService.getInstance().isAccessibilitySettingsOn(getActivity())) {
            DialogUtil dialogUtil = new DialogUtil();
            dialogUtil.showDialog(getActivity(),content);
            dialogUtil.setmOnLinster(new DialogUtil.onDialogUtilLinster() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void isOk(boolean cancelOrOk) {
                    if(cancelOrOk){
                        sureService();

                        String jsKs = pkgName;
                        boolean isJsks = AUtil.checkAppInstalled(getActivity(),jsKs);
                        if(!isJsks){
                            ToastUtil.toastShort(getActivity(),toastStr);
                        }else{
                            IntentUtil.intentPackage(getActivity(),jsKs);
                        }
                    }
                }
            });
        }else{
            diaHint();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void sureService(){
        MyVariable.isStartAuto=true;
        if (MyVariable.isFloating) {
            return;
        }
        if (!Settings.canDrawOverlays(getActivity())) {
            ToastUtil.toastShort(getActivity(),"当前无权限，请授权");
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getActivity().getPackageName())), 123);
        } else {
            MyVariable.isStartAuto=true;
            getActivity().startService(new Intent(getActivity(), FloatingImageDisplayService.class));
//            getActivity().startService(new Intent(getActivity(), FloatingTextService.class));

            Toast.makeText(getActivity(),"服务已开启",Toast.LENGTH_LONG).show();
        }
    }

    private void diaHint(){
        final DialogUtil dialogUtil = new DialogUtil();
        dialogUtil.showDialog(getActivity(),"必须先开启无障碍权限！");
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT&& resultCode == RESULT_OK) {

        }
        if(requestCode==123){
            if (!Settings.canDrawOverlays(getActivity())) {
                ToastUtil.toastShort(getActivity(),"授权失败");
            } else {
                ToastUtil.toastShort(getActivity(),"授权成功");
                getActivity().startService(new Intent(getActivity(), FloatingImageDisplayService.class));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(BaseAccessibilityService.getInstance().isAccessibilitySettingsOn(getActivity())) {
            btnAccredit.setVisibility(View.GONE);
        }
    }




}