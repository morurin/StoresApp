package com.neko.storesapp;

import static android.view.View.INVISIBLE;

import android.annotation.SuppressLint;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;


public class StoresFragment extends Fragment implements PopupMenu.OnMenuItemClickListener{


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerStore;
    private ArrayList<Store> storeList;
    private CustomViewModel customViewModel;
    private ProgressBar progressBar;



    public StoresFragment() {
        // Required empty public constructor
    }


    public static StoresFragment newInstance(String param1, String param2) {
        StoresFragment fragment = new StoresFragment();
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
        View view = inflater.inflate(R.layout.fragment_stores, container, false);


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        progressBar = view.findViewById(R.id.progressBarId2);
        storeList = new ArrayList<>();
        customViewModel = new ViewModelProvider(requireActivity()).get(CustomViewModel.class);

        FloatingActionButton addStoreButton = view.findViewById(R.id.addStoreButton);
        addStoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Navigation.findNavController(v).navigate(R.id.newStoreFragment);

            }
        });



        recyclerStore = view.findViewById(R.id.recyclerId);
        recyclerStore.setLayoutManager(new LinearLayoutManager(getContext()));

        db.collection("Tiendas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Store store = new Store
                                        (document.getId(),
                                        document.getString("name"),
                                        document.getString("owner"),
                                        document.getDouble("latitude"),
                                        document.getDouble("longitude"),
                                        R.drawable.store);

                                storeList.add(store);

                            }


                            CustomAdapter adapter = new CustomAdapter(storeList);
                            setOnclick(adapter);



                        }
                    }
                });

        swipeRefreshLayout = view.findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                storeList.clear();
                db.collection("Tiendas")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                        Store store = new Store
                                                (document.getId(),
                                                        document.getString("name"),
                                                        document.getString("owner"),
                                                        document.getDouble("latitude"),
                                                        document.getDouble("longitude"),
                                                        R.drawable.store);

                                        storeList.add(store);

                                    }

                                    CustomAdapter adapter = new CustomAdapter(storeList);
                                    adapter.notifyDataSetChanged();
                                    swipeRefreshLayout.setRefreshing(false);
                                    setOnclick(adapter);



                                }
                            }
                        });

            }
        });

        return view;
    }

    public void setOnclick(CustomAdapter adapter){

        recyclerStore.setAdapter(adapter);
        progressBar.setVisibility(INVISIBLE);

        adapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String storeId = storeList.get(recyclerStore.getChildAdapterPosition(v)).getId();
                TextView textView = requireView().findViewById(R.id.StoreId);
                textView.setText(storeId);

                customViewModel.setName(storeList.get(recyclerStore.getChildAdapterPosition(v)).getName());
                customViewModel.setDescription(storeList.get(recyclerStore.getChildAdapterPosition(v)).getDescription());
                showPopup(v);
                viewModelData(storeId);
                return false;

            }
        });
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customViewModel.setLatitude(storeList.get(recyclerStore.getChildAdapterPosition(v)).getLatitude());
                customViewModel.setLongitude(storeList.get(recyclerStore.getChildAdapterPosition(v)).getLongitude());
                customViewModel.setName(storeList.get(recyclerStore.getChildAdapterPosition(v)).getName());
                customViewModel.setDescription(storeList.get(recyclerStore.getChildAdapterPosition(v)).getDescription());
                String storeId = storeList.get(recyclerStore.getChildAdapterPosition(v)).getId();
                viewModelData(storeId);
                Navigation.findNavController(requireView()).navigate(R.id.storeMapFragment);

            }
        });

    }

    public void viewModelData(String id){

        CustomViewModel customViewModel = new ViewModelProvider(requireActivity()).get(CustomViewModel.class);
        customViewModel.setStoreViewId(id);

    }



    public void showPopup(View v){
        PopupMenu popupMenu = new PopupMenu(getContext(), v, Gravity.END);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.recycler_menu);
        popupMenu.show();


    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){

            case R.id.delete:

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                TextView textView = requireView().findViewById(R.id.StoreId);
                String storeId = textView.getText().toString();



                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setMessage("¿Desea eliminar este establecimiento?")
                        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                db.collection("Tiendas")
                                        .document(storeId)
                                        .delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(requireContext(), "Establecimiento eliminado",
                                                            Toast.LENGTH_SHORT).show();

                                                    storeList.clear();
                                                    db.collection("Tiendas")
                                                            .get()
                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                                                                @SuppressLint("NotifyDataSetChanged")
                                                                @Override
                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                    if (task.isSuccessful()) {
                                                                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                                                            Store store = new Store
                                                                                    (document.getId(),
                                                                                            document.getString("name"),
                                                                                            document.getString("owner"),
                                                                                            document.getDouble("latitude"),
                                                                                            document.getDouble("longitude"),
                                                                                            R.drawable.store);

                                                                            storeList.add(store);

                                                                        }

                                                                        CustomAdapter adapter = new CustomAdapter(storeList);
                                                                        setOnclick(adapter);



                                                                    }
                                                                }
                                                            });


                                                }
                                            }
                                        });
                            }

                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        })
                        .create().show();



                return true;

            case R.id.edit:

                Navigation.findNavController(requireView()).navigate(R.id.edit);

                return true;

            default:
                return false;
        }
    }


}
