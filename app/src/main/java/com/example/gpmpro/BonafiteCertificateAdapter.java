package com.example.gpmpro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BonafiteCertificateAdapter extends RecyclerView.Adapter<BonafiteCertifateViewHolder> {
    @NonNull
    @Override
    public BonafiteCertifateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bonafite_model,parent,false);
        BonafiteCertifateViewHolder bonafiteCertifateViewHolder = new BonafiteCertifateViewHolder(itemView);
        bonafiteCertifateViewHolder.setOnClickListener(new BonafiteCertifateViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });


        return bonafiteCertifateViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BonafiteCertifateViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
