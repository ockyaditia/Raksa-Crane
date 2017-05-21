package com.example.ockyaditiasaputra.raksacrane;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DescriptionActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String sharedName, sharedEmail;

    String title, tipe;

    UserSession session;

    String nama_produk, kapasitas, boom, jib, harga, produk_id_post, produk_published = "";

    JSONParser jsonParser = new JSONParser();
    private static final String LOGIN_URL = new Connections().getDeskripsi();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences sp2 = getSharedPreferences("usersession_raksacrane", MODE_PRIVATE);
        sharedName = sp2.getString("Fullname", "");
        sharedEmail = sp2.getString("Email", "");

        TextView userName = (TextView) findViewById(R.id.username);
        userName.setText(sharedName);

        TextView email = (TextView) findViewById(R.id.email);
        email.setText(sharedEmail);

        session = new UserSession(getApplicationContext());

        ImageButton logout = (ImageButton) findViewById(R.id.fab);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:

                                session.logoutUser();

                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivityForResult(intent, 0);

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder ab = new AlertDialog.Builder(DescriptionActivity.this);
                ab.setMessage("Are you sure want to Logout?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        title = getIntent().getStringExtra("title");
        tipe = getIntent().getStringExtra("tipe");

        if (isInternetConnected(new Connections().con())) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("produk_judul", title));
                params.add(new BasicNameValuePair("produk_tipe", tipe));

                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);

                JSONArray mJsonArray = json.getJSONArray("produk");

                for (int i = 0; i < mJsonArray.length(); i++) {

                    TextView produk_judul = (TextView) findViewById(R.id.produk_judul);
                    produk_judul.setText(mJsonArray.getJSONObject(i).getString("produk_judul"));

                    ImageView produk_image = (ImageView) findViewById(R.id.imageView);
                    produk_image.setImageResource(getResources().getIdentifier(mJsonArray.getJSONObject(i).getString("produk_gambar").replace(".jpg", "").replace(".jpeg", "").replace(".png", ""),
                            "drawable", getPackageName()));

                    TextView produk_content = (TextView) findViewById(R.id.produk_content);
                    produk_content.setText(mJsonArray.getJSONObject(i).getString("produk_content"));

                    TextView produk_harga = (TextView) findViewById(R.id.produk_harga);
                    produk_harga.setText("Rp. " + mJsonArray.getJSONObject(i).getString("produk_harga"));

                    TextView produk_id = (TextView) findViewById(R.id.produk_id);
                    produk_id.setText("SKU: " + mJsonArray.getJSONObject(i).getString("produk_id"));

                    TextView produk_pabrikan = (TextView) findViewById(R.id.produk_pabrikan);
                    produk_pabrikan.setText("Pabrikan: " + mJsonArray.getJSONObject(i).getString("produk_pabrikan"));

                    TextView produk_model = (TextView) findViewById(R.id.produk_model);
                    produk_model.setText("Model: " + mJsonArray.getJSONObject(i).getString("produk_model"));

                    TextView produk_tahun = (TextView) findViewById(R.id.produk_tahun);
                    produk_tahun.setText("Tahun: " + mJsonArray.getJSONObject(i).getString("produk_tahun"));

                    TextView produk_kapasitas = (TextView) findViewById(R.id.produk_kapasitas);
                    produk_kapasitas.setText("Kapasitas: " + mJsonArray.getJSONObject(i).getString("produk_kapasitas"));

                    TextView produk_boom = (TextView) findViewById(R.id.produk_boom);
                    produk_boom.setText("Boom: " + mJsonArray.getJSONObject(i).getString("produk_boom"));

                    TextView produk_jib = (TextView) findViewById(R.id.produk_jib);
                    produk_jib.setText("Jib: " + mJsonArray.getJSONObject(i).getString("produk_jib"));

                    TextView jumlah_barang = (TextView) findViewById(R.id.jumlah_barang);
                    jumlah_barang.setText("Jumlah Barang: " + mJsonArray.getJSONObject(i).getString("jumlah_barang"));

                    produk_id_post = mJsonArray.getJSONObject(i).getString("produk_id");
                    harga = mJsonArray.getJSONObject(i).getString("produk_harga");
                    nama_produk = mJsonArray.getJSONObject(i).getString("produk_judul");
                    kapasitas = mJsonArray.getJSONObject(i).getString("produk_kapasitas");
                    boom = mJsonArray.getJSONObject(i).getString("produk_boom");
                    jib = mJsonArray.getJSONObject(i).getString("produk_jib");
                    produk_published = mJsonArray.getJSONObject(i).getString("produk_published");

                    Button order = (Button) findViewById(R.id.order);
                    order.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (tipe.equals("Dijual")) {

                                Intent intent = new Intent(v.getContext(), OrderJualActivity.class);
                                intent.putExtra("produk_id", produk_id_post);
                                intent.putExtra("harga", harga);
                                intent.putExtra("nama_produk", nama_produk);
                                intent.putExtra("kapasitas", kapasitas);
                                intent.putExtra("boom", boom);
                                intent.putExtra("jib", jib);
                                intent.putExtra("produk_published", produk_published);
                                intent.putExtra("tipe", "Dijual");

                                startActivity(intent);
                            } else if (tipe.equals("Disewakan")) {

                                Intent intent = new Intent(v.getContext(), OrderSewaActivity.class);
                                intent.putExtra("produk_id", produk_id_post);
                                intent.putExtra("harga", harga);
                                intent.putExtra("nama_produk", nama_produk);
                                intent.putExtra("kapasitas", kapasitas);
                                intent.putExtra("boom", boom);
                                intent.putExtra("jib", jib);
                                intent.putExtra("produk_published", produk_published);
                                intent.putExtra("tipe", "Disewakan");

                                startActivity(intent);
                            }
                        }
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            showAlertDialog(this, "Internet Connection",
                    "Please check your internet connection", false);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.beranda, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            Intent i = new Intent(this, BerandaActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivityForResult(i, 0);
        } else if (id == R.id.profile) {
            Intent i = new Intent(this, ProfileActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivityForResult(i, 0);
        } else if (id == R.id.beli) {
            Intent i = new Intent(this, PembelianPenyewaan.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivityForResult(i, 0);
        } else if (id == R.id.bantuan) {
            Intent i = new Intent(this, BantuanActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivityForResult(i, 0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(context).create();

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
