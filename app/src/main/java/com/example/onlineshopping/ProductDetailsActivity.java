package com.example.onlineshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {
    private ImageView productimage;
    private TextView productdescription,productprice,productname;
    private ElegantNumberButton numberbtn;
    private Button addtocart;
    private String productid ="";
    private  Sharedpref pref;
    private String proprice= "";
     private String phoneno;
      private  String productimageurl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productimage =(ImageView)findViewById(R.id.product_image_details);
        productdescription =(TextView)findViewById(R.id.product_description_details);
        productprice =(TextView)findViewById(R.id.product_price_details);
        productname=(TextView)findViewById(R.id.product_name_details);
        addtocart =(Button)findViewById(R.id.addtocartbtn);
        numberbtn =(ElegantNumberButton)findViewById(R.id.number_btn);

        productid =getIntent().getStringExtra("pid");

        pref = new Sharedpref(getApplicationContext());
        phoneno=pref.getuser();

       getproductdetails(productid);

       addtocart.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
            addproductstocart();
           }
       });
    }

    private void addproductstocart() {
       String savecurrentdate,savecurrenttime;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate= new SimpleDateFormat("MMM dd, yyyy");
        savecurrentdate = currentDate.format(calendar.getTime());

        SimpleDateFormat currenttime= new SimpleDateFormat("HH:mm:ss a");
        savecurrenttime = currenttime.format(calendar.getTime());


     final  DatabaseReference cartlistref = FirebaseDatabase.getInstance().getReference().child("cart list");

        final HashMap<String,Object> cartdetails =new HashMap<>();
        cartdetails.put("pid",productid);
        cartdetails.put("pname",productname.getText().toString());
        cartdetails.put("price",proprice);
        cartdetails.put("date",savecurrentdate);
        cartdetails.put("time",savecurrenttime);
        cartdetails.put("quantity",numberbtn.getNumber());
        cartdetails.put("image",productimageurl);
        cartdetails.put("discount", "");

        cartlistref.child("User view").child(phoneno).child("products").child(productid)
                .updateChildren(cartdetails)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        cartlistref.child("Admin view").child(pref.getuser()).child("products").child(productid)
                                .updateChildren(cartdetails)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(ProductDetailsActivity.this,"Item Added to cart",Toast.LENGTH_SHORT).show();
                                        Intent intent =new Intent(ProductDetailsActivity.this,HomeActivity.class);
                                        startActivity(intent);
                                    }
                                });
                    }
                });

    }

    public void  getproductdetails(String pid)
    {
        DatabaseReference productref = FirebaseDatabase.getInstance().getReference().child("Product Details");
        productref.child(pid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    Products products =dataSnapshot.getValue(Products.class);

                    productimageurl= products.getProductUrl();
                    productname.setText(products.getProductName());
                    productdescription.setText(products.getProductDescription());
                    productprice.setText("Price: $"+products.getProductPrice());

                    Picasso.get().load(products.getProductUrl()).into(productimage);
                    proprice = products.getProductPrice();




                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
