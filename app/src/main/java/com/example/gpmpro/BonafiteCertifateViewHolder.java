package com.example.gpmpro;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BonafiteCertifateViewHolder extends RecyclerView.ViewHolder {

    TextView date,name,enrollment,branch,year;
    ImageView verify,downloadPdf;

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


        date = itemView.findViewById(R.id.date);
        name = itemView.findViewById(R.id.name);
        enrollment = itemView.findViewById(R.id.enrollment);
        branch = itemView.findViewById(R.id.branch);
        year = itemView.findViewById(R.id.year);

        verify = itemView.findViewById(R.id.varify);
        downloadPdf = itemView.findViewById(R.id.downloadPdf);


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
