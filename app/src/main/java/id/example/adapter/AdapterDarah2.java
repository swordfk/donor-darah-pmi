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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

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

public class AdapterDarah2 extends RecyclerView.Adapter<AdapterDarah2.viewHolder> {
    Context context;
    List<Darah> listDarah;
    String token;


    public AdapterDarah2(Context context, List<Darah> listDarah) {
        this.context = context;
        this.listDarah = listDarah;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_darah2, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        holder.golder.setText(listDarah.get(position).getGoldar());
        holder.stok.setText(listDarah.get(position).getQty());
    }

    @Override
    public int getItemCount() {
        return listDarah.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView golder, stok;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            golder = itemView.findViewById(R.id.golongan);
            stok = itemView.findViewById(R.id.stok);
        }
    }
}
