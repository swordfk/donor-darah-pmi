package id.example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import id.example.adapter.AdapterKpmi;
import id.example.adapter.AdapterPendonor;
import id.example.donordarah.R;
import id.example.donordarah.ViewKPMI;
import id.example.model.LoginToDB;
import id.example.model.Pendonor;
import id.example.retrofit.ApiClient;
import id.example.retrofit.ApiService;
import id.example.retrofit.response.KpmiResponse;
import id.example.room.AppDatabase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.example.room.MyApp.dbUsers;

public class ViewPendonor extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    String token;
    ProgressDialog loading;
    List<Pendonor> kpmiResponseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pendonor);

        recyclerView = findViewById(R.id.recyclerviewP);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        getPendonor();
    }

    private void getPendonor() {
        getLastToken();

        loading = ProgressDialog.show(
                ViewPendonor.this,
                null,
                "Loading...",
                true,
                true
        );

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Pendonor>> call = apiService.getPendonor(token);
        call.enqueue(new Callback<List<Pendonor>>() {
            @Override
            public void onResponse(Call<List<Pendonor>> call, Response<List<Pendonor>> response) {
                if (response.code()==200){
                    loading.dismiss();
                    kpmiResponseList = response.body();

                    adapter = new AdapterPendonor(ViewPendonor.this, kpmiResponseList);
                    recyclerView.setAdapter(adapter);
                }else {
                    Toast.makeText(ViewPendonor.this,response.code()+ " "+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Pendonor>> call, Throwable t) {
                Toast.makeText(ViewPendonor.this,t.getMessage()+ "", Toast.LENGTH_SHORT).show();
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