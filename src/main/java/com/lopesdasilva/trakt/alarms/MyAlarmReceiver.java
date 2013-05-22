package com.lopesdasilva.trakt.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by lopesdasilva on 18/05/13.
 */
public class MyAlarmReceiver extends BroadcastReceiver {
    private final String REMINDER_BUNDLE = "MyReminderBundle";

    // this constructor is called by the alarm manager.
    public MyAlarmReceiver() {
    }

    // you can use this constructor to create the alarm.
    //  Just pass in the main activity as the context,
    //  any extras you'd like to get later when triggered
    //  and the timeout
    public MyAlarmReceiver(Context context, Bundle extras, int timeoutInSeconds) {
        AlarmManager alarmMgr =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MyAlarmReceiver.class);
        intent.putExtra(REMINDER_BUNDLE, extras);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(System.currentTimeMillis());
        time.add(Calendar.SECOND, timeoutInSeconds);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(),
                pendingIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // here you can get the extras you passed in when creating the alarm
        //intent.getBundleExtra(REMINDER_BUNDLE));
        Log.d("Trakt Fragments", "The alarm has ended");
        Toast.makeText(context, "Alarm went off", Toast.LENGTH_SHORT).show();
    }
}
