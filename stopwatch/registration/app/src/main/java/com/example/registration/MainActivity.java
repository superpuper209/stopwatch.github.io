package com.example.registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button login_button;
    private EditText PhoneEditText;
    private EditText PasswordEditText;
    private TextView AdminLink, NotAdminLink;
    private ProgressDialog loadingBar;

    private String parentDbName = "Users";


    public void OnGoReg(View view) {
        Intent intent = new Intent(this, RegisterActivity1.class);
        startActivity(intent);
    }
    public void OnEntrance(View view) {
        findViewById(R.id.first_entrance_layout).setVisibility(View.INVISIBLE);
        findViewById(R.id.entrance_layout).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);



        loadingBar = new ProgressDialog(this);

        login_button = (Button) findViewById(R.id.login_button);
        PhoneEditText = (EditText) findViewById(R.id.PhoneEditText );
        PasswordEditText= (EditText) findViewById(R.id.PasswordEditText);

        AdminLink = findViewById(R.id.admin_panel_link);
        NotAdminLink = findViewById(R.id.not_admin_panel_link);
          Paper.init(this);


       AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentDbName = "Admins";
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                login_button.setText("Вход для админа");
            }
        });


        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentDbName = "Users";
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                login_button.setText("Вход для пользователя");
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

    }
    private void loginUser() {
        String phone = PhoneEditText.getText().toString();
        String password = PasswordEditText.getText().toString();

        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Введите номер телефона", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Введите пароль", Toast.LENGTH_LONG).show();
        }
        else {
            loadingBar.setTitle("Вход в приложение ");
            loadingBar.setMessage("Пожалуйста, подождите...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidateUser(phone, password);
        }
    }

    private void ValidateUser(final String phone, String password) {


        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(parentDbName).child(phone).exists()) {
                    Users usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if(usersData.getPhone().equals(phone)) {

                        if(usersData.getPassword().equals(password)) {

                            if(parentDbName.equals("Users")) {
                                loadingBar.dismiss();
                                Toast.makeText(MainActivity.this, "Успешный вход", Toast.LENGTH_LONG).show();

                                Intent UserIntent = new Intent(MainActivity.this, UserActivity1.class);
                                startActivity(UserIntent);
                            }
                            else if(parentDbName.equals("Admins")) {
                                loadingBar.dismiss();
                                Toast.makeText(MainActivity.this, "Успешный вход", Toast.LENGTH_LONG).show();

                                Intent AdminIntent = new Intent(MainActivity.this, AdminsActivity.class);
                                startActivity(AdminIntent);
                            }

                        }
                        else {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Неверный пароль", Toast.LENGTH_LONG).show();
                        }
                    }

                } else {
                    loadingBar.dismiss();
                    Toast.makeText(MainActivity.this, "Аккаунта с таким номером телефона не сущестует", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(MainActivity.this, RegisterActivity1.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

