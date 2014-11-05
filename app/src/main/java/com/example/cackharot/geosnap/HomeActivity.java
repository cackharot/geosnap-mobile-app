package com.example.cackharot.geosnap;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.cackharot.geosnap.activities.NewSiteActivity;
import com.example.cackharot.geosnap.lib.UserSessionManager;


public class HomeActivity extends ActionBarActivity {

    private Context _context;
    private UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _context = getApplicationContext();
        session = new UserSessionManager(_context);

        if(!session.isUserLoggedIn()){
            navigate(LoginActivity.class);
        }

        setContentView(R.layout.activity_home);
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

    public void doNavigation(View target){
        switch (target.getId()){
            case R.id.btnExit:
                onBackPressed();
                break;
            case R.id.btnNewSite:
                navigate(NewSiteActivity.class);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void navigate(Class<?> activityClass) {
        // navigate to next activity
        // user is not logged in redirect him to Login Activity
        Intent i = new Intent(_context, activityClass);

        // Closing all the Activities from stack
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }
}
