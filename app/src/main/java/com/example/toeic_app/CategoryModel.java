package com.example.toeic_app;

public class CategoryModel {
    private  String docID;
    private String Name;
    private  int noOfTests;

    public CategoryModel(String docID, String name, int noOfTests) {
        this.docID = docID;
        Name = name;
        this.noOfTests = noOfTests;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getNoOfTests() {
        return noOfTests;
    }

    public void setNoOfTests(int noOfTests) {
        this.noOfTests = noOfTests;
    }
}
