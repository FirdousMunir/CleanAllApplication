package com.example.cleanapplication;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

public class BaseAPI {

    public String BASE_URL = "https://clean.daigiboy.com/api/";
    AsyncHttpClient client = new AsyncHttpClient();
    Context context;
    //    SharedPrefsData sharedPrefsData = new SharedPrefsData(context);
//    String token = sharedPrefsData.getTokenFromPreference();
    RequestParams params = new RequestParams();

    public BaseAPI() {
    }

    public BaseAPI(Context context) {
        this.context = context;
    }

    public void get(String url, JsonHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), responseHandler);
    }

    public void post(String url, RequestParams params, ResponseHandlerInterface responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }


    public void postWithParams(String url, String googleID, String googleToken, String deviceToken, String firstName, String lastName, AsyncHttpResponseHandler responseHandler) {
        params.put("google_id", googleID);
        params.put("google_token", googleToken);
        params.put("device_token", deviceToken);
        params.put("first_name", firstName);
        params.put("last_name", lastName);

        client.post(getAbsoluteUrl(url), params, responseHandler);
    }


    public void postWithHeader(Context con, String url, ResponseHandlerInterface responseHandler) {
        SharedPrefsData sharedPrefsData = new SharedPrefsData(con);
        sharedPrefsData.getTokenFromPreference();
        String token = sharedPrefsData.loginUserToken;
        client.addHeader("Authorization", "Bearer " + token);
        client.post(getAbsoluteUrl(url), responseHandler);
    }


    String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

}
