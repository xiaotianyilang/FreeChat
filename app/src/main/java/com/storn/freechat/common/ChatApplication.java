package com.storn.freechat.common;

import android.app.Application;
import android.content.Context;

import com.common.common.Constants;
import com.common.util.PreferenceTool;
import com.storn.freechat.vo.UserVo;

/**
 * Created by tianshutong on 2016/12/7.
 */

public class ChatApplication extends Application {

    public static int menu_left;
    public static UserVo userVo;

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceTool.init(this);
        DBHelper.initDataBase(this, Constants.DB_NAME);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(base);
    }

    public static int getMenu_left() {
        return menu_left;
    }

    public static void setMenu_left(int menu_left) {
        ChatApplication.menu_left = menu_left;
    }

    public static UserVo getUserVo() {
        return userVo;
    }

    public static void setUserVo(UserVo userVo) {
        ChatApplication.userVo = userVo;
    }
}
