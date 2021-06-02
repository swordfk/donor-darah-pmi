package id.example.donordarah;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.os.Bundle;

import java.util.List;

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

public class DaftarKPMI extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<KpmiResponse> kpmiResponseList;
    String token;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_k_p_m_i);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        getKpmi();
    }

    private void getKpmi() {
        getLastToken();

        loading = ProgressDialog.show(
                DaftarKPMI.this,
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
                    loading.dismiss();
                    kpmiResponseList = response.body();

                    adapter = new AdapterKpmi2(DaftarKPMI.this, kpmiResponseList);
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