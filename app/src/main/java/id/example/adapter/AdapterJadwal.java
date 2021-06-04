package id.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.example.donordarah.R;
import id.example.retrofit.response.Jadwal;

public class AdapterJadwal extends RecyclerView.Adapter<AdapterJadwal.viewHolder> {
    Context context;
    List<Jadwal> listJadwal;

    public AdapterJadwal(Context context, List<Jadwal> listJadwal) {
        this.context = context;
        this.listJadwal = listJadwal;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_jadwal, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.waktu.setText(": "+listJadwal.get(position).getDate());
        holder.nama.setText(": "+listJadwal.get(position).getKpmi().getNama());
        holder.alamat.setText(": "+listJadwal.get(position).getKpmi().getAlamat());
    }

    @Override
    public int getItemCount() {
        return listJadwal.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView waktu, nama, alamat;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            nama = itemView.findViewById(R.id.namaPMI);
            alamat = itemView.findViewById(R.id.alamatPMI);
            waktu = itemView.findViewById(R.id.tglPMI);
        }
    }
}
