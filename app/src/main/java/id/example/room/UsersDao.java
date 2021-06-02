package id.example.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import id.example.model.LoginToDB;
import id.example.retrofit.response.LoginResponse;

@Dao
public interface UsersDao {
    @Query("Select * from users")
    List<LoginToDB> getAllUser();

    @Insert
    void insertUser(LoginToDB loginResponse);

}
