package id.example.retrofit.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import id.example.model.Kpmi;

public class Jadwal implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("id_kpmi")
    @Expose
    private String idKpmi;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("kpmi")
    @Expose
    private Kpmi kpmi;

    public Jadwal(String idKpmi, String date) {
        this.idKpmi = idKpmi;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdKpmi() {
        return idKpmi;
    }

    public void setIdKpmi(String idKpmi) {
        this.idKpmi = idKpmi;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Kpmi getKpmi() {
        return kpmi;
    }

    public void setKpmi(Kpmi kpmi) {
        this.kpmi = kpmi;
    }
}
