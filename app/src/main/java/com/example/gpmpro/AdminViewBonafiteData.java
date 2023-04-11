package com.example.gpmpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

    androidx.appcompat.widget.SearchView searchView;

    SwipeRefreshLayout refreshLayout;

    @SuppressLint("MissingInflatedId")
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
//        modelList.clear();
        showData();


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

                BonafiteCertificateAdapter adapter = new BonafiteCertificateAdapter(AdminViewBonafiteData.this,models);
                recyclerView.setAdapter(adapter);

                return true;
            }
        });

    }

    private void showData() {
        fStore.collection("StudentBonafiteCertificateApplicationForm").orderBy("Date", Query.Direction.DESCENDING).get()
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
                                    doc.getString("AllName")
                            );
                            modelList.add(model);
                        }
                        bonafiteCertificateAdapter = new BonafiteCertificateAdapter(AdminViewBonafiteData.this,modelList);
                        recyclerView.setAdapter(bonafiteCertificateAdapter);
                    }
                });
    }

    public void downloandPdf(int i) {



       Bitmap bmp= BitmapFactory.decodeResource(getResources(),R.drawable.mabte);
       Bitmap bms=BitmapFactory.decodeResource(getResources(),R.drawable.mabte);
       Bitmap scalebitmap=Bitmap.createScaledBitmap(bmp,50,30,false);
        Bitmap msbtebitmap=Bitmap.createScaledBitmap(bms,50,30,false);

        PdfDocument mypdfdocument=new PdfDocument();
        Paint paint=new Paint();
        Paint forelinepaint=new Paint();
        PdfDocument.PageInfo mypageinfo=new PdfDocument.PageInfo.Builder(360,280,1).create();
        PdfDocument.Page mypage=mypdfdocument.startPage(mypageinfo);
        Canvas canvas=mypage.getCanvas();
        paint.setTextSize(14f);


        paint.setStrokeWidth(4);
        paint.setColor(Color.parseColor("#000000"));

        canvas.drawLine(0,0,0,280,paint);// left
        canvas.drawLine(360,0,360,280,paint); //right
        canvas.drawLine(0,0,400,0,paint); // top
        canvas.drawLine(0,280,400,280,paint); // bottom
        paint.setColor(Color.parseColor("#000000"));


        paint.setTypeface(Typeface.create("Times New Roman",Typeface.NORMAL));
        canvas.drawText("GOVERNMENT POLYTECHNIC, MURTIZAPUR",50,20,paint);
        forelinepaint.setStyle(Paint.Style.STROKE);
        canvas.drawBitmap(scalebitmap,10,22,paint);
        canvas.drawBitmap(msbtebitmap,300,22,paint);
        paint.setTextSize(9f);
        canvas.drawText("N.H.06,Murtizapur-Amravati Road,Hendaj,Murtizapur",75,35,paint);
        canvas.drawText("Tq.Murtizapur,Dist,Akola. ph.(07256)207876,207877",75,50,paint);

        paint.setTextSize(7f);
        canvas.drawText("Email: principle.gpmurtijapur@dtemaharashtra.gov.in/Office.gpmurtijapur@dtemaharashtra.gov.in",25,65,paint);
        canvas.drawText("To produce Diploma Engineers to serve Industry & Society and Make them Capable for Lifelong Learning.",15,75,paint);

        paint.setStrokeWidth(0);
        canvas.drawLine(0,85,360,85,paint);


        paint.setTextSize(10f);

        canvas.drawText("No:GPMZR//SS/BONA/20     /",205,100,paint);
        canvas.drawText("Date:",205,120,paint);
        paint.setTextSize(12f);
        paint.setTextSize(14f);
        paint.setFakeBoldText(true);
        canvas.drawText("Bonafide Certificate",110,140,paint);
        paint.setTextSize(12f);
        paint.setFakeBoldText(false);
        canvas.drawText("This is to certify that "+ modelList.get(i).getAllName()  +" is a",40,165,paint);
        canvas.drawText("student of this institute during the year  20    -      studying  in",10,185,paint);
        canvas.drawText("___________th sem of Diploma Course in "+modelList.get(i).getBranch() +" Engg. ",10,205,paint);
        canvas.drawText("For:His/Her Own Request ",10,260,paint);
        canvas.drawText("Principal",270,260,paint);


        mypdfdocument.finishPage(mypage);
        String folderName = "Student Information";
        String fileName = "Student info.pdf";

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + folderName + File.separator + fileName;

        File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + folderName);

        if (!file.exists()){
            file.mkdirs();
        }
        try {
            mypdfdocument.writeTo(new FileOutputStream(path));
            Toast.makeText(AdminViewBonafiteData.this, "Priting ", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        mypdfdocument.close();



    }

}