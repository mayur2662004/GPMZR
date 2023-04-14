package com.example.gpmpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Rejected_Activity extends AppCompatActivity {

    // For retrieving data from firebase i
    List<BonafiteModel> modelList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    BonafiteCertificateAdapterForRejectedAcivity bonafiteCertificateAdapter;

    FirebaseFirestore fStore;

    ProgressDialog pd;

    androidx.appcompat.widget.SearchView searchView;

    SwipeRefreshLayout refreshLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejected);

        fStore = FirebaseFirestore.getInstance();
        pd = new ProgressDialog(this);
        pd.show();

        recyclerView = findViewById(R.id.recycleview_in_adminpage);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        modelList.clear();
        showData();

        ActivityCompat.requestPermissions(Rejected_Activity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        refreshLayout = findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showData();
                refreshLayout.setRefreshing(false);

            }
        });

        searchView = findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<BonafiteModel> models = new ArrayList<>();
                for (BonafiteModel model : modelList){
                    if (model.getName().toLowerCase().trim().contains(s.toLowerCase().trim())){
                        models.add(model);
                    }
                    else if (model.getSurName().toLowerCase().trim().contains(s.toLowerCase().trim())){
                        models.add(model);
                    }
                    else if (model.getMiddleName().toLowerCase().trim().contains(s.toLowerCase().trim())){
                        models.add(model);
                    }
                    else if (model.getDate().toLowerCase().trim().contains(s.toLowerCase().trim())){
                        models.add(model);
                    }
                    else if (model.getEnrollmentNo().toLowerCase().trim().contains(s.toLowerCase().trim())){
                        models.add(model);
                    }
                    else if (model.getBranch().toLowerCase().trim().contains(s.toLowerCase().trim())){
                        models.add(model);
                    }
                    else if (model.getYears().toLowerCase().trim().contains(s.toLowerCase().trim())){
                        models.add(model);
                    }
                    else if (model.getAllName().toLowerCase().trim().contains(s.toLowerCase().trim())){
                        models.add(model);
                    }


                }

                BonafiteCertificateAdapterForRejectedAcivity adapter = new BonafiteCertificateAdapterForRejectedAcivity(Rejected_Activity.this,models);
                recyclerView.setAdapter(adapter);

                return true;
            }
        });

    }
    private void showData() {
        fStore.collection("StudentBonafiteCertificateApplicationForm").orderBy("Date", Query.Direction.DESCENDING)
                .whereEqualTo("Verify","Rejected").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        modelList.clear();
                        pd.dismiss();
                        refreshLayout.setRefreshing(false);
                        for (DocumentSnapshot doc : queryDocumentSnapshots){
                            BonafiteModel model = new BonafiteModel(
                                    doc.getString("Id"),
                                    doc.getString("Name"),
                                    doc.getString("MiddleName"),
                                    doc.getString("LastName"),
                                    doc.getString("Date"),
                                    doc.getString("EnrollmentNo"),
                                    doc.getString("Branch"),
                                    doc.getString("Year"),
                                    doc.getString("Subject"),
                                    doc.getString("Verify"),
                                    doc.getString("AllName"),
                                    doc.getString("UserId"),
                                    doc.getString("Note")
                            );
                            modelList.add(model);
                        }
                        bonafiteCertificateAdapter = new BonafiteCertificateAdapterForRejectedAcivity(Rejected_Activity.this,modelList);
                        recyclerView.setAdapter(bonafiteCertificateAdapter);
                    }
                });
    }
}