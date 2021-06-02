package id.example.retrofit.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Darah implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("id_kpmi")
    @Expose
    private String idKpmi;
    @SerializedName("goldar")
    @Expose
    private String goldar;
    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Darah(String idKpmi, String goldar, String qty) {
        this.idKpmi = idKpmi;
        this.goldar = goldar;
        this.qty = qty;
    }

    public Darah(int id, String qty) {
        this.id = id;
        this.qty = qty;
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

    public String getGoldar() {
        return goldar;
    }

    public void setGoldar(String goldar) {
        this.goldar = goldar;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
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
}