package com.example.cackharot.geosnap.activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cackharot.geosnap.R;
import com.example.cackharot.geosnap.model.Brand;
import com.example.cackharot.geosnap.model.Site;
import com.example.cackharot.geosnap.services.ISiteDownloadCallback;
import com.example.cackharot.geosnap.services.SiteService;

import org.bson.types.ObjectId;

import java.net.MalformedURLException;
import java.util.Collection;

public class ManageSiteDetailsFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ManageSiteDetailsFragment";
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

    public void doSave() {
        Site entity = new Site();
        entity.district_id = new ObjectId("547188cab41d06106c9e334a");
        entity.name = getTextValue(R.id.txtSiteName);
        entity.square_feet = Double.parseDouble(getTextValue(R.id.txtSqFt));
        entity.consumption = Double.parseDouble(getTextValue(R.id.txtConsumptionExcepted));
        entity.address = getTextValue(R.id.txtAddress);
        entity.rebars_considered = getTextValue(R.id.txtRebarsConsidered);

        double b1Consumption = Double.parseDouble(getTextValue(R.id.txtBrand1Consumption));
        String b1Name = getTextValue(R.id.txtBrand1Name);
        Brand brand1 = new Brand(b1Name, b1Consumption);
        entity.used_brands.add(brand1);

        entity.location.latitude = 11.063;
        entity.location.longitude = 54.023;

        try {
            SiteService siteService = new SiteService(getActivity());
            siteService.Create(entity, new ISiteDownloadCallback() {
                @Override
                public void doAfterGetAll(Collection<Site> results) {

                }

                @Override
                public void doAfterCreate(Site entity) {
                    Log.e(TAG, "Saved Successfully!");
                    Toast.makeText(getActivity(), "Saved Successfully!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG, e.getLocalizedMessage());
        }
    }

    private String getTextValue(int txtId) {
        return ((EditText) findViewById(txtId)).getText().toString();
    }

    private android.view.View findViewById(int controlId) {
        return view.findViewById(controlId);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSaveSite:
                doSave();
                break;
        }
    }
}