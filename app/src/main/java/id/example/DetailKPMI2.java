package id.example;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.example.adapter.AdapterDarah;
import id.example.adapter.AdapterJadwal;
import id.example.donordarah.R;
import id.example.donordarah.ViewKPMI;
import id.example.donordarah.ViewKPMIUser;
import id.example.model.DaftarDonor;
import id.example.model.LoginToDB;
import id.example.retrofit.ApiClient;
import id.example.retrofit.ApiService;
import id.example.retrofit.response.Darah;
import id.example.retrofit.response.Jadwal;
import id.example.retrofit.response.Latlng;
import id.example.retrofit.response.MessageResponse;
import id.example.room.AppDatabase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.example.room.MyApp.dbUsers;

public class DetailKPMI2 extends AppCompatActivity implements OnMapReadyCallback {
    TextView nama, alamat;
    RecyclerView rcJadwal;
    LinearLayoutManager linearLayoutManager2;
    RecyclerView.Adapter adapter2;
    int idKpmi;
    String token;
    GoogleMap map;
    double latitudes;
    double longitudes;
    String name;
    Button petunjuk, daftar;
    Dialog dialog;
    ProgressDialog loading;

    List<Jadwal> jadwals = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_k_p_m_i2);

        Intent i = getIntent();
        idKpmi = i.getIntExtra("id", 0);
        name = i.getStringExtra("nama");
        String address = i.getStringExtra("alamat");
        latitudes = Double.parseDouble(i.getStringExtra("lat"));
        longitudes = Double.parseDouble(i.getStringExtra("lng"));
//        List<Jadwal> listJadwal = (List <Jadwal>) i.getSerializableExtra("listJadwal");

        Log.i("TAG", "onCreate: "+latitudes+longitudes);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(DetailKPMI2.this);

        getJadwal(idKpmi);
        getLastToken();
        daftar = findViewById(R.id.daftar);
        petunjuk = findViewById(R.id.petunjuk);
        nama = findViewById(R.id.namaKpmi);
        alamat = findViewById(R.id.alamatKpmi);
        rcJadwal = findViewById(R.id.rcJadwal);
        linearLayoutManager2 = new LinearLayoutManager(this);
        rcJadwal.setLayoutManager(linearLayoutManager2);
        rcJadwal.setHasFixedSize(true);

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(view.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.daftar_donor);
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                EditText nama = dialog.findViewById(R.id.nama);
                EditText hp = dialog.findViewById(R.id.hp);

                Button daftar = dialog.findViewById(R.id.daftarDonor);
                daftar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (daftar.getText().toString().length()<1){
                            daftar.setError("Cek lagi !");
                        }else if (hp.getText().toString().length()<1){
                            hp.setError("Cek lagi !");
                        }else {
                            loading = ProgressDialog.show(
                                    view.getContext(),
                                    null,
                                    "Loading...",
                                    true,
                                    true
                            );

                            ApiService apiService = ApiClient.getClient().create(ApiService.class);
                            Call<MessageResponse> call = apiService.daftarDonor(token, new DaftarDonor(idKpmi, nama.getText().toString(), hp.getText().toString()));
                            call.enqueue(new Callback<MessageResponse>() {
                                @Override
                                public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                                    if (response.code()==200){
                                        loading.dismiss();

                                        Toast.makeText(DetailKPMI2.this,response.body().getMessage()+ "", Toast.LENGTH_SHORT).show();
                                        dialog.cancel();
                                        startActivity(new Intent(DetailKPMI2.this, ViewKPMIUser.class));
                                        finish();
                                    }else {
                                        loading.dismiss();
                                        Toast.makeText(DetailKPMI2.this, response.code()+" "+response.message(), Toast.LENGTH_SHORT).show();
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
        });

        petunjuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+latitudes+","+longitudes);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        nama.setText(name);
        alamat.setText(address);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng current = new LatLng(latitudes, longitudes);
        map.addMarker(new MarkerOptions().position(current).title(name));
        map.moveCamera(CameraUpdateFactory.newLatLng(current));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(current,20) );
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                map.clear();
                map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,20) );
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

    private void getJadwal(int idKpmi){
        getLastToken();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Jadwal>> call = apiService.getJadwal(token, idKpmi);
        call.enqueue(new Callback<List<Jadwal>>() {
            @Override
            public void onResponse(Call<List<Jadwal>> call, Response<List<Jadwal>> response) {
                if (response.code()==200){
                    jadwals = response.body();

                    adapter2 = new AdapterJadwal(DetailKPMI2.this, jadwals);
                    rcJadwal.setAdapter(adapter2);
                }else {
                    Toast.makeText(DetailKPMI2.this,response.code()+ " "+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Jadwal>> call, Throwable t) {

            }
        });
    }
}