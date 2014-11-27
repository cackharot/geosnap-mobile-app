package com.example.cackharot.geosnap.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.cackharot.geosnap.HomeActivity;
import com.example.cackharot.geosnap.R;
import com.example.cackharot.geosnap.lib.SpinnerAdapter;
import com.example.cackharot.geosnap.lib.UserSessionManager;
import com.example.cackharot.geosnap.model.BaseModel;
import com.example.cackharot.geosnap.model.Dealer;
import com.example.cackharot.geosnap.model.Distributor;
import com.example.cackharot.geosnap.model.District;
import com.example.cackharot.geosnap.services.DistributorService;
import com.example.cackharot.geosnap.services.IEntityDownloadCallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class ChooseDistributorActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spDistributor;
    private Spinner spDistrict;
    private Spinner spConsumptionCenter;
    private Spinner spDealer;
    private final HashMap<String, List<District>> mapDistributorsDistrict = new HashMap<String, List<District>>();
    private final HashMap<String, List<String>> mapDistrictCenters = new HashMap<String, List<String>>();
    private final HashMap<String, List<String>> mapCenterDealers = new HashMap<String, List<String>>();
    private UserSessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_distributor);

        session = new UserSessionManager(getApplicationContext());

//        if (session.isUserLoggedIn() && session.getDefaultsValues()[0] != null) {
//            navigate(HomeActivity.class);
//        }

        spDistributor = (Spinner) findViewById(R.id.spinnerDistributor);
        spDistrict = (Spinner) findViewById(R.id.spinnerDistrict);
        spConsumptionCenter = (Spinner) findViewById(R.id.spinnerConsumptionCenter);
        spDealer = (Spinner) findViewById(R.id.spinnerDealer);

        spDistributor.setOnItemSelectedListener(this);
        spDistrict.setOnItemSelectedListener(this);
        spConsumptionCenter.setOnItemSelectedListener(this);
        spDealer.setOnItemSelectedListener(this);

        DistributorService distributorService = new DistributorService(getApplication());

        final ProgressDialog dialog = ProgressDialog.show(this,
                "Please wait ...",
                "Loading data from server...", true);
        dialog.setCancelable(false);
        dialog.show();
        distributorService.GetAll(new IEntityDownloadCallback<Distributor>() {
            @Override
            public void doAfterGetAll(Collection<Distributor> results) {
                if (results == null) {
                    results = new ArrayList<Distributor>();
                }

                updateModels(results);

                //ArrayList<String> distributorNames = new ArrayList<String>(mapDistributorsDistrict.keySet());
                SpinnerAdapter adapter = new SpinnerAdapter(getApplication(), R.layout.my_spinner_item, new ArrayList<BaseModel>(results));
                spDistributor.setAdapter(adapter);
                dialog.dismiss();
            }

            @Override
            public void doAfterCreate(Distributor entity) {

            }

            @Override
            public void doAfterGet(Distributor item) {

            }
        });
    }

    private void updateModels(Collection<Distributor> results) {
        ArrayList<Distributor> distributorCollection = new ArrayList<Distributor>(results);

        for (Distributor item : distributorCollection) {
            if (item.districts == null)
                item.districts = new ArrayList<District>();

            mapDistributorsDistrict.put(item.name, item.districts);

            for (District d : item.districts) {
                if (d.centers == null)
                    d.centers = new ArrayList<String>();
                String districtKey = item.name + d.name;
                mapDistrictCenters.put(districtKey, d.centers);

                if (d.dealers == null)
                    d.dealers = new ArrayList<Dealer>();

                for (String center : d.centers) {
                    String centerKey = item.name + d.name + center;
                    List<String> dealers = new ArrayList<String>();
                    for (Dealer i : d.dealers) {
                        if (i.center.equals(center)) {
                            dealers.add(i.name);
                        }
                    }
                    mapCenterDealers.put(centerKey, dealers);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void navigate(Class<?> activityClass) {
        // navigate to next activity
        // user is not logged in redirect him to Login Activity
        Context _context = getApplicationContext();
        Intent i = new Intent(_context, activityClass);

        // Closing all the Activities from stack
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spinnerDistributor:
                String distributorName = ((Distributor) adapterView.getItemAtPosition(i)).name;
                List<District> districts = mapDistributorsDistrict.get(distributorName);
                SpinnerAdapter adapter = new SpinnerAdapter(getApplication(), R.layout.my_spinner_item, new ArrayList<BaseModel>(districts));
                spDistrict.setAdapter(adapter);
                break;
            case R.id.spinnerDistrict:
                String districtName = ((District) adapterView.getItemAtPosition(i)).name;
                String dName = ((Distributor) spDistributor.getSelectedItem()).name;
                List<String> centers = mapDistrictCenters.get(dName + districtName);
                ArrayAdapter<String> centerAdapter = new ArrayAdapter<String>(getApplication(),
                        R.layout.my_spinner_item, centers);
                spConsumptionCenter.setAdapter(centerAdapter);
                break;
            case R.id.spinnerConsumptionCenter:
                String centerName = (String) adapterView.getItemAtPosition(i);
                String drName = ((Distributor) spDistributor.getSelectedItem()).name;
                String dsName = ((District) spDistrict.getSelectedItem()).name;
                List<String> dealers = mapCenterDealers.get(drName + dsName + centerName);
                ArrayAdapter<String> dealerAdapter = new ArrayAdapter<String>(getApplication(),
                        R.layout.my_spinner_item, dealers);
                spDealer.setAdapter(dealerAdapter);
                break;
        }
    }

    public void doContinue(View view) {
        String distributor = ((Distributor) spDistributor.getSelectedItem()).name;
        String district = ((District) spDistrict.getSelectedItem()).name;
        String center = (String) spConsumptionCenter.getSelectedItem();
        String dealer = (String) spDealer.getSelectedItem();

        session.setDefaultsValues(distributor, district, center, dealer);

        navigate(HomeActivity.class);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
