package com.i7xaphe.weatherfragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Kamil on 2016-07-05.
 */
public class UrlDialog extends DialogFragment implements View.OnClickListener{

    SharedPreferences sheredpreferences;
    SharedPreferences.Editor editor;
    EditText etURL;
    Button bsetUrl;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.url_dialog, null);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        sheredpreferences = getActivity().getSharedPreferences("com.i7xaphe.weatherfragments", Context.MODE_PRIVATE);
        editor = sheredpreferences.edit();
        etURL =(EditText) view.findViewById(R.id.et_url);
        etURL.setText(sheredpreferences.getString("Last_URL","http://192.168.8.99"));
        bsetUrl = (Button) view.findViewById(R.id.b_set_url);
        bsetUrl.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
       if(v.getId()==R.id.b_set_url){

            String newURL;
            newURL=etURL.getText().toString();
            editor.putString("Last_URL", newURL);
            editor.commit();
            dismiss();

        }
    }
}
