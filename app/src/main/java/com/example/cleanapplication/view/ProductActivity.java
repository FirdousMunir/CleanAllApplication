package com.example.cleanapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cleanapplication.BaseAPI;
import com.example.cleanapplication.LoginActivity;
import com.example.cleanapplication.R;
import com.example.cleanapplication.SharedPrefsData;
import com.example.cleanapplication.adapter.ProductAdapter;
import com.example.cleanapplication.cartdata.CartActivity;
import com.example.cleanapplication.model.ProductModel;
import com.example.cleanapplication.view_model.ProductViewModel;
import com.facebook.login.LoginManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class ProductActivity extends AppCompatActivity {

    String subCatID;
    ProductAdapter productAdapter;
    RecyclerView productsRecyclerView;
    String TAG = "ProductActivity";
    AsyncHttpClient client;
    SharedPrefsData sharedPrefsData;
    BaseAPI baseAPI;
    String loginToken, logout_URL, authType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        sharedPrefsData = new SharedPrefsData(ProductActivity.this);
        productsRecyclerView = findViewById(R.id.productsRecyclerView);

        Bundle bundle = getIntent().getExtras();
        String catID = bundle.getString("SubCategoryID");

        if (catID != null) {
            subCatID = catID;

            ProductViewModel productViewModel = new ProductViewModel(subCatID, ProductActivity.this);
            try {
                productViewModel.getCategoryProducts().observe(ProductActivity.this, new Observer<List<ProductModel>>() {
                    @Override
                    public void onChanged(List<ProductModel> productModelList) {
                        productAdapter = new ProductAdapter();
                        productsRecyclerView.setLayoutManager(new LinearLayoutManager(ProductActivity.this, LinearLayoutManager.VERTICAL, false));
                        productsRecyclerView.setAdapter(productAdapter);
                        productAdapter.categoriesProducts(productModelList, ProductActivity.this);
                        productAdapter.notifyDataSetChanged();
                    }
                });
            } catch (Exception e) {
                Log.i(TAG, "SubCategoriesMainException  " + e.getMessage());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemID = item.getItemId();
        if (itemID == R.id.action_menu_cart) {

            sharedPrefsData.setPickDeliver(false);
            startActivity(new Intent(ProductActivity.this, CartActivity.class));
        } else if (itemID == R.id.action_menu_logout) {
            logoutForFacebook();
        }
        return super.onOptionsItemSelected(item);
    }

    public void logoutForFacebook() {

        sharedPrefsData.getTokenFromPreference();
        loginToken = sharedPrefsData.loginUserToken;
        authType = sharedPrefsData.authType;

        if (authType.equals("Facebook") && loginToken != null) {
            Log.i("ProductActivityToken", loginToken + ":::" + authType + "Facebook");
            Toast.makeText(this, "Facebook", Toast.LENGTH_SHORT).show();
            LoginManager.getInstance().logOut();
            callLogoutApi();
        } else if (authType.equals("Google") && loginToken != null) {
            Log.i("ProductActivityToken", loginToken + ":::" + authType + "google");
            Toast.makeText(this, "Google", Toast.LENGTH_SHORT).show();
        } else {
            Log.i("ProductActivityToken", loginToken + ":::" + authType + "else");
            Toast.makeText(this, "User Created", Toast.LENGTH_SHORT).show();
            callLogoutApi();
        }
    }

    void callLogoutApi() {
        client = new AsyncHttpClient();
        baseAPI = new BaseAPI();
        logout_URL = baseAPI.BASE_URL;
        logout_URL = logout_URL + "logout";

        Log.i("LoginUSerToken", "Token:::" + loginToken);
        client.addHeader("Authorization", "Bearer " + loginToken);
        client.post(logout_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String resp = response.toString();
                Log.i("ResponseOnSuccessObject", resp);

                sharedPrefsData.setValueInPreference("", false, "");

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                Toast.makeText(ProductActivity.this, "Successfully Logout", Toast.LENGTH_SHORT).show();
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("ResponseOnFailureString", Objects.requireNonNull(throwable.getMessage()));
                Toast.makeText(ProductActivity.this, "Exception on Logout", Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}