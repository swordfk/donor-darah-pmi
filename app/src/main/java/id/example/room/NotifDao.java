package id.example.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import id.example.model.DataNotification;


@Dao
public interface NotifDao {
    @Query("Select * from notification WHERE token =:token")
    List<DataNotification> getAllNotification(String token);

    @Insert
    void insertNotif(DataNotification dataNotification);

}
