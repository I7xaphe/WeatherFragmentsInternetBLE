package com.i7xaphe.weatherfragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kamil on 2016-07-01.
 */
public class SensorFragment extends Fragment implements View.OnClickListener {
    TextView textViewTemp;
    TextView textViewPress;
    TextView textViewLight;
    TextView textViewHum;
    TextView message;
    static int SessionCounter = 0;
    static int light = 0;
    static int humidity = 0;
    static double temperature = 0;
    static double pressure = 0;
    String receiveData = "";
    static MyAsyncTask myAsyncTask;
    static Context context;
    Button bSave,bTimer;
    static Animation animHide;
    boolean CreatedOnce;
    SharedPreferences sheredpreferences;

    public static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyWeatherApp";
    public static String fullfilepath=path+"/weather.txt";
    static String url = "http://192.168.8.99";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sensor_fragment,container,false);
        context = getContext();

        sheredpreferences = this.getActivity().getSharedPreferences("com.i7xaphe.weatherfragments", Context.MODE_PRIVATE);
        url=sheredpreferences.getString("Last_URL","http://192.168.8.99");

        textViewTemp = (TextView) v.findViewById(R.id.textWiewTemperature);
        textViewPress = (TextView) v.findViewById(R.id.textWiewPressure);
        textViewLight = (TextView) v.findViewById(R.id.textWiewLight);
        textViewHum = (TextView) v.findViewById(R.id.textWiewHumidity);
        message = (TextView) v.findViewById(R.id.message);
        myAsyncTask = new MyAsyncTask();
        myAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        bSave = (Button) v.findViewById(R.id.bssss);
        bSave.setOnClickListener(this);
        bTimer = (Button) v.findViewById(R.id.bsss);
        bTimer.setOnClickListener(this);

        animHide = AnimationUtils.loadAnimation(context, R.anim.hide);

        RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.sensor_view_activity_id);
        rl.setOnTouchListener(new OnSwipeTouchListener(context) {

            @Override
            public void onSwipeTop() {
                gotoUrlDialog();
            }

            @Override
            public void onSwipeBottom() {
                getActivity().moveTaskToBack(true);
            }
        });

        Animation pulseHideLong, pulseHideDownLong;
        pulseHideLong = AnimationUtils.loadAnimation(context, R.anim.hide_pulse_after_some_sec);
        pulseHideDownLong = AnimationUtils.loadAnimation(context, R.anim.hide_down_pulse_after_some_sec);

        if (!CreatedOnce) {
            ImageView imageView = (ImageView) v.findViewById(R.id.pulse_image);
            imageView.startAnimation(pulseHideDownLong);
            TextView textView = (TextView) v.findViewById(R.id.pulse_text);
            textView.startAnimation(pulseHideLong);
            CreatedOnce = true;
        }

        return v;

    }

    public String getUrlContent(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection con = null;
            con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(500);
            con.setReadTimeout(500);
            con.setDoOutput(false);
            con.setDoInput(true);
            con.setRequestMethod("GET");
            InputStreamReader in = new InputStreamReader(con.getInputStream(), Charset.forName("ISO-8859-2"));
            BufferedReader r = new BufferedReader(in);                         //ISO-8859-2 odbierane z esp-01
            String s = "";
            s = r.readLine();
            r.close();
            return s;
        } catch (Exception e) {
            Log.d("getUrlContent", "Error: " + e.toString());
            receiveData = null;
        }
        return receiveData;

    }

    @Override
    public void onClick(View v) {
        v.startAnimation(animHide);
        switch(v.getId()){
            case R.id.bsss:
                Toast.makeText(context,"save",Toast.LENGTH_SHORT).show();;
                saveNewData();
                break;
            case R.id.bssss:
                Toast.makeText(context,"Timer",Toast.LENGTH_SHORT).show();;
                saveNewData();
                break;
        }
    }

    private class MyAsyncTask extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {


            while (true) {
                try {
                    receiveData = getUrlContent(url);
                    publishProgress(SessionCounter);
                    SessionCounter++;
                    Thread.sleep(1000);



                } catch (Exception e) {
                    Log.d("polaczenie", "Error: " + e.toString());
                    publishProgress(0);
                }

            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            char[] charTable = new char[25];
            String tempS = "";
            String presS = "";
            String humS = "";
            String lightS = "";
            try {
                    charTable = receiveData.toCharArray();
                    tempS = String.valueOf(charTable, 0, 5);
                    presS = String.valueOf(charTable, 6, 6);
                    humS = String.valueOf(charTable, 13, 3);
                    lightS = String.valueOf(charTable, 17, 5);
                    humidity = Integer.parseInt(humS);
                    textViewHum.setText(humidity + " %");

                    light = Integer.parseInt(lightS);
                    textViewLight.setText(light + " lx");

                    temperature = Double.parseDouble(tempS);
                    textViewTemp.setText(temperature + " °C");

                    pressure = Double.parseDouble(presS);
                    textViewPress.setText(pressure + " hPa");

                    message.setText("CONNECTED");
            } catch (Exception e) {
                    Log.d("conversion", "Error: " + e.toString() + "x" + receiveData + "x");
                     message.setText("NOT CONNECTED");
            }


        }
    }//==============================================================================================
    private void saveNewData() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss     yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());
        File file = new File(SensorFragment.path + "/weather.txt");
        String[] s = new String[1];
        s[0] = "TEMPERATURE = " + SensorFragment.temperature + " °C   HUMIDITY = " + SensorFragment.humidity + " %   LIGHT = " +
                SensorFragment.light + " lx   PRESSURE = " + SensorFragment.pressure + " hPa                   " +
                currentDateandTime + "";
        MyFileClass.AddNewDataToLoadedFile2(SensorFragment.fullfilepath, s[0]);
    }
    private void gotoUrlDialog() {

        FragmentManager fm = getFragmentManager();
        UrlDialog urldialog = new UrlDialog();
        urldialog.show(fm,"Mydialog");

    }
}
