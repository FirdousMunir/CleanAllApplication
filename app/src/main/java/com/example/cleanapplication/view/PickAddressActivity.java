package com.example.cleanapplication.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.cleanapplication.R;
import com.example.cleanapplication.adapter.AddressAdapter;
import com.example.cleanapplication.adapter.CategoriesAdapter;
import com.example.cleanapplication.map.LocationActivity;
import com.example.cleanapplication.model.AddressModel;
import com.example.cleanapplication.model.CategoryModel;
import com.example.cleanapplication.view_model.AddressViewModel;
import com.example.cleanapplication.view_model.CategoryViewModel;

import java.util.List;

public class PickAddressActivity extends AppCompatActivity {

String TAG = "PickAddressActivityAddress";
    RecyclerView addressesRecyclerView;
    AddressAdapter addressAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_address);

        initViews();

        AddressViewModel addressViewModel = new AddressViewModel(PickAddressActivity.this);
        try {
            addressViewModel.getAddresses().observe(PickAddressActivity.this, new Observer<List<AddressModel>>() {
                @Override
                public void onChanged(List<AddressModel> categoryModels) {
                    addressAdapter = new AddressAdapter();
                    addressesRecyclerView.setLayoutManager(new LinearLayoutManager(PickAddressActivity.this, LinearLayoutManager.VERTICAL, false));
                    addressesRecyclerView.setAdapter(addressAdapter);
                    addressAdapter.callAddresses(categoryModels, PickAddressActivity.this);
                    addressAdapter.notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            Log.i(TAG, "AddressesMainException  " + e.getMessage());
        }

    }

    void initViews(){
        addressesRecyclerView = findViewById(R.id.addressesRecyclerView);

    }

    public void goToLocationActivity(View view) {
        startActivity(new Intent(PickAddressActivity.this, LocationActivity.class));
    }
}