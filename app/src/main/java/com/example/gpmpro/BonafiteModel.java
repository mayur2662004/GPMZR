package com.example.gpmpro;

public class BonafiteModel {
    String id,name,middleName,surName,date,enrollmentNo,branch,years,subject,Verify,allName;

    public BonafiteModel(String id, String name, String middleName, String surName, String date, String enrollmentNo, String branch, String years, String subject,String Verify,String allName) {
        this.id = id;
        this.name = name;
        this.middleName = middleName;
        this.surName = surName;
        this.date = date;
        this.enrollmentNo = enrollmentNo;
        this.branch = branch;
        this.years = years;
        this.subject = subject;
        this.Verify = Verify;
        this.allName = allName;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getSurName() {
        return surName;
    }

    public String getDate() {
        return date;
    }

    public String getEnrollmentNo() {
        return enrollmentNo;
    }

    public String getBranch() {
        return branch;
    }

    public String getYears() {
        return years;
    }

    public String getSubject() {
        return subject;
    }

    public String getVerify() {
        return Verify;
    }

    public String getAllName() {
        return allName;
    }
}
