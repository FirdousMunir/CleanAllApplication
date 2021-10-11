package com.example.cleanapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cleanapplication.PasswordResetActivity;
import com.example.cleanapplication.R;
import com.example.cleanapplication.SharedPrefsData;
import com.example.cleanapplication.adapter.CategoriesAdapter;
import com.example.cleanapplication.map.LocationActivity;
import com.example.cleanapplication.model.CategoryModel;
import com.example.cleanapplication.view_model.CategoryViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    SharedPrefsData sharedPrefsData;
    RecyclerView recyclerViewCategories;
    CategoriesAdapter categoriesAdapter;
    String TAG = "MainActivityTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        CategoryViewModel categoryViewModel = new CategoryViewModel(MainActivity.this);
        try {
            categoryViewModel.getCategories().observe(MainActivity.this, new Observer<List<CategoryModel>>() {
                @Override
                public void onChanged(List<CategoryModel> categoryModels) {
                    categoriesAdapter = new CategoriesAdapter();
                    recyclerViewCategories.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
                    recyclerViewCategories.setAdapter(categoriesAdapter);
                    categoriesAdapter.callCategories(categoryModels, MainActivity.this);
                    categoriesAdapter.notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            Log.i(TAG, "CategoriesMainException  " + e.getMessage());
        }

    }

    void initViews() {
        recyclerViewCategories = findViewById(R.id.categoriesRecyclerView);
        sharedPrefsData = new SharedPrefsData(MainActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_menu_location)
            startActivity(new Intent(MainActivity.this, LocationActivity.class));
        if (id == R.id.action_menu_reset_password) {
            sharedPrefsData.setPasswordResetType("LoggedInUser");
            startActivity(new Intent(MainActivity.this, PasswordResetActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}