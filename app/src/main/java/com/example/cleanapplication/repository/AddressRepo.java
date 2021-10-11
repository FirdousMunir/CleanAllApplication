package com.example.cleanapplication.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.cleanapplication.BaseAPI;
import com.example.cleanapplication.ProgDialog;
import com.example.cleanapplication.SharedPrefsData;
import com.example.cleanapplication.model.AddressModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class AddressRepo {

    Context context;
    private final String TAG = "AddressRepo";
    AsyncHttpClient client = new AsyncHttpClient();
    BaseAPI baseAPI = new BaseAPI();
    SharedPrefsData sharedPrefsData;
    String address_URL = baseAPI.BASE_URL;
    ArrayList<AddressModel> addressModelList = new ArrayList<>();
    String loginUSerToken;
    String authType;
    ProgDialog progressDialog;

    public MutableLiveData<List<AddressModel>> requestAddresses(Context c) {

        context = c;
        address_URL = address_URL + "addresses";

        progressDialog = new ProgDialog(context);
        progressDialog.showProgress();

        sharedPrefsData = new SharedPrefsData(context);
        sharedPrefsData.getTokenFromPreference();
        loginUSerToken = sharedPrefsData.loginUserToken;
        authType = sharedPrefsData.authType;

        final MutableLiveData<List<AddressModel>> addressListMutableLiveData = new MutableLiveData<>();
        Log.i("AddressesToken", loginUSerToken + " Type: " + sharedPrefsData.authType);

        client.addHeader("Accept", "application/json");
        client.addHeader("Authorization", "Bearer " + loginUSerToken);
        client.get(address_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                progressDialog.hideProgress();

                JSONObject firsObject = response;
                Log.i(TAG, "AddressesResponse  " + response.toString());
                try {
                    JSONArray addressArray = firsObject.getJSONArray("addresses");

                    for (int i = 0; i < addressArray.length(); i++) {
                        String address = addressArray.getJSONObject(i).getString("text");
                        String userID = addressArray.getJSONObject(i).getString("user_id");
                        String addressID = addressArray.getJSONObject(i).getString("id");
                        String createdAt = addressArray.getJSONObject(i).getString("created_at");
                        String updatedAt = addressArray.getJSONObject(i).getString("updated_at");
                        float lat = addressArray.getJSONObject(i).getLong("latitude");
                        float lng = addressArray.getJSONObject(i).getLong("longitude");

                        addressModelList.add(new AddressModel(address, addressID, userID));
                    }

                    addressListMutableLiveData.setValue(addressModelList);

                } catch (Exception e) {
                    Log.i(TAG, "AddressesResponseException  " + e.getMessage());
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressDialog.hideProgress();
                Log.i(TAG, "AddressesOnFailureString");
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.hideProgress();
                Log.i(TAG, "AddressesOnFailureObject" + errorResponse);
            }
        });
        return addressListMutableLiveData;
    }


}
