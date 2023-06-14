package com.example.tracking_room;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

class RouteRepository {

    private RouteDao mRouteDao;
    private LiveData<List<Route>> mAllRoutes;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    RouteRepository(Application application) {
        RouteRoomDatabase db = RouteRoomDatabase.getDatabase(application);
        mRouteDao = db.routeDao();
        mAllRoutes = mRouteDao.getAlphabetizedRoutes();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Route>> getAllRoutes() {
        return mAllRoutes;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(Route route) {
        RouteRoomDatabase.databaseWriteExecutor.execute(() -> {
            mRouteDao.insert(route);
        });
    }
}


