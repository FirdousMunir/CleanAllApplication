package com.example.cleanapplication.view_model;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cleanapplication.model.MainDaySlot;
import com.example.cleanapplication.model.SlotsModel;
import com.example.cleanapplication.repository.SlotsDeliverRepo;

import java.util.List;

public class DeliverViewModel extends ViewModel {

    Context context;
    SlotsDeliverRepo slotsRepo;
    MutableLiveData<List<MainDaySlot>> slotsMutableLiveData;

    public DeliverViewModel(Context c) {
        slotsRepo = new SlotsDeliverRepo();
        context = c;
    }

    public LiveData<List<MainDaySlot>> getDeliverSlots() {

        if (slotsMutableLiveData == null)
            slotsMutableLiveData = slotsRepo.requestDeliverSlots(context);
        return slotsMutableLiveData;

    }
}