package com.myself.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.myself.business.R;
import com.myself.business.activity.base.BaseActivity;
import com.myself.business.manager.DialogManager;
import com.myself.business.manager.UserManager;
import com.myself.business.model.user.User;
import com.myself.business.network.http.RequestCenter;
import com.myself.vuandroidadsdk.okhttp.listener.DisposeDataListener;

/**
 * Created by Kamh on 2018/7/3.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    /**
     * UI
     */
    private EditText mUserNameView;
    private EditText mPasswordView;
    private TextView mLoginView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_login_layout);
        initView();
    }

    private void initView() {
        mUserNameView = (EditText) findViewById(R.id.associate_email_input);
        mPasswordView = (EditText) findViewById(R.id.login_input_password);
        mLoginView = (TextView) findViewById(R.id.login_button);
        mLoginView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_button:
                login();
                break;
            default:
                break;
        }
    }

    private void login() {
        String userName = mUserNameView.getText().toString().trim();
        String pwd = mPasswordView.getText().toString().trim();
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(pwd)){
            return;
        }
        DialogManager.getInstance().showProgressDialog(this);
        RequestCenter.login(userName, pwd, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                User user = (User) responseObj;
                UserManager.getInstance().setUser(user);
                sendLoginBroadcast();

            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }

    public static final String LOGIN_ACTION = "com.myself.business.LOGIN_ACTION";

    /**
     * 发送登录局部广播
     */
    private void sendLoginBroadcast(){
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(LOGIN_ACTION));
    }
}
