package com.example.cleanapplication.view_model;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cleanapplication.model.CategoryModel;
import com.example.cleanapplication.repository.CategoryRepo;

import java.util.List;

public class CategoryViewModel extends ViewModel {

    Context context;
    CategoryRepo categoryRepo;
    MutableLiveData<List<CategoryModel>> catMutableLiveData;

    public CategoryViewModel(Context c) {
        categoryRepo = new CategoryRepo();
        context = c;
    }

    public LiveData<List<CategoryModel>> getCategories() {

        if (catMutableLiveData == null)
            catMutableLiveData = categoryRepo.requestCategories(context);
        return catMutableLiveData;

    }

}
