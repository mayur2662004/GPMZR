package com.example.gpmpro;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BonafiteCertificateAdapterForStudentProfile extends RecyclerView.Adapter<BonafiteCertifateViewHolder> {

    Student_profile adminViewBonafiteData;
    List<BonafiteModel> modelList;

    public BonafiteCertificateAdapterForStudentProfile(Student_profile adminViewBonafiteData, List<BonafiteModel> modelList) {
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
                String note = modelList.get(position).getNote();

                Intent intent = new Intent(adminViewBonafiteData,StudentProfileEdit.class);
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
                intent.putExtra("Note",note);

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

        String verify = modelList.get(i).getVerify();


        if (verify.equalsIgnoreCase("False")){
            holder.verify.setImageResource(R.drawable.pending_logo);
        }
        else if (verify.equalsIgnoreCase("True")){
            holder.verify.setImageResource(R.drawable.yes_logo_new);
        }
        else if (verify.equalsIgnoreCase("Rejected")){
            holder.verify.setImageResource(R.drawable.wrong_logo_new);
        }

        holder.downloadPdf.setVisibility(View.GONE);


    }


    @Override
    public int getItemCount() {
        return modelList.size();
    }
}
