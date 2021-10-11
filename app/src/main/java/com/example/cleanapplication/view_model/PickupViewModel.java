package com.example.cleanapplication.view_model;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cleanapplication.model.MainDaySlot;
import com.example.cleanapplication.model.SlotsModel;
import com.example.cleanapplication.repository.SlotsPickupRepo;

import java.util.List;

public class PickupViewModel extends ViewModel {

    Context context;
    SlotsPickupRepo slotsRepo;
    MutableLiveData<List<MainDaySlot>> slotsMutableLiveData;

    public PickupViewModel(Context c) {
        slotsRepo = new SlotsPickupRepo();
        context = c;
    }

    public LiveData<List<MainDaySlot>> getPickupSlots() {

        if (slotsMutableLiveData == null)
            slotsMutableLiveData = slotsRepo.requestPickupSlots(context);
        return slotsMutableLiveData;

    }
}