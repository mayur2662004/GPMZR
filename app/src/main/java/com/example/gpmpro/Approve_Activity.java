package com.example.gpmpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Approve_Activity extends AppCompatActivity {
    // For retrieving data from firebase i
    List<BonafiteModel> modelList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    BonafiteCertificateAdapterForApproveAcivity bonafiteCertificateAdapter;
    FirebaseFirestore fStore;
    ProgressDialog pd;
    androidx.appcompat.widget.SearchView searchView;
    SwipeRefreshLayout refreshLayout;
    int nextDate = 0;
    String years;

    Dialog dialog;

    RadioGroup branch_rg;

    String [] arr = {"CO","ME","IT","EE","CE","1st sem","2nd sem","3rd sem","4th sem","5th sem","6th sem"};
    String  y;
    FloatingActionButton multipleBtn;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve);

        fStore = FirebaseFirestore.getInstance();
        pd = new ProgressDialog(this);
        pd.show();
        branch_rg=findViewById(R.id.ed);
        recyclerView = findViewById(R.id.recycleview_in_adminpage);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        modelList.clear();
        showData();

        ActivityCompat.requestPermissions(Approve_Activity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        refreshLayout = findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showData();
                refreshLayout.setRefreshing(false);

            }
        });


        searchView = findViewById(R.id.searchView);

        multipleBtn = findViewById(R.id.multipleBtn);
        multipleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog1();
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

                BonafiteCertificateAdapterForApproveAcivity adapter = new BonafiteCertificateAdapterForApproveAcivity(Approve_Activity.this,models);
                recyclerView.setAdapter(adapter);

                return true;
            }
        });

    }

    private void showDialog1() {

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.pop_pdf_download);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.back_background));
        dialog.show();

