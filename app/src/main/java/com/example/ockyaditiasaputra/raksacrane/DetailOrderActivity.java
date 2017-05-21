package com.example.ockyaditiasaputra.raksacrane;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetailOrderActivity extends AppCompatActivity {

    InputStream is = null;
    String result = null;
    String line = null;
    int code;

    UserSession session;

    String nama_produk, kapasitas, boom, jib, nama_bank, atas_nama, nomor_rekening, tipe, tujuan_transfer, harga, date1, date2, current_date, sharedUsername, order_status, order_nomor_transaksi = "";
    String order_tujuan_id, order_jumlah_transfer, order_publish, produk_id, order_id, order_duration = "";
    String angka1Convert, angka2Convert = "";

    Locale locale = null;
    NumberFormat rupiahFormat = null;

    JSONParser jsonParser = new JSONParser();
    private static final String LOGIN_URL = new Connections().getRekening();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        kapasitas = getIntent().getStringExtra("kapasitas");
        boom = getIntent().getStringExtra("boom");
        jib = getIntent().getStringExtra("jib");
        nama_bank = getIntent().getStringExtra("nama_bank");

        order_id = getIntent().getStringExtra("order_id");

        produk_id = getIntent().getStringExtra("produk_id");

        nama_produk = getIntent().getStringExtra("nama_produk");

        current_date = getIntent().getStringExtra("current_date");

        tipe = getIntent().getStringExtra("tipe");
        date1 = getIntent().getStringExtra("date1");
        date2 = getIntent().getStringExtra("date2");

        SharedPreferences sp2 = getSharedPreferences("usersession_raksacrane", MODE_PRIVATE);
        sharedUsername = sp2.getString("Username", "");

        tujuan_transfer = getIntent().getStringExtra("tujuan_transfer");
        atas_nama = getIntent().getStringExtra("atas_nama");
        nomor_rekening = getIntent().getStringExtra("nomor_rekening");
        order_jumlah_transfer = getIntent().getStringExtra("order_jumlah_transfer");
        order_status = getIntent().getStringExtra("order_status");
        order_publish = getIntent().getStringExtra("order_publish");
        harga = getIntent().getStringExtra("harga");
        order_tujuan_id = getIntent().getStringExtra("order_tujuan_id");

        int success;
        String cabang = "";
        String nama_pemilik = "";

        if (isInternetConnected(new Connections().con())) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("tujuan_transfer", tujuan_transfer));

                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);

                success = json.getInt(TAG_SUCCESS);
                if (success == 3) {
                    cabang = json.getString("cabang");
                    nama_pemilik = json.getString("nama_pemilik");
                    order_nomor_transaksi = json.getString("nomor_rekening");

                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            showAlertDialog(this, "Internet Connection",
                    "Please check your internet connection", false);
        }

        order_duration = getIntent().getStringExtra("order_duration");

        TextView rincian = (TextView) findViewById(R.id.rincian);

        if (tipe.equals("Dijual")) {
            rincian.setText("Rincian pembelian " + nama_produk + ".");
        } else if (tipe.equals("Disewakan")) {
            rincian.setText("Rincian penyewaan " + nama_produk + ".");
        }

        TextView desc = (TextView) findViewById(R.id.desc);
        desc.setText(nama_produk);

        TextView jumlah_hari = (TextView) findViewById(R.id.jumlah_hari);
        jumlah_hari.setText(order_publish);

        long hargaHarian = Long.parseLong(harga.replace(".", "")) / Long.parseLong(order_publish);

        DetailOrderActivity kpa = new DetailOrderActivity();

        String hargaString = kpa.toRupiahFormat(Long.toString(hargaHarian)).replace("Rp", "").replace(",00", "");

        TextView harian = (TextView) findViewById(R.id.harian);
        harian.setText("Rp. " + hargaString);

        TextView total = (TextView) findViewById(R.id.total);
        total.setText("Rp. " + harga);

        if (order_status.equals("Success")) {

            LinearLayout hidden = (LinearLayout) findViewById(R.id.hidden);
            hidden.removeAllViews();

            LinearLayout remove = (LinearLayout) findViewById(R.id.remove);
            remove.removeAllViews();

            TextView status = (TextView) findViewById(R.id.status);
            status.setText("SUCCESS");

        } else if (order_status.equals("In Progress")) {

            TextView status = (TextView) findViewById(R.id.status);
            status.setText("IN PROGRESS");

            String dpAngka = harga.replace(".", "");

            BigInteger angka1 = new BigInteger(dpAngka);
            BigInteger tigaPuluh = new BigInteger("30");
            BigInteger seratus = new BigInteger("100");

            angka1 = angka1.multiply(tigaPuluh).divide(seratus);

            BigInteger angka2 = new BigInteger(dpAngka);
            BigInteger tujuhPuluh = new BigInteger("70");
            seratus = new BigInteger("100");

            angka2 = angka2.multiply(tujuhPuluh).divide(seratus);

            angka1Convert = kpa.toRupiahFormat(angka1.toString()).replace("Rp", "").replace(",00", "");
            angka2Convert = kpa.toRupiahFormat(angka2.toString()).replace("Rp", "").replace(",00", "");

            TextView dp = (TextView) findViewById(R.id.dp);
            dp.setText("Rp. " + angka1Convert);

            TextView sisa = (TextView) findViewById(R.id.sisa);
            sisa.setText("Rp. " + angka2Convert);

            TextView info = (TextView) findViewById(R.id.info);
            info.setText("Anda memilih pembayaran menuju ke rekening " + tujuan_transfer + " kami. Rincian rekening sebagai berikut :");

            TextView rek = (TextView) findViewById(R.id.rek);

            if (tipe.equals("Dijual")) {
                rek.setText("Nomor Rekening " + tujuan_transfer + " : " + order_nomor_transaksi +
                        "\nNama Pemilik : " + nama_pemilik + "\nCabang : " + cabang);
            } else if (tipe.equals("Disewakan")) {
                rek.setText("Nomor Rekening " + tujuan_transfer + " : " + order_nomor_transaksi +
                        "\nNama Pemilik : " + nama_pemilik + "\nCabang : " + cabang +
                        "\n\nTanggal Awal Sewa : " + date1 + "\nTanggal Akhir Sewa : " + date2 + "");
            }

            if (order_jumlah_transfer.equals("")) {

                TextView finish1 = (TextView) findViewById(R.id.finish1);
                finish1.setText("Menunggu");

                TextView finish2 = (TextView) findViewById(R.id.finish2);
                finish2.setText("Menunggu");

                ImageView finishImage1 = (ImageView) findViewById(R.id.finishImage1);
                finishImage1.setBackground(getResources().getDrawable(R.drawable.fail));

                ImageView finishImage2 = (ImageView) findViewById(R.id.finishImage2);
                finishImage2.setBackground(getResources().getDrawable(R.drawable.fail));

                Button submit = (Button) findViewById(R.id.submit);
                submit.setText("Konfirmasi Pembayaran DP");

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {

                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:

                                        if (isInternetConnected(new Connections().con())) {
                                            updateDP();

                                            Intent i = new Intent(getApplicationContext(), UploadImageActivity.class);
                                            startActivityForResult(i, 0);

                                        } else {
                                            showAlertDialog(getApplication(), "Internet Connection",
                                                    "Please check your internet connection", false);
                                        }

                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder ab = new AlertDialog.Builder(DetailOrderActivity.this);
                        ab.setMessage("Konfirmasi Pembayaran DP?").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
                    }
                });

            } else {

                TextView finish1 = (TextView) findViewById(R.id.finish1);
                finish1.setText("Selesai");

                TextView finish2 = (TextView) findViewById(R.id.finish2);
                finish2.setText("Menunggu");

                ImageView finishImage1 = (ImageView) findViewById(R.id.finishImage1);
                finishImage1.setBackground(getResources().getDrawable(R.drawable.success));

                ImageView finishImage2 = (ImageView) findViewById(R.id.finishImage2);
                finishImage2.setBackground(getResources().getDrawable(R.drawable.fail));

                Button submit = (Button) findViewById(R.id.submit);
                submit.setText("Konfirmasi Pembayaran Sisa");

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:

                                        if (isInternetConnected(new Connections().con())) {
                                            updateSisa();

                                            Intent i = new Intent(getApplicationContext(), UploadImageActivity.class);
                                            startActivityForResult(i, 0);
                                        } else {
                                            showAlertDialog(getApplication(), "Internet Connection",
                                                    "Please check your internet connection", false);
                                        }

                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder ab = new AlertDialog.Builder(DetailOrderActivity.this);
                        ab.setMessage("Konfirmasi Pembayaran Sisa?").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();

                    }
                });

            }
        }
    }

    public String toRupiahFormat(String nominal) {
        String rupiah = "";

        locale = new Locale("id", "ID");
        rupiahFormat = NumberFormat.getCurrencyInstance(locale);

        rupiah = rupiahFormat.format(Double.parseDouble(nominal));

        return rupiah;
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

    public void updateDP() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();

        nameValuePairs.add(new BasicNameValuePair("order_id", order_id));
        nameValuePairs.add(new BasicNameValuePair("angka1Convert", angka1Convert));

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(new Connections().updateDP());
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
                showAlertDialog(this, "Success", "Konfirmasi Pembayaran DP Sukses", true);
            } else {
                showAlertDialog(this, "Failed", "Konfirmasi Pembayaran DP Gagal", false);
            }
        } catch (Exception e) {
            Log.e("Fail 3", e.toString());
        }
    }

    public void updateSisa() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();

        nameValuePairs.add(new BasicNameValuePair("order_id", order_id));
        nameValuePairs.add(new BasicNameValuePair("harga", harga));

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(new Connections().updateSisa());
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
                showAlertDialog(this, "Success", "Konfirmasi Pembayaran Sisa Sukses", true);
            } else {
                showAlertDialog(this, "Failed", "Konfirmasi Pembayaran Sisa Gagal", false);
            }
        } catch (Exception e) {
            Log.e("Fail 3", e.toString());
        }
    }

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(context).create();

        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

        /*if (message.equals("Konfirmasi Pembayaran DP Sukses")) {
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(getApplicationContext(), PembelianPenyewaan.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivityForResult(i, 0);
                }
            });

            alertDialog.show();
        } else if (message.equals("Konfirmasi Pembayaran Sisa Sukses")) {
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(getApplicationContext(), PembelianPenyewaan.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivityForResult(i, 0);
                }
            });

            alertDialog.show();
        } else {
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(getApplicationContext(), PembelianPenyewaan.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivityForResult(i, 0);
                }
            });

            alertDialog.show();
        }*/
    }
}
