package com.example.cleanapplication.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.cleanapplication.BaseAPI;
import com.example.cleanapplication.ProgDialog;
import com.example.cleanapplication.SharedPrefsData;
import com.example.cleanapplication.model.ProductModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ProductRepo {
    Context context;
    private final String TAG = "ProductRepo";
    AsyncHttpClient client = new AsyncHttpClient();
    BaseAPI baseAPI = new BaseAPI();
    String categoryProduct_URL = baseAPI.BASE_URL;
    ArrayList<ProductModel> productModelList = new ArrayList<>();
    String subCategoryID;
    ProgDialog progressDialog;

    SharedPrefsData sharedPrefsData;
    String loginToken, type;

    public MutableLiveData<List<ProductModel>> getCategoryProducts(Context c, String catID) {

        subCategoryID = catID;
        context = c;
        categoryProduct_URL = categoryProduct_URL + "categories/" + subCategoryID + "/products";
        progressDialog = new ProgDialog(context);
        progressDialog.showProgress();

        sharedPrefsData = new SharedPrefsData(context);
        sharedPrefsData.getTokenFromPreference();
        loginToken = sharedPrefsData.loginUserToken;
        type = sharedPrefsData.authType;

        final MutableLiveData<List<ProductModel>> productListMutableLiveData = new MutableLiveData<>();

        client.addHeader("Authorization", "Bearer " + loginToken);
        client.get(categoryProduct_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                progressDialog.hideProgress();

                JSONObject firsObject = response;
                Log.i(TAG, "CategoryProductsResponse  " + response.toString());
                try {
                    JSONArray CategoryProductsArray = firsObject.getJSONArray("products");

                    for (int i = 0; i < CategoryProductsArray.length(); i++) {
                        String productName = CategoryProductsArray.getJSONObject(i).getString("name");
                        String productID = CategoryProductsArray.getJSONObject(i).getString("id");
                        String productPrice = CategoryProductsArray.getJSONObject(i).getString("price");
                        String productImage = CategoryProductsArray.getJSONObject(i).getString("image");
                        String productGender = CategoryProductsArray.getJSONObject(i).getString("gender");
                        String active = CategoryProductsArray.getJSONObject(i).getString("active");
                        String statusText = CategoryProductsArray.getJSONObject(i).getString("status_text");
                        String productSlug = CategoryProductsArray.getJSONObject(i).getString("slug");
                        String createdAt = CategoryProductsArray.getJSONObject(i).getString("created_at");
                        String updatedAt = CategoryProductsArray.getJSONObject(i).getString("updated_at");

                        productModelList.add(new ProductModel(productName, "Price: "+productPrice, productID, loginToken, type));
                    }

                    productListMutableLiveData.setValue(productModelList);
                } catch (Exception e) {
                    Log.i(TAG, "categoryProductResponseException  " + e.getMessage());
                }

                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                progressDialog.hideProgress();

                Log.i(TAG, "CategoryProductOnFailure");
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
        return productListMutableLiveData;
    }

}
