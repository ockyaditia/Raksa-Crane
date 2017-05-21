package com.example.ockyaditiasaputra.raksacrane;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.GridView;

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
public class Sewa extends Fragment {

    private GridView gridView;
    private GridViewAdapter gridAdapter;

    JSONParser jsonParser = new JSONParser();
    private static final String LOGIN_URL = new Connections().getProduk();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sewa, container, false);

        gridView = (GridView) v.findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(v.getContext(), R.layout.grid_item_layout, getData(getActivity().getIntent().getIntExtra("kategori", 0)));
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);

                if (item.getId().equals("0")) {
                    showAlertDialog(v.getContext(), "Failed", "Stock barang habis", false);
                } else {
                    Intent intent = new Intent(v.getContext(), DescriptionActivity.class);
                    intent.putExtra("title", item.getTitle());
                    intent.putExtra("tipe", "Disewakan");

                    startActivity(intent);
                }
            }
        });

        return v;
    }

    /**
     * Prepare some dummy data for gridview
     */
    private ArrayList<ImageItem> getData(int kategori) {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();

        if (isInternetConnected(new Connections().con())) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("category_id", Integer.toString(kategori)));
                params.add(new BasicNameValuePair("produk_tipe", "Disewakan"));

                JSONObject json = jsonParser.makeHttpRequest(
                        LOGIN_URL, "POST", params);

                JSONArray mJsonArray = json.getJSONArray("produk");

                for (int i = 0; i < mJsonArray.length(); i++) {

                    if (mJsonArray.getJSONObject(i).getString("produk_keywords") != null) {

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
                        imageItems.add(new ImageItem(bitmap, mJsonArray.getJSONObject(i).getString("produk_judul"), mJsonArray.getJSONObject(i).getString("jumlah_barang")));
                    }
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
