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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

public class ValidasiOrderActivity extends AppCompatActivity {

    UserSession session;

    String nama_produk, kapasitas, boom, jib, nama_bank, atas_nama, nomor_rekening, tipe, tujuan_transfer, harga, date1, date2, produk_id, produk_published = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validasi_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

                AlertDialog.Builder ab = new AlertDialog.Builder(ValidasiOrderActivity.this);
                ab.setMessage("Are you sure want to Logout?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        Spinner staticSpinner = (Spinner) findViewById(R.id.spinner);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.bank,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        staticSpinner.setAdapter(staticAdapter);

        harga = getIntent().getStringExtra("harga");
        nama_produk = getIntent().getStringExtra("nama_produk");
        kapasitas = getIntent().getStringExtra("kapasitas");
        boom = getIntent().getStringExtra("boom");
        jib = getIntent().getStringExtra("jib");
        nama_bank = getIntent().getStringExtra("nama_bank");
        atas_nama = getIntent().getStringExtra("atas_nama");
        nomor_rekening = getIntent().getStringExtra("nomor_rekening");
        tipe = getIntent().getStringExtra("tipe");
        date1 = getIntent().getStringExtra("date1");
        date2 = getIntent().getStringExtra("date2");
        produk_id = getIntent().getStringExtra("produk_id");
        produk_published = getIntent().getStringExtra("produk_published");


        Button validasi = (Button) findViewById(R.id.validasi);
        validasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Spinner tujuan = (Spinner) findViewById(R.id.spinner);
                tujuan_transfer = tujuan.getSelectedItem().toString();

                Intent intent = new Intent(v.getContext(), KonfirmasiPembayaranActivity.class);
                intent.putExtra("harga", harga);
                intent.putExtra("date1", date1);
                intent.putExtra("date2", date2);
                intent.putExtra("nama_produk", nama_produk);
                intent.putExtra("kapasitas", kapasitas);
                intent.putExtra("boom", boom);
                intent.putExtra("jib", jib);
                intent.putExtra("nama_bank", nama_bank);
                intent.putExtra("atas_nama", atas_nama);
                intent.putExtra("nomor_rekening", nomor_rekening);
                intent.putExtra("tipe", tipe);
                intent.putExtra("tujuan_transfer", tujuan_transfer);
                intent.putExtra("produk_id", produk_id);
                intent.putExtra("produk_published", produk_published);

                startActivity(intent);
            }
        });
    }
}
