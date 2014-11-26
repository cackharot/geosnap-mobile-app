package com.example.cackharot.geosnap;

import com.example.cackharot.geosnap.activities.ChooseDistributorActivity;
import com.example.cackharot.geosnap.contract.IUserService;
import com.example.cackharot.geosnap.lib.UserSessionManager;
import com.example.cackharot.geosnap.services.UserService;
import com.example.cackharot.geosnap.util.SystemUiHider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class LoginActivity extends Activity {

    private IUserService userService;
    private Context _context;
    private UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _context = getApplicationContext();
        session = new UserSessionManager(_context);

        if(session.isUserLoggedIn()){
            //navigate(HomeActivity.class);
            navigate(ChooseDistributorActivity.class);
        }

        userService = new UserService(getApplicationContext());

        setContentView(R.layout.activity_login);

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.btnLogin).setOnClickListener(doLogin);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnClickListener doLogin = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText txtUserName = (EditText) findViewById(R.id.txtUsername);
            EditText txtPassword = (EditText) findViewById(R.id.txtPassword);
            validateUser(txtUserName.getText().toString().trim(), txtPassword.getText().toString().trim());
        }
    };

    private void validateUser(String userName, String password) {
        if (userService.validateUser(userName, password)) {
            //store in session
            session.createUserLoginSession(userName, userName);
            navigate(ChooseDistributorActivity.class);
        } else {
            // show error dialog
            Toast.makeText(getApplicationContext(), "Username/Password is incorrect", Toast.LENGTH_LONG).show();
        }
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
