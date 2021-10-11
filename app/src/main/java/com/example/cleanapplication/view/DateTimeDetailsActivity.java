package com.example.cleanapplication.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cleanapplication.R;
import com.example.cleanapplication.SharedPrefsData;
import com.example.cleanapplication.adapter.SlotsPickupAdapter;
import com.example.cleanapplication.model.SlotsModel;
import com.example.cleanapplication.view_model.PickupViewModel;

import java.util.List;

public class DateTimeDetailsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time_details);


    }


}