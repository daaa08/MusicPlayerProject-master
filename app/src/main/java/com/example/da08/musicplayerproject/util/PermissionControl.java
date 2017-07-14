package com.example.da08.musicplayerproject.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.widget.Toast;

/**
 * Created by Da08 on 2017. 6. 15..
 */

public class PermissionControl {
    public static final int REQ_FLAG = 1002313123;  // 겹칠일이없는 숫자로
    private static String permissions[] = {
             Manifest.permission.READ_EXTERNAL_STORAGE };  // permission 자체는 다른곳에서 쓸 일이 없으므로 private

    public static void checkVersion(Activity activity){
        // 마시멜로 이상버전에서만 런타임 권한체크
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission(activity);
        }else {
            callInit(activity);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void checkPermission(Activity activity){
        //1 권한체크 - 특정권한이 있는지 시스템에 물어본다
        boolean denied = false;
        for(String perm : permissions){
            if(activity.checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED){
                denied = true;
                break;
            }
        }
        if(denied){
            // 2. 권한이 없으면 사용자에 권한을 달라고 요청
            activity.requestPermissions(permissions ,REQ_FLAG);
        }else{
            callInit(activity);
        }
    }

    public static void onResult(Activity activity, int requestCode, @NonNull int[] grantResults){
        if(requestCode == REQ_FLAG){
            boolean granted = true;
            for(int grant : grantResults){
                if(grant != PackageManager.PERMISSION_GRANTED){
                    granted = false;
                    break;
                }
            }
            // 3.1 사용자가 승인을 했음
            if(granted){
                callInit(activity);
            }else{
                //activity.finish();
                Toast.makeText(activity, "권한요청을 승인하셔야 앱을 사용할 수 있습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static void callInit(Activity activity){
        if(activity instanceof CallBack)
            ((CallBack)activity).init();
        else
            throw new RuntimeException("must implement this.CallBack");
    }

    public interface CallBack {
        public void init();
    }
}
