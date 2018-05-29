package com.myself.business.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myself.business.R;
import com.myself.business.view.fragment.home.HomeFragment;
import com.myself.business.view.fragment.home.MessageFragment;
import com.myself.business.view.fragment.home.MineFragment;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    /**
     * fragment相关
     */
    private FragmentManager fm;
    private HomeFragment mHomeFragment;
    private MessageFragment mMessageFragment;
    private MineFragment mMineFragment;
    private Fragment mCurrent;

    /**
     * UI相关
     */
    private RelativeLayout mHomeLayout;
    private RelativeLayout mMessageLayout;
    private RelativeLayout mMineLayout;
    private TextView mHomeTextView;
    private TextView mPondTextView;
    private TextView mMessageTextView;
    private TextView mMineTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        mHomeFragment = new HomeFragment();
        fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content_layout, mHomeFragment);
        fragmentTransaction.commit();
        
    }

    private void initView() {
        mHomeLayout = (RelativeLayout) findViewById(R.id.home_layout_view);
        mHomeLayout.setOnClickListener(this);
        mMessageLayout = (RelativeLayout) findViewById(R.id.pond_layout_view);
        mMessageLayout.setOnClickListener(this);
        mMineLayout = (RelativeLayout) findViewById(R.id.mine_layout_view);
        mMineLayout.setOnClickListener(this);

        mHomeTextView = (TextView) findViewById(R.id.home_image_view);
        mPondTextView = (TextView) findViewById(R.id.fish_image_view);
        mMessageTextView = (TextView) findViewById(R.id.message_image_view);
        mMineTextView = (TextView) findViewById(R.id.mine_image_view);
    }

    /**
     * 用来隐藏具体的Fragment
     * @param fragment
     * @param ft
     */
    private void hideFragment(Fragment fragment, FragmentTransaction ft){
        if (fragment != null){
            ft.hide(fragment);
        }
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        switch (v.getId()){
            case R.id.home_image_view:
                mHomeTextView.setBackgroundResource(R.drawable.comui_tab_home_selected);
                mPondTextView.setBackgroundResource(R.drawable.comui_tab_pond);
                mMessageTextView.setBackgroundResource(R.drawable.comui_tab_message);
                mMineTextView.setBackgroundResource(R.drawable.comui_tab_person);
                hideFragment(mMessageFragment, fragmentTransaction);
                hideFragment(mMineFragment, fragmentTransaction);
                if (mHomeFragment == null){
                    mHomeFragment = new HomeFragment();
                    fragmentTransaction.add(R.id.content_layout, mHomeFragment);
                }else{
                    fragmentTransaction.show(mHomeFragment);
                }
                break;
            case R.id.message_image_view:
                mHomeTextView.setBackgroundResource(R.drawable.comui_tab_home);
                mPondTextView.setBackgroundResource(R.drawable.comui_tab_pond);
                mMessageTextView.setBackgroundResource(R.drawable.comui_tab_message_selected);
                mMineTextView.setBackgroundResource(R.drawable.comui_tab_person);
                hideFragment(mHomeFragment, fragmentTransaction);
                hideFragment(mMineFragment, fragmentTransaction);
                if (mMessageFragment == null){
                    mMessageFragment = new MessageFragment();
                    fragmentTransaction.add(R.id.content_layout, mMessageFragment);
                }else{
                    fragmentTransaction.show(mMessageFragment);
                }
                break;
            case R.id.mine_image_view:
                mHomeTextView.setBackgroundResource(R.drawable.comui_tab_home);
                mPondTextView.setBackgroundResource(R.drawable.comui_tab_pond);
                mMessageTextView.setBackgroundResource(R.drawable.comui_tab_message);
                mMineTextView.setBackgroundResource(R.drawable.comui_tab_person_selected);
                hideFragment(mHomeFragment, fragmentTransaction);
                hideFragment(mMessageFragment, fragmentTransaction);
                if (mMineFragment == null){
                    mMineFragment = new MineFragment();
                    fragmentTransaction.add(R.id.content_layout, mMineFragment);
                }else{
                    fragmentTransaction.show(mMineFragment);
                }
                break;
            default:
                break;
        }

        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
