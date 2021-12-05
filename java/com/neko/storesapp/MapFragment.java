package com.neko.storesapp;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Objects;


public class MapFragment extends Fragment implements OnMapReadyCallback {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private MapView mapView;
    private GoogleMap gMap;
    public final int REQUEST_PERMISSION = 1;
    FusedLocationProviderClient client;
    private FirebaseFirestore db;
    private ArrayList<Store> storesLocation;


    public MapFragment() {
        // Required empty public constructor
    }


    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
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
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        db = FirebaseFirestore.getInstance();


        storesLocation = new ArrayList<>();
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        client = LocationServices.getFusedLocationProviderClient(requireActivity());

        db.collection("Tiendas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
               @Override
               public void onComplete(@NonNull Task<QuerySnapshot> task) {
                   if (task.isSuccessful()) {
                       for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                           Store store = new Store
                                   (
                                   document.getString("name"),
                                   document.getDouble("latitude"),
                                   document.getDouble("longitude"));

                           storesLocation.add(store);

                       }
                       mapView.getMapAsync(new OnMapReadyCallback() {
                           @Override
                           public void onMapReady(@NonNull GoogleMap googleMap) {
                               for (Store storeLocation : storesLocation) {

                                   String storeName = storeLocation.getName();
                                   double latitude = storeLocation.getLatitude();
                                   double longitude = storeLocation.getLongitude();

                                   if(latitude != 0 && longitude != 0){
                                       LatLng latLng = new LatLng(latitude, longitude);
                                       MarkerOptions options = new MarkerOptions().position(latLng).title(storeName);
                                       googleMap.addMarker(options);
                                   }

                               }
                           }
                       });
                   }
               }
           });



        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION);
        }
        else {

            gMap.setMyLocationEnabled(true);
            gMap.setPadding(0,999,10,0);
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }


    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}