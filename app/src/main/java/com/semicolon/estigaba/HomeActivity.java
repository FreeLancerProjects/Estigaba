package com.semicolon.estigaba;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;
import com.zcw.togglebutton.ToggleButton;


public class HomeActivity extends AppCompatActivity {
    private TextView tv_time;
    private IndicatorSeekBar seekBar;
    private Button btn;
    private ToggleButton toggle;
    private SharedPreferences spref;
    private Intent intent,intent2;
    private AdView adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        MobileAds.initialize(this,"ca-app-pub-3411251647705287~6436586774");
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);


        tv_time = findViewById(R.id.tv_time);
        seekBar = findViewById(R.id.seekBar);
        btn = findViewById(R.id.btn);
        toggle = findViewById(R.id.toggle);


        spref = getSharedPreferences("alarm",MODE_PRIVATE);
        String time =spref.getString("time","-1");
        Log.e("time",time);
        if (time.equals("-1"))
        {
            tv_time.setText("0 دقيقة");
            seekBar.setProgress(0);
            btn.setVisibility(View.GONE);
        }else
            {
                seekBar.setProgress(Integer.parseInt(time));
                tv_time.setText(time+" دقيقة");
                btn.setVisibility(View.VISIBLE);
            }

        String toggle_state = spref.getString("state","off");

        if (toggle_state.equals("off"))
        {
            toggle.setToggleOff();
        }else if (toggle_state.equals("on"))
        {
            toggle.setToggleOn();
            startSoundService();
        }






        toggle.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on)
                {
                    UpdatePrefState("on");
                    startRememberService();
                    Log.e("on","on");
                }else
                    {
                        UpdatePrefState("off");
                        Log.e("off","off");
                        stopRememberService();


                    }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int prog = seekBar.getProgress();
                UpdatePrefTime(String.valueOf(prog));
                Toast.makeText(HomeActivity.this, "تم", Toast.LENGTH_LONG).show();
                startSoundService();

            }
        });

        seekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                int prog = seekParams.progress;
               if (prog==0)
               {
                   tv_time.setText("0 دقيقة");
                   btn.setVisibility(View.GONE);
                   UpdatePrefTime("0");
                   stopSoundService();
                   Toast.makeText(HomeActivity.this, "تم الالغاء", Toast.LENGTH_SHORT).show();
               }else
                   {
                       tv_time.setText(prog+" دقيقة");
                       btn.setVisibility(View.VISIBLE);
                   }
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });



        }



    private void UpdatePrefTime (String time)
    {
        spref = getSharedPreferences("alarm",MODE_PRIVATE);
        SharedPreferences.Editor editor = spref.edit();
        editor.putString("time",time);
        editor.apply();
    }

    private void UpdatePrefState (String state)
    {
        spref = getSharedPreferences("alarm",MODE_PRIVATE);
        SharedPreferences.Editor editor = spref.edit();
        editor.putString("state",state);
        editor.apply();
    }

    public void startSoundService()
    {

        Log.e("122222222233","qqqqqqqqqqqqqqqqqqqq");
       /* updateServiceState(2);

        SharedPreferences.Editor editor = spref.edit();
        editor.putInt("pos",0);
        editor.apply();*/

       if (isServiceRunning(new TimerService().getClass()))
       {
           stopSoundService();

       }
        intent = new Intent(HomeActivity.this,TimerService.class);
        startService(intent);




    }

    private void stopSoundService() {


            if (intent!=null)
            {
                stopService(intent);
            }





    }

    public void startRememberService()
    {

        Log.e("555","qqqqqqqqqqqqqqqqqqqq");
       /* updateServiceState(2);

        SharedPreferences.Editor editor = spref.edit();
        editor.putInt("pos",0);
        editor.apply();*/

        if (isServiceRunning(new TimerRememberService().getClass()))
        {
            stopRememberService();

        }
        intent2 = new Intent(HomeActivity.this,TimerRememberService.class);
        startService(intent2);




    }

    private void stopRememberService() {


        if (intent2!=null)
        {
            stopService(intent2);
        }





    }

    private void updateServiceState(int state)
    {
        Log.e("emmm",state+"_");
        SharedPreferences.Editor editor = spref.edit();
        editor.putInt("stop",state);
        editor.apply();
    }

    private boolean isServiceRunning(Class<?> serviceClass)
    {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceInfo:activityManager.getRunningServices(Integer.MAX_VALUE))
        {
            if (serviceInfo.service.getClassName().equals(serviceClass.getName()))
            {
                return true;
            }
        }
        return false;
    }



}
