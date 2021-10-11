package com.example.cleanapplication.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.cleanapplication.BaseAPI;
import com.example.cleanapplication.R;
import com.example.cleanapplication.SharedPrefsData;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraMoveCanceledListener {
    private GoogleMap mMap;
    double currentLatitude;
    double currentLongitude;
    public FusedLocationProviderClient client;
    private GpsTracker gpsTracker;
    int currentLoc;
    PlacesClient placesClient;
    double userLat;
    double userLong;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private GoogleMap map;
    Marker mCenterMarker, currentMarker;
    LatLng latLngMovingAddress;

    AutocompleteSupportFragment autocompleteFragment;

    BaseAPI baseAPI = new BaseAPI();
    SharedPrefsData sharedPrefsData;
    String loginUSerToken;
    AsyncHttpClient httpClient;
    String address_URL;
    RequestParams params;
    String userAddressApi ;
    Double latApi, longApi;
    String TAG = "LocationActivityLocation" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        getSupportActionBar().setTitle("Location");

        sharedPrefsData = new SharedPrefsData(LocationActivity.this);
        httpClient = new AsyncHttpClient();

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        client = LocationServices.getFusedLocationProviderClient(this);
        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLatitude = location.getLatitude();
                    currentLongitude = location.getLongitude();
                    latApi = location.getLatitude();
                    longApi = location.getLongitude();

                    Log.i("FusedLocation", "Lat   " + currentLatitude + "  Long" + currentLongitude);
                } else {
                    Toast.makeText(LocationActivity.this, "Location Object null", Toast.LENGTH_SHORT).show();
                }
            }
        });

        String apiKey = getString(R.string.api_key);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        // Create a new Places client instance.
        placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
      autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setTypeFilter(TypeFilter.CITIES);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.PHOTO_METADATAS));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Toast.makeText(getApplicationContext(), place.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Toast.makeText(getApplicationContext(), status.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        // I suppressed the missing-permission warning because this wouldn't be executed in my
        // case without location services being enabled
        @SuppressLint("MissingPermission") android.location.Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        userLat = lastKnownLocation.getLatitude();
        userLong = lastKnownLocation.getLongitude();
        latApi = lastKnownLocation.getLatitude();
        longApi = lastKnownLocation.getLongitude();
        Log.i("userLatLong", "Lat  " + userLat + "  Long  " + userLong);

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

       /* client.getCurrentLocation(currentLoc, new CancellationToken() {
            @Override
            public boolean isCancellationRequested() {
                Log.i("FusedGetCurrentLoc" , "isCancellationRequested");
                return false;
            }

            @NonNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                Log.i("FusedGetCurrentLoc" , "onCanceledRequested");
                return null;
            }
        }).addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                if(task == null){
                    Toast.makeText(LocationActivity.this, "task is null", Toast.LENGTH_SHORT).show();
                    Log.i("FusedGetCurrentLoc" , "task is null");
                }else {
                    Toast.makeText(gpsTracker, "task not null", Toast.LENGTH_SHORT).show();
                    Log.i("FusedGetCurrentLoc" , "task not null");
                }

            }

        });*/

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.map = googleMap;

        Log.i("LocationOnMapReadyDev", userLat + ":::" + userLong);
        LatLng sydney = new LatLng(userLat, userLong);
        currentMarker = map.addMarker(new MarkerOptions().position(sydney).title("Marker in Current Location"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18));
        try {
            fetchAddress(userLat, userLong);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("onMapReadyMoveException", e.getMessage().toString());
        }


/*        map.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {

                if (mCenterMarker != null) {
                    mCenterMarker.remove();
                }
                CameraPosition test = map.getCameraPosition();
                latLngMovingAddress = new LatLng(test.target.latitude, test.target.longitude);
                //Assign mCenterMarker reference:
                mCenterMarker = map.addMarker(new MarkerOptions().position(map.getCameraPosition().target).anchor(0.5f, .05f).title("Test"));
//                mMap.getUiSettings().setZoomControlsEnabled(false);
//                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngMovingAddress, 18));
                Log.d("TAGLocation", "Map Coordinate: " + String.valueOf(test));

            }
        });*/

/*     map.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                if (mCenterMarker != null) {
                    mCenterMarker.remove();
                }
                CameraPosition test = map.getCameraPosition();
                latLngMovingAddress = new LatLng(test.target.latitude, test.target.longitude);
                //Assign mCenterMarker reference:
                mCenterMarker = map.addMarker(new MarkerOptions().position(map.getCameraPosition().target).anchor(0.5f, .05f).title("Test"));
//                mMap.getUiSettings().setZoomControlsEnabled(false);
//                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngMovingAddress, 18));
                Log.d("TAGLocation", "Map Coordinate: " + String.valueOf(test));
            }
        });

        if (latLngMovingAddress != null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngMovingAddress, 18));
            map.getUiSettings().setZoomControlsEnabled(true);
        }*/

        map.setOnCameraMoveListener(this);
        map.setOnCameraIdleListener(this);
        map.setOnCameraMoveStartedListener(this);
        map.setOnCameraMoveCanceledListener(this);

    }

    public void getLocation(View view) {
     /*   gpsTracker = new GpsTracker(LocationActivity.this);
        if (gpsTracker.canGetLocation()) {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();

            currentLongitude = gpsTracker.getLongitude();
            currentLatitude = gpsTracker.getLatitude();

            Log.i("getLocation", "LAT  " + latitude + "Long  " + longitude);

        } else {
            gpsTracker.showSettingsAlert();
        }*/

        params = new RequestParams();
        address_URL = baseAPI.BASE_URL;
        address_URL = address_URL + "addresses";
        sharedPrefsData.getTokenFromPreference();
        loginUSerToken = sharedPrefsData.loginUserToken;

        params.put("text",userAddressApi);
        params.put("latitude",latApi);
        params.put("longitude",longApi);

        Log.i(TAG, "lat: " + latApi + " long: " + longApi +" Address " + userAddressApi);
        Log.i(TAG,    "Token:  " + loginUSerToken);

        httpClient.addHeader("Accept", "application/json");
        httpClient.addHeader("Authorization", "Bearer " + loginUSerToken);
        httpClient.post(address_URL,params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i(TAG , "OnSuccessLocation" + response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.i(TAG , "OnFailureLocation" + errorResponse);
            }
        });

    }

    @Override
    public void onCameraMove() {
        Log.i("MapListenersTest", "The camera is moving.");
        if (mCenterMarker != null && currentMarker != null) {
            mCenterMarker.remove();
            currentMarker.remove();
        }
        CameraPosition test = map.getCameraPosition();
        latLngMovingAddress = new LatLng(test.target.latitude, test.target.longitude);
        mCenterMarker = map.addMarker(new MarkerOptions().position(map.getCameraPosition().target).anchor(0.5f, .05f).title("Test"));
        try {
            latApi = test.target.latitude;
            longApi = test.target.longitude;
            fetchAddress(test.target.latitude, test.target.longitude);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("CameraMoveException", e.getMessage().toString());
        }
    }

    @Override
    public void onCameraIdle() {
//        Toast.makeText(this, "The camera has stopped moving.", Toast.LENGTH_SHORT).show();
        Log.i("MapListenersTest", "The camera has stopped moving.");
        if (mCenterMarker != null) {
//            map.getUiSettings().setZoomControlsEnabled(false);
//            map.getUiSettings().setZoomGesturesEnabled(false);
        }
//        mCenterMarker = map.addMarker(new MarkerOptions().position(latLngMovingAddress).anchor(0.5f,.05f).title("Testing"));
    }

    @Override
    public void onCameraMoveStarted(int reason) {
        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            Log.i("MapListenersTest", "The user gestured on the map.");
        } else if (reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_API_ANIMATION) {
            Log.i("MapListenersTest", "The user tapped something on the map.");
        } else if (reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_DEVELOPER_ANIMATION) {

            Log.i("MapListenersTest", "The app moved the camera.");
        }
    }

    @Override
    public void onCameraMoveCanceled() {
        Log.i("MapListenersTest", "Camera movement canceled.");
    }


    void fetchAddress(double latitude, double longitude) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();

        userAddressApi =  addresses.get(0).getAddressLine(0);

        autocompleteFragment.setText(userAddressApi);

        Log.i("Addressssss", address + " : " + city + " : " + postalCode);
    }

}