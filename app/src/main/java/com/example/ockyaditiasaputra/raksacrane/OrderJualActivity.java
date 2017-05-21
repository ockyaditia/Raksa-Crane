package com.example.ockyaditiasaputra.raksacrane;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class OrderJualActivity extends AppCompatActivity {

    UserSession session;

    String harga = "";

    EditText order_judul, produk_kapasitas, produk_boom, produk_jib;
    EditText order_bank, order_atas_nama, order_nomor_transfer;
    TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_jual);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        order_judul = (EditText) findViewById(R.id.order_judul);
        produk_kapasitas = (EditText) findViewById(R.id.produk_kapasitas);
        produk_boom = (EditText) findViewById(R.id.produk_boom);
        produk_jib = (EditText) findViewById(R.id.produk_jib);

        order_bank = (EditText) findViewById(R.id.order_bank);
        order_atas_nama = (EditText) findViewById(R.id.order_atas_nama);
        order_nomor_transfer = (EditText) findViewById(R.id.order_nomor_transfer);

        order_judul.setText(getIntent().getStringExtra("nama_produk"));
        produk_kapasitas.setText(getIntent().getStringExtra("kapasitas"));
        produk_boom.setText(getIntent().getStringExtra("boom"));
        produk_jib.setText(getIntent().getStringExtra("jib"));
        harga = getIntent().getStringExtra("harga");

        error = (TextView) findViewById(R.id.error);

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

                AlertDialog.Builder ab = new AlertDialog.Builder(OrderJualActivity.this);
                ab.setMessage("Are you sure want to Logout?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        Button validasi = (Button) findViewById(R.id.validasi);
        validasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (order_bank.length() <= 0) {
                    error.setText("Isi Nama Bank.");
                    order_bank.setBackground(getResources().getDrawable(R.drawable.edittext_default_bg_error));
                    order_atas_nama.setBackground(getResources().getDrawable(R.drawable.edittext_top_bg));
                    order_nomor_transfer.setBackground(getResources().getDrawable(R.drawable.edittext_default_bg));
                } else if (order_atas_nama.length() <= 0) {
                    error.setText("Isi Atas Nama.");
                    order_bank.setBackground(getResources().getDrawable(R.drawable.edittext_top_bg));
                    order_atas_nama.setBackground(getResources().getDrawable(R.drawable.edittext_default_bg_error));
                    order_nomor_transfer.setBackground(getResources().getDrawable(R.drawable.edittext_default_bg));
                } else if (order_nomor_transfer.length() <= 0) {
                    error.setText("Isi Nomor Rekening.");
                    order_bank.setBackground(getResources().getDrawable(R.drawable.edittext_top_bg));
                    order_atas_nama.setBackground(getResources().getDrawable(R.drawable.edittext_top_bg));
                    order_nomor_transfer.setBackground(getResources().getDrawable(R.drawable.edittext_default_bg_error));
                } else {
                    Intent intent = new Intent(v.getContext(), ValidasiOrderActivity.class);
                    intent.putExtra("harga", harga);
                    intent.putExtra("nama_produk", order_judul.getText().toString());
                    intent.putExtra("kapasitas", produk_kapasitas.getText().toString());
                    intent.putExtra("boom", produk_boom.getText().toString());
                    intent.putExtra("jib", produk_jib.getText().toString());
                    intent.putExtra("nama_bank", order_bank.getText().toString());
                    intent.putExtra("atas_nama", order_atas_nama.getText().toString());
                    intent.putExtra("nomor_rekening", order_nomor_transfer.getText().toString());
                    intent.putExtra("produk_id", getIntent().getStringExtra("produk_id"));
                    intent.putExtra("produk_published", getIntent().getStringExtra("produk_published"));
                    intent.putExtra("tipe", "Dijual");

                    startActivity(intent);
                }
            }
        });
    }

}
