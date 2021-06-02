package id.example.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("name")
    @Expose
    String nama;
    @SerializedName("email")
    @Expose
    String emal;
    @SerializedName("password")
    @Expose
    String password;
    @SerializedName("role")
    @Expose
    String role;

    public User(String nama, String emal, String password, String role) {
        this.nama = nama;
        this.emal = emal;
        this.password = password;
        this.role = role;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmal() {
        return emal;
    }

    public void setEmal(String emal) {
        this.emal = emal;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
