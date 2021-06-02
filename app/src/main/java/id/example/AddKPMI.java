package id.example;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import id.example.donordarah.R;
import id.example.donordarah.ViewKPMI;
import id.example.model.LoginToDB;
import id.example.model.PostKPMI;
import id.example.retrofit.ApiClient;
import id.example.retrofit.ApiService;
import id.example.retrofit.response.MessageResponse;
import id.example.room.AppDatabase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.example.room.MyApp.dbUsers;

public class AddKPMI extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;
    Button btn_ok;
    private FusedLocationProviderClient fusedLocationClient;
    private  final int REQUEST_CODE = 101;
    TextView tv_address, tv_latlng;
    EditText ed_name;
    double latitudes,longitudes;
    Geocoder geocoder;
    ProgressDialog loading;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_k_p_m_i);

        tv_latlng = findViewById(R.id.latlng);
        tv_address = findViewById(R.id.address);
        ed_name = findViewById(R.id.name);
        btn_ok = findViewById(R.id.btn_ok);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(AddKPMI.this);
        fetchLastLocation();
        getLastToken();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ed_name.getText().toString().length()<1){
                    ed_name.setError("Cek form!");
                }else {
                    loading = ProgressDialog.show(
                            view.getContext(),
                            null,
                            "Loading...",
                            true,
                            true
                    );

                    ApiService apiService = ApiClient.getClient().create(ApiService.class);
                    Call<MessageResponse> call = apiService.addKPMI(token, new PostKPMI(ed_name.getText().toString(), tv_address.getText().toString(), String.valueOf(latitudes), String.valueOf(longitudes)));
                    call.enqueue(new Callback<MessageResponse>() {
                        @Override
                        public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                            if (response.code()==200){
                                loading.dismiss();
                                Toast.makeText(AddKPMI.this, response.body().getMessage()+"", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddKPMI.this, ViewKPMI.class));
                                finish();
                            }else {
                                loading.dismiss();
                                Toast.makeText(AddKPMI.this,response.code()+ ""+response.message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MessageResponse> call, Throwable t) {

                        }
                    });
                }

            }
        });
    }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    latitudes = location.getLatitude();
                    longitudes = location.getLongitude();
                    List<Address> addresses;
                    geocoder = new Geocoder(AddKPMI.this, Locale.getDefault());

                    try {
                        addresses = geocoder.getFromLocation(latitudes, longitudes, 1);
                        tv_address.setText(addresses.get(0).getAddressLine(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    tv_latlng.setText(latitudes+", "+longitudes);
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(AddKPMI.this);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                fetchLastLocation();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng current = new LatLng(latitudes, longitudes);
        map.addMarker(new MarkerOptions().position(current).title("You Here"));
        map.moveCamera(CameraUpdateFactory.newLatLng(current));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(current,20) );
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                map.clear();
                map.addMarker(new MarkerOptions().position(latLng).title("You Here"));
                map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,20) );
                latitudes = latLng.latitude;
                longitudes= latLng.longitude;

                List<Address> addresses;
                geocoder = new Geocoder(AddKPMI.this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(latitudes, longitudes, 1);
                    tv_address.setText(addresses.get(0).getAddressLine(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                tv_latlng.setText(latitudes+", "+longitudes);
            }
        });
    }

    private void getLastToken(){
        dbUsers = Room.databaseBuilder(this,
                AppDatabase.class, "users").allowMainThreadQueries().build();
        List<LoginToDB> list = dbUsers.usersDao().getAllUser();
        if (list != null){
            token ="Bearer "+ list.get(list.size()-1).getToken();
        }
    }
}