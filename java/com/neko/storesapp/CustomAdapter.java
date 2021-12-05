package com.neko.storesapp;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> implements View.OnClickListener, View.OnLongClickListener{

    private View.OnClickListener listener;
    private View.OnLongClickListener longListener;
    private final ArrayList<Store> storeList;



    public CustomAdapter(ArrayList<Store> storeList) {
        this.storeList = storeList;
    }

    @NonNull
    @Override

    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, null, false);

        view.setOnClickListener(this);
        view.setOnLongClickListener(this);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {


        holder.eName.setText(storeList.get(position).getName());
        holder.eDescription.setText(storeList.get(position).getDescription());
        holder.photo.setImageResource(storeList.get(position).getPhoto());


    }



    @Override
    public int getItemCount() {
        return storeList.size();
    }


    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {

        if(listener != null){
            listener.onClick(v);
        }

    }

    public void setOnLongClickListener(View.OnLongClickListener longListener) {
        this.longListener =  longListener;
    }

    @Override
    public boolean onLongClick(View v) {

        if(longListener != null) {
            longListener.onLongClick(v);
        }
        return false;
    }


    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView eName;
        private TextView eDescription;
        private final ImageView photo;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            eName = itemView.findViewById(R.id.nameView);
            eDescription = itemView.findViewById(R.id.descView);
            photo = itemView.findViewById(R.id.storePic);

        }

        void setName (String name) {
            eName = itemView.findViewById(R.id.nameView);
            eName.setText(name);
        }

        void setDescription (String description) {
            eDescription = itemView.findViewById(R.id.descView);
            eDescription.setText(description);
        }


    }
}
