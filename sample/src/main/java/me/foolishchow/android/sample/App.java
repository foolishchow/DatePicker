package me.foolishchow.android.sample;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.MainThread;

/**
 * Description:
 * Author: foolishchow
 * Date: 19/12/2020 4:17 PM
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
        Thread.setDefaultUncaughtExceptionHandler(mExceptionHandler);
    }
    static private App sApp;
    private synchronized void setInstance(App app) {
        sApp = app;
    }
    public static App getAppContext() {
        return sApp;
    }



    public Thread.UncaughtExceptionHandler mExceptionHandler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            restart();
        }
    };

    private static void restart() {
        App context = App.getAppContext();
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
