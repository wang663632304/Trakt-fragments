package com.lopesdasilva.trakt.extras;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.jakewharton.trakt.ServiceManager;
import com.lopesdasilva.trakt.TVtraktApp;

/**
 * Created by lopesdasilva on 21/05/13.
 */
public class UserChecker {


    private static TVtraktApp appState;
    private static ServiceManager manager;
    public static final String PREFS_NAME = "TraktPrefsFile";

    public static ServiceManager checkUserLogin(FragmentActivity fragmentActivity) {
        appState = ((TVtraktApp) fragmentActivity.getApplication());

        manager=appState.getManager();
        if(manager==null){
            SharedPreferences settings = fragmentActivity.getSharedPreferences(PREFS_NAME, 0);
            String mUsername=settings.getString("username", null);
            String mPassword_SHA1=settings.getString("password_sha1", null);

            if(mUsername==null || mPassword_SHA1==null){
              //TODO: launch login SCREEN
                Log.d("Trakt","Not Logged implement login, finishing now");
                fragmentActivity.finish();
            }else{
                manager=new ServiceManager();
                manager.setApiKey(appState.getAPIKEY());
                manager.setAuthentication(mUsername,mPassword_SHA1);
            }
        }if(!manager.readyWithUser()){

            SharedPreferences settings = fragmentActivity.getSharedPreferences(PREFS_NAME, 0);
            String mUsername=settings.getString("username", null);
            String mPassword_SHA1=settings.getString("password_sha1", null);

            manager.setApiKey(appState.getAPIKEY());
            manager.setAuthentication(mUsername,mPassword_SHA1);
        }
        return manager;
    }
    public static String getUsername(FragmentActivity fragmentActivity) {
        SharedPreferences settings = fragmentActivity.getSharedPreferences(PREFS_NAME, 0);
        return settings.getString("username", null);

    }
    public static void saveUserAndPassword(FragmentActivity fragmentActivity,String mUsername, String mPassword_sha1){
        ServiceManager manager = new ServiceManager();

        appState = ((TVtraktApp) fragmentActivity.getApplication());
        appState.setManager(manager);

        SharedPreferences settings = fragmentActivity.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("logged", true);
        editor.putString("username", mUsername);
        editor.putString("password_sha1", mPassword_sha1);
        editor.commit();


    }
}
