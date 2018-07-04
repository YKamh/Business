package com.myself.business.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.myself.business.R;
import com.myself.business.adutil.Utils;
import com.myself.business.manager.UserManager;
import com.myself.business.util.Util;

import org.w3c.dom.Text;

/**
 * Created by Kamh on 2018/7/4.
 */

public class MyQrodeDialog extends Dialog{

    private Context mContext;

    private ImageView mQrcodeView;
    private TextView mTickView;
    private TextView mCloseView;

    public MyQrodeDialog(@NonNull Context context) {
        super(context, 0);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_mycode_layout);
        initView();
    }

    private void initView() {
        mQrcodeView = (ImageView) findViewById(R.id.qrcode_view);
        mTickView = (TextView) findViewById(R.id.tick_view);
        mCloseView = (TextView) findViewById(R.id.close_view);
        mCloseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        String name = UserManager.getInstance().getUser().data.name;
        mQrcodeView.setImageBitmap(Util.createQRCode(
                Utils.dip2px(mContext, 200),
                Utils.dip2px(mContext, 200),
                name));
        mTickView.setText(name + mContext.getString(R.string.personal_info));
    }
}
