package com.neko.storesapp;

import static android.view.View.INVISIBLE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class userDescriptionFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private FirebaseFirestore db;
    private CustomViewModel customViewModel;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private static int TIME_OUT = 1000;
    private ProgressBar progressBar;

    public userDescriptionFragment() {
        // Required empty public constructor
    }


    public static userDescriptionFragment newInstance(String param1, String param2) {
        userDescriptionFragment fragment = new userDescriptionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_d_escription, container, false);

        EditText desc = view.findViewById(R.id.descriptionText);
        Button descButton = view.findViewById(R.id.descriptionButton);

        progressBar = view.findViewById(R.id.progressBarDesc);
        progressBar.setVisibility(INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        customViewModel = new ViewModelProvider(requireActivity()).get(CustomViewModel.class);

        db = FirebaseFirestore.getInstance();


        descButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = desc.getText().toString();
                if (TextUtils.isEmpty(description)){

                    Navigation.findNavController(requireView()).navigate(R.id.nav_home);

                }
                else{

                    progressBar.setVisibility(View.VISIBLE);
                    db.collection("Usuarios")
                            .document(user.getUid())
                            .update("description", description)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(requireContext(),"Descripción Actualizada",
                                                Toast.LENGTH_SHORT).show();

                                        customViewModel.setUserDescription(description);


                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {

                                                progressBar.setVisibility(INVISIBLE);
                                                Navigation.findNavController(requireView()).navigate(R.id.nav_home);
                                            }
                                        },TIME_OUT);
                                    }

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(requireContext(), "Error actualizando descripción",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });


                }
            }
        });

        return  view;
    }
}