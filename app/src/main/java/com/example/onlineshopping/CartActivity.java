package com.example.onlineshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private  Button placeorder;
    private TextView totalamt;
    private RecyclerView.LayoutManager layoutManager;
    private Sharedpref pref;
    private int total_amt=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        placeorder =(Button)findViewById(R.id.placeorder);
        totalamt =(TextView) findViewById(R.id.totalamt);
        placeorder =(Button)findViewById(R.id.placeorder);
        recyclerView = (RecyclerView)findViewById(R.id.cartlist);

        recyclerView.setHasFixedSize(true);
        layoutManager =new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        pref = new Sharedpref(getApplicationContext());

        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CartActivity.this,OrderConfirmationActivity.class);
                intent.putExtra("amt",String.valueOf(total_amt));
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("cart list");

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(databaseReference.child("User view")
                                .child(pref.getuser())
                                .child("products"), Cart.class)
                                .build();

        FirebaseRecyclerAdapter<Cart,CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull final Cart cart) {
                cartViewHolder.txtProductName.setText(cart.getPname());
                cartViewHolder.txtProductPrice.setText(cart.getPrice());
                cartViewHolder.txtProductQuantity.setText("Qty - "+cart.getQuantity());
                Picasso.get().load(cart.getImage()).into(cartViewHolder.imageView);

                int amt = (Integer.valueOf(cart.getPrice())*Integer.valueOf(cart.getQuantity()));
                total_amt += amt;

                totalamt.setText("Total Amount $"+ String.valueOf(total_amt) );
                cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        CharSequence options [] =new CharSequence[]
                        {
                            "Edit","Remove item"

                        };
                        final AlertDialog.Builder builder =new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options:");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i==0)
                                {
                                    Intent intent =new Intent(CartActivity.this,ProductDetailsActivity.class);
                                    intent.putExtra("pid",cart.getPid());
                                    startActivity(intent);

                                }
                                if (i==1)
                                {
                                    databaseReference.child("User view")
                                            .child(pref.getuser())
                                            .child("products")
                                            .child(cart.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful())
                                                    {
                                                        Toast.makeText(CartActivity.this,"Item Removed succesfully",Toast.LENGTH_SHORT).show();

                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(CartActivity.this,"Something went wrong :(",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });


                                }

                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;

            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}
