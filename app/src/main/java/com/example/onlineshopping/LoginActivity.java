package com.example.onlineshopping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.text.TextUtils.isEmpty;

public class LoginActivity extends AppCompatActivity {
    private Button loginButton;
    private EditText  etPhoneNumber, etPassword;
    private ProgressDialog loadingBar;
    private TextView AdminLink,NotAdminLink;
    private Sharedpref preff;
    private String dbname = "Users";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.login_btn);
        etPhoneNumber = (EditText) findViewById(R.id.login_phone_number_input);
        etPassword = (EditText) findViewById(R.id.login_password_input);
        loadingBar = new ProgressDialog(this);
        AdminLink = (TextView) findViewById(R.id.admin_panel_link);
        NotAdminLink = (TextView) findViewById(R.id.not_admin_panel_link);
        preff =new Sharedpref((getApplicationContext()));
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Loginuser();
            }
        });

        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                loginButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                dbname = "Admins";
                clearet();
            }
        });

        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                loginButton.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                dbname = "Users";
                clearet();
            }
        });
    }

    private void Loginuser() {
        String pw = etPassword.getText().toString();
        String phone =etPhoneNumber.getText().toString();
        if(isEmpty(etPhoneNumber.getText().toString()) || isEmpty(etPassword.getText().toString())  )
        {
            Toast.makeText(this, "Please write your phone number and password...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Authenticating Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            Authenticateaccount(phone,pw);
        }

    }

    private void Authenticateaccount(final String phone, final String pw) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(dbname).child(phone).exists())
                {
                    // retrieving data from Users database by unique phonr no. and saving its values to users.class for verificstion
                    Users userdata =dataSnapshot.child(dbname).child(phone).getValue(Users.class);
                    if ( userdata.getPhone().equals(phone))
                    {
                        if (userdata.getPassword().equals(pw))
                        {
                            if (dbname.equals("Users")) {
                                preff.createLoginSession(phone, pw,userdata.getName(),userdata.getAddress(),userdata.getImage());
                                Toast.makeText(LoginActivity.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                clearet();
                            }
                            if (dbname.equals("Admins"))
                            {
                                Toast.makeText(LoginActivity.this, "Welcome Admin, you are logged in Successfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                startActivity(intent);
                                clearet();
                            }

                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Incorrect Password..", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            clearet();

                        }
                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Account with this " + phone + " number do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    clearet();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void  clearet()
    {
        etPassword.setText("");
        etPhoneNumber.setText("");

    }
}
