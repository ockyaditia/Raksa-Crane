package com.example.ockyaditiasaputra.raksacrane;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by Ocky Aditia Saputra on 09/10/2015.
 */
public class UserSession {

    SharedPreferences pref;
    Editor editor;
    Context _context;

    private static final String IS_USER_LOGIN = "IsUserLoggedIn";
    public static final String KEY_NAME = "Username";
    public static final String KEY_PASSWORD = "Password";
    public static final String KEY_EMAIL = "Email";
    public static final String KEY_FULLNAME = "Fullname";
    public static final String KEY_ADDRESS = "Address";
    public static final String KEY_PHONE = "Phone";

    public UserSession(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences("usersession_raksacrane", Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createUserLoginSession(String name, String password, String email, String fullname, String address, String phone) {
        editor.putBoolean(IS_USER_LOGIN, true);

        editor.putString(KEY_NAME, name);

        editor.putString(KEY_PASSWORD, password);

        editor.putString(KEY_EMAIL, email);

        editor.putString(KEY_FULLNAME, fullname);

        editor.putString(KEY_ADDRESS, address);

        editor.putString(KEY_PHONE, phone);

        editor.commit();
    }

    public boolean checkLogin() {
        if (!this.isUserLoggedIn()) {
            return false;
        } else {
            return true;
        }
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
    }

    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }
}
