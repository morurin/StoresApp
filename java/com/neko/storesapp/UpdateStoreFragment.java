package com.neko.storesapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
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



public class UpdateStoreFragment extends Fragment implements View.OnClickListener{


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private CustomViewModel customViewModel;

    public UpdateStoreFragment() {
        // Required empty public constructor
    }

    public static UpdateStoreFragment newInstance(String param1, String param2) {
        UpdateStoreFragment fragment = new UpdateStoreFragment();
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

        View view = inflater.inflate(R.layout.fragment_update_store, container, false);


        Button updateButton;
        updateButton = view.findViewById(R.id.updateBtnId);
        updateButton.setOnClickListener(this);
        customViewModel = new ViewModelProvider(requireActivity()).get(CustomViewModel.class);


        String textName = customViewModel.getName();
        String textOwner = customViewModel.getDescription();
        EditText editText = view.findViewById(R.id.updateTxtStoreId);
        EditText editText1 = view.findViewById(R.id.updateTxtOwnerId);
        editText.setText(textName);
        editText1.setText(textOwner);



        return view;
    }

    @Override
    public void onClick(View v) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CustomViewModel customViewModel = new ViewModelProvider(requireActivity()).get(CustomViewModel.class);

        String document = customViewModel.getStoreViewId();

        EditText editText = requireView().findViewById(R.id.updateTxtStoreId);
        EditText editText1 = requireView().findViewById(R.id.updateTxtOwnerId);
        String newName = editText.getText().toString();
        String newOwner = editText1.getText().toString();

        if (TextUtils.isEmpty(newName)){
            editText.setError("Escribe el nombre del establecimiento");
            editText.requestFocus();

        }
        else if (TextUtils.isEmpty(newOwner)){
            editText1.setError("Escribe el nombre del propietario");
            editText1.requestFocus();

        }
        else {
            db.collection("Tiendas")
                    .document(document)
                    .update("name", newName,
                            "owner",newOwner)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("uwu", "DocumentSnapshot successfully updated!");

                            Toast.makeText(requireContext(),"Establecimiento actualizado",Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("error", "Error updating document", e);

                            Toast.makeText(requireContext(),"Error actualizando establecimiento",Toast.LENGTH_SHORT).show();
                        }
                    });

        }

        Navigation.findNavController(requireView()).navigate(R.id.storesFragment);



    }
}