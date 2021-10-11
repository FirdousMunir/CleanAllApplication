package com.example.cleanapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class PasswordResetActivity extends AppCompatActivity {

    Button buttonPassResetEmail, buttonPassReset;
    EditText editTextResetEmail;
    EditText editTextPassResetEmail, editTextPassword, editTextConfirmPassword;
    LinearLayout linearLayoutLoggedIn, linearLayoutNotLoggedIn;
    AsyncHttpClient client;
    BaseAPI baseAPI;
    String emailResetURL, passwordResetURL;
    RequestParams params;
    SharedPrefsData sharedPrefsData;
    String TAG = "PasswordResetActivityPassword";
    String resetType;
    String loginUserToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        initViews();

        if (resetType.equals("LoggedInUser")) {
            linearLayoutLoggedIn.setVisibility(View.VISIBLE);
            linearLayoutNotLoggedIn.setVisibility(View.GONE);
        }
        if (resetType.equals("NotLoggedInUser")) {
            linearLayoutNotLoggedIn.setVisibility(View.VISIBLE);
            linearLayoutLoggedIn.setVisibility(View.GONE);
        }

    }

    public void initViews() {
        buttonPassResetEmail = findViewById(R.id.buttonResetEmail);
        editTextResetEmail = findViewById(R.id.editTextResetEmail);
        buttonPassReset = findViewById(R.id.buttonResetPassword);
        editTextPassResetEmail = findViewById(R.id.editTextLoggedInResetEmail);
        editTextPassword = findViewById(R.id.editTextLoggedInResetPassword);
        editTextConfirmPassword = findViewById(R.id.editTextLoggedInResetConfirmPass);
        linearLayoutLoggedIn = findViewById(R.id.linearLayoutLoggedInUser);
        linearLayoutNotLoggedIn = findViewById(R.id.linearLayoutNotLoggedInUser);
        client = new AsyncHttpClient();
        baseAPI = new BaseAPI(PasswordResetActivity.this);
        emailResetURL = baseAPI.BASE_URL;
        passwordResetURL = baseAPI.BASE_URL;
        sharedPrefsData = new SharedPrefsData(PasswordResetActivity.this);
        resetType = sharedPrefsData.getPasswordResetType();
        sharedPrefsData.getTokenFromPreference();
        loginUserToken = sharedPrefsData.loginUserToken;
    }

    public void sendPasswordResetEmail(View view) {
        String resetEmail = editTextResetEmail.getText().toString();
        if (resetEmail.isEmpty()) {
            Toast.makeText(PasswordResetActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
        } else if (isEmailValid(resetEmail)) {
            sendPasswordEmail(resetEmail);
        } else {
            Toast.makeText(PasswordResetActivity.this, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendPasswordEmail(String email) {
        emailResetURL = emailResetURL + "password/email";
        params = new RequestParams();
        Log.i(TAG, "checkURL: " + emailResetURL);
        try {
            params.put("email", email);

        } catch (Exception e) {
            Log.i(TAG, "ResetEmailParametersException" + e.getMessage());
        }

        client.post(emailResetURL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i(TAG, "OnSuccess");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.i(TAG, "OnFailure");
            }
        });

    }

    public boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void setPasswordReset(View view) {
        String resetEmail = editTextPassResetEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();
        if (resetEmail.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(PasswordResetActivity.this, "Please Enter Credentials", Toast.LENGTH_SHORT).show();
        } else if (isEmailValid(resetEmail)) {
            if(password.equals(confirmPassword)){
                setPassword(resetEmail,password,confirmPassword);
            }else{
                Toast.makeText(this, "Make sure you entered same password", Toast.LENGTH_SHORT).show();
                editTextPassword.setText("");
                editTextConfirmPassword.setText("");
                editTextPassword.setFocusable(true);
            }
        } else {
            Toast.makeText(PasswordResetActivity.this, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
        }
    }

    void setPassword(String email, String pass, String confirmPass){

        passwordResetURL = passwordResetURL + "password/reset" ;
        params = new RequestParams();
        Log.i(TAG, "checkPasswordURL: " + passwordResetURL);
        Log.i(TAG, "TokenCheck" + loginUserToken);
        try {
            params.put("token", loginUserToken);
            params.put("email", email);
            params.put("password", pass );
            params.put("password_confirmation", confirmPass);

        } catch (Exception e) {
            Log.i(TAG, "ResetEmailParametersException" + e.getMessage());
        }


        client.post(passwordResetURL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i(TAG, "OnSuccess");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.i(TAG, "OnFailure");
            }
        });
    }
}