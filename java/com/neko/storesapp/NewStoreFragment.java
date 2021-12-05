package com.neko.storesapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;



public class NewStoreFragment extends Fragment implements View.OnClickListener{


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;


    public NewStoreFragment() {
        // Required empty public constructor
    }


    public static NewStoreFragment newInstance(String param1, String param2) {
        NewStoreFragment fragment = new NewStoreFragment();
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
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_new_store, container, false);

        Button saveButton = view.findViewById(R.id.saveBtnId);
        saveButton.setOnClickListener(this);


        return view;

    }



    @Override
    public void onClick(View v) {

        EditText editText = requireView().findViewById(R.id.newTxtStoreId);
        EditText editText1 = requireView().findViewById(R.id.newTxtOwnerId);
        String txtName = editText.getText().toString();
        String txtOwner = editText1.getText().toString();



        if (TextUtils.isEmpty(txtName)) {
            editText.setError("Escribe el nombre del establecimiento");
            editText.requestFocus();

        }
        else if (TextUtils.isEmpty(txtOwner)){
            editText1.setError("Escribe el nombre del propietario");
            editText1.requestFocus();
        }
        else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            Map<String, Object> store = new HashMap<>();
            store.put("name", txtName);
            store.put("owner", txtOwner);
            store.put("latitude", 0);
            store.put("longitude", 0);


            db.collection("Tiendas").document()
                    .set(store)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("Completed", "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("Error", "Error writing document", e);
                        }
                    });

            Toast.makeText(getContext(), "Establecimiento añadida con exito", Toast.LENGTH_SHORT).show();
        }



    }



}
