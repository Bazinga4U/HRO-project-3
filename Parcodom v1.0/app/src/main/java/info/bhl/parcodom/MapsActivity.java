package info.bhl.parcodom;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.sql.*;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import info.bhl.parcodom.POJO.Example;
import info.bhl.parcodom.R;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    //Variables and values
    DBHandler db = new DBHandler(this);
    public static GoogleMap mMap;
    static double dblat;
    static double dblon;
    static String dbname;
    double latitude;
    double longitude;
    private int PROXIMITY_RADIUS = 10000;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    Marker mParkingMarker;
    LocationRequest mLocationRequest;

    //Checks Googleplay services availability
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        if (!isGooglePlayServicesAvailable()) {
            Log.d("onCreate", "Google Play Services not available. Ending Test case.");
            finish();
        }
        else {
            Log.d("onCreate", "Google Play Services available. Continuing.");
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    //If available open Map if not show error
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }

    //Asks user for location permission and uses GPS for location on map
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        db.getA1();
        db.getA2();
        db.getB1();
        mMap.clear();
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        //Different buttons for showing the markers of parkinggarages, gasstations, parkingmeters.
        Button btnParking = (Button) findViewById(R.id.btnParking);
        btnParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                build_retrofit_and_get_response("parking");
            }
        });

        Button btnGasstation = (Button) findViewById(R.id.btnGasstation);
        btnGasstation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                build_retrofit_and_get_response("gas_station");

            }
        });

        Button btnParkeermeter = (Button) findViewById(R.id.btnParkeermeter);
        btnParkeermeter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                db.getRows();
            }
        });
        Button btnA1 = (Button) findViewById(R.id.btnA1);
        btnA1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                db.getA1();
            }
        });
        Button btnA2 = (Button) findViewById(R.id.btnA2);
        btnA2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                db.getA2();
            }
        });
        Button btnB1 = (Button) findViewById(R.id.btnB1);
        btnB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                db.getB1();
            }
        });
    }

    //Saves carlocation into preferencefile
    public void saveCarlocation(View view){
        SharedPreferences sharedPref = getSharedPreferences("carLocation", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("lat", Double.toString(latitude));
        editor.putString("lon", Double.toString(longitude));
        editor.apply();

        Toast.makeText(this, "De locatie van uw auto is opgeslagen!", Toast.LENGTH_LONG).show();

    }
//Retrieves saved carlocation from file and places marker with car icon on map
    public void displayCarlocation(View view){
        SharedPreferences sharedPref = getSharedPreferences("carLocation", MODE_PRIVATE);

        String lati = sharedPref.getString("lat", "");
        String longi = sharedPref.getString("lon", "");
        Toast.makeText(this, lati + " " + longi, Toast.LENGTH_SHORT).show();
        double latid = Double.parseDouble(lati);
        double longid = Double.parseDouble(longi);
        mParkingMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latid, longid))
                .title("Hier staat uw auto!")
                .zIndex(1.0f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
        LatLng Autolocatie = new LatLng(latid, longid);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(Autolocatie));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

    }

    //Initialises places nearby from Google Maps and setup marker for Parking garage and gasstation
    private void build_retrofit_and_get_response(String type) {

        String url = "https://maps.googleapis.com/maps/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitMaps service = retrofit.create(RetrofitMaps.class);

        Call<Example> call = service.getNearbyPlaces(type, latitude + "," + longitude, PROXIMITY_RADIUS);

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Response<Example> response, Retrofit retrofit) {

                try {
                    mMap.clear();
                    for (int i = 0; i < response.body().getResults().size(); i++) {
                        Double lat = response.body().getResults().get(i).getGeometry().getLocation().getLat();
                        Double lng = response.body().getResults().get(i).getGeometry().getLocation().getLng();
                        String placeName = response.body().getResults().get(i).getName();
                        String vicinity = response.body().getResults().get(i).getVicinity();
                        MarkerOptions markerOptions = new MarkerOptions();
                        LatLng latLng = new LatLng(lat, lng);
                        markerOptions.position(latLng);
                        markerOptions.title(placeName + " : " + vicinity);
                        Marker m = mMap.addMarker(markerOptions);
                        m.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

                    }
                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }
            }
            // Markers voor verschillende voorzieningen zijn oranje gekleurd.
            @Override
            public void onFailure(Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        Log.d("onLocationChanged", "entered");

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        Log.d("onLocationChanged", String.format("latitude:%.3f longitude:%.3f", latitude, longitude));

        Log.d("onLocationChanged", "Exit");
    }
    // Hiermee wordt de huidige locatie van de gebruiker getoont.
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
    // Hier wordt de permissie aangevraagd voor GPS signaal van de gebruiker.
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }
}


