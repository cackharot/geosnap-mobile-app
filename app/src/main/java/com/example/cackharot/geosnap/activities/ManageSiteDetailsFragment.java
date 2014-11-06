package com.example.cackharot.geosnap.activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.cackharot.geosnap.R;

public class ManageSiteDetailsFragment extends Fragment implements View.OnClickListener {

    private View view;

    public ManageSiteDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_site, container, false);
        Button btn = (Button) view.findViewById(R.id.btnSaveSite);
        btn.setOnClickListener(this);
        return view;
    }

    public void doSave(){
        EditText txtSiteName = (EditText) findViewById(R.id.txtSiteName);
        EditText txtSqFt = (EditText) findViewById(R.id.txtSqFt);
    }

    private android.view.View findViewById(int controlId) {
        return view.findViewById(controlId);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSaveSite:
                doSave();
                break;
        }
    }
}