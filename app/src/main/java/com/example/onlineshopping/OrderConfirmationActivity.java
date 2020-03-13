package com.example.onlineshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class OrderConfirmationActivity extends AppCompatActivity {

  private   EditText name,address,phone;
   private TextView amt;
   private Button pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        name =(EditText)findViewById(R.id.order_nameet);
        address =(EditText)findViewById(R.id.order_addresset);
        phone =(EditText)findViewById(R.id.order_phoneet);
        amt =(TextView)findViewById(R.id.totalamt);
        pay=(Button)findViewById(R.id.paybtn);



        Displaydetail();


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderconfirm();
            }
        });
    }

    private void orderconfirm() {

        if (TextUtils.isEmpty(name.getText().toString()))
        {
            Toast.makeText(this, "Reciever name is mandatory", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone.getText().toString()) && phone.length()!=10)
        {
            Toast.makeText(this, "Please write your phone number correctly...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(address.getText().toString()) )
        {
            Toast.makeText(this, " Delivery Address is mandatory. ", Toast.LENGTH_SHORT).show();
        }
        else

        {


        }
    }

    private void Displaydetail() {

        Sharedpref pref =new Sharedpref(getApplicationContext());
        HashMap<String, String> user = pref.getuserdetails();

       name.setText(user.get(Sharedpref.KEY_NAME));
        address.setText(user.get(Sharedpref.KEY_ADDRESS));
        phone.setText(user.get(Sharedpref.KEY_PHONE));

        amt.setText("Total Amount : $"+getIntent().getStringExtra("amt"));
    }
}
