package com.i7xaphe.weatherfragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MemoryFragment extends Fragment implements View.OnClickListener {
    static RecyclerView recyclerview;
    static MyAdapter Adapter;
    static int numberOfPosition = 0;
    static String[] s ;
    static Animation animHide;
    static Animation animHideDeep;
    Button bClearData,bRefreshData;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.memory_recycle_view_activity,container,false);
        context = getContext();

        File file = new File(SensorFragment.path + "/weather.txt");
        s= MyFileClass.LoadFile(file);
        numberOfPosition = s.length;

        recyclerview = (RecyclerView) v.findViewById(R.id.recylerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL,false)); // zmieni!1
        Adapter = new MyAdapter();
        recyclerview.setAdapter(Adapter);
        bClearData= (Button) v.findViewById(R.id.bClearData);
        bClearData.setOnClickListener(this);

        bRefreshData= (Button) v.findViewById(R.id.bRefreshData);
        bRefreshData.setOnClickListener(this);

        animHide = AnimationUtils.loadAnimation(context, R.anim.hide);
        animHideDeep = AnimationUtils.loadAnimation(context, R.anim.hidedeep);
        return v;

    }

    @Override
    public void onClick(View v) {
        v.startAnimation(animHide);
        File file = new File(SensorFragment.fullfilepath);
        switch(v.getId()){
            case R.id.bClearData:
                MyFileClass.ClearFile(file);
                s= MyFileClass.LoadFile(file);
                numberOfPosition =s.length;
                Toast.makeText(context,"clear",Toast.LENGTH_SHORT).show();
                break;

            case R.id.bRefreshData:
                s= MyFileClass.LoadFile(file);
                numberOfPosition = s.length;
                Toast.makeText(context,"refresh",Toast.LENGTH_SHORT).show();
                break;
        }
        refreshAdapter();

    }

    public static void refreshAdapter() {
        File file = new File(SensorFragment.fullfilepath);
        s= MyFileClass.LoadFile(file);
        numberOfPosition = s.length;
        recyclerview.setAdapter(Adapter);
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder{
        TextView SavedData;

        public MyViewHolder(View itemView) {
            super(itemView);

            SavedData = (TextView) itemView.findViewById(R.id.seved_data);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.memory_card_view,parent,false);
            MyViewHolder myViewHolder = new MyViewHolder(view);


            return myViewHolder;
        }
        @Override
        public void onBindViewHolder(MyViewHolder holder,final int position) {
            try {
                holder.SavedData.setText((numberOfPosition-position)+". "+s[numberOfPosition-1-position]);
            }catch (Exception e) {
                e.printStackTrace();
            }
            //====================usuwanieElementówZPamieci==============================
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    try {
                        view.startAnimation(animHideDeep);
                        //========mielenie danych radze nie wnikać============
                        List<String> stringList = new ArrayList<String>(Arrays.asList(s));
                        stringList.remove(numberOfPosition - 1 - position);
                        String[] stringArray = stringList.toArray(new String[numberOfPosition - 1]);
                        File file = new File(SensorFragment.path + "/weather.txt");
                        MyFileClass.ClearFile(file);
                        MyFileClass.SaveFile(file, stringArray);
                        s = MyFileClass.LoadFile(file);
                        numberOfPosition = stringArray.length;
                        //=====================================================
                        Thread thread1 = new Thread() {
                            @Override
                            public void run() {
                                try {
                                    /////////opuznienie odswieżania dla celu Animacji/////////////////
                                    sleep(400);
                                    Message msg = myHandler.obtainMessage();
                                    myHandler.sendMessageAtFrontOfQueue(msg);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        thread1.start();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return true;
                }
            });
        }
        Handler myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                MyAdapter Adapter = new MyAdapter();
                MemoryFragment.recyclerview.setAdapter(Adapter);
            }
        };//==============================================================================================
        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public int getItemCount() {
            return numberOfPosition;
        }
    }
}
