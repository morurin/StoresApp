package com.neko.storesapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;




public class StoreMapFragment extends Fragment implements OnMapReadyCallback {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private MapView mapView;
    private GoogleMap gmap;
    private Marker marker;
    private CustomViewModel customViewModel;
    private FusedLocationProviderClient client;
    private FirebaseFirestore db;
    public final int REQUEST_PERMISSION = 1;


    public StoreMapFragment() {
        // Required empty public constructor
    }


    public static StoreMapFragment newInstance(String param1, String param2) {
        StoreMapFragment fragment = new StoreMapFragment();
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
        View view = inflater.inflate(R.layout.fragment_store_map, container, false);

        db = FirebaseFirestore.getInstance();
        customViewModel = new ViewModelProvider(requireActivity()).get(CustomViewModel.class);


        String storeName = customViewModel.getName();
        String storeOwner = customViewModel.getDescription();
        TextView sName = view.findViewById(R.id.storeName);
        TextView sOwner = view.findViewById(R.id.storeOwner);

        sName.setText(storeName);
        sOwner.setText(storeOwner);


        mapView = view.findViewById(R.id.storeMapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        client = LocationServices.getFusedLocationProviderClient(requireActivity());

        double lat = customViewModel.getLatitude();
        double lon = customViewModel.getLongitude();

        if (lat == 0 && lon == 0){
            getCurrentLocation();
        }
        else {
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull GoogleMap googleMap) {
                    LatLng storePosition = new LatLng(lat, lon);
                    MarkerOptions markerOp = new MarkerOptions().position(storePosition);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(storePosition, 18));
                    googleMap.addMarker(markerOp);


                }
            });
        }

        return view;
    }



    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION);

        }
        else {

            Task<Location> task = client.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if (location != null) {
                        mapView.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(@NonNull GoogleMap googleMap) {
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                            }
                        });
                    }


                }
            });
        }

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        gmap = googleMap;
        String document = customViewModel.getStoreViewId();
        gmap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {

                if(marker == null){
                    marker = googleMap.addMarker(new MarkerOptions().position(latLng));

                }
                else {
                    marker.setPosition(latLng);
                }

                Button setLocationButton = requireView().findViewById(R.id.setLocationStore);

                setLocationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        double lat = latLng.latitude;
                        double lon = latLng.longitude;


                        db.collection("Tiendas")
                                .document(document)
                                .update("latitude", lat,
                                        "longitude", lon)
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

                        Toast.makeText(requireContext(),"Posición guardada",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
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