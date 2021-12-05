package com.neko.storesapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.neko.storesapp.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity implements View.OnLongClickListener{

    private static final int REQUEST_PERMISSION = 1 ;
    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private CustomViewModel customViewModel;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.neko.storesapp.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.storesFragment, R.id.mapFragment, R.id.newStoreFragment, R.id.newStoreFragment)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        customViewModel = new ViewModelProvider(this).get(CustomViewModel.class);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();


        View navView = navigationView.getHeaderView(0);
        TextView displayEmail = navView.findViewById(R.id.displayEmail);
        TextView displayName = navView.findViewById(R.id.displayUsername);
        TextView displayNameH = findViewById(R.id.displayUsernameH);
        TextView cardDescription = findViewById(R.id.bio);
        CardView cardView = findViewById(R.id.cardViewDescription);

        cardView.setOnLongClickListener(this);


         if (user != null){
             for(UserInfo profile:user.getProviderData()){
                 String name = profile.getDisplayName();
                 String email = profile.getEmail();

                 displayName.setText(name);
                 displayEmail.setText(email);
                 displayNameH.setText(name);

                 customViewModel.setUsername(name);
                 customViewModel.setUserEmail(email);
             }

             db.collection("Usuarios")
                     .document(user.getUid())
                     .get()
                     .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                         @Override
                         public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                             if (task.isSuccessful()){
                                 DocumentSnapshot document = task.getResult();
                                 if(document.exists()){

                                     String description = document.getString("description");
                                     cardDescription.setText(description);
                                     customViewModel.setUserDescription(description);

                                 }

                             }
                         }
                     });


         }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.log_out) {
            mAuth.signOut();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public boolean onLongClick(View v) {
        Navigation.findNavController(v).navigate(R.id.userDescriptionFragment);
        return false;
    }
}