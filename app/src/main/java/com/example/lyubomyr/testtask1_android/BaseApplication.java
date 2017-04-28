package com.example.lyubomyr.testtask1_android;

import android.app.Application;
import android.content.Context;

import com.example.lyubomyr.testtask1_android.database.greendao.DaoMaster;
import com.example.lyubomyr.testtask1_android.database.greendao.DaoSession;

import org.greenrobot.greendao.database.Database;

public class BaseApplication extends Application {
    public static Context context;
    static private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        initDB();
    }

    private void initDB() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "company_db");
        Database db = helper.getWritableDb();
        daoSession  = new DaoMaster(db).newSession();
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }
}
