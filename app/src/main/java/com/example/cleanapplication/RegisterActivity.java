package com.example.cleanapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class RegisterActivity extends AppCompatActivity {

    Button buttonSignUp;
    EditText editTextEmail, editTextPassword, editTextConfirmPass;
    EditText editTextFirstName, editTextLastName;
    BaseAPI baseAPI;
    RequestParams params;
    String TAG = "RegisterActivityRegister";
    String signUp_URL = "", createdUserResToken;
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();

    }

    void initViews() {
        buttonSignUp = findViewById(R.id.buttonRegister);
        editTextFirstName = findViewById(R.id.editTextSignInFirstName);
        editTextLastName = findViewById(R.id.editTextSignInLastName);
        editTextEmail = findViewById(R.id.editTextSignInEmail);
        editTextPassword = findViewById(R.id.editTextSignInPassword);
        editTextConfirmPass = findViewById(R.id.editTextSignInConfirmPassword);
        client = new AsyncHttpClient();
        baseAPI = new BaseAPI(RegisterActivity.this);
        signUp_URL = baseAPI.BASE_URL;
    }

    public void registerUser(View view) {
        String userEmail = editTextEmail.getText().toString();
        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        String password = editTextPassword.getText().toString();
        String confirmPass = editTextConfirmPass.getText().toString();

        registerUserApi(userEmail, password, firstName, lastName, confirmPass);

    }

    private void registerUserApi(String Email, String Password, String firstName, String lastName, String passConfirmation) {

        params = new RequestParams();
        try {
            params.put("email", Email);
            params.put("password", Password);
            params.put("device_token", "android");
            params.put("first_name", firstName);
            params.put("last_name", lastName);
            params.put("password", Password);
            params.put("password_confirmation", passConfirmation);
        } catch (Exception e) {
            Log.i(TAG, "RegisterParametersException" + e.getMessage());
        }
        client.addHeader("Accept", "application/json");
        client.post(signUp_URL + "register", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("ResponseRegister", response.toString());
                String resData = response.toString();
                try {
                    createdUserResToken = new JSONObject(resData).getString("token");
//                    String uName = new JSONObject(resData).getJSONObject("user").getString("name");
                    Log.i(TAG, "RegisterUserRespToken  " + createdUserResToken + ":::");
                    setFieldsEmpty();
                    loginActivity();
                } catch (JSONException e) {
                    Log.i(TAG, "RegisterUserRespTokenException  " + e.getMessage());
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.i(TAG, "OnFailureResponseRegisterUserObject  " + errorResponse);

                try {
                    JSONObject errors = errorResponse.getJSONObject("errors");
                    for (int i = 0; i < errors.length(); i++) {
                        Log.i("ErrorsArrayCheck", "CheckLength:::" + errors.length());
                        if (errors.has("first_name")) {
                            String name = errors.getJSONArray("first_name").getString(i);
                            editTextFirstName.setError(name);
                        }
                        if (errors.has("last_name")) {
                            String lastName = errors.getJSONArray("last_name").getString(i);
                            editTextLastName.setError(lastName);
                        }
                        if (errors.has("email")) {
                            String email = errors.getJSONArray("email").getString(i);
                            editTextEmail.setError(email);
                        }
                        if (errors.has("password")) {
                            String password = errors.getJSONArray("password").getString(i);
                            editTextPassword.setError(password);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void loginActivity() {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }

    void setFieldsEmpty() {
        editTextFirstName.setText("");
        editTextLastName.setText("");
        editTextEmail.setText("");
        editTextPassword.setText("");
        editTextConfirmPass.setText("");
    }

    public boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}