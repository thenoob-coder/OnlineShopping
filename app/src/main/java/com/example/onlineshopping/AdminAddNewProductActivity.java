package com.example.onlineshopping;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static android.text.TextUtils.isEmpty;

public class AdminAddNewProductActivity extends AppCompatActivity {
    private EditText productname, productdescription, productprice;
    private Button addproduct;
    private ImageView productimage;
    private Uri imageUri;
    private StorageReference ProductImagesRef;
    private String name,description,price, CurrentDate, CurrentTime ,productkey,downloadImageUrl,CategoryName;
    private final int IMAGE_REQUEST = 71;
    private ProgressDialog loadingBar;
    private DatabaseReference productref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);
        productname = (EditText) findViewById(R.id.product_name);
        productdescription = (EditText) findViewById(R.id.productt_description);
        productprice = (EditText) findViewById(R.id.product_price);
        addproduct = (Button) findViewById(R.id.add_new_product);
        productimage = (ImageView) findViewById(R.id.product_image);
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        CategoryName = getIntent().getExtras().get("category").toString();
        productref = FirebaseDatabase.getInstance().getReference().child("Product Details");
        loadingBar = new ProgressDialog(this);
        productimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectimage();
            }
        });
        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validatedetails();
            }
        });
    }



    private void selectimage() {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, IMAGE_REQUEST);
        } catch (Exception exception) {
            Toast.makeText(AdminAddNewProductActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                productimage.setImageBitmap(bitmap);
            } catch (Exception e) {
                Toast.makeText(AdminAddNewProductActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    private void Validatedetails() {
        name =productname.getText().toString();
        description =productdescription.getText().toString();
        price =productprice.getText().toString();

        if(isEmpty(productprice.getText().toString()) || isEmpty(productdescription.getText().toString()) ||isEmpty(productname.getText().toString()) )
        {
            Toast.makeText(this, "Please Enter allthe fields..", Toast.LENGTH_SHORT).show();
        }
        else if (imageUri ==null)
            Toast.makeText(this, "You need to upload the product image !", Toast.LENGTH_SHORT).show();
        else
        {
            storeinformation();
        }
    }

    private void storeinformation() {
        loadingBar.setTitle("Addding a New Product");
        loadingBar.setMessage("Please wait while details are being added..");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        Calendar calendar =Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat("MMM , dd ,yyyy");
        CurrentDate = currentdate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        CurrentTime = currentTime.format(calendar.getTime());
        productkey = CurrentDate+" "+CurrentTime;
        try{
            final StorageReference imageref = ProductImagesRef.child(imageUri.getLastPathSegment()+"."+ getfileExtension(imageUri));
            final UploadTask uploadTask = imageref.putFile(imageUri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    Toast.makeText(AdminAddNewProductActivity.this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    Toast.makeText(AdminAddNewProductActivity.this, "Product Image uploaded!!", Toast.LENGTH_SHORT).show();

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                        {
                            if (!task.isSuccessful())
                            {
                                throw task.getException();
                            }

                            downloadImageUrl = imageref.getDownloadUrl().toString();
                            return imageref.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task)
                        {
                            if (task.isSuccessful())
                            {
                                downloadImageUrl = task.getResult().toString();

                                SaveProductInfoToDatabase();
                            }
                        }
                    });
                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }


    }

    private String getfileExtension(Uri imageUri) {
        String extension;
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        extension= mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
        return extension;
    }

    private void SaveProductInfoToDatabase()
    {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("Productkey",productkey);
        productMap.put("Producttime",CurrentTime);
        productMap.put("ProductDate",CurrentDate);
        productMap.put("ProductPrice",price);
        productMap.put("ProductDescription",description);
        productMap.put("ProductName",name);
        productMap.put("ProductCategory",CategoryName);
        productMap.put("ProductUrl",downloadImageUrl);

        productref.child(productkey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
                            startActivity(intent);
                            Toast.makeText(AdminAddNewProductActivity.this, "Details added..", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                        else
                        {

                            String message = task.getException().toString();
                            Toast.makeText(AdminAddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }

                    }
                });

    }
}