package id.example.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import id.example.model.DataNotification;
import id.example.model.LoginToDB;


@Database(entities = {LoginToDB.class, DataNotification.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UsersDao usersDao();

    public abstract NotifDao notifDao();
}
