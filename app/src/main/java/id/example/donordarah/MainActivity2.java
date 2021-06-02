package id.example.donordarah;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import id.example.model.User;
import id.example.retrofit.ApiClient;
import id.example.retrofit.ApiService;
import id.example.retrofit.response.MessageResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity2 extends AppCompatActivity {
    EditText nama, email, pass;
    Button regis;
    TextView login;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        nama = findViewById(R.id.nama);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        regis = findViewById(R.id.registrasi);
        login = findViewById(R.id.textLogin);

        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proccesRegis();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), MainActivity.class));
                finish();
            }
        });
    }

    private void regis(User user) {
        loading = ProgressDialog.show(
                MainActivity2.this,
                null,
                "Loading...",
                true,
                true
        );

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<MessageResponse> call = apiService.registrasi(user);
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.code()==200){
                    loading.dismiss();
                    Toast.makeText(MainActivity2.this, response.body().getStatus()+"", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity2.this, MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {

            }
        });
    }

    private void proccesRegis() {
        if (nama.getText().toString().length()<1){
            nama.setError("Cek data lagi");
        }else if (email.getText().toString().length()<1){
            email.setError("Cek data lagi");
        }else if (pass.getText().toString().length()<1){
            pass.setError("Cek lagi");
        }else {
            regis(new User(nama.getText().toString(), email.getText().toString(), pass.getText().toString(), "user"));
        }
    }
}