package com.example.cleanapplication.view_model;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cleanapplication.model.ProductModel;
import com.example.cleanapplication.repository.ProductRepo;

import java.util.List;

public class ProductViewModel extends ViewModel {

    String subCategoryID;
    Context context;
    private ProductRepo productRepo;
    private MutableLiveData<List<ProductModel>> productLiveData;

    public ProductViewModel(String subCategoryID, Context context) {
        productRepo = new ProductRepo();
        this.subCategoryID = subCategoryID;
        this.context = context;
    }

    public LiveData<List<ProductModel>> getCategoryProducts() {
        if (productLiveData == null)
            productLiveData = productRepo.getCategoryProducts(context, subCategoryID);
        return productLiveData;
    }
}
