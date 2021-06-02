package id.example.donordarah;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import id.example.model.Login;
import id.example.model.LoginToDB;
import id.example.retrofit.ApiClient;
import id.example.retrofit.ApiService;
import id.example.retrofit.response.LoginResponse;
import id.example.room.AppDatabase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.example.room.MyApp.dbUsers;

public class MainActivity extends AppCompatActivity {
    EditText email, pass;
    Button login;
    ProgressDialog loading;
    TextView regis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        login = findViewById(R.id.login);
        regis = findViewById(R.id.txtRegis);
        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MainActivity2.class));
            }
        });

        dbUsers = Room.databaseBuilder(this,
                AppDatabase.class, "users").allowMainThreadQueries().build();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proccesLogin();
            }
        });

    }

    private void login(String email, String pass) {
        loading = ProgressDialog.show(
                MainActivity.this,
                null,
                "Loading...",
                true,
                true
        );

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<LoginResponse> call = apiService.loginAdmin(new Login(email, pass));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.code()==200){
                    loading.dismiss();

                    LoginResponse data = response.body();
                    String tokenUser = response.body().getToken();

                    LoginToDB usersResponse = new LoginToDB(tokenUser);
                    dbUsers.usersDao().insertUser(usersResponse);

                    if (data.getData().getRole().toLowerCase().equals("admin")){
                        Intent intent = new Intent(MainActivity.this, ViewKPMI.class);
                        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    }else{
                        Intent intent = new Intent(MainActivity.this, ViewKPMIUser.class);
                        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    }
                }
                else {
                    loading.dismiss();
                    Toast.makeText(MainActivity.this, response.code()+" "+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    void proccesLogin() {
        if (email.getText().toString().length()<1){
            Toast.makeText(this, "Cek Form", Toast.LENGTH_SHORT).show();
        }else if (pass.getText().toString().length()<1){
            Toast.makeText(this, "Cek Form", Toast.LENGTH_SHORT).show();
        }else {
            login(email.getText().toString(), pass.getText().toString());
        }
    }
}