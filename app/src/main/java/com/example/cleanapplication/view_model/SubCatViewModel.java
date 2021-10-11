package com.example.cleanapplication.view_model;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cleanapplication.model.CategoryModel;
import com.example.cleanapplication.model.SubCatModel;
import com.example.cleanapplication.repository.CategoryRepo;
import com.example.cleanapplication.repository.SubCatRepo;

import java.util.List;

public class SubCatViewModel  extends ViewModel {

    String categoryID;
    Context context;
    SubCatRepo subCategoryRepo;
    MutableLiveData<List<SubCatModel>> subCatMutableLiveData;

    public SubCatViewModel(Context c,String catID) {
        subCategoryRepo = new SubCatRepo();
        context = c;
        categoryID = catID;
    }

    public LiveData<List<SubCatModel>> getSubCategories() {

        if (subCatMutableLiveData == null)
            subCatMutableLiveData = subCategoryRepo.requestSubCategories(context , categoryID);
        return subCatMutableLiveData;

    }

}
