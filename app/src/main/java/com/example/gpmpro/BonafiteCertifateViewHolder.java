package com.example.gpmpro;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BonafiteCertifateViewHolder extends RecyclerView.ViewHolder {

    View mView;
    public BonafiteCertifateViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v,getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v,getAdapterPosition());
                return true;
            }
        });
    }


    private BonafiteCertifateViewHolder.ClickListener mClickListener;

    public interface ClickListener{
        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);
    }
    public void setOnClickListener(BonafiteCertifateViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }
}
