package com.example.tracking_room;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PoiViewModel extends AndroidViewModel {

    private final PoiRepository mRepository;



    public PoiViewModel(Application application,long routeId) {
        super(application);
        mRepository = new PoiRepository(application,routeId);
    }

    public void insert(Poi poi) {
        mRepository.insert(poi);
    }

    public LiveData<List<Poi>> getPois(List<Long> poiIds){
        return mRepository.getPois(poiIds);
    }

}

