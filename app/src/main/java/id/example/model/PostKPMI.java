package id.example.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostKPMI {
    @SerializedName("nama")
    @Expose
    String nama;
    @SerializedName("alamat")
    @Expose
    String alamat;
    @SerializedName("lat")
    @Expose
    String lat;
    @SerializedName("lng")
    @Expose
    String lng;

    public PostKPMI(String nama, String alamat, String lat, String lng) {
        this.nama = nama;
        this.alamat = alamat;
        this.lat = lat;
        this.lng = lng;
    }
}
