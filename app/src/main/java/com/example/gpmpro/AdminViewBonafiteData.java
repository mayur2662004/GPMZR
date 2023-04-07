package com.example.gpmpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminViewBonafiteData extends AppCompatActivity {

    // For retrieving data from firebase i
    List<BonafiteModel> modelList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    BonafiteCertificateAdapter bonafiteCertificateAdapter;

    FirebaseFirestore fStore;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_bonafite_data);

        fStore = FirebaseFirestore.getInstance();
        pd = new ProgressDialog(this);

        recyclerView = findViewById(R.id.recycleview_in_adminpage);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        modelList.clear();
        showData();
    }

    private void showData() {
        fStore.collection("StudentBonafiteCertificateApplicationForm").orderBy("Date", Query.Direction.DESCENDING).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        modelList.clear();
                        pd.dismiss();
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
                                    doc.getString("Verify")
                            );
                            modelList.add(model);
                        }
                        bonafiteCertificateAdapter = new BonafiteCertificateAdapter(AdminViewBonafiteData.this,modelList);
                        recyclerView.setAdapter(bonafiteCertificateAdapter);
                    }
                });
    }
}