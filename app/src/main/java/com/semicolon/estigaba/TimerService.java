package com.semicolon.estigaba;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TimerService extends Service {

    private SharedPreferences time_pref;
    private String time;
    private MediaPlayer mediaPlayer;
    private  String [] titles;
    private static Timer timer;
    private static TimerTask timerTask;
    private NotificationManager manager;



    private void init()
    {


        timer = new Timer();

        StartSoundTimer(Integer.parseInt(time));





    }


    private void StartSoundTimer(int time) {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                updateUi();
            }
        };
        timer.scheduleAtFixedRate(timerTask,time*60000,time*60000);
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

    private void updateUi()
    {
        String url = "android.resource://"+this.getPackageName()+"/";
        titles = getResources().getStringArray(R.array.array_estigaba);

        List<Integer> raws = new ArrayList<>();
        raws.add(R.raw.sally);
        raws.add(R.raw.sob7an);
        raws.add(R.raw.hamd);


        int pos = time_pref.getInt("pos",0);
        Log.e("p",pos+"pos");

        if (pos<raws.size())
        {
            url=url+raws.get(pos);
            pos++;
            UpdateShared(pos);
            Log.e("ccc","553366");

        }else if (pos>=raws.size())
            {

                UpdateShared(0);
                url=url+raws.get(0);

                UpdateShared(1);

                Log.e("nn","553388");

            }


            if (time_pref.getInt("pos",0)<raws.size())
            {
                Log.e("pooooooooos",time_pref.getInt("pos",0)+"fffffffff");
                switch (time_pref.getInt("pos",0))
                {
                    case 0:
                        createNotification(titles[0]);
                        break;
                    case 1:
                        createNotification(titles[0]);

                        break;
                    case 2:
                       /* Intent intent2 = new Intent(TimerService.this,TextActivity.class);

                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent2.putExtra("title",titles[1]);

                        startActivity(intent2);*/
                        createNotification(titles[1]);

                        break;


                }

            }else
                {
                    createNotification(titles[2]);

                }


        new AsynkTask().execute(url,String.valueOf(time_pref.getInt("pos",0)));








    }

    private void UpdateShared(int pos) {
        SharedPreferences.Editor editor= time_pref.edit();
        editor.putInt("pos",pos);
        editor.apply();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        time_pref = getSharedPreferences("alarm",MODE_PRIVATE);
        time= time_pref.getString("time","-1");
        Log.e("tiiiiiiiiiiiiime",time);
        init();

    }

    @Override
    public void onDestroy() {
        if (timer!=null)
        {
            timer.cancel();
            timer.purge();
        }
        if (manager!=null)
        {
            manager.cancelAll();
        }
        UpdateShared(0);
        super.onDestroy();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class AsynkTask extends AsyncTask<String,Void,Void>{
        @Override
        protected Void doInBackground(String... strings) {


            String uri = strings[0];
            mediaPlayer = MediaPlayer.create(TimerService.this, Uri.parse(uri));
            mediaPlayer.start();


            return null;
        }


    }





}
