package com.i7xaphe.weatherfragments;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.File;

/**
 * Created by Kamil on 2016-05-15.
 */
public class NavigationDialog extends DialogFragment implements View.OnClickListener{


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.navigation_fragment, null);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.b_set_url){


        }
    }
}