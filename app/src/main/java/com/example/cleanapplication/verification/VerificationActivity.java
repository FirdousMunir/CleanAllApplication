package com.example.cleanapplication.verification;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cleanapplication.BaseAPI;
import com.example.cleanapplication.R;
import com.example.cleanapplication.SharedPrefsData;
import com.example.cleanapplication.view.MainActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class VerificationActivity extends AppCompatActivity {

    EditText editTextUSerNumber, editTextCodeNumber;
    Button buttonSendCode, buttonCodeVerify;
    LinearLayout linearLayoutNumber, linearLayoutCode;
    String userNumber, codeNumber;
    AsyncHttpClient client;
    BaseAPI baseAPI;
    RequestParams params;
    SharedPrefsData sharedPrefsData;
    String loginUserToken;
    String verificationURL;
    String TAG = "VerificationActivityVerify";

    Random rNo = new Random();
    final int code = rNo.nextInt((99999 - 10000) + 1) + 10000;
    BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        initViews();

        if (linearLayoutNumber.getVisibility() == View.GONE) {
            linearLayoutNumber.setVisibility(View.VISIBLE);
            linearLayoutCode.setVisibility(View.GONE);
        }

        buttonSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNumber = editTextUSerNumber.getText().toString();

                if (userNumber.isEmpty()){
                    editTextUSerNumber.setError("Please Enter Number");
                    editTextUSerNumber.setFocusable(true);
                }else {
                    userNumber = normalizeMsisdn(userNumber);
                    Log.i("CheckNumber", userNumber);
                    sendVerificationNumApi(userNumber);
                }

                /*final CountDownTimer timer = new CountDownTimer(120000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        Log.v("ranjapp", "Ticking " + millisUntilFinished / 1000);
//                        progressdialog.setMessage("Waiting for the message " + millisUntilFinished / 1000);
                    }

                    @Override
                    public void onFinish() {
                        unregisterReceiver(receiver);
//                        progressdialog.dismiss();
                    }
                }.start();

                receiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        Bundle bundle = intent.getExtras();
                        if (bundle != null) {
                            if (readSMS(intent, code)) {
                                Log.v("ranjapp", "SMS read");
                                timer.cancel();
                                try {
                                   unregisterReceiver(receiver);
                                } catch (Exception e) {
                                }
                            }
                        }
                    }
                };
               registerReceiver(receiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));*/
            }
        });

        buttonCodeVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeNumber = editTextCodeNumber.getText().toString();
                sendCodeVerificationApi(codeNumber);
            }
        });


    }

    void initViews() {

        linearLayoutCode = findViewById(R.id.linearLayoutCodeVerification);
        linearLayoutNumber = findViewById(R.id.linearLayoutNumberVerification);
        editTextUSerNumber = findViewById(R.id.editTextUserNumber);
        editTextCodeNumber = findViewById(R.id.editTextCodeNumber);
        buttonSendCode = findViewById(R.id.buttonSendVerification);
        buttonCodeVerify = findViewById(R.id.buttonCodeVerification);
        baseAPI = new BaseAPI(VerificationActivity.this);
        verificationURL = baseAPI.BASE_URL;
        sharedPrefsData = new SharedPrefsData(VerificationActivity.this);
        client = new AsyncHttpClient();
    }

    void sendVerificationNumApi(String number) {

        params = new RequestParams();
        verificationURL = baseAPI.BASE_URL;
        verificationURL = verificationURL + "user/verify";
        sharedPrefsData.getTokenFromPreference();
        loginUserToken = sharedPrefsData.loginUserToken;

        params.put("phone", number);

        client.addHeader("Authorization", "Bearer " + loginUserToken);
        client.post(verificationURL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i(TAG, "OnSuccessNumberVerification  " + response);

                linearLayoutNumber.setVisibility(View.GONE);
                linearLayoutCode.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.i(TAG, "OnFailureNumberVerification " + responseString);
            }
        });

    }

    void sendCodeVerificationApi(String codeNum) {
        params = new RequestParams();
        verificationURL = baseAPI.BASE_URL;
        verificationURL = verificationURL + "user/verify";
        sharedPrefsData.getTokenFromPreference();
        loginUserToken = sharedPrefsData.loginUserToken;

        params.put("code", codeNum);

        client.addHeader("Authorization", "Bearer " + loginUserToken);
        client.post(verificationURL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i(TAG, "OnSuccessCodeVerification " + response);
                nextActivity();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.i(TAG, "OnFailureCodeVerification " + throwable.getMessage());
            }
        });
    }

    void nextActivity() {
        startActivity(new Intent(VerificationActivity.this, MainActivity.class));
    }

    String normalizeMsisdn(String msisdn) {
        if (msisdn == null) {
            return msisdn;
        }
        if (msisdn.startsWith("3") && msisdn.length() == 10) {
            msisdn = "+92" + msisdn;
            return msisdn;
        } else if (msisdn.startsWith("03") && msisdn.length() == 11) {
            msisdn = msisdn.substring(1);
            msisdn = "+92" + msisdn;
            return msisdn;
        } else if (msisdn.startsWith("92") && msisdn.length() == 12) {
            msisdn = msisdn.substring(2);
            msisdn = "+92" + msisdn;
            return msisdn;
        } else if (msisdn.startsWith("+92") && msisdn.length() == 13) {
            return msisdn;
        } else if (msisdn.startsWith("0092") && msisdn.length() == 14) {
            msisdn = msisdn.substring(4);
            msisdn = "+92" + msisdn;
            return msisdn;
        } else {
            return msisdn;
        }
    }

}