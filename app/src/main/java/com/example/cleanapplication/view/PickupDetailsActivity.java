package com.example.cleanapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cleanapplication.R;
import com.example.cleanapplication.adapter.SlotsDeliverAdapter;
import com.example.cleanapplication.adapter.SlotsPickupAdapter;
import com.example.cleanapplication.cartdata.CartActivity;
import com.example.cleanapplication.model.MainDaySlot;
import com.example.cleanapplication.model.SlotsModel;
import com.example.cleanapplication.view_model.DeliverViewModel;
import com.example.cleanapplication.view_model.PickupViewModel;

import java.util.List;

public class PickupDetailsActivity extends AppCompatActivity {

    RelativeLayout relativeLayoutPickup, relativeLayoutDeliver;
    RecyclerView recyclerViewPickup, recyclerViewDeliver;
    Button buttonPickupDone, buttonDeliverDone;

    String TAG = "DateTimeDetailsActivity";
    SlotsPickupAdapter pickupAdapter;
    SlotsDeliverAdapter deliverAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_details);

        initViews();


        PickupViewModel pickupViewModel = new PickupViewModel(PickupDetailsActivity.this);
        try {
            pickupViewModel.getPickupSlots().observe(PickupDetailsActivity.this, new Observer<List<MainDaySlot>>() {
                @Override
                public void onChanged(List<MainDaySlot> slotsPickupModels) {

                    Log.i("DataCheckSlotsPickup", "Slots: " + slotsPickupModels);

                    pickupAdapter = new SlotsPickupAdapter();
                    recyclerViewPickup.setLayoutManager(new LinearLayoutManager(PickupDetailsActivity.this, LinearLayoutManager.VERTICAL, false));
                    recyclerViewPickup.setAdapter(pickupAdapter);
                    pickupAdapter.pickupSlots(slotsPickupModels, PickupDetailsActivity.this);
                    pickupAdapter.notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            Log.i(TAG, "PickupMainException  " + e.getMessage());
        }

    }

    void initViews() {
        relativeLayoutDeliver = findViewById(R.id.relativeLayoutDeliver);
        relativeLayoutPickup = findViewById(R.id.relativeLayoutPickup);
        recyclerViewPickup = findViewById(R.id.pickupRecyclerView);
        recyclerViewDeliver = findViewById(R.id.deliverRecyclerView);
        buttonDeliverDone = findViewById(R.id.buttonDeliverDone);
        buttonPickupDone = findViewById(R.id.buttonPickupDone);
    }

    public void pickupDone(View view) {

//        if (relativeLayoutPickup.getVisibility() == View.VISIBLE) {
//            relativeLayoutPickup.setVisibility(View.GONE);
//            relativeLayoutDeliver.setVisibility(View.VISIBLE);
//        }

        relativeLayoutPickup.setVisibility(View.GONE);
        relativeLayoutDeliver.setVisibility(View.VISIBLE);

        DeliverViewModel deliverViewModel = new DeliverViewModel(PickupDetailsActivity.this);
        try {
            deliverViewModel.getDeliverSlots().observe(PickupDetailsActivity.this, new Observer<List<MainDaySlot>>() {
                @Override
                public void onChanged(List<MainDaySlot> slotsDeliverModels) {

                    Log.i("DataCheckSlotsDeliver", "Slots: " + slotsDeliverModels);

                    deliverAdapter = new SlotsDeliverAdapter();
                    recyclerViewDeliver.setLayoutManager(new LinearLayoutManager(PickupDetailsActivity.this, LinearLayoutManager.VERTICAL, false));
                    recyclerViewDeliver.setAdapter(deliverAdapter);
                    deliverAdapter.deliverSlots(slotsDeliverModels, PickupDetailsActivity.this);
                    deliverAdapter.notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            Log.i(TAG, "DeliverMainException  " + e.getMessage());
        }
    }


    public void deliverDone(View view) {
        startActivity(new Intent(PickupDetailsActivity.this, CartActivity.class));
    }
}