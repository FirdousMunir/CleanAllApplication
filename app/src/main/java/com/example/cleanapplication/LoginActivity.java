package com.example.cleanapplication;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cleanapplication.map.LocationActivity;
import com.example.cleanapplication.verification.VerificationActivity;
import com.example.cleanapplication.view.MainActivity;
import com.example.cleanapplication.view.PickAddressActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    SignInButton buttonLoginGmail;
    LoginButton buttonLoginFacebook;
    Button buttonLogin;
    EditText editTextEmail, editTextPassword;
    int google_login = 9001;
    GoogleSignInClient mGoogleSignInClient;
    String googleID, facebookID;
    String googleToken, facebookToken;
    AsyncHttpClient client;
    CallbackManager callbackManager;
    String login_URL = "" , addressURL="";
    String TAG = "LoginActivityLogin";
    String googleFirstName, googleLastName, googleDisplayName;
    BaseAPI baseAPI;
    RequestParams params;
    String deviceToken = "andriod";
    SharedPrefsData sharedPrefsData;
    String createdUserResToken;
    boolean isLogin;
    Boolean isVerified = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        printHashKey();
        initViews();

        sharedPrefsData.getTokenFromPreference();
        isLogin = sharedPrefsData.isLogIn;
        Log.i(TAG, "isLogin  " + isLogin);
        if (isLogin) {
            nextActivity();
            Log.i("CllKRniH", "wpssssss");
        }

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String loginEmail = editTextEmail.getText().toString();
                String loginPassword = editTextPassword.getText().toString();
                createdUserSignInApi(loginEmail, loginPassword);

            }
        });

        buttonLoginGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, google_login);
            }
        });

        buttonLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                facebookID = loginResult.getAccessToken().getUserId();
                facebookToken = loginResult.getAccessToken().getToken();
                Log.i(TAG, "FacebookToken  " + facebookToken);
                Log.i(TAG, "FacebookToken  " + facebookID);
                facebookSignInAPI();
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "FacebookOnCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.i(TAG, "FacebookOnError  " + error.getMessage());
            }
        });

    }

    void initViews() {
        buttonLoginGmail = findViewById(R.id.gmailLoginButton);
        buttonLoginFacebook = findViewById(R.id.facebookLoginButton);
        buttonLogin = findViewById(R.id.buttonLogin);
        editTextEmail = findViewById(R.id.editTextLoginEmail);
        editTextPassword = findViewById(R.id.editTextLoginPassword);
        baseAPI = new BaseAPI(LoginActivity.this);
        login_URL = baseAPI.BASE_URL;
        addressURL = baseAPI.BASE_URL;
        sharedPrefsData = new SharedPrefsData(LoginActivity.this);
        buttonLoginFacebook.setReadPermissions(Arrays.asList("email"));
        callbackManager = CallbackManager.Factory.create();
        client = new AsyncHttpClient();
        client.addHeader("Accept", "application/json");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == google_login) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                handleGmailSignInResult(task);
                if (task.isSuccessful()) {
                    gmailSignInAPI();
                    Log.i(TAG, "GmailSignInSuccessful");
                } else
                    Log.i(TAG, "GmailSignInFailed");
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                Log.i(TAG, "GmailSignInException  " + throwable.getMessage());
            }
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleGmailSignInResult(Task<GoogleSignInAccount> completedTask) throws Throwable {
        try {
            GoogleSignInAccount account = (GoogleSignInAccount) completedTask.getResult(ApiException.class);

            Log.i("email", "getEmail:::  " + account.getEmail());
            Log.i("name", "displayName:::  " + account.getDisplayName());
            Log.i("id", "accountID::: " + account.getId());
            Log.i("profile", account.getPhotoUrl().toString());
            Log.i("gmail", account.getIdToken());
            String mailToken = account.getIdToken();
            Log.i("gmailToken", "tok::: " + mailToken);
            googleID = account.getId();
            googleToken = mailToken;
            googleDisplayName = account.getDisplayName();

            String[] parts = googleDisplayName.split("\\s+");
            Log.d("Length-->", "" + parts.length);
            if (parts.length == 2) {
                String firstname = parts[0];
                String lastname = parts[1];
                Log.i("First-->", "" + firstname);
                Log.i("Last-->", "" + lastname);
                googleFirstName = firstname;
                googleLastName = lastname;

            } else if (parts.length == 3) {
                String firstname = parts[0];
                String middlename = parts[1];
                String lastname = parts[2];
                Log.i("First-->", "" + firstname);
                Log.i("Last-->", "" + lastname);
                Log.i("Middle-->", "" + middlename);
                googleFirstName = firstname;
                googleLastName = lastname;
            }

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("signInResult", ":failed code=" + e.getStatusCode());
        }
    }

    void gmailSignInAPI() {
        params = new RequestParams();
        try {
            params.put("google_id", googleID);
            params.put("google_token", googleToken);
            params.put("device_token", deviceToken);
            params.put("first_name", googleFirstName);
            params.put("last_name", googleLastName);
        } catch (Exception e) {
            Log.i(TAG, "ExceptionInRequestParameters" + e.getMessage());
        }

        client.post(login_URL + "login/google", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i(TAG, "OnSuccessResponseGmail" + response);
                try {
                    createdUserResToken = response.getString("token");
                    isVerified = response.getJSONObject("user").getBoolean("verified");
                    Log.i(TAG, "GoogleRespToken  " + createdUserResToken + "::: " + isVerified);
                    setValueInPref(createdUserResToken, true, "Google")
                    if (isVerified) {
                        Toast.makeText(LoginActivity.this, "Number Verified", Toast.LENGTH_SHORT).show();
//                        nextActivity();
                        checkAddresses(createdUserResToken);
                    } else {
                        Toast.makeText(LoginActivity.this, "Number not Verified", Toast.LENGTH_SHORT).show();
                        numberVerificationActivity();
                    }
                } catch (JSONException e) {
                    Log.i(TAG, "GoogleRespTokenException  " + e.getMessage());
                }
            }

            /* @Override
             public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                 Log.i(TAG, "OnFailureResponseGmailString  " + throwable.getMessage());
                 super.onFailure(statusCode, headers, responseString, throwable);
             }
 */
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i(TAG, "OnFailureResponseGmailObject" + errorResponse);
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    void facebookSignInAPI() {
        params = new RequestParams();
        try {
            params.put("facebook_id", facebookID);
            params.put("facebook_token", facebookToken);
            params.put("device_token", deviceToken);
        } catch (Exception e) {
            Log.i(TAG, "FacebookParametersException" + e.getMessage());
        }

        client.post(login_URL + "login/facebook", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i(TAG, "OnSuccessResponseFacebook" + response);
                try {
                    createdUserResToken = response.getString("token");
                    isVerified = response.getJSONObject("user").getBoolean("verified");
                    Log.i(TAG, "FacebookRespToken  " + createdUserResToken + ":::  " + isVerified);
                    setValueInPref(createdUserResToken, true, "Facebook");
//                    nextActivity();
                    if (isVerified) {
                        Toast.makeText(LoginActivity.this, "Number Verified", Toast.LENGTH_SHORT).show();
//                        nextActivity();
                        checkAddresses(createdUserResToken);
                    } else {
                        Toast.makeText(LoginActivity.this, "Number not verified", Toast.LENGTH_SHORT).show();
                        numberVerificationActivity();
                    }
                } catch (JSONException e) {
                    Log.i(TAG, "FacebookRespTokenException  " + e.getMessage());
                }
            }

           /* @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.i(TAG, "OnFailureResponseFacebookString  " + throwable.getMessage());
            }*/

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.i(TAG, "OnFailureResponseFacebookObject " + errorResponse);
            }
        });
    }

    void createdUserSignInApi(String Email, String Password) {
        params = new RequestParams();
        try {
            params.put("email", Email);
            params.put("password", Password);
            params.put("device_token", deviceToken);
        } catch (Exception e) {
            Log.i(TAG, "CreatedParametersException" + e.getMessage());
        }

        client.post(login_URL + "login", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i(TAG, "Response" + response.toString());
                String resData = response.toString();
                try {
                    createdUserResToken = new JSONObject(resData).getString("token");
                    isVerified = response.getJSONObject("user").getBoolean("verified");
                Log.i(TAG, "CreatedUSerRespToken  " + createdUserResToken + ":::  " + isVerified);
                setValueInPref(createdUserResToken, true, "Created");
                    if (isVerified) {
                        Toast.makeText(LoginActivity.this, "Number Verified", Toast.LENGTH_SHORT).show();
//                        nextActivity();
                        checkAddresses(createdUserResToken);
                    } else {
                        Toast.makeText(LoginActivity.this, "Number not verified", Toast.LENGTH_SHORT).show();
                        numberVerificationActivity();
                    }
                } catch (JSONException e) {
                    Log.i(TAG, "CreatedUserRespTokenException  " + e.getMessage());
                }
            }

            /*@Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                Log.i(TAG, "OnFailureResponseCreatedResponseString  " + response);
                super.onFailure(statusCode, headers, response, throwable);
            }*/

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                Log.i(TAG, "OnFailureResponseCreatedResponseObject  " + errorResponse);

                try {
                    JSONObject errors = errorResponse.getJSONObject("errors");
                    for (int i = 0; i < errors.length(); i++) {

                        if (errors.has("email")) {
                            String message = errors.getJSONArray("email").getString(i);
                            editTextEmail.setError(message);
                        }

                        if (errors.has("failed")) {
                            String failed = errors.getJSONArray("failed").getString(i);
                            editTextPassword.setError(failed);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    void checkAddresses(String token){

        client.addHeader("Authorization", "Bearer " + token);
        client.get(addressURL+"addresses",new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i("CheckAddresses" , "OnSuccess" + response);

                Log.i("CheckDataaaaa" , token+" ::: " + addressURL);
                if (response.has("addresses")){
                    try {
                        Log.i("CheckAddressLength" , response.getJSONArray("addresses").length()+"");
                        Toast.makeText(LoginActivity.this, "user has addresses", Toast.LENGTH_SHORT).show();
                        pickAddresses();
//                        nextActivity();
                    } catch (JSONException e) {
                        Log.i(TAG,"addressesResponseException " + e.getMessage());
                    }

                }else{
                    Toast.makeText(LoginActivity.this, "user has no address", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, LocationActivity.class));
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.i(TAG , "CheckAddressesOnFailureObject" + errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.i(TAG , "CheckAddressesOnFailureString" + responseString);
            }
        });

    }

    void pickAddresses(){
        startActivity(new Intent(LoginActivity.this, PickAddressActivity.class));
    }

    private void nextActivity() {
        finish();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    private void numberVerificationActivity() {
        startActivity(new Intent(LoginActivity.this, VerificationActivity.class));
    }

    private void setValueInPref(String token, Boolean isLogin, String authType) {
        sharedPrefsData.setValueInPreference(token, isLogin, authType);
        Log.i("CheckLoginTokenNType", token + ":::" + authType);
    }


    //    facebook hash key generated from application and put in facebook developer console
    public void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("bbcf", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("bbcf", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("bbcf", "printHashKey()", e);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("onStartLoginActivity", "token" + createdUserResToken);
    }

    public void signUp(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    public void passwordReset(View view) {
        sharedPrefsData.setPasswordResetType("NotLoggedInUser");
        startActivity(new Intent(LoginActivity.this, PasswordResetActivity.class));
    }
}