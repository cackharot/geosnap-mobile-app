package com.example.cackharot.geosnap.activities;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.cackharot.geosnap.HomeActivity;
import com.example.cackharot.geosnap.R;
import com.example.cackharot.geosnap.lib.ConfigurationHelper;
import com.example.cackharot.geosnap.lib.DownloadImageTask;
import com.example.cackharot.geosnap.lib.Helpers;
import com.example.cackharot.geosnap.lib.UploadTask;
import com.example.cackharot.geosnap.model.Brand;
import com.example.cackharot.geosnap.model.Site;
import com.example.cackharot.geosnap.services.ISiteDownloadCallback;
import com.example.cackharot.geosnap.services.SiteService;

import org.bson.types.ObjectId;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class ManageSiteDetailsFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ManageSiteDetailsFragment";
    private OnManageSiteFragmentInteractionListener mListener;
    private Site Model = new Site();
    private ArrayList<Bitmap> unSavedImages = new ArrayList<Bitmap>();
    private View view;

    public ManageSiteDetailsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_new_site, container, false);
        Button btn = (Button) view.findViewById(R.id.btnSaveSite);
        btn.setOnClickListener(this);
        btn = (Button) view.findViewById(R.id.btnCapture);
        btn.setOnClickListener(this);
        unSavedImages.clear();

        Bundle extras = getArguments();
        if (extras != null) {
            String site_id = extras.getString("site_id");
            if (site_id != null && !site_id.isEmpty()) {
                final ProgressDialog dialog = ProgressDialog.show(getActivity(),
                        "Please wait ...",
                        "Loading data from server...", true);
                dialog.setCancelable(false);
                dialog.show();
                SiteService siteService = new SiteService(getActivity());
                siteService.GetById(site_id, new ISiteDownloadCallback() {
                    @Override
                    public void doAfterGetAll(Collection<Site> results) {

                    }

                    @Override
                    public void doAfterCreate(Site entity) {

                    }

                    @Override
                    public void doAfterGet(Site item) {
                        updateViewInputValues(item);
                        dialog.dismiss();
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

        if (item.photos != null && !item.photos.isEmpty()) {
            for (String name : item.photos) {
                new DownloadImageTask(createImageCtrl(name))
                        .execute(ConfigurationHelper.SiteImageUrl + name);
            }
        }
    }

    private ImageView createImageCtrl(String name) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.layoutSitePhotos);
        ImageView img = new ImageView(getActivity());
        img.setTag(R.id.imgSitePhotoTagKey, name);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(110, 100);
        img.setLayoutParams(layoutParams);
        img.setRight(10);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        layout.addView(img);
        return img;
    }

    public void doSave() {
        if (!loadEntityValues()) return;

        final ProgressDialog dialog = ProgressDialog.show(getActivity(),
                "Please wait ...",
                "Saving data in the server...", true);
        dialog.setCancelable(false);
        dialog.show();

        ISiteDownloadCallback callback = new ISiteDownloadCallback() {
            @Override
            public void doAfterGetAll(Collection<Site> results) {

            }

            @Override
            public void doAfterCreate(Site entity) {
                dialog.dismiss();
                Toast.makeText(getActivity(), "Saved Successfully!", Toast.LENGTH_SHORT).show();
                mListener.doNavigate(HomeActivity.class);
            }

            @Override
            public void doAfterGet(Site item) {

            }
        };

        if (unSavedImages.isEmpty()) {
            doSaveSiteDetails(callback);
        } else {
            try {
                sendPhoto(unSavedImages, callback);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean loadEntityValues() {
        Site entity = this.Model;
        entity.name = getTextValue(R.id.txtSiteName);

        if (entity.name == null || entity.name.isEmpty())
            return false;

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
        return true;
    }

    public void doSaveSiteDetails(ISiteDownloadCallback callback) {
        SiteService siteService = new SiteService(getActivity());
        siteService.Create(this.Model, callback);
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
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, ConfigurationHelper.CAMERA_REQUEST);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        String storageDir = Environment.getExternalStorageDirectory() + File.separator + "geosnap";
        File dir = new File(storageDir);
        if (!dir.exists())
            dir.mkdir();
        File image = new File(storageDir + File.separator + imageFileName + ".jpg");
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConfigurationHelper.CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            String name = saveToFile(photo);
            unSavedImages.add(photo);
            ImageView img = createImageCtrl(name);
            img.setImageBitmap(photo);
        }
    }

    private String saveToFile(Bitmap photo) {
        if (!Helpers.isExternalStorageWritable())
            return "tempfile.jpg";
        try {
            File file = createImageFile();
            file.createNewFile();
            OutputStream imgFile = new FileOutputStream(file);
            photo.compress(Bitmap.CompressFormat.JPEG, 100, imgFile);
            imgFile.close();
            return file.getName();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "tempfile.jpg";
    }

    private void sendPhoto(ArrayList<Bitmap> bitmap, final ISiteDownloadCallback callback) throws Exception {
        new UploadTask(ConfigurationHelper.SiteImageUploadUrl, new UploadTask.IUploadComplete() {
            @Override
            public void complete(List<String> lst) {
                if (lst != null && !lst.isEmpty()) {
                    if (Model.photos == null)
                        Model.photos = new ArrayList<String>();
                    Model.photos.addAll(lst);
                }
                doSaveSiteDetails(callback);
            }
        }).execute(bitmap);
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