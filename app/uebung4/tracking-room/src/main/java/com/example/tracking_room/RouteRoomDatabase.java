package com.example.tracking_room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Route.class, Poi.class}, version = 1, exportSchema = false)
public abstract class RouteRoomDatabase extends RoomDatabase {

    public abstract RouteDao routeDao();
    public abstract PoiDao poiDao();

    public static int count;
    private static volatile RouteRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static RouteRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RouteRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    RouteRoomDatabase.class, "database")
                            .addCallback(sRoomDatabaseCallback)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback sRoomDatabaseCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                RouteDao dao = INSTANCE.routeDao();
                dao.deleteAll();
            });
        }
    };
    public static void addPoi(Poi poi){
        INSTANCE.poiDao().insert(poi);
    }
    public static long getRouteCount(){
        return INSTANCE.poiDao().getRouteCount();
    }
}
