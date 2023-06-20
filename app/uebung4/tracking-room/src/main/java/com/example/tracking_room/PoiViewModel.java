package com.example.tracking_room;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PoiViewModel extends AndroidViewModel {

    private final PoiRepository mRepository;


    public PoiViewModel(Application application) {
        super(application);
        mRepository = new PoiRepository(application);
    }

    public void insert(Poi poi) {
        mRepository.insert(poi);
    }

    public LiveData<List<Poi>> getPois(long routeId) {
        return mRepository.getPois(routeId);
    }

}

