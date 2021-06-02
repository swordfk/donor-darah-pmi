package id.example.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import org.w3c.dom.Text;

import java.util.List;

import id.example.donordarah.R;
import id.example.donordarah.ViewKPMI;
import id.example.model.LoginToDB;
import id.example.retrofit.ApiClient;
import id.example.retrofit.ApiService;
import id.example.retrofit.response.Darah;
import id.example.retrofit.response.MessageResponse;
import id.example.room.AppDatabase;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static id.example.room.MyApp.dbUsers;

public class AdapterDarah extends RecyclerView.Adapter<AdapterDarah.viewHolder> {
    Context context;
    List<Darah> listDarah;
    String token;


    public AdapterDarah(Context context, List<Darah> listDarah) {
        this.context = context;
        this.listDarah = listDarah;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_darah, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        holder.golder.setText(listDarah.get(position).getGoldar());
        holder.stok.setText(listDarah.get(position).getQty());
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbUsers = Room.databaseBuilder(context,
                        AppDatabase.class, "users").allowMainThreadQueries().build();
                List<LoginToDB> list = dbUsers.usersDao().getAllUser();
                if (list != null){
                    token ="Bearer "+ list.get(list.size()-1).getToken();
                }

                Dialog dialog = new Dialog(view.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.upadate_darah);
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                EditText qty = dialog.findViewById(R.id.qty);
                Button update = dialog.findViewById(R.id.updateDarah);

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (qty.getText().toString().length()<1){
                            qty.setError("Cek lagi!");
                        }else {
                            ProgressDialog loading = ProgressDialog.show(
                                    context,
                                    null,
                                    "Loading...",
                                    true,
                                    true
                            );

                            ApiService apiService = ApiClient.getClient().create(ApiService.class);
                            Call<MessageResponse> call = apiService.updateStok(token, new Darah(listDarah.get(position).getId(), qty.getText().toString()));
                            call.enqueue(new Callback<MessageResponse>() {
                                @Override
                                public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                                    if (response.code()==200){
                                        loading.dismiss();
                                        dialog.cancel();
                                        Toast.makeText(context, response.body().getMessage()+"", Toast.LENGTH_SHORT).show();
                                        context.startActivity(new Intent(context, ViewKPMI.class));

                                    }else {
                                        loading.dismiss();
                                        Toast.makeText(context,response.code()+ ""+response.message(), Toast.LENGTH_SHORT).show();
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

    @Override
    public int getItemCount() {
        return listDarah.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView golder, stok;
        Button update;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            update = itemView.findViewById(R.id.updateStok);
            golder = itemView.findViewById(R.id.golongan);
            stok = itemView.findViewById(R.id.stok);
        }
    }
}
