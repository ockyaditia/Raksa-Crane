package com.example.ockyaditiasaputra.raksacrane;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;

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

/**
 * Created by Ocky Aditia Saputra on 01/03/2016.
 */
public class All extends Fragment {

    private GridView gridView;
    private GridViewAdapter2 gridAdapter;

    JSONParser jsonParser = new JSONParser();
    private static final String LOGIN_URL = new Connections().getAll();
    private static final String LOGIN_URL2 = new Connections().getOrder();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.all, container, false);

        gridView = (GridView) v.findViewById(R.id.gridView);

        SharedPreferences sp2 = getActivity().getSharedPreferences("usersession_raksacrane", getActivity().MODE_PRIVATE);

        gridAdapter = new GridViewAdapter2(v.getContext(), R.layout.grid_item_layout2, getData(sp2.getString("Username", "")));
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem2 item = (ImageItem2) parent.getItemAtPosition(position);

                int success;
                String order_id = item.getId();
                String produk_id = "";
                String nama_produk = "";
                String current_date = "";
                String tipe = "";
                String date1 = "";
                String date2 = "";
                String sharedUsername = "";
                String tujuan_transfer = "";
                String atas_nama = "";
                String nomor_rekening = "";
                String order_jumlah_transfer = "";
                String order_status = "";
                String order_publish = "";
                String harga = "";
                String order_tujuan_id = "";
                String order_nomor_transaksi = "";
                String order_duration = "";

                if (isInternetConnected(new Connections().con())) {
                    try {
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("order_id", order_id));

                        JSONObject json = jsonParser.makeHttpRequest(
                                LOGIN_URL2, "POST", params);

                        success = json.getInt(TAG_SUCCESS);
                        if (success == 3) {
                            produk_id = json.getString("produk_id");
                            nama_produk = json.getString("nama_produk");
                            current_date = json.getString("current_date");
                            tipe = json.getString("tipe");
                            date1 = json.getString("date1");
                            date2 = json.getString("date2");
                            sharedUsername = json.getString("sharedUsername");
                            tujuan_transfer = json.getString("tujuan_transfer");
                            atas_nama = json.getString("atas_nama");
                            nomor_rekening = json.getString("nomor_rekening");
                            order_jumlah_transfer = json.getString("order_jumlah_transfer");
                            order_status = json.getString("order_status");
                            order_publish = json.getString("order_publish");
                            harga = json.getString("harga");
                            order_tujuan_id = json.getString("order_tujuan_id");
                            order_nomor_transaksi = json.getString("order_nomor_transaksi");
                            order_duration = json.getString("order_duration");

                            Intent intent = new Intent(v.getContext(), DetailOrderActivity.class);
                            intent.putExtra("order_id", item.getId());
                            intent.putExtra("produk_id", produk_id);
                            intent.putExtra("nama_produk", nama_produk);
                            intent.putExtra("current_date", current_date);
                            intent.putExtra("tipe", tipe);
                            intent.putExtra("date1", date1);
                            intent.putExtra("date2", date2);
                            intent.putExtra("sharedUsername", sharedUsername);
                            intent.putExtra("tujuan_transfer", tujuan_transfer);
                            intent.putExtra("atas_nama", atas_nama);
                            intent.putExtra("nomor_rekening", nomor_rekening);
                            intent.putExtra("order_jumlah_transfer", order_jumlah_transfer);
                            intent.putExtra("order_status", order_status);
                            intent.putExtra("order_publish", order_publish);
                            intent.putExtra("harga", harga);
                            intent.putExtra("order_tujuan_id", order_tujuan_id);
                            intent.putExtra("order_nomor_transaksi", order_nomor_transaksi);
                            intent.putExtra("order_duration", order_duration);

                            startActivity(intent);

                        } else {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    showAlertDialog(v.getContext(), "Internet Connection",
                            "Please check your internet connection", false);
                }
            }
        });

        return v;
    }

    /**
     * Prepare some dummy data for gridview
     */
    private ArrayList<ImageItem2> getData(String data1) {
        final ArrayList<ImageItem2> imageItems = new ArrayList<>();

        if (isInternetConnected(new Connections().con())) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("order_buyer_name", data1));

                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);

                JSONArray mJsonArray = json.getJSONArray("orders");

                for (int i = 0; i < mJsonArray.length(); i++) {

                    String image = mJsonArray.getJSONObject(i).getString("produk_gambar");

                    if (image.contains(".jpg")) {
                        image = image.replace(".jpg", "");
                    }

                    if (image.contains(".png")) {
                        image = image.replace(".png", "");
                    }

                    if (image.contains(".jpeg")) {
                        image = image.replace(".jpeg", "");
                    }

                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                            getResources().getIdentifier(image, "drawable", getActivity().getPackageName()));
                    imageItems.add(new ImageItem2(bitmap, mJsonArray.getJSONObject(i).getString("order_judul"), mJsonArray.getJSONObject(i).getString("order_id"), mJsonArray.getJSONObject(i).getString("order_status")));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            showAlertDialog(getContext(), "Internet Connection",
                    "Please check your internet connection", false);
        }

        return imageItems;
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
        ConnectivityManager mConnMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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
