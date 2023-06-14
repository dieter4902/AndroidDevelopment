package com.example.tracking_room;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class RouteViewModel extends AndroidViewModel {

    private final RouteRepository mRepository;

    private final LiveData<List<Route>> mAllRoutes;

    public RouteViewModel(Application application) {
        super(application);
        mRepository = new RouteRepository(application);
        mAllRoutes = mRepository.getAllRoutes();
    }

    LiveData<List<Route>> getAllRoutes() {
        return mAllRoutes;
    }

    public void insert(Route route) {
        mRepository.insert(route);
    }
}

