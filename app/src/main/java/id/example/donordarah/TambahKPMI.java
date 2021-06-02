package id.example.donordarah;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import id.example.model.LoginToDB;
import id.example.retrofit.ApiClient;
import id.example.retrofit.ApiService;
import id.example.retrofit.response.KpmiResponse;
import id.example.retrofit.response.LoginResponse;
import id.example.retrofit.response.MessageResponse;
import id.example.room.AppDatabase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.example.room.MyApp.dbUsers;

public class TambahKPMI extends AppCompatActivity {
    TextView nama, alamat;
    ProgressDialog loading;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_k_p_m_i);

        nama = findViewById(R.id.nama);
        alamat = findViewById(R.id.alamat);

    }

    private void createKPMI(KpmiResponse kpmiResponse) {
        getLastToken();
        loading = ProgressDialog.show(
                TambahKPMI.this,
                null,
                "Loading...",
                true,
                true
        );

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<MessageResponse> call = apiService.createKpmi(token, kpmiResponse);
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.code()==200){
                    loading.dismiss();
                    Toast.makeText(TambahKPMI.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }else {

                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {

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