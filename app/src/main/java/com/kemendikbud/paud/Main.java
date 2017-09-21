package com.kemendikbud.paud;

import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.kemendikbud.paud.model.User;
import com.kemendikbud.paud.util.SessionManager;
import com.kemendikbud.paud.view.fragment.CategoryFragment;
import com.kemendikbud.paud.view.fragment.HomeFragment;
import com.kemendikbud.paud.view.fragment.MapsFragment;
import com.kemendikbud.paud.view.fragment.ProfileFragment;
import com.medialablk.easytoast.EasyToast;


import butterknife.BindView;
import butterknife.ButterKnife;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.kemendikbud.paud.util.ViewUtils.showMessageOKCancel;
import static com.kemendikbud.paud.util.ViewUtils.showSnackbar;
import static com.kemendikbud.paud.view.activity.SignInActivity.USER_SESSION;

public class Main extends AppCompatActivity implements LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = Main.class.getSimpleName();
    public static String userName = "";
    public static String id_sekolah;

    final int REQUEST_CHECK_SETTINGS = 122;
    final int REQUEST_LOCATION = 125;

    @BindView(R.id.navigation)
    BottomNavigation navigationView;

    private FragmentManager fm;

    private BottomNavigation.OnMenuItemSelectionListener mOnNavigationItemSelectedListener
            = new BottomNavigation.OnMenuItemSelectionListener() {
        @Override
        public void onMenuItemSelect(@IdRes int i, int i1, boolean b) {
            fm = getFragmentManager();
            if (i == R.id.navigation_home){
                fm.beginTransaction().replace(R.id.content, new HomeFragment()).commit();
            } else if (i == R.id.navigation_category){
                fm.beginTransaction().replace(R.id.content, new CategoryFragment()).commit();
            } else if (i == R.id.navigation_profile){
                fm.beginTransaction().replace(R.id.content, new ProfileFragment()).commit();
            }
        }

        @Override
        public void onMenuItemReselect(@IdRes int i, int i1, boolean b) {

        }
    };

    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;

    public static Double longitude, latitude;

    public static void start(Context context) {
        Intent intent = new Intent(context, Main.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ButterKnife.bind(this);

        isGooglePlayServicesAvailable();

        if(!isLocationEnabled()) {
            showAlert();
        }

        initGoogleMapApi();

        initComponents();
    }

    private void initComponents(){
        User user = SessionManager.getUser(this, USER_SESSION);
        userName = user.getData().getName();
        id_sekolah = user.getData().getIdSekolah();

        navigationView.setOnMenuItemClickListener(mOnNavigationItemSelectedListener);

        fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.content, new HomeFragment()).commit();
    }

    private void initGoogleMapApi(){
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Main.this,
                    new String[]{ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            return;
        }
        Log.d(TAG, "onConnected");

        Location ll = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        Log.d(TAG, "LastLocation: " + (ll == null ? "No LastLocation" : ll.toString()));

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted){
                        EasyToast.success(Main.this, "Permission was granted");

                        try{
                            LocationServices.FusedLocationApi.requestLocationUpdates(
                                    googleApiClient, locationRequest, this);
                        } catch (SecurityException e) {
                            EasyToast.error(Main.this, "SecurityException:\n" + e.toString());
                        }
                    } else {
                        EasyToast.error(Main.this, "Permission denied");

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access ",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION},
                                                            MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }

                }
                break;
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(Main.this, "onConnectionFailed: \n" + connectionResult.toString(),
                Toast.LENGTH_LONG).show();
        Log.d(TAG, connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            updateUI(location);
        }
    }

    private void updateUI(Location loc) {
        Log.d(TAG, "updateUI");
        latitude = loc.getLatitude();
        longitude = loc.getLongitude();
        SessionManager.save("latitude", String.valueOf(latitude));
        SessionManager.save("longitude", String.valueOf(longitude));
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private boolean isGooglePlayServicesAvailable() {
        final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.d(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        Log.d(TAG, "This device is supported.");
        return true;
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        finish();
                    }
                });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
