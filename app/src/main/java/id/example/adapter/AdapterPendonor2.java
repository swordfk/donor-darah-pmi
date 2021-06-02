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
import id.example.model.Pendonor;

public class AdapterPendonor2 extends RecyclerView.Adapter<AdapterPendonor2.viewHolder> {
    Context context;
    List<Pendonor> responseList;

    public AdapterPendonor2(Context context, List<Pendonor> responseList) {
        this.context = context;
        this.responseList = responseList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.pendonor2, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.nama.setText(responseList.get(position).getNama());
        holder.alamat.setText(responseList.get(position).getKpmi().getAlamat());
    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView nama, alamat;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            nama = itemView.findViewById(R.id.namaP);
            alamat = itemView.findViewById(R.id.alamatP);
        }
    }
}
