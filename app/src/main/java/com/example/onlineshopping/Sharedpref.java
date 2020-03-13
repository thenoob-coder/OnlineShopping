package com.example.onlineshopping;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;


public class Sharedpref {

    SharedPreferences pref;
    Editor editor;
    Context context;
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "MyPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_PHONE = "phone";

    // Email address (make variable public to access from outside)
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_NAME = "name";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_IMAGE = "image";



    public Sharedpref(Context context)
    {
        this.context =context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();

    }
    public void createLoginSession(String phone, String password,String name,String address ,String image){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_PHONE, phone);

        // Storing email in pref
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_IMAGE, image);
        editor.putString(KEY_ADDRESS, address);

        // commit changes
        editor.commit();
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
    public HashMap<String , String >getuserdetails()
    {
        HashMap<String , String> user  = new HashMap<String, String>();
        user.put(KEY_PHONE,pref.getString(KEY_PHONE,null));
        // user email id
        user.put(KEY_PASSWORD,pref.getString(KEY_PASSWORD,null));
        user.put(KEY_NAME,pref.getString(KEY_NAME,null));
        user.put(KEY_ADDRESS,pref.getString(KEY_ADDRESS,null));
        user.put(KEY_IMAGE,pref.getString(KEY_IMAGE,null));

        return  user;
    }

    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        context.startActivity(i);
    }
    public String getuser ()
    {
        return pref.getString(KEY_PHONE,null);

    }
    public String getusername ()
    {
        return pref.getString(KEY_NAME,null);

    }


}
