package com.myself.business.manager;

import android.app.ProgressDialog;
import android.content.Context;

import com.myself.business.R;

/**
 * Created by Kamh on 2018/7/3.
 */

public class DialogManager {

    private ProgressDialog mProgressDialog = null;

    private static class Holder{
        private static final DialogManager INSTANCE = new DialogManager();
    }

    public static DialogManager getInstance(){
        return Holder.INSTANCE;
    }

    public void showProgressDialog(Context context){
        if (mProgressDialog == null){
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage(context.getResources().getString(R.string.please_wait));
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    public void dismissProgressDialog(){
        if (mProgressDialog != null){
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;
    }

}
