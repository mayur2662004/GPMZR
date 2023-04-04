package com.example.gpmpro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BonafiteCertificateAdapter extends RecyclerView.Adapter<BonafiteCertifateViewHolder> {

    AdminViewBonafiteData adminViewBonafiteData;
    List<BonafiteModel> modelList;

    public BonafiteCertificateAdapter(AdminViewBonafiteData adminViewBonafiteData, List<BonafiteModel> modelList) {
        this.adminViewBonafiteData = adminViewBonafiteData;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public BonafiteCertifateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bonafite_model,parent,false);
        BonafiteCertifateViewHolder bonafiteCertifateViewHolder = new BonafiteCertifateViewHolder(itemView);
        bonafiteCertifateViewHolder.setOnClickListener(new BonafiteCertifateViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String id = modelList.get(position).getId();
                String date = modelList.get(position).getDate();
                String name = modelList.get(position).getName();
                String middleName = modelList.get(position).getMiddleName();
                String lastName = modelList.get(position).getSurName();
                String branch = modelList.get(position).getBranch();
                String year = modelList.get(position).getYears();
                String enrollmentNo = modelList.get(position).getEnrollmentNo();
                String subject = modelList.get(position).getSubject();
                String verify = modelList.get(position).getVerify();

                Intent intent = new Intent(adminViewBonafiteData,AdminViewSpecificData.class);
                intent.putExtra("Id",id);
                intent.putExtra("Name",name);
                intent.putExtra("MiddleName",middleName);
                intent.putExtra("LastName",lastName);
                intent.putExtra("Date",date);
                intent.putExtra("EnrollmentNo",enrollmentNo);
                intent.putExtra("Branch",branch);
                intent.putExtra("Year",year);
                intent.putExtra("Subject",subject);
                intent.putExtra("Verify",verify);

                adminViewBonafiteData.startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });


        return bonafiteCertifateViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BonafiteCertifateViewHolder holder, @SuppressLint("RecyclerView") int i) {
        holder.name.setText(modelList.get(i).getName()+" "+modelList.get(i).getMiddleName()+" "+modelList.get(i).getSurName());
        holder.date.setText(modelList.get(i).getDate());
        holder.enrollment.setText(modelList.get(i).getEnrollmentNo());
        holder.branch.setText(modelList.get(i).getBranch());
        holder.year.setText(modelList.get(i).getYears());

        // This is for verifying application form of bonafite certificate
        holder.verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verify = modelList.get(i).getVerify();
                if (verify.equalsIgnoreCase("True")){
                    Toast.makeText(adminViewBonafiteData, "Already Verify", Toast.LENGTH_LONG).show();
                }
                else {

                }
            }
        });

          // This is for download PDF application form of bonafite certificate
        holder.downloadPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verify = modelList.get(i).getVerify();
                if (verify.equalsIgnoreCase("False")){
                    Toast.makeText(adminViewBonafiteData, "Please Verify First!!", Toast.LENGTH_LONG).show();
                }
                else {

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
