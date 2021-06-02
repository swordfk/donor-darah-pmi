package id.example.room;

import android.app.Application;

import androidx.room.Room;

public class MyApp extends Application {
    public static final String DATABASE_USERS_NAME = "db_users";
    public static AppDatabase dbUsers;

    @Override
    public void onCreate() {
        super.onCreate();
        dbUsers = Room.databaseBuilder(getApplicationContext(),AppDatabase.class, DATABASE_USERS_NAME).allowMainThreadQueries().build();
    }

}
