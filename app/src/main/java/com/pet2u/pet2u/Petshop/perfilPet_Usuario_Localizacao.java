package com.pet2u.pet2u.Petshop;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AlertDialog;
        import androidx.core.app.ActivityCompat;
        import androidx.fragment.app.FragmentActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;

        import android.Manifest;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.graphics.Camera;
        import android.location.Address;
        import android.location.Geocoder;
        import android.location.Location;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.util.Log;

        import com.google.android.gms.location.FusedLocationProviderClient;
        import com.google.android.gms.location.LocationServices;
        import com.google.android.gms.maps.CameraUpdate;
        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.LatLngBounds;
        import com.google.android.gms.maps.model.Marker;
        import com.google.android.gms.maps.model.MarkerOptions;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.ValueEventListener;
        import com.pet2u.pet2u.ConexaoDB.Conexao;
        import com.pet2u.pet2u.Helper.Adapter;
        import com.pet2u.pet2u.Helper.Criptografia;
        import com.pet2u.pet2u.Helper.Permissao;
        import com.pet2u.pet2u.R;
        import com.pet2u.pet2u.modelo.Petshop;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Locale;
        import java.util.Map;

public class perfilPet_Usuario_Localizacao extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private DatabaseReference databaseReference;
    private  Geocoder geocoder;
    List<Address> addresses;
    MarkerOptions[] markersList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_pet__usuario__localizacao);
        //geocoder = new Geocoder(this, Locale.getDefault());
        addresses = null;
        markersList = new MarkerOptions[2];

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        databaseReference = Conexao.getFirebaseDatabase();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //LatLng l1 = new LatLng(-15.838340, -48.011480);
        //mMap.addMarker(new MarkerOptions().position(l1).title("teste"));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
                Log.d("aa", String.valueOf(location.getLatitude()));
                MarkerOptions m1 = new MarkerOptions();
                mMap.addMarker(m1.position(latlng).title("Você"));
                markersList[0] = m1;
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
                //mMap.animateCamera(CameraUpdateFactory.zoomIn());
                //mMap.animateCamera(CameraUpdateFactory.zoomTo(15),2000, null);
            }
        });

        final String petshopCriptografado = getIntent().getExtras().getString("emailPetshop");
        DatabaseReference usuarioRef = databaseReference.child("Petshop").child(petshopCriptografado);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {

                    String cep = dataSnapshot.child("cep").getValue().toString();
                    String endereco = dataSnapshot.child("endereco").getValue().toString();

                    /*addresses = geocoder.getFromLocationName(cep, 1);

                    if (addresses.isEmpty()) {
                        addresses = geocoder.getFromLocationName(endereco, 1);
                    }*/

                    String link = "https://maps.googleapis.com/maps/api/geocode/json?address=" + cep + "&key=AIzaSyDVGfVyok-Tu-HB6Hk-7Ws1O32N8vtWQbw";

                    GetLocationDownloadTask getLocation = new GetLocationDownloadTask();

                    JSONObject locationObject = new JSONObject(getLocation.execute(link).get());
                    if (locationObject.getString("status").equals("ZERO_RESULTS")) {
                        link = "https://maps.googleapis.com/maps/api/geocode/json?address=" + endereco + "&key=AIzaSyDVGfVyok-Tu-HB6Hk-7Ws1O32N8vtWQbw";
                        GetLocationDownloadTask getLocation2 = new GetLocationDownloadTask();
                        getLocation2.execute(link);
                    }

                } catch (Exception e)  {
                    e.printStackTrace();
                }

            }
            @Override
            public void onCancelled(DatabaseError dbError) {
                Log.d("Cancelou", dbError.getMessage()); //Don't ignore errors!
            }
        };
        usuarioRef.addListenerForSingleValueEvent(eventListener);
    }

    public class GetLocationDownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection;
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream is = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(is);

                int data = inputStreamReader.read();
                while(data != -1){
                    char curr = (char) data;
                    result += curr;
                    data = inputStreamReader.read();
                }
                return result;

            } catch (Exception e) {

                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result != null) {
                try {
                    JSONObject locationObject = new JSONObject(result);
                    String lat = locationObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lat");
                    String lng = locationObject.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lng");

                    LatLng latlngPetshop = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                    MarkerOptions m2 = new MarkerOptions();
                    mMap.addMarker(m2.position(latlngPetshop).title("Petshop"));
                    markersList[1] = m2;
                    //Calculate the markers to get their position
                    LatLngBounds.Builder b = new LatLngBounds.Builder();
                    b.include(markersList[0].getPosition());
                    b.include(markersList[1].getPosition());
                    LatLngBounds bounds = b.build();
                    int width = getResources().getDisplayMetrics().widthPixels;
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width - 100,width - 100,25);
                    mMap.animateCamera(cu);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}