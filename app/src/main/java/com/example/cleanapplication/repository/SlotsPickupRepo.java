package com.example.cleanapplication.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.cleanapplication.BaseAPI;
import com.example.cleanapplication.ProgDialog;
import com.example.cleanapplication.SharedPrefsData;
import com.example.cleanapplication.model.MainDaySlot;
import com.example.cleanapplication.model.SlotsModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SlotsPickupRepo {

    Context context;
    private final String TAG = "SlotsPickupRepoSlots";
    AsyncHttpClient client = new AsyncHttpClient();
    BaseAPI baseAPI = new BaseAPI();
    SharedPrefsData sharedPrefsData;
    String slots_URL = baseAPI.BASE_URL;
    ArrayList<SlotsModel> slotsModelList = new ArrayList<>();
    ArrayList<MainDaySlot> maindayslotlist = new ArrayList<>();

    String loginUSerToken;
    String authType;
    ProgDialog progressDialog;

    public MutableLiveData<List<MainDaySlot>> requestPickupSlots(Context c) {

        context = c;
        slots_URL = slots_URL + "slots";

        progressDialog = new ProgDialog(context);
        progressDialog.showProgress();

        sharedPrefsData = new SharedPrefsData(context);
        sharedPrefsData.getTokenFromPreference();
        loginUSerToken = sharedPrefsData.loginUserToken;
        authType = sharedPrefsData.authType;

        final MutableLiveData<List<MainDaySlot>> slotListMutableLiveData = new MutableLiveData<>();
        Log.i(TAG, "SlotsToken " + loginUSerToken + " Type: " + sharedPrefsData.authType);

        client.addHeader("Authorization", "Bearer " + loginUSerToken);
        client.get(slots_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.hideProgress();

                Log.i(TAG, "SlotsPickupResponse  " + response.toString());
                try {
                    JSONArray slotsPickupMainArray = response.getJSONArray("pickup");
                    JSONArray slotsPickupSecArray;

                    List<SlotsModel> mylist = new ArrayList<>();

                    for (int i = 0; i < slotsPickupMainArray.length(); i++) {

                        slotsPickupSecArray = slotsPickupMainArray.getJSONArray(i);
                        String dayname = "";
                        String dateSec = "";

                        for (int j = 0; j < slotsPickupSecArray.length(); j++) {

                            dayname = slotsPickupSecArray.getJSONObject(j).getString("name");
                            dateSec = slotsPickupSecArray.getJSONObject(j).getString("date");

                            String name = slotsPickupSecArray.getJSONObject(j).getString("name");
                            String weekDayName = slotsPickupSecArray.getJSONObject(j).getString("day");
                            int id = slotsPickupSecArray.getJSONObject(j).getInt("id");
                            String timeStart = slotsPickupSecArray.getJSONObject(j).getString("start");
                            String timeEnd = slotsPickupSecArray.getJSONObject(j).getString("end");
//                        int type = slotsPickupArray.getJSONObject(j).getInt("type");
//                        int regular = slotsPickupArray.getJSONObject(j).getInt("regular");
//                        int dayOfWeek = slotsPickupArray.getJSONObject(j).getInt("day_of_week");
//                        String fullText = slotsPickupArray.getJSONObject(j).getString("full_text");
//                        String startDateTime = slotsPickupArray.getJSONObject(j).getString("start_date_time");
                            String date = slotsPickupSecArray.getJSONObject(j).getString("date");
//                        String createdAt = slotsPickupArray.getJSONObject(j).getString("created_at");
//                        String updatedAt = slotsPickupArray.getJSONObject(j).getString("updated_at");
                            String availableTime = timeStart + "-" + timeEnd;

                            Log.i("CheckNAMESlots", "weekDay: " + name);
                            slotsModelList.add(new SlotsModel(name, date, availableTime, id + ""));
                            mylist.add(new SlotsModel(name, date, availableTime, id + ""));
                        }
                        Log.i("testListData" , dayname +" ::: " + mylist.size());
                        maindayslotlist.add(new MainDaySlot(dayname, dateSec,mylist));
                        Log.i("secondTryList", maindayslotlist.size() + "");
                        Log.i("secondTryListName", "name:::: " + dayname);
                    }

                    slotListMutableLiveData.setValue(maindayslotlist);
                } catch (Exception e) {
                    Log.i(TAG, "PickupSlotsResponseException  " + e.getMessage());
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressDialog.hideProgress();
                Log.i(TAG, "PickupSlotsOnFailure");
                super.onFailure(statusCode, headers, responseString, throwable);
            }

        });
        return slotListMutableLiveData;
    }


}
