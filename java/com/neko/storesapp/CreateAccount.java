package com.neko.storesapp;

import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateAccount extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private static int LOADING_TIME_OUT = 3400;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);


        mAuth = FirebaseAuth.getInstance();
        EditText email = findViewById(R.id.newEmail);
        EditText password = findViewById(R.id.newPassword);
        EditText username = findViewById(R.id.newUserName);
        progressBar = findViewById(R.id.progressBarId);
        progressBar.setVisibility(View.INVISIBLE);


        Button createAccountButton = findViewById(R.id.newAccoutButton);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newEmail = email.getText().toString();
                String newPassword = password.getText().toString();
                String newUsername = username.getText().toString();


                int lenght = newPassword.length();

                if (lenght < 8){
                    password.setError("El mínimo de caracteres es 8");
                    password.requestFocus();
                }
                else if (TextUtils.isEmpty(newEmail)){
                    email.setError("El correo no puede estar vacio");
                    email.requestFocus();
                }
                else if (TextUtils.isEmpty(newUsername)){
                    username.setError("Escribe un nombre de ususario");
                    username.requestFocus();
                }
                else {
                    createAccount(newUsername,newEmail, newPassword);

                }


            }
        });

    }

    private void createAccount (String username, String email, String password){


        progressBar.setVisibility(VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Log.d("uwu", "Create user with email: Success!");


                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();

                            user = mAuth.getCurrentUser();
                            assert user != null;
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("uwu", "User profile updated.");
                                            }
                                        }
                                    });

                            if(user != null){

                                String uid = user.getUid();
                                Map<String, Object> userDesc = new HashMap<>();

                                userDesc.put("description", "Aquí iría una descripción de ti..");

                                db = FirebaseFirestore.getInstance();
                                db.collection("Usuarios")
                                        .document(uid)
                                        .set(userDesc)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Log.d("uwu", "writing description Success");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("uwu", "writing description failed");
                                            }
                                        });
                            }


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    progressBar.setVisibility(View.INVISIBLE);


                                }
                            },LOADING_TIME_OUT);

                        }
                        else {
                            Log.w("uwu", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Autentificación fallida",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }
}