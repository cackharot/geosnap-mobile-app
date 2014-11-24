package com.example.cackharot.geosnap.activities;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.AlertDialog;

import com.example.cackharot.geosnap.HomeActivity;
import com.example.cackharot.geosnap.R;
import com.example.cackharot.geosnap.lib.ConfigurationHelper;
import com.example.cackharot.geosnap.model.Brand;
import com.example.cackharot.geosnap.model.Site;
import com.example.cackharot.geosnap.services.ISiteDownloadCallback;
import com.example.cackharot.geosnap.services.SiteService;

import org.bson.types.ObjectId;

import java.net.MalformedURLException;
import java.util.Collection;

public class ManageSiteDetailsFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ManageSiteDetailsFragment";
    private SiteService siteService;
    private View view;
    private OnManageSiteFragmentInteractionListener mListener;
    private Site Model = new Site();

    public ManageSiteDetailsFragment() {
        siteService = new SiteService(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_site, container, false);
        Button btn = (Button) view.findViewById(R.id.btnSaveSite);
        btn.setOnClickListener(this);
        btn = (Button) view.findViewById(R.id.btnCapture);
        btn.setOnClickListener(this);

        Bundle extras = getArguments();
        if (extras != null) {
            String site_id = extras.getString("site_id");
            if (site_id != null && !site_id.isEmpty()) {
                this.siteService.GetById(site_id, new ISiteDownloadCallback() {
                    @Override
                    public void doAfterGetAll(Collection<Site> results) {

                    }

                    @Override
                    public void doAfterCreate(Site entity) {

                    }

                    @Override
                    public void doAfterGet(Site item) {
                        updateViewInputValues(item);
                    }
                });
            }
        }

        return view;
    }

    private void updateViewInputValues(Site item) {
        if (item == null) return;
        this.Model = item;
        getViewById(R.id.txtSiteName).setText(item.name);
        getViewById(R.id.txtAddress).setText(item.address);
        getViewById(R.id.txtSqFt).setText(String.valueOf(item.square_feet));
        getViewById(R.id.txtConsumptionExcepted).setText(String.valueOf(item.consumption));
        getViewById(R.id.txtRebarsConsidered).setText(item.rebars_considered);

        if (item.used_brands != null && !item.used_brands.isEmpty()) {
            for (Brand b : item.used_brands) {
                getViewById(R.id.txtBrand1Name).setText(String.valueOf(b.name));
                getViewById(R.id.txtBrand1Consumption).setText(String.valueOf(b.consumption));
                break;
            }
        }
    }

    public void doSave() {
        Site entity = this.Model;
        entity.name = getTextValue(R.id.txtSiteName);

        if(entity.name == null || entity.name.isEmpty())
            return;

        entity.district_id = new ObjectId("546ca00fb41d060ec8dcdf9f");
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

        SiteService siteService = new SiteService(getActivity());
        siteService.Create(entity, new ISiteDownloadCallback() {
            @Override
            public void doAfterGetAll(Collection<Site> results) {

            }

            @Override
            public void doAfterCreate(Site entity) {
                Log.e(TAG, "Saved Successfully!");
                Toast.makeText(getActivity(), "Saved Successfully!", Toast.LENGTH_SHORT).show();
                mListener.doNavigate(HomeActivity.class);
            }

            @Override
            public void doAfterGet(Site item) {

            }
        });
    }

    private String getTextValue(int txtId) {
        return getViewById(txtId).getText().toString();
    }

    private EditText getViewById(int txtId) {
        return ((EditText) findViewById(txtId));
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
            case R.id.btnCapture:
                doCapture();
                break;
        }
    }

    private void doCapture() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, ConfigurationHelper.CAMERA_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConfigurationHelper.CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ImageView imageView = ((ImageView) findViewById(R.id.imgSiteTmpImage));
            imageView.setImageBitmap(photo);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnManageSiteFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnManageSiteFragmentInteractionListener {
        public void doNavigate(Class<?> id);
    }
}