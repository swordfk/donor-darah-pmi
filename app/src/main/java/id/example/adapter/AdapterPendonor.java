package id.example.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

import id.example.DetailKMI;
import id.example.donordarah.R;
import id.example.model.Pendonor;
import id.example.retrofit.response.Darah;
import id.example.retrofit.response.Jadwal;
import id.example.retrofit.response.KpmiResponse;

public class AdapterPendonor extends RecyclerView.Adapter<AdapterPendonor.viewHolder> {
    Context context;
    List<Pendonor> responseList;

    public AdapterPendonor(Context context, List<Pendonor> responseList) {
        this.context = context;
        this.responseList = responseList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.pendonor, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.nama.setText(responseList.get(position).getNama());
        holder.alamat.setText(responseList.get(position).getKpmi().getAlamat());
        holder.nohp.setText(String.valueOf(responseList.get(position).getNoHp()));
    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView nama, alamat, nohp;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            nama = itemView.findViewById(R.id.namaP);
            nohp = itemView.findViewById(R.id.nohp);
            alamat = itemView.findViewById(R.id.alamatP);
        }
    }
}
