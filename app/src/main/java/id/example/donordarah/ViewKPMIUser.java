package id.example.donordarah;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.List;

import id.example.AddKPMI;
import id.example.ViewPendonor2;
import id.example.adapter.AdapterKpmi;
import id.example.adapter.AdapterKpmi2;
import id.example.model.LoginToDB;
import id.example.retrofit.ApiClient;
import id.example.retrofit.ApiService;
import id.example.retrofit.response.KpmiResponse;
import id.example.room.AppDatabase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.example.room.MyApp.dbUsers;

public class ViewKPMIUser extends AppCompatActivity {
    CardView cdAss;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    String token;
    Button daftarNow;
    ProgressDialog loading;
    List<KpmiResponse> kpmiResponseList;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_k_p_m_i_user);

        cdAss = findViewById(R.id.cdAss);
        swipeRefreshLayout = findViewById(R.id.swiperesfresh);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        daftarNow = findViewById(R.id.daftarNow);

        cdAss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewKPMIUser.this, ViewPendonor2.class));
            }
        });

        daftarNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewKPMIUser.this, DaftarKPMI.class));
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getKpmi();
            }
        });
        onLoadingSwipeRefresh();
    }

    private void onLoadingSwipeRefresh(){

        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        getKpmi();
                    }
                }
        );

    }

    private void getKpmi() {
        swipeRefreshLayout.setRefreshing(true);
        getLastToken();

        loading = ProgressDialog.show(
                ViewKPMIUser.this,
                null,
                "Loading...",
                true,
                true
        );

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<KpmiResponse>> call = apiService.listKpmi(token);
        call.enqueue(new Callback<List<KpmiResponse>>() {
            @Override
            public void onResponse(Call<List<KpmiResponse>> call, Response<List<KpmiResponse>> response) {
                if (response.code()==200){
                    swipeRefreshLayout.setRefreshing(false);
                    loading.dismiss();
                    kpmiResponseList = response.body();

                    adapter = new AdapterKpmi2(ViewKPMIUser.this, kpmiResponseList);
                    recyclerView.setAdapter(adapter);
                }else {

                }
            }

            @Override
            public void onFailure(Call<List<KpmiResponse>> call, Throwable t) {

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