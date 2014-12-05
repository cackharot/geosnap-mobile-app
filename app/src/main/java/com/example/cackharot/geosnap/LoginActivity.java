package com.example.cackharot.geosnap;

import com.example.cackharot.geosnap.activities.ChooseDistributorActivity;
import com.example.cackharot.geosnap.activities.GPSTracker;
import com.example.cackharot.geosnap.contract.ILoginCallback;
import com.example.cackharot.geosnap.contract.IUserService;
import com.example.cackharot.geosnap.lib.UserSessionManager;
import com.example.cackharot.geosnap.model.User;
import com.example.cackharot.geosnap.services.IEntityDownloadCallback;
import com.example.cackharot.geosnap.services.UserService;
import com.example.cackharot.geosnap.util.SystemUiHider;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Collection;


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

        userService = new UserService(_context);
        setContentView(R.layout.activity_login);
        findViewById(R.id.btnLogin).setOnClickListener(doLogin);

        GPSTracker gpsTracker = new GPSTracker(this);

        if (!gpsTracker.canGetLocation()) {
            gpsTracker.showSettingsAlert();
            return;
        }

        if (session.isUserLoggedIn()) {
            navigate(ChooseDistributorActivity.class);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_login, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_login_settings:
                showSettingsDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    View.OnClickListener doLogin = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText txtUserName = (EditText) findViewById(R.id.txtUsername);
            EditText txtPassword = (EditText) findViewById(R.id.txtPassword);
            validateUser(txtUserName.getText().toString().trim(), txtPassword.getText().toString().trim());
        }
    };

    private void showSettingsDialog() {
        Context context = getApplicationContext();
        final LayoutInflater li = LayoutInflater.from(context);
        final View promptsView = li.inflate(R.layout.server_settings, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);

        final String serverAddress = session.getServerAddress();
        final int serverPort = session.getServerPort();

        final EditText txtServerAddress = ((EditText) promptsView.findViewById(R.id.txtServerAddress));
        final EditText txtServerPort = ((EditText) promptsView.findViewById(R.id.txtServerPort));
        txtServerAddress.setText(serverAddress);
        txtServerPort.setText(String.valueOf(serverPort));

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String serverAddress = txtServerAddress.getText().toString();
                                Integer serverPort = Integer.parseInt(txtServerPort.getText().toString());

                                session.setServerAddress(serverAddress);
                                session.setServerPort(serverPort);
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        alertDialogBuilder.create().show();
    }

    private void validateUser(final String email, final String password) {
        final ProgressDialog dialog = ProgressDialog.show(this,
                "Please wait ...",
                "Authenticating...", true);
        dialog.setCancelable(false);
        dialog.show();
        userService.ValidateUser(email, password, new ILoginCallback() {
            @Override
            public void onValidate(boolean isValid) {
                if (isValid) {
                    userService.GetUserByEmail(email, new IEntityDownloadCallback<User>() {
                        @Override
                        public void doAfterGetAll(Collection<User> results) {

                        }

                        @Override
                        public void doAfterCreate(User entity) {

                        }

                        @Override
                        public void doAfterGet(User item) {
                            if (item != null) {
                                session.createUserLoginSession(item);
                                navigate(ChooseDistributorActivity.class);
                            } else {
                                Toast.makeText(getApplicationContext(), "Username/Password is incorrect", Toast.LENGTH_LONG).show();
                                session.clear();
                            }
                            dialog.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Username/Password is incorrect", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    session.clear();
                }
            }
        });
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
        finish();
    }
}
