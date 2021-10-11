package com.example.cleanapplication.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.cleanapplication.BaseAPI;
import com.example.cleanapplication.ProgDialog;
import com.example.cleanapplication.SharedPrefsData;
import com.example.cleanapplication.model.SubCatModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SubCatRepo {

    Context context;
    private final String TAG = "SubCatRepo";
    AsyncHttpClient client = new AsyncHttpClient();
    BaseAPI baseAPI = new BaseAPI();
    SharedPrefsData sharedPrefsData;
    String subCategory_URL = baseAPI.BASE_URL;
    ArrayList<SubCatModel> subCategoryModelList = new ArrayList<>();
    String loginUSerToken;
    String categoryID;
    ProgDialog progressDialog;

    public MutableLiveData<List<SubCatModel>> requestSubCategories(Context c, String catID) {

        categoryID = catID;
        context = c;
        subCategory_URL = subCategory_URL + "categories/" + categoryID;

        progressDialog = new ProgDialog(context);
        progressDialog.showProgress();

        sharedPrefsData = new SharedPrefsData(context);
        sharedPrefsData.getTokenFromPreference();
        loginUSerToken = sharedPrefsData.loginUserToken;
        final MutableLiveData<List<SubCatModel>> catListMutableLiveData = new MutableLiveData<>();
        Log.i("blablablaSubCategory", loginUSerToken);

        client.addHeader("Authorization", "Bearer " + loginUSerToken);
        client.get(subCategory_URL, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.hideProgress();

                JSONObject firsObject = response;
                Log.i(TAG, "SubCategoryResponse  " + response.toString());
                try {
                    JSONArray SubCategoryArray = firsObject.getJSONArray("categories");

                    for (int i = 0; i < SubCategoryArray.length(); i++) {
                        String subCatName = SubCategoryArray.getJSONObject(i).getString("name");
                        String subCatId = SubCategoryArray.getJSONObject(i).getString("id");
                        String parent_id = SubCategoryArray.getJSONObject(i).getString("parent_id");
                        String subCatImage = SubCategoryArray.getJSONObject(i).getString("image");
                        String subCatDesc = SubCategoryArray.getJSONObject(i).getString("description");
                        String subCatActive = SubCategoryArray.getJSONObject(i).getString("active");
                        String subCatStatusText = SubCategoryArray.getJSONObject(i).getString("status_text");
                        subCategoryModelList.add(new SubCatModel(subCatId, subCatName));
                    }

                    catListMutableLiveData.setValue(subCategoryModelList);
                } catch (Exception e) {
                    Log.i(TAG, "SubCategoryResponseException  " + e.getMessage());
                }

                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressDialog.hideProgress();
                Log.i(TAG, "SubCategoryOnFailure");
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
        return catListMutableLiveData;
    }

}
