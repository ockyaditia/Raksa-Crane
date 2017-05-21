package com.example.ockyaditiasaputra.raksacrane;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;
import android.widget.ViewFlipper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    InputStream is = null;
    String result = null;
    String line = null;
    int code;

    Animation fade_in, fade_out;
    ViewFlipper viewFlipper;

    String fullname, username, email, contact, password = "";
    EditText fullnameTxt, usernameTxt, emailTxt, contactTxt, passwordTxt;
    TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView login = (TextView) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(v.getContext(), LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivityForResult(i, 0);
            }
        });

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

        fullnameTxt = (EditText) findViewById(R.id.fullname);
        usernameTxt = (EditText) findViewById(R.id.username);
        emailTxt = (EditText) findViewById(R.id.email);
        contactTxt = (EditText) findViewById(R.id.contact);
        passwordTxt = (EditText) findViewById(R.id.password);

        error = (TextView) findViewById(R.id.error);

        Button register = (Button) findViewById(R.id.registerButton);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullname = fullnameTxt.getText().toString();
                username = usernameTxt.getText().toString();
                email = emailTxt.getText().toString();
                contact = contactTxt.getText().toString();
                password = passwordTxt.getText().toString();

                if (fullname.length() <= 0) {
                    error.setText("Insert Your Fullname.");
                    fullnameTxt.setBackground(getResources().getDrawable(R.drawable.edittext_top_bg_error));
                    usernameTxt.setBackground(getResources().getDrawable(R.drawable.edittext_default_bg));
                    emailTxt.setBackground(getResources().getDrawable(R.drawable.edittext_default_bg));
                    contactTxt.setBackground(getResources().getDrawable(R.drawable.edittext_default_bg));
                    passwordTxt.setBackground(getResources().getDrawable(R.drawable.edittext_default_bg));
                } else if (username.length() <= 0) {
                    error.setText("Insert Your Username.");
                    usernameTxt.setBackground(getResources().getDrawable(R.drawable.edittext_default_bg_error));
                    fullnameTxt.setBackground(getResources().getDrawable(R.drawable.edittext_top_bg));
                    emailTxt.setBackground(getResources().getDrawable(R.drawable.edittext_default_bg));
                    contactTxt.setBackground(getResources().getDrawable(R.drawable.edittext_default_bg));
                    passwordTxt.setBackground(getResources().getDrawable(R.drawable.edittext_default_bg));
                } else if (email.length() <= 0) {
                    error.setText("Insert Your Email.");
                    emailTxt.setBackground(getResources().getDrawable(R.drawable.edittext_default_bg_error));
                    fullnameTxt.setBackground(getResources().getDrawable(R.drawable.edittext_top_bg));
                    usernameTxt.setBackground(getResources().getDrawable(R.drawable.edittext_default_bg));
                    contactTxt.setBackground(getResources().getDrawable(R.drawable.edittext_default_bg));
                    passwordTxt.setBackground(getResources().getDrawable(R.drawable.edittext_default_bg));
                } else if (contact.length() <= 0) {
                    error.setText("Insert Your Contact.");
                    passwordTxt.setBackground(getResources().getDrawable(R.drawable.edittext_top_bg));
                    fullnameTxt.setBackground(getResources().getDrawable(R.drawable.edittext_default_bg));
                    usernameTxt.setBackground(getResources().getDrawable(R.drawable.edittext_default_bg));
                    contactTxt.setBackground(getResources().getDrawable(R.drawable.edittext_default_bg_error));
                    emailTxt.setBackground(getResources().getDrawable(R.drawable.edittext_default_bg));
                } else if (password.length() <= 0) {
                    error.setText("Insert Your Password.");
                    passwordTxt.setBackground(getResources().getDrawable(R.drawable.edittext_default_bg_error));
                    fullnameTxt.setBackground(getResources().getDrawable(R.drawable.edittext_top_bg));
                    usernameTxt.setBackground(getResources().getDrawable(R.drawable.edittext_default_bg));
                    contactTxt.setBackground(getResources().getDrawable(R.drawable.edittext_default_bg));
                    emailTxt.setBackground(getResources().getDrawable(R.drawable.edittext_default_bg));
                } else {
                    if (isInternetConnected(new Connections().con())) {
                        insert();
                    } else {
                        showAlertDialog(v.getContext(), "Internet Connection",
                                "Please check your internet connection", false);
                    }
                }
            }
        });
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
        if (mConnMgr != null)  {
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

    public void insert() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();

        nameValuePairs.add(new BasicNameValuePair("username", username));
        nameValuePairs.add(new BasicNameValuePair("password", password));
        nameValuePairs.add(new BasicNameValuePair("email", email));
        nameValuePairs.add(new BasicNameValuePair("contact", contact));
        nameValuePairs.add(new BasicNameValuePair("fullname", fullname));

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(new Connections().insertUser());
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("Pass 1", "Connection Success");
        } catch (Exception e) {
            Log.e("Fail 1", e.toString());
            Toast.makeText(this, "Connection Failed",
                    Toast.LENGTH_LONG).show();
        }

        try {
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
            Log.e("Pass 2", "Connection Success");
        } catch (Exception e) {
            Log.e("Fail 2", e.toString());
        }

        try {
            JSONObject json_data = new JSONObject(result);
            code = (json_data.getInt("code"));

            if (code == 1) {
                showAlertDialog(this, "Success", "Register Success", true);
            } else {
                showAlertDialog(this, "Failed", "Register Failed", false);
            }
        } catch (Exception e) {
            Log.e("Fail 3", e.toString());
        }
    }

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

        if (message.equals("Register Success")) {
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivityForResult(i, 0);
                }
            });
        } else {
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        }

        alertDialog.show();
    }
}
