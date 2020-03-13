package com.example.onlineshopping;



import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private Button register,login;
    Sharedpref pref;
    String answer ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register =(Button)findViewById(R.id.registerbtn);
        login=(Button)findViewById(R.id.loginbtn);
        pref =new Sharedpref((getApplicationContext()));
        if(pref.isLoggedIn())
        {   Intent i=new Intent(MainActivity.this,HomeActivity.class);
            startActivity(i);
            Toast.makeText(getBaseContext(), "Welcome " + pref.getusername(),
                    Toast.LENGTH_LONG).show();


        }




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(CheckConnection()) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else
                    Toast.makeText(MainActivity.this,answer,Toast.LENGTH_SHORT).show();

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(CheckConnection()) {
                    Intent intent  = new Intent(MainActivity.this ,RegisterActivity.class);
                    startActivity(intent);
                }
                else
                    Toast.makeText(MainActivity.this,answer,Toast.LENGTH_SHORT).show();

            }
        });
    }

    private boolean CheckConnection() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                answer = "You are connected to a WiFi Network";

            }
            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                answer = "You are connected to a Mobile Network";
            }
            return true;
        }
        else {
            answer = "No internet Connectivity";
            return  false;
        }
    }
}

