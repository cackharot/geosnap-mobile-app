package com.example.cackharot.geosnap.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.cackharot.geosnap.HomeActivity;
import com.example.cackharot.geosnap.R;
import com.example.cackharot.geosnap.model.Site;

public class ListSiteActivity extends ActionBarActivity implements ListSiteFragment.OnListSiteFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_site);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            ListSiteFragment fragment = new ListSiteFragment();
            fragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.list_container, fragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_site, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {
            //NavUtils.navigateUpFromSameTask(this);
            navigate(HomeActivity.class, null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSelectSite(Class<?> id, String site_id) {
        navigate(id, site_id);
    }

    private void navigate(Class<?> activityClass, String site_id) {
        // navigate to next activity
        // user is not logged in redirect him to Login Activity
        Context _context = getApplicationContext();
        Intent i = new Intent(_context, activityClass);

        if (site_id != null) {
            i.putExtra("site_id", site_id);
        }

        // Closing all the Activities from stack
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
        finish();
    }
}
