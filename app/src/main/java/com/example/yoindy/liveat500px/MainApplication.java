package com.example.yoindy.liveat500px;

import android.app.Application;

import com.inthecheesefactory.thecheeselibrary.manager.Contextor;

/**
 * Created by YoInDy on 3/6/2560.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Initialize thing(s) here
        Contextor.getInstance().init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