//        AutoCompleteTextView autoCompleteTextView1 = dialog.findViewById(R.id.autoCompleteTextView1);
        ImageView download = dialog.findViewById(R.id.download);
        RadioGroup r = dialog.findViewById(R.id.ed);
        Spinner spinner = dialog.findViewById(R.id.sem);
        final RadioButton[] radio_button = new RadioButton[5];
        
   



        r.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                for(int i=0; i<r.getChildCount(); i++) {
                    radio_button[0] = (RadioButton) r.getChildAt(0);
                    radio_button[1] = (RadioButton) r.getChildAt(1);
                    radio_button[2] = (RadioButton) r.getChildAt(2);
                    radio_button[3] = (RadioButton) r.getChildAt(3);
                    radio_button[4] = (RadioButton) r.getChildAt(4);
//                    int id = radio_button[i].getId();
                    if(radio_button[0].getId() == checkedId) {
                        y = radio_button[0].getText().toString();
                    }
                    else if(radio_button[1].getId() == checkedId) {
                        y = radio_button[1].getText().toString();
                    }
                    else if(radio_button[2].getId() == checkedId) {
                        y = radio_button[2].getText().toString();
                    }
                    else if(radio_button[3].getId() == checkedId) {
                        y = radio_button[3].getText().toString();
                    }
                    else if(radio_button[4].getId() == checkedId) {
                        y = radio_button[4].getText().toString();
                    }
                }

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String years = parent.getItemAtPosition(position).toString();
                if (years.equalsIgnoreCase("Select Semester")) {

                } else {
                    String branch = y;
                    downloadPdfMultiple(branch, years);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    download();
            }
        });

    }

    private void downloadPdfMultiple(String branch,String sem) {

        pd.show();
        fStore.collection("StudentBonafiteCertificateApplicationForm")
                .whereEqualTo("Year",sem)
                .whereEqualTo("Verify","True")
                .whereEqualTo("Branch",branch)
                .get()
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
                        bonafiteCertificateAdapter = new BonafiteCertificateAdapterForApproveAcivity(Approve_Activity.this,modelList);
                        recyclerView.setAdapter(bonafiteCertificateAdapter);
                    }
                });



    }

    private void download() {
        for (int i=0;i<modelList.size();i++){
            String date = modelList.get(i).getDate();
            int finalDate = Integer.parseInt(date.substring(2,4));

            String sem = modelList.get(i).getYears();
            int finalSem = Integer.parseInt(sem.substring(0,1));

            if (finalSem%2==0){
                nextDate = finalDate - 1;
                years = "20"+nextDate+"-"+finalDate;
            }
            else {
                nextDate = finalDate + 1;
                years = "20"+finalDate+"-"+nextDate;
            }

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

            String y = years.replace("-","/");

            canvas.drawText("No:GPMZR//SS/BONA/"+y,205,100,paint);
            canvas.drawText("Date:",205,120,paint);
            paint.setTextSize(12f);
            paint.setTextSize(14f);
            paint.setFakeBoldText(true);
            canvas.drawText("Bonafide Certificate",110,140,paint);
            paint.setTextSize(12f);
            paint.setFakeBoldText(false);
            if (modelList.get(i).getAllName().length() < 15 ){
                canvas.drawText("This is to certify that "+ modelList.get(i).getAllName()  +" is a student of this",40,165,paint);
                canvas.drawText("institute during the year "+years+" studying  in "+ modelList.get(i).getYears() +" of",18,185,paint);
                canvas.drawText("Diploma Course in "+modelList.get(i).getBranch() +" Engg. ",18,205,paint);
            }
            else if(modelList.get(i).getAllName().length() < 12){
                canvas.drawText("This is to certify that "+ modelList.get(i).getAllName()  +" is a student of this",40,165,paint);
                canvas.drawText("institute during the year "+years+" studying  in "+ modelList.get(i).getYears(),20,185,paint);
                canvas.drawText("of Diploma Course in "+modelList.get(i).getBranch() +" Engg. ",20,205,paint);
            } else if (modelList.get(i).getAllName().length() <=20) {
                canvas.drawText("This is to certify that "+ modelList.get(i).getAllName()  +" is a student ",40,165,paint);
                canvas.drawText("of this institute during the year "+years+" studying  in "+ modelList.get(i).getYears(),15,185,paint);
                canvas.drawText("of Diploma Course in "+modelList.get(i).getBranch() +" Engg. ",15,205,paint);
            }
            else if(modelList.get(i).getAllName().length() < 25){
                canvas.drawText("This is to certify that "+ modelList.get(i).getAllName()  +" is a ",40,165,paint);
                canvas.drawText(" student of this institute during the year "+years+" studying ",15,185,paint);
                canvas.drawText(modelList.get(i).getYears()+" in of Diploma Course in "+modelList.get(i).getBranch() +" Engg. ",15,205,paint);
            }
            else if(modelList.get(i).getAllName().length() < 30){
                canvas.drawText("This is to certify that "+ modelList.get(i).getAllName() ,40,165,paint);
                canvas.drawText("is a student of this institute during the year "+years+" studying ",15,185,paint);
                canvas.drawText( "in "+ modelList.get(i).getYears() + " of Diploma Course in "+modelList.get(i).getBranch() +" Engg. ",15,205,paint);
            }
            else if(modelList.get(i).getAllName().length() < 35){
                canvas.drawText("This is to certify that "+ modelList.get(i).getAllName() ,30,165,paint);
                canvas.drawText("is a student of this institute during the year "+years+" studying ",10,185,paint);
                canvas.drawText( "in "+ modelList.get(i).getYears() + " of Diploma Course in "+modelList.get(i).getBranch() +" Engg. ",10,205,paint);
            }
            else if (modelList.get(i).getAllName().length() < 40){
                canvas.drawText("This is to certify that "+ modelList.get(i).getName()+" "+modelList.get(i).getMiddleName() ,40,165,paint);
                canvas.drawText(modelList.get(i).getSurName()+" is a student of this institute during the year ",15,185,paint);
                canvas.drawText(years+" studying in " + modelList.get(i).getYears() +" of Diploma Course in "+modelList.get(i).getBranch() +" Engg. ",15,205,paint);
            }






            canvas.drawText("For:His/Her Own Request ",10,260,paint);
            canvas.drawText("Principal",270,260,paint);

            mypdfdocument.finishPage(mypage);
            String folderName = "GPMZR Student Bonafite";
            String subFolderName = modelList.get(i).getBranch();
            String subSubFolder = modelList.get(i).getYears();
            String fileName = modelList.get(i).getEnrollmentNo()+" Bonafite.pdf";

            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + folderName + File.separator  + subFolderName + File.separator + subSubFolder + File.separator+ fileName;

            File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + folderName+ File.separator + subFolderName + File.separator+ subSubFolder);

        if (!file.exists()){
            file.mkdirs();
        }

            try {
                file.mkdirs();
                mypdfdocument.writeTo(new FileOutputStream(path));
                Toast.makeText(Approve_Activity.this, "Printing ", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            mypdfdocument.close();



        }
    }

    private void showData() {
        fStore.collection("StudentBonafiteCertificateApplicationForm").orderBy("Date", Query.Direction.DESCENDING)
                .whereEqualTo("Verify","True").get()
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
                        bonafiteCertificateAdapter = new BonafiteCertificateAdapterForApproveAcivity(Approve_Activity.this,modelList);
                        recyclerView.setAdapter(bonafiteCertificateAdapter);
                    }
                });
    }
    public void downloandPdf(int i) {
        String date = modelList.get(i).getDate();
        int finalDate = Integer.parseInt(date.substring(2,4));

        String sem = modelList.get(i).getYears();
        int finalSem = Integer.parseInt(sem.substring(0,1));

        if (finalSem%2==0){
            nextDate = finalDate - 1;
            years = "20"+nextDate+"-"+finalDate;
        }
        else {
            nextDate = finalDate + 1;
            years = "20"+finalDate+"-"+nextDate;
        }

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

        String y = years.replace("-","/");

        canvas.drawText("No:GPMZR//SS/BONA/"+y,205,100,paint);
        canvas.drawText("Date:",205,120,paint);
        paint.setTextSize(12f);
        paint.setTextSize(14f);
        paint.setFakeBoldText(true);
        canvas.drawText("Bonafide Certificate",110,140,paint);
        paint.setTextSize(12f);
        paint.setFakeBoldText(false);
        if (modelList.get(i).getAllName().length() < 15 ){
            canvas.drawText("This is to certify that "+ modelList.get(i).getAllName()  +" is a student of this",40,165,paint);
            canvas.drawText("institute during the year "+years+" studying  in "+ modelList.get(i).getYears() +" of",18,185,paint);
            canvas.drawText("Diploma Course in "+modelList.get(i).getBranch() +" Engg. ",18,205,paint);
        }
        else if(modelList.get(i).getAllName().length() < 12){
            canvas.drawText("This is to certify that "+ modelList.get(i).getAllName()  +" is a student of this",40,165,paint);
            canvas.drawText("institute during the year "+years+" studying  in "+ modelList.get(i).getYears(),20,185,paint);
            canvas.drawText("of Diploma Course in "+modelList.get(i).getBranch() +" Engg. ",20,205,paint);
        } else if (modelList.get(i).getAllName().length() <=20) {
            canvas.drawText("This is to certify that "+ modelList.get(i).getAllName()  +" is a student ",40,165,paint);
            canvas.drawText("of this institute during the year "+years+" studying  in "+ modelList.get(i).getYears(),15,185,paint);
            canvas.drawText("of Diploma Course in "+modelList.get(i).getBranch() +" Engg. ",15,205,paint);
        }
        else if(modelList.get(i).getAllName().length() < 25){
            canvas.drawText("This is to certify that "+ modelList.get(i).getAllName()  +" is a ",40,165,paint);
            canvas.drawText(" student of this institute during the year "+years+" studying ",15,185,paint);
            canvas.drawText(modelList.get(i).getYears()+" in of Diploma Course in "+modelList.get(i).getBranch() +" Engg. ",15,205,paint);
        }
        else if(modelList.get(i).getAllName().length() < 30){
            canvas.drawText("This is to certify that "+ modelList.get(i).getAllName() ,40,165,paint);
            canvas.drawText("is a student of this institute during the year "+years+" studying ",15,185,paint);
            canvas.drawText( "in "+ modelList.get(i).getYears() + " of Diploma Course in "+modelList.get(i).getBranch() +" Engg. ",15,205,paint);
        }
        else if(modelList.get(i).getAllName().length() < 35){
            canvas.drawText("This is to certify that "+ modelList.get(i).getAllName() ,30,165,paint);
            canvas.drawText("is a student of this institute during the year "+years+" studying ",10,185,paint);
            canvas.drawText( "in "+ modelList.get(i).getYears() + " of Diploma Course in "+modelList.get(i).getBranch() +" Engg. ",10,205,paint);
        }
        else if (modelList.get(i).getAllName().length() < 40){
            canvas.drawText("This is to certify that "+ modelList.get(i).getName()+" "+modelList.get(i).getMiddleName() ,40,165,paint);
            canvas.drawText(modelList.get(i).getSurName()+" is a student of this institute during the year ",15,185,paint);
            canvas.drawText(years+" studying in " + modelList.get(i).getYears() +" of Diploma Course in "+modelList.get(i).getBranch() +" Engg. ",15,205,paint);
        }






        canvas.drawText("For:His/Her Own Request ",10,260,paint);
        canvas.drawText("Principal",270,260,paint);

        mypdfdocument.finishPage(mypage);
        String folderName = "GPMZR Student Bonafite";
        String subFolderName = modelList.get(i).getBranch();
        String subSubFolder = modelList.get(i).getYears();
        String fileName = modelList.get(i).getEnrollmentNo()+" Bonafite.pdf";

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + folderName + File.separator  + subFolderName + File.separator + subSubFolder + File.separator+ fileName;

        File file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + folderName+ File.separator + subFolderName + File.separator+ subSubFolder);

        if (!file.exists()){
            file.mkdirs();
        }

        try {
            file.mkdirs();
            mypdfdocument.writeTo(new FileOutputStream(path));
            Toast.makeText(Approve_Activity.this, "Printing ", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        mypdfdocument.close();





    }
}