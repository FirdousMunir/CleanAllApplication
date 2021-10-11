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

public class SlotsDeliverRepo {
    Context context;
    private final String TAG = "SlotsDeliverRepoSlots";
    AsyncHttpClient client = new AsyncHttpClient();
    BaseAPI baseAPI = new BaseAPI();
    SharedPrefsData sharedPrefsData;
    String slots_URL = baseAPI.BASE_URL;
    ArrayList<SlotsModel> slotsModelList = new ArrayList<>();
    ArrayList<MainDaySlot> maindayslotlist = new ArrayList<>();
    String loginUSerToken;
    String pickupID;
    String authType;
    ProgDialog progressDialog;
    int serviceType = 0;

    public MutableLiveData<List<MainDaySlot>> requestDeliverSlots(Context c) {

        context = c;
        progressDialog = new ProgDialog(context);
        progressDialog.showProgress();

        sharedPrefsData = new SharedPrefsData(context);
        sharedPrefsData.getTokenFromPreference();
        loginUSerToken = sharedPrefsData.loginUserToken;
        authType = sharedPrefsData.authType;
        pickupID = sharedPrefsData.getPickupID();
        Log.i("nnbh76Pick", pickupID);

        slots_URL = slots_URL + "slots?pickup=" + pickupID + "&express=" + serviceType;

        final MutableLiveData<List<MainDaySlot>> slotListMutableLiveData = new MutableLiveData<>();
        Log.i(TAG, "SlotsDeliverToken " + loginUSerToken + " Type: " + sharedPrefsData.authType);
        client.addHeader("Authorization", "Bearer " + loginUSerToken);
        client.get(slots_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.hideProgress();

                Log.i(TAG, "DeliverSlotsResponse  " + response.toString());
                try {
                    JSONArray DeliverMainArray = response.getJSONArray("delivery");
                    JSONArray slotsDeliverSecArray;

                    if (serviceType == 1) {
                        for (int j = 0; j < DeliverMainArray.length(); j++) {
                            String name = DeliverMainArray.getJSONObject(j).getString("name");
//                        String weekDayName = DeliverExpressArray.getJSONObject(j).getString("day");
                            int id = DeliverMainArray.getJSONObject(j).getInt("id");
                            String timeStart = DeliverMainArray.getJSONObject(j).getString("start");
                            String timeEnd = DeliverMainArray.getJSONObject(j).getString("end");
                            int type = DeliverMainArray.getJSONObject(j).getInt("type");
                            int regular = DeliverMainArray.getJSONObject(j).getInt("regular");
                            int dayOfWeek = DeliverMainArray.getJSONObject(j).getInt("day_of_week");
                            String fullText = DeliverMainArray.getJSONObject(j).getString("full_text");
                            String startDateTime = DeliverMainArray.getJSONObject(j).getString("start_date_time");
                            String date = DeliverMainArray.getJSONObject(j).getString("date");
                            String createdAt = DeliverMainArray.getJSONObject(j).getString("created_at");
                            String updatedAt = DeliverMainArray.getJSONObject(j).getString("updated_at");
                            String availableTime = timeStart + "-" + timeEnd;

                            Log.i(TAG, "checkExpressType: " + date +" : " + availableTime);
                            slotsModelList.add(new SlotsModel(name, date, availableTime, id + ""));
                        }
                    }

                    if (serviceType == 0) {
                        List<SlotsModel> mylist = new ArrayList<>();
                        for (int i = 0; i < DeliverMainArray.length(); i++) {
                            slotsDeliverSecArray = DeliverMainArray.getJSONArray(i);
                            String dayname = "";
                            String dateSec = "";
                            for (int j = 0; j < slotsDeliverSecArray.length(); j++) {

                                dayname = slotsDeliverSecArray.getJSONObject(j).getString("name");
                                dateSec = slotsDeliverSecArray.getJSONObject(j).getString("date");

                                String name = slotsDeliverSecArray.getJSONObject(j).getString("name");
                                String weekDayName = slotsDeliverSecArray.getJSONObject(j).getString("day");
                                int id = slotsDeliverSecArray.getJSONObject(j).getInt("id");
                                String timeStart = slotsDeliverSecArray.getJSONObject(j).getString("start");
                                String timeEnd = slotsDeliverSecArray.getJSONObject(j).getString("end");
                                int type = slotsDeliverSecArray.getJSONObject(j).getInt("type");
                                int regular = slotsDeliverSecArray.getJSONObject(j).getInt("regular");
                                int dayOfWeek = slotsDeliverSecArray.getJSONObject(j).getInt("day_of_week");
                                String fullText = slotsDeliverSecArray.getJSONObject(j).getString("full_text");
                                String startDateTime = slotsDeliverSecArray.getJSONObject(j).getString("start_date_time");
                                String date = slotsDeliverSecArray.getJSONObject(j).getString("date");
                                String createdAt = slotsDeliverSecArray.getJSONObject(j).getString("created_at");
                                String updatedAt = slotsDeliverSecArray.getJSONObject(j).getString("updated_at");
                                String availableTime = timeStart + "-" + timeEnd;

                                Log.i(TAG, "checkRegularType: " + date + " : " + availableTime);
                                slotsModelList.add(new SlotsModel(name, date, availableTime, id + ""));
                                mylist.add(new SlotsModel(name, date, availableTime, id + ""));
                            }
                            Log.i("testListData" , dayname +" ::: " + mylist.size());
                            maindayslotlist.add(new MainDaySlot(dayname, dateSec,mylist));
                            Log.i("secondTryList", maindayslotlist.size() + "");
                            Log.i("secondTryListName", "name:::: " + dayname);
                        }


                    }

                    slotListMutableLiveData.setValue(maindayslotlist);
                } catch (Exception e) {
                    Log.i(TAG, "DeliverSlotsResponseException  " + e.getMessage());
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressDialog.hideProgress();
                Log.i(TAG, "DeliverSlotsOnFailure" + throwable.getMessage());
                super.onFailure(statusCode, headers, responseString, throwable);
            }

        });
        return slotListMutableLiveData;
    }
}