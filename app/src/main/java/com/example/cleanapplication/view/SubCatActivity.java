package com.example.cleanapplication.view;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cleanapplication.R;
import com.example.cleanapplication.adapter.SubCatAdapter;
import com.example.cleanapplication.model.SubCatModel;
import com.example.cleanapplication.view_model.SubCatViewModel;

import java.util.List;

public class SubCatActivity extends AppCompatActivity {

    String categoryID;
    RecyclerView recyclerViewSubCategories;
    SubCatAdapter subCategoriesAdapter;
    String TAG = "SubCatActivityActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_cat);

        recyclerViewSubCategories = findViewById(R.id.subCategoriesRecyclerView);

        Bundle bundle = getIntent().getExtras();
        String catID = bundle.getString("CategoryID");

        if (catID != null) {
            categoryID = catID;


            SubCatViewModel subCategoryViewModel = new SubCatViewModel(SubCatActivity.this, categoryID);
            try {
                subCategoryViewModel.getSubCategories().observe(SubCatActivity.this, new Observer<List<SubCatModel>>() {
                    @Override
                    public void onChanged(List<SubCatModel> categoryModels) {
                        subCategoriesAdapter = new SubCatAdapter();
                        recyclerViewSubCategories.setLayoutManager(new LinearLayoutManager(SubCatActivity.this, LinearLayoutManager.VERTICAL, false));
                        recyclerViewSubCategories.setAdapter(subCategoriesAdapter);
                        subCategoriesAdapter.callSubCategories(categoryModels, SubCatActivity.this);
                        subCategoriesAdapter.notifyDataSetChanged();
                    }
                });
            } catch (Exception e) {
                Log.i(TAG, "SubCategoriesMainException  " + e.getMessage());
            }

        }
    }
}