package id.example.donordarah;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class ViewDarah extends AppCompatActivity {
    RecyclerView rvDarah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_darah);

        rvDarah = findViewById(R.id.recyclerview);
    }
}