package com.example.cleanapplication.cartdata;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cleanapplication.BaseAPI;
import com.example.cleanapplication.R;
import com.example.cleanapplication.SharedPrefsData;
import com.example.cleanapplication.map.LocationActivity;
import com.example.cleanapplication.view.PickAddressActivity;
import com.example.cleanapplication.model.ProductModel;
import com.example.cleanapplication.view.PickupDetailsActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    RecyclerView cartRecyclerView;
    SharedPrefsData sharedPrefsData;
    TextView pickupEdit, deliverEdit;
    TextView tvPickupTime, tvPickupDate;
    TextView tvDeliverDate, tvDeliverTime;
    TextView textView;
    Button buttonCheckOut;
    Gson gson;
    String TAG = "CartActivityCart";
    String pickupID, pickupSelectedDate, pickupSelectedTime;
    String deliverID, deliverSelectedDate, deliverSelectedTime;
    Boolean pickDeliverResult = false;
    String result;
    AsyncHttpClient client;
    BaseAPI baseAPI;
    String loginUserToken;
    String orderURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initViews();
        result = sharedPrefsData.getCartValuesFromPrefs();
        Log.i(TAG, result);
        textView.setText(result);

        if (pickDeliverResult) {
            getSetPickupDetails();
            getSetDeliverDetails();
        } else {
            Toast.makeText(this, "PickupDeliver False", Toast.LENGTH_SHORT).show();
        }

        if (result.isEmpty()) {
            Toast.makeText(this, "Result is empty", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Result contains values", Toast.LENGTH_SHORT).show();
            gson = new Gson();
            try {
                 List<ProductModel> productModellist = gson.fromJson(result, new TypeToken<List<ProductModel>>() {}.getType());
                 for (ProductModel m : productModellist){
                     String name = m.getProductName();
                     Log.i(TAG, name + ":");
                 }
            }catch (Exception e){
                Log.i(TAG, "Exception: " + e.getMessage());
            }

        }
    }

    void initViews() {
        buttonCheckOut = findViewById(R.id.buttonCheckOut);
        tvDeliverDate = findViewById(R.id.textViewUserDeliverDate);
        tvDeliverTime = findViewById(R.id.textViewUserDeliverTime);
        tvPickupDate = findViewById(R.id.textViewUserPickupDate);
        tvPickupTime = findViewById(R.id.textViewUserPickupTime);
        deliverEdit = findViewById(R.id.textViewDeliverDetailsEdit);
        pickupEdit = findViewById(R.id.textViewPickupDetailsEdit);
        textView = findViewById(R.id.textViewCartData);
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        sharedPrefsData = new SharedPrefsData(CartActivity.this);
        sharedPrefsData.getPickDeliver();
        sharedPrefsData.getTokenFromPreference();
        loginUserToken = sharedPrefsData.loginUserToken;
        pickDeliverResult = sharedPrefsData.pickDeliver;
        client = new AsyncHttpClient();
        client.addHeader("Accept", "application/json");
        baseAPI = new BaseAPI(CartActivity.this);
        orderURL = baseAPI.BASE_URL;
    }

    void getSetPickupDetails() {
        sharedPrefsData.getPickupID();
        pickupID = sharedPrefsData.pickupID;
        pickupSelectedDate = sharedPrefsData.pickupDate;
        pickupSelectedTime = sharedPrefsData.pickupTime;
        tvPickupDate.setText(pickupSelectedDate);
        tvPickupTime.setText(pickupSelectedTime);
    }

    void getSetDeliverDetails() {
        sharedPrefsData.getDeliverID();
        deliverID = sharedPrefsData.deliverID;
        deliverSelectedDate = sharedPrefsData.deliverDate;
        deliverSelectedTime = sharedPrefsData.deliverTime;
        tvDeliverDate.setText(deliverSelectedDate);
        tvDeliverTime.setText(deliverSelectedTime);
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
            startActivity(new Intent(CartActivity.this, LocationActivity.class));

        return super.onOptionsItemSelected(item);
    }

    public void pickupDetails(View view) {
        moveToDateTimeDetails();
    }

    public void deliverDetails(View view) {
        moveToDateTimeDetails();
    }

    public void moveToDateTimeDetails() {
        startActivity(new Intent(CartActivity.this, PickupDetailsActivity.class));
    }

    public void pickAddresses(View view) {
        startActivity(new Intent(CartActivity.this, PickAddressActivity.class));
    }
}