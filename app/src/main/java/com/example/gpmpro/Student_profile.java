package com.example.gpmpro;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;

public class
Student_profile extends AppCompatActivity {

    // For retrieving data from firebase i
    List<BonafiteModel> modelList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    BonafiteCertificateAdapterForStudentProfile bonafiteCertificateAdapter;

    FirebaseFirestore fStore;

    ProgressDialog pd;

    androidx.appcompat.widget.SearchView searchView;

    SwipeRefreshLayout refreshLayout;

    ImageView scan;

    FirebaseAuth mAuth;
    String years,userId;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);



        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        pd = new ProgressDialog(this);
        pd.show();

        recyclerView = findViewById(R.id.recycleview_in_adminpage);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        modelList.clear();


        refreshLayout = findViewById(R.id.refresh);


        searchView = findViewById(R.id.searchView);
        scan = findViewById(R.id.scan);

        showData();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showData();
                refreshLayout.setRefreshing(false);
            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ScanCode();
            }
        });
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

                BonafiteCertificateAdapterForStudentProfile adapter = new BonafiteCertificateAdapterForStudentProfile(Student_profile.this,models);
                recyclerView.setAdapter(adapter);

                return true;
            }
        });
    }

    private void showData() {
        fStore.collection("StudentBonafiteCertificateApplicationForm").orderBy("Date", Query.Direction.DESCENDING)
                .whereEqualTo("UserId",userId).get()
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
                        bonafiteCertificateAdapter = new BonafiteCertificateAdapterForStudentProfile(Student_profile.this,modelList);
                        recyclerView.setAdapter(bonafiteCertificateAdapter);
                    }
                });
    }

    private void ScanCode() {

        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);

    }
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result ->{
        if (result.getContents() !=null)
        {
            if (result.getContents().equalsIgnoreCase("I Love You")){

                startActivity(new Intent(getApplicationContext(),BonafiteCertificateForm.class));

            }
            else {
                Toast.makeText(this, "Wrong QR Code", Toast.LENGTH_SHORT).show();
            }


        }
    });

    static class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.logOut:
                AlertDialog.Builder builder = new AlertDialog.Builder(Student_profile.this);
                String[] options = {"Yes", "No"};
                builder.setTitle("Are you sure to Logout ?");


                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
                    }
                }).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(),Student_Registretoin_Activity.class));
                        finish();
                        Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_SHORT).show();
                    }
                }).create().show();
                return  true;
            default:

        }


        return super.onOptionsItemSelected(item);
    }


}