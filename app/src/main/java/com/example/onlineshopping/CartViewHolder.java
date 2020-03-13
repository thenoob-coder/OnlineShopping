package com.example.onlineshopping;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txtProductName, txtProductQuantity, txtProductPrice;
    public ImageView imageView;
    public ItemClickListner listner;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.cartproduct_image);
        txtProductName = (TextView) itemView.findViewById(R.id.cartproduct_name);
        txtProductQuantity = (TextView) itemView.findViewById(R.id.cartproduct_quantity);
        txtProductPrice = (TextView) itemView.findViewById(R.id.cartproduct_price);
    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view) {
        listner.onClick(view, getAdapterPosition(), false);

    }
}
