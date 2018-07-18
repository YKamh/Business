package com.myself.business.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

/**
 * Created by Kamh on 2018/5/29.
 */

public class BaseFragment extends Fragment {
    protected Activity mContext;

    /**
     * 判断是否已拥有对应权限
     * @param permissions
     * @return
     */
    public boolean hasPermission(String... permissions){
        for (String permission : permissions){
            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    /**
     * 申请相关权限
     * @param code
     * @param permissions
     */
    public void requestPermission(int code, String... permissions){
        if (Build.VERSION.SDK_INT >= 23){
            requestPermissions(permissions, code);
        }
    }
    public static final int EXTERNAL_STORAGE_PERMISSION = 0x01;//读写SD卡权限
    public static final int CAMERA_PERMISSTION = 0x02;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case EXTERNAL_STORAGE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    doSDCard();
                }
                break;
            case CAMERA_PERMISSTION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    doCamera();
                }
                break;
        }
    }

    private void doCamera() {}

    //读写SDcard业务逻辑，具体的子类实现
    public void doSDCard() {}
}
