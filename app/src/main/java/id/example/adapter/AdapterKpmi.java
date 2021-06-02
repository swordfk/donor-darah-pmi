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
import id.example.retrofit.response.Darah;
import id.example.retrofit.response.Jadwal;
import id.example.retrofit.response.KpmiResponse;

public class AdapterKpmi extends RecyclerView.Adapter<AdapterKpmi.viewHolder> {
    Context context;
    List<KpmiResponse> responseList;

    public AdapterKpmi(Context context, List<KpmiResponse> responseList) {
        this.context = context;
        this.responseList = responseList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_kpmi, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        List<Darah> darah = responseList.get(position).getDarah();
        List<Jadwal> jadwal = responseList.get(position).getJadwal();

        holder.nama.setText(responseList.get(position).getNama());
        holder.alamat.setText(responseList.get(position).getAlamat());
        holder.darah.setText(": "+String.valueOf(responseList.get(position).getDarah().size()));
        holder.jadwal.setText(": "+String.valueOf(responseList.get(position).getJadwal().size()));
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DetailKMI.class);
                i.putExtra("id", responseList.get(position).getId());
                i.putExtra("nama", responseList.get(position).getNama());
                i.putExtra("alamat", responseList.get(position).getAlamat());
                i.putExtra("listDarah", (Serializable) darah);
                i.putExtra("listJadwal", (Serializable) jadwal);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView nama, alamat, darah, jadwal;
        LinearLayout parent;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            nama = itemView.findViewById(R.id.nama);
            darah = itemView.findViewById(R.id.darah);
            jadwal = itemView.findViewById(R.id.jadwal);
            alamat = itemView.findViewById(R.id.alamat);
            parent = itemView.findViewById(R.id.parentLayout);
        }
    }
}
