package com.example.onlineshopping;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;



public class AdminCategoryActivity extends AppCompatActivity {
    GridView simpleGrid;
    int logos[] = {R.drawable.books, R.drawable.female_dresses, R.drawable.glasses, R.drawable.hats,
            R.drawable.headphoness, R.drawable.laptops, R.drawable.mobiles, R.drawable.purses_bags, R.drawable.shoess,
            R.drawable.sports, R.drawable.sweather, R.drawable.tshirts, R.drawable.watches};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);
        simpleGrid = (GridView) findViewById(R.id.simpleGridView); // init GridView
        // Create an object of CustomAdapter and set Adapter to GirdView
        CustomAdapterAdminCategory customAdapter = new CustomAdapterAdminCategory(getApplicationContext(), logos);
        simpleGrid.setAdapter(customAdapter);
        // implement setOnItemClickListener event on GridView
        simpleGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // set an Intent to Another Activity
                Intent i =new Intent(AdminCategoryActivity.this,AdminAddNewProductActivity.class);
                switch (position)
                {
                    case 0:
                        i.putExtra("category", "Books");
                        startActivity(i);
                        break;
                    case 1:
                        i.putExtra("category", "Female Dresses");
                        startActivity(i);
                        break;

                    case 2:
                        i.putExtra("category", "Sun glasses");
                        startActivity(i);
                        break;

                    case 3:
                        i.putExtra("category", "Hats and Caps");
                        startActivity(i);
                        break;

                    case 4:
                        i.putExtra("category", "Headphones");
                        startActivity(i);
                        break;

                    case 5:
                        i.putExtra("category", "Laptops");
                        startActivity(i);
                        break;

                    case 6:
                        i.putExtra("category", "Mobiles");
                        startActivity(i);
                        break;

                    case 7:
                        i.putExtra("category", "Purses Wallets");
                        startActivity(i);
                        break;

                    case 8:
                        i.putExtra("category", "Shoes");
                        startActivity(i);
                        break;

                    case 9:
                        i.putExtra("category", "Sports T shirts");
                        startActivity(i);
                        break;
                    case 10:
                        i.putExtra("category", "Sweather");
                        startActivity(i);
                        break;
                    case 11:
                        i.putExtra("category", "T shirts");
                        startActivity(i);
                        break;
                    case 12:
                        i.putExtra("category", "Watches");
                        startActivity(i);
                        break;

                }
            }
        });
    }
}