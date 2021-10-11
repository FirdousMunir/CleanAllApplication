package com.example.cleanapplication.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.cleanapplication.BaseAPI;
import com.example.cleanapplication.ProgDialog;
import com.example.cleanapplication.SharedPrefsData;
import com.example.cleanapplication.model.CategoryModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class CategoryRepo {

    Context context;
    private final String TAG = "CategoryRepo";
    AsyncHttpClient client = new AsyncHttpClient();
    BaseAPI baseAPI = new BaseAPI();
    SharedPrefsData sharedPrefsData;
    String category_URL = baseAPI.BASE_URL;
    ArrayList<CategoryModel> categoryModelList = new ArrayList<>();
    String loginUSerToken;
    String authType;
    ProgDialog progressDialog;

    public MutableLiveData<List<CategoryModel>> requestCategories(Context c) {

        context = c;
        category_URL = category_URL + "categories";

        progressDialog = new ProgDialog(context);
        progressDialog.showProgress();

        sharedPrefsData = new SharedPrefsData(context);
        sharedPrefsData.getTokenFromPreference();
        loginUSerToken = sharedPrefsData.loginUserToken;
        authType = sharedPrefsData.authType;

        final MutableLiveData<List<CategoryModel>> catListMutableLiveData = new MutableLiveData<>();
        Log.i("blablablaCategory", loginUSerToken + " Type: " + sharedPrefsData.authType);

        client.addHeader("Accept", "application/json");
        client.addHeader("Authorization", "Bearer " + loginUSerToken);
        client.get(category_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                progressDialog.hideProgress();

                JSONObject firsObject = response;
                Log.i(TAG, "CategoryResponse  " + response.toString());
                try {
                    JSONArray categoryArray = firsObject.getJSONArray("categories");

                    for (int i = 0; i < categoryArray.length(); i++) {
                        String catName = categoryArray.getJSONObject(i).getString("name");
                        String catDesc = categoryArray.getJSONObject(i).getString("description");
                        String catImage = categoryArray.getJSONObject(i).getString("image");
                        String catStatusText = categoryArray.getJSONObject(i).getString("status_text");
                        String catID = categoryArray.getJSONObject(i).getString("id");
                        String catSlug = categoryArray.getJSONObject(i).getString("slug");
                        String catActive = categoryArray.getJSONObject(i).getString("active");
                        String createdAt = categoryArray.getJSONObject(i).getString("created_at");
                        String updatedAt = categoryArray.getJSONObject(i).getString("updated_at");
                        categoryModelList.add(new CategoryModel(catID, catName, loginUSerToken, authType));
                    }

                    catListMutableLiveData.setValue(categoryModelList);

                } catch (Exception e) {
                    Log.i(TAG, "CategoryResponseException  " + e.getMessage());
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressDialog.hideProgress();
                Log.i(TAG, "CategoryOnFailure");
                super.onFailure(statusCode, headers, responseString, throwable);
            }

        });
        return catListMutableLiveData;
    }

}
