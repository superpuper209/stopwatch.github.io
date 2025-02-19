package com.example.registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity1 extends AppCompatActivity {
    private Button reg_button1;
    private EditText NameEditText;
    private EditText EmailEditText1;
    private EditText PasswordEditText1;

    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register1);

        Button reg_button1 = findViewById(R.id.reg_button1);
        NameEditText =  findViewById(R.id.NameEditText);
        EmailEditText1 = findViewById(R.id.EmailEditText1);
        PasswordEditText1= (EditText)findViewById(R.id.PasswordEditText1);

        loadingBar = new ProgressDialog(this);

       reg_button1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {CreateAccount();}
       });

    }

    private void CreateAccount() {
        String name = NameEditText.getText().toString();
        String phone = EmailEditText1.getText().toString();
        String password = PasswordEditText1.getText().toString();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Введите имя", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Введите номер телефона", Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Введите пароль", Toast.LENGTH_LONG).show();
        }
        else {
            loadingBar.setTitle("Создание аккаунта ");
            loadingBar.setMessage("Пожалуйста, подождите...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidateEmail(name, phone, password);
        }

    }

    private void ValidateEmail(final String name, final String phone, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("Users").child(phone).exists())) {

                    HashMap<String, Object> userDataMap = new HashMap<>();

                    userDataMap.put("phone", phone);
                    userDataMap.put("name", name);
                    userDataMap.put("password", password);

                    RootRef.child("Users").child(phone).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>(){

                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity1.this, "Регистрация прошла упешно", Toast.LENGTH_LONG).show();

                                        Intent intent = new Intent(RegisterActivity1.this, UserActivity1.class);
                                        startActivity(intent);
                                    }
                                    else {

                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity1.this, "Ошибка регистрации", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });

                }
                else {
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity1.this, "Такой номер уже используется", Toast.LENGTH_LONG).show();


                    Intent intent = new Intent(RegisterActivity1.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}