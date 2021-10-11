package com.example.cleanapplication.view_model;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cleanapplication.model.AddressModel;
import com.example.cleanapplication.model.CategoryModel;
import com.example.cleanapplication.repository.AddressRepo;
import com.example.cleanapplication.repository.CategoryRepo;

import java.util.List;

public class AddressViewModel extends ViewModel {

    Context context;
    AddressRepo addressRepo;
    MutableLiveData<List<AddressModel>> addressMutableLiveData;

    public AddressViewModel(Context c) {
        addressRepo = new AddressRepo();
        context = c;
    }

    public LiveData<List<AddressModel>> getAddresses() {

        if (addressMutableLiveData == null)
            addressMutableLiveData = addressRepo.requestAddresses(context);
        return addressMutableLiveData;

    }

}
