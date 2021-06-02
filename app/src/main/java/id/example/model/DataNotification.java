package id.example.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notification")
public class DataNotification {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String value;

    private String tanggal;

    private String token;

    private String title;

    public DataNotification() {
    }

    public DataNotification(String value, String tanggal, String token, String title) {
        this.value = value;
        this.tanggal = tanggal;
        this.token = token;
        this.title = title;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
