package com.myself.business.manager;

import com.myself.business.model.user.User;

/**
 * Created by Kamh on 2018/7/3.
 */

public class UserManager {

    private User mUser = null;

    private static class Holder{
        private static final UserManager INSTANCE = new UserManager();
    }

    public static UserManager getInstance(){
        return Holder.INSTANCE;
    }

    public void setUser(User user){
        mUser = user;
    }

    public User getUser(){
        return mUser;
    }

    public boolean hasLogined(){
        return mUser != null;
    }

    public void removeUser(){
        this.mUser = null;
    }

}
