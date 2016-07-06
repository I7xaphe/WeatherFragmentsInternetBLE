package com.i7xaphe.weatherfragments;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FloatingWindow extends Service {

    WindowManager wm;
    LinearLayout ll;

    SharedPreferences sheredpreferences;
    SharedPreferences.Editor editor;
    EditText etURL;
    Button  bSaveURL;
    double x;
    double y;
    double pressedX;
    double pressedY;
    LayoutParams llParameters;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        sheredpreferences = getSharedPreferences("com.i7xaphe.weatherfragments", Context.MODE_PRIVATE);
        editor = sheredpreferences.edit();
        //===============================OKNO z BACKGROUND============================================
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        ll = new LinearLayout(this);
        ll.setBackgroundResource(R.drawable.buttonshape);
        ll.setOrientation(LinearLayout.VERTICAL);
        llParameters = new LayoutParams(600, 350, LayoutParams.TYPE_PHONE,
                LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        llParameters.gravity = Gravity.CENTER | Gravity.CENTER;
        llParameters.x = 0;
        llParameters.y = 0;

        LayoutInflater li = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        ll = (LinearLayout) li.inflate(R.layout.url_floating_window, null);

        wm.addView(ll, llParameters);
        //================================Edit Text URL=============================================

        etURL= new EditText(getApplicationContext());
        etURL.setText(sheredpreferences.getString("Last_URL","http://192.168.4.1"));
        ViewGroup.LayoutParams etParameters = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        etURL.setLayoutParams(etParameters);
        etURL.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        etURL.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etURL.setInputType(
                InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);

        ll.addView(etURL,etParameters);

        //================================ Button ==================================================
        bSaveURL = new Button(this);
        bSaveURL.setText("SAVE");
        //bSaveURL=(Button) c

        ViewGroup.LayoutParams btnParameters = new ViewGroup.LayoutParams(100,100);
        bSaveURL.setLayoutParams(btnParameters);
        ll.addView(bSaveURL,etParameters);

        //=================================PRZESUWANIE==============================================
        ll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        x = llParameters.x;
                        y = llParameters.y;

                        pressedX = event.getRawX();
                        pressedY = event.getRawY();

                        break;

                    case MotionEvent.ACTION_MOVE:
                        llParameters.x = (int) (x + (event.getRawX() - pressedX));
                        llParameters.y = (int) (y + (event.getRawY() - pressedY));

                        wm.updateViewLayout(ll, llParameters);

                    default:
                        break;
                }

                return false;
            }
        });
        //==========================================================================================
        bSaveURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEndExit();
            }
        });
        etURL.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
             //   Toast.makeText(getApplicationContext(),"ai"+actionId,Toast.LENGTH_SHORT).show();
                if (actionId == KeyEvent.KEYCODE_ENDCALL) {
                    saveEndExit();
                }
                return false;
            }
        });
        etURL.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
               // Toast.makeText(getApplicationContext(),"kc"+keyCode,Toast.LENGTH_SHORT).show();
                if (keyCode == KeyEvent.KEYCODE_BACK ){
                    wm.removeView(ll);
                    stopSelf();

                }
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }
    private void saveEndExit() {
        String newURL;
        newURL=etURL.getText().toString();
        editor.putString("Last_URL", newURL);
        editor.commit();
        SensorFragment.url=newURL;
        wm.removeView(ll);
        stopSelf();
    }


}