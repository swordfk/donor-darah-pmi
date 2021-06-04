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

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.List;

import id.example.adapter.AdapterDarah;
import id.example.adapter.AdapterJadwal;
import id.example.donordarah.R;
import id.example.donordarah.ViewKPMI;
import id.example.model.LoginToDB;
import id.example.retrofit.ApiClient;
import id.example.retrofit.ApiService;
import id.example.retrofit.response.Darah;
import id.example.retrofit.response.Jadwal;
import id.example.retrofit.response.MessageResponse;
import id.example.room.AppDatabase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.example.room.MyApp.dbUsers;

public class DetailKMI extends AppCompatActivity {
    TextView nama, alamat;
    RecyclerView rcDarah;
    RecyclerView rcJadwal;
    LinearLayoutManager linearLayoutManager;
    LinearLayoutManager linearLayoutManager2;
    RecyclerView.Adapter adapter;
    RecyclerView.Adapter adapter2;
    int idKpmi;
    Button tambahDarah, tambahJadwal;
    Dialog dialog, dialog2;
    String token;
    ProgressDialog loading;

    List<Jadwal> jadwals = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_k_m_i);

        Intent i = getIntent();
        idKpmi = i.getIntExtra("id", 0);
        String name = i.getStringExtra("nama");
        String address = i.getStringExtra("alamat");
        List<Darah> listDarah = (List<Darah>) i.getSerializableExtra("listDarah");
//        List<Jadwal> listJadwal = (List<Jadwal>) i.getSerializableExtra("listJadwal");

        getJadwal(idKpmi);
        getLastToken();
        tambahJadwal = findViewById(R.id.addJadwal);
        tambahDarah = findViewById(R.id.tambahDarah);
        nama = findViewById(R.id.namaKpmi);
        alamat = findViewById(R.id.alamatKpmi);
        rcDarah = findViewById(R.id.rcDarah);
        rcJadwal = findViewById(R.id.rcJadwal);
        linearLayoutManager = new LinearLayoutManager(this);
        rcDarah.setLayoutManager(linearLayoutManager);
        rcDarah.setHasFixedSize(true);
        adapter = new AdapterDarah(this, listDarah);
        rcDarah.setAdapter(adapter);
        linearLayoutManager2 = new LinearLayoutManager(this);
        rcJadwal.setLayoutManager(linearLayoutManager2);
        rcJadwal.setHasFixedSize(true);

        nama.setText(name);
        alamat.setText(address);

        tambahJadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(view.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.add_jadwal);
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                EditText date1 = dialog.findViewById(R.id.date);
                Button add = dialog.findViewById(R.id.addJadwal1);
                date1.setFocusable(false);
                date1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog2 = new Dialog(view.getContext());
                        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog2.setContentView(R.layout.dialog_calender);
                        dialog2.getWindow().setGravity(Gravity.CENTER);
                        dialog2.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog2.show();

                        MaterialCalendarView calendarView;
                        calendarView = dialog2.findViewById(R.id.calendarView);

                        if (calendarView.getSelectedDate()==null){
                            calendarView.setDateSelected(CalendarDay.today(), true);
                        }
                        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
                            @Override
                            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                                List<CalendarDay> dayList = new ArrayList<>();
                                Log.i("TAG", "onDateSelected: "+dayList);
                                date1.setText(date.getYear()+"-"+date.getMonth()+"-"+date.getDay());
//                                        Log.i("TAG", "onDate: "+cal.getMonth());
                                if(calendarView.getSelectedDate() != date){
                                    calendarView.setDateSelected(date, true);
                                }
                                dialog2.cancel();
                            }
                        });

                    }
                });

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (date1.getText().toString().length()<1){
                            date1.setError("cek form !");
                        }else {
                            loading = ProgressDialog.show(
                                    view.getContext(),
                                    null,
                                    "Loading...",
                                    true,
                                    true
                            );

                            ApiService apiService = ApiClient.getClient().create(ApiService.class);
                            Call<MessageResponse> call = apiService.addJadwal(token, new Jadwal(String.valueOf(idKpmi), date1.getText().toString()));
                            call.enqueue(new Callback<MessageResponse>() {
                                @Override
                                public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                                    if (response.code()==200){
                                        loading.dismiss();
                                        dialog.cancel();
                                        Toast.makeText(DetailKMI.this,response.body().getMessage()+ "", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(DetailKMI.this, ViewKPMI.class));
                                        finish();
                                    }else {
                                        loading.dismiss();
                                        Toast.makeText(DetailKMI.this,response.code()+ ""+response.message(), Toast.LENGTH_SHORT).show();
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
        tambahDarah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(view.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.add_darah);
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                EditText goldar = dialog.findViewById(R.id.goldar);
                EditText qty = dialog.findViewById(R.id.qty);

                Button add = dialog.findViewById(R.id.addDarah);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (goldar.getText().toString().length()<1){
                            goldar.setError("Cek lagi !");
                        }else if (qty.getText().toString().length()<1){
                            qty.setError("Cek lagi !");
                        }else {
                            loading = ProgressDialog.show(
                                    view.getContext(),
                                    null,
                                    "Loading...",
                                    true,
                                    true
                            );

                            ApiService apiService = ApiClient.getClient().create(ApiService.class);
                            Call<MessageResponse> call = apiService.addDarah(token, new Darah(String.valueOf(idKpmi), goldar.getText().toString().toUpperCase(), qty.getText().toString()));
                            call.enqueue(new Callback<MessageResponse>() {
                                @Override
                                public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                                    if (response.code()==200){
                                        loading.dismiss();

                                        Toast.makeText(DetailKMI.this,response.body().getMessage()+ "", Toast.LENGTH_SHORT).show();
                                        adapter2.notifyDataSetChanged();
                                        dialog.cancel();
                                        startActivity(new Intent(DetailKMI.this, ViewKPMI.class));
                                        finish();
                                    }else {
                                        loading.dismiss();
                                        Toast.makeText(DetailKMI.this, response.code()+" "+response.message(), Toast.LENGTH_SHORT).show();
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

                    adapter2 = new AdapterJadwal(DetailKMI.this, jadwals);
                    rcJadwal.setAdapter(adapter2);
                }else {
                    Toast.makeText(DetailKMI.this,response.code()+ " "+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Jadwal>> call, Throwable t) {

            }
        });
    }
}