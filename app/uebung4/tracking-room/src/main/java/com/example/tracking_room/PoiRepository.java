package com.example.tracking_room;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

class PoiRepository {

    private PoiDao mPoiDao;
    private LiveData<List<Poi>> mPois;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    PoiRepository(Application application,long routeId) {
        RouteRoomDatabase db = RouteRoomDatabase.getDatabase(application);
        mPoiDao = db.poiDao();
        mPois = mPoiDao.getAll(routeId);
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(Poi poi) {
        RouteRoomDatabase.databaseWriteExecutor.execute(() -> {
            mPoiDao.insert(poi);
        });
    }

    LiveData<List<Poi>> getPois(List<Long> poiIds){
        return mPois;
    }
}


