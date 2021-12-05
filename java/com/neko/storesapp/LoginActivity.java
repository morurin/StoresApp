package com.neko.storesapp;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    TextView createdView;
    private FirebaseAuth mAuth;
    private EditText emailId;
    private EditText passwordId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);


        mAuth = FirebaseAuth.getInstance();

        loginButton = findViewById(R.id.loginbtn);
        createdView = findViewById(R.id.createAcountId);
        emailId = findViewById(R.id.emailId);
        passwordId = findViewById(R.id.passwordId);



        createdView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),CreateAccount.class);
                startActivity(intent);

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String password = passwordId.getText().toString();

                if(TextUtils.isEmpty(email)){
                    emailId.setError("El correo no puede estar vacio");
                    emailId.requestFocus();
                }
                else if(TextUtils.isEmpty(password)){
                    passwordId.setError("Ingresa tu contraseña");
                    passwordId.requestFocus();
                }
                else{
                    login(email, password);
                }
            }
        });

    }


    private void login (String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Log.d("uwu","SingIn: Success");
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Log.w("uwu", "signIn: failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Autenticación fallida",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    protected void onStart(){
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}

