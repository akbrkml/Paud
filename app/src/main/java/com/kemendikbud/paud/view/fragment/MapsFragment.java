package com.kemendikbud.paud.view.fragment;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.kemendikbud.paud.R;
import com.kemendikbud.paud.util.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    @BindView(R.id.search)AutoCompleteTextView mSearch;
    @BindView(R.id.myLoc)ImageButton myLocButton;

    public MapsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        ButterKnife.bind(this, view);

        initComponents();

        return view;
    }

    private void initComponents(){
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MapFragment fragment = (MapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Double latitude = Double.parseDouble(SessionManager.grabString("latitude"));
        Double longitude = Double.parseDouble(SessionManager.grabString("longitude"));

        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12.0f));
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.setPadding(20, 0, 20, 10);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//        mMap.setMyLocationEnabled(true);
        myLocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            GPSrequest();
                        } else {
                            mMap.getMyLocation();
                            Toast.makeText(getActivity(), "Waiting for location...", Toast.LENGTH_LONG).show();
                        }
                        return false;
                    }
                });
            }
        });
    }

    public void GPSrequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient

//        PendingResult<LocationSettingsResult> result =
//                LocationServices.SettingsApi.checkLocationSettings(pickerHelper.mGoogleApiClient, builder.build());
//        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
//            @Override
//            public void onResult(LocationSettingsResult result) {
//                final Status status = result.getStatus();
//                final LocationSettingsStates state = result.getLocationSettingsStates();
//                switch (status.getStatusCode()) {
//                    case LocationSettingsStatusCodes.SUCCESS:
//                        // All location settings are satisfied. The client can initialize location
//                        // requests here.
//                        break;
//                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                        // Location settings are not satisfied. But could be fixed by showing the user
//                        // a dialog.
//                        try {
//                            // Show the dialog by calling startResolutionForResult(),
//                            // and check the result in onActivityResult().
//                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
//                        } catch (IntentSender.SendIntentException e) {
//                            // Ignore the error.
//                        }
//                        break;
//                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                        // Location settings are not satisfied. However, we have no way to fix the
//                        // settings so we won't show the dialog.
//                        break;
//                }
//            }
//        });
    }
}