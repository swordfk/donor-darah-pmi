package id.example.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DaftarDonor {
    @SerializedName("id_kpmi")
    @Expose
    int idKpmi;
    @SerializedName("nama")
    @Expose
    String name;
    @SerializedName("no_hp")
    @Expose
    String noHp;

    public DaftarDonor(int idKpmi, String name, String noHp) {
        this.idKpmi = idKpmi;
        this.name = name;
        this.noHp = noHp;
    }
}
