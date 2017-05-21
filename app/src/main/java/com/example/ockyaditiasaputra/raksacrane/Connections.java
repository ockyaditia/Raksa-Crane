package com.example.ockyaditiasaputra.raksacrane;

/**
 * Created by Ocky Aditia Saputra on 15/11/2015.
 */
public class Connections {
    String host;

    public Connections() {
        host = "http://raksacrane.esy.es";
    }

    public String con() {
        return host;
    }

    public String getUser() {
        return "" + host + "/raksa_crane/mobile/get_user.php";
    }

    public String getProduk() {
        return "" + host + "/raksa_crane/mobile/get_produk.php";
    }

    public String getDeskripsi() {
        return "" + host + "/raksa_crane/mobile/get_deskripsi.php";
    }

    public String getAll() {
        return "" + host + "/raksa_crane/mobile/get_all.php";
    }

    public String getPembelian() {
        return "" + host + "/raksa_crane/mobile/get_pembelian.php";
    }

    public String getPenyewaan() {
        return "" + host + "/raksa_crane/mobile/get_penyewaan.php";
    }

    public String insertUser() {
        return "" + host + "/raksa_crane/mobile/insert_user.php";
    }

    public String insertKontak() {
        return "" + host + "/raksa_crane/mobile/insert_kontak.php";
    }

    public String insertBantuan() {
        return "" + host + "/raksa_crane/mobile/insert_bantuan.php";
    }

    public String updateProfile() {
        return "" + host + "/raksa_crane/mobile/update_profile.php";
    }

    public String getRekening() {
        return "" + host + "/raksa_crane/mobile/get_rekening.php";
    }

    public String insertOrder() {
        return "" + host + "/raksa_crane/mobile/insert_order.php";
    }

    public String getOrder() {
        return "" + host + "/raksa_crane/mobile/get_order.php";
    }

    public String updateDP() {
        return "" + host + "/raksa_crane/mobile/update_dp.php";
    }

    public String updateSisa() {
        return "" + host + "/raksa_crane/mobile/update_sisa.php";
    }
}
