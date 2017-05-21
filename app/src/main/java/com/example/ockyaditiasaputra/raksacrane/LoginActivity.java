package com.example.ockyaditiasaputra.raksacrane;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.view.View.OnClickListener;
import android.widget.ViewFlipper;

public class LoginActivity extends AppCompatActivity implements OnClickListener {
    EditText user, pass;
    Button bLogin;
    ProgressDialog pDialog;

    TextView error;

    JSONParser jsonParser = new JSONParser();
    private static final String LOGIN_URL = new Connections().getUser();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    UserSession session;
    Animation fade_in, fade_out;
    ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new UserSession(getApplicationContext());

        TextView register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(v.getContext(), RegisterActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivityForResult(i, 0);
            }
        });

        error = (TextView) findViewById(R.id.error);

        user = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);
        bLogin = (Button) findViewById(R.id.loginButton);
        bLogin.setOnClickListener(this);

        viewFlipper = (ViewFlipper) this.findViewById(R.id.bckgrndViewFlipper1);
        fade_in = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out);
        viewFlipper.setInAnimation(fade_in);
        viewFlipper.setOutAnimation(fade_out);
        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(5000);
        viewFlipper.startFlipping();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.loginButton:
                new AttemptLogin().execute();

            default:
                break;
        }
    }

    class AttemptLogin extends AsyncTask<String, String, String> {
        boolean failure = false;

        int success;
        String username;
        String password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Attempting for login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            username = user.getText().toString();
            password = pass.getText().toString();

            if (isInternetConnected(new Connections().con())) {
                try {
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("username", username));
                    params.add(new BasicNameValuePair("password", password));

                    JSONObject json = jsonParser.makeHttpRequest(
                            LOGIN_URL, "POST", params);

                    success = json.getInt(TAG_SUCCESS);
                    if (success == 3) {
                        session.createUserLoginSession(username, password, json.getString("Email"), json.getString("Fullname"), json.getString("Address"), json.getString("Phone"));

                        Intent intent = new Intent(LoginActivity.this, BerandaActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivityForResult(intent, 0);
                        finish();

                    } else {
                        return json.getString(TAG_MESSAGE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                pDialog.dismiss();

                success = -1;
            }

            return null;
        }

        /**
         * Once the background process is done we need to  Dismiss the progress dialog asap
         **/
        protected void onPostExecute(String message) {
            pDialog.dismiss();

            if (message != null) {
                error.setText(message);
            }

            if (success == -1) {

                showAlertDialog(LoginActivity.this, "Internet Connection",
                        "Please check your internet connection", false);

            }
            if (success == 0) {
                user.setBackground(getResources().getDrawable(R.drawable.edittext_top_bg_error));
                pass.setBackground(getResources().getDrawable(R.drawable.edittext_bottom_bg));
            } else if (success == 1) {
                user.setBackground(getResources().getDrawable(R.drawable.edittext_top_bg));
                pass.setBackground(getResources().getDrawable(R.drawable.edittext_bottom_bg_error));
            } else if (success == 2) {
                user.setBackground(getResources().getDrawable(R.drawable.edittext_top_bg_error));
                pass.setBackground(getResources().getDrawable(R.drawable.edittext_bottom_bg_error));
            }
        }
    }

    public boolean isInternetConnected(String mUrl) {
        final int CONNECTION_TIMEOUT = 5000;
        if (isNetworksAvailable()) {
            try {
                HttpURLConnection mURLConnection = (HttpURLConnection) (new URL(mUrl).openConnection());
                mURLConnection.setRequestProperty("User-Agent", "ConnectionTest");
                mURLConnection.setRequestProperty("Connection", "close");
                mURLConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                mURLConnection.setReadTimeout(CONNECTION_TIMEOUT);
                mURLConnection.connect();
                return (mURLConnection.getResponseCode() == 200);
            } catch (IOException ioe) {
                Log.e("isInternetConnected", "Exception occured while checking for Internet connection: ", ioe);
            }
        } else {
            Log.e("isInternetConnected", "Not connected to WiFi/Mobile and no Internet available.");
        }
        return false;
    }

    public boolean isNetworksAvailable() {
        ConnectivityManager mConnMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mConnMgr != null) {
            NetworkInfo[] mNetInfo = mConnMgr.getAllNetworkInfo();
            if (mNetInfo != null) {
                for (int i = 0; i < mNetInfo.length; i++) {
                    if (mNetInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertDialog.show();
    }

}
