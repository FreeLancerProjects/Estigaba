package com.semicolon.estigaba;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class TimerRememberService extends Service {
    private static Timer timer2;
    private static TimerTask timerTask2;
    private Calendar calendar;
    private NotificationManager manager;



    private void init()
    {


        timer2 = new Timer();
        StartTimerForRemember();





    }

    private void StartTimerForRemember() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE",new Locale("en"));
        final String dayName = dateFormat.format(calendar.getTime());
        timerTask2 = new TimerTask() {
            @Override
            public void run() {

                if (dayName.toLowerCase().equals("sunday")||dayName.toLowerCase().equals("wednesday"))
                {

                    createNotification("لا تنسى صيام الاثنين والخميس");

                }

            }
        };
        timer2.scheduleAtFixedRate(timerTask2,60000*60*5,60000*60*5);

    }



    private void createNotification(String msg) {

        String channel_name="myChannel123123";
        String channel_id = "123123";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(channel_id,channel_name,importance);
            builder.setChannelId(channel_id);
            channel.setShowBadge(false);
            channel.setSound(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.not),new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT).build()
            );
            manager.createNotificationChannel(channel);

        }

        builder.setSmallIcon(R.mipmap.rounded_icon_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.icon_launcher));
        builder.setContentTitle("إستجابة");
        builder.setContentText(msg);
        builder.setSound(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.not));
        builder.setVibrate(new long[]{3000});
        builder.setLights(3000,3000,3000);

        manager.notify(1,builder.build());


    }


    @Override
    public void onCreate() {
        super.onCreate();
        calendar = Calendar.getInstance();
        init();

    }

    @Override
    public void onDestroy() {
        if (timer2 !=null)
        {
            timer2.cancel();
            timer2.purge();
        }

        if (timerTask2 !=null)
        {
            timerTask2.cancel();
        }

        if (manager!=null)
        {
            manager.cancelAll();

        }
        super.onDestroy();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }








}
