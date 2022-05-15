package com.anghar.serviceio.Model.Data;

import android.content.Intent;

public class Category {

    String catId;
    String title;
    Integer noOfWorkers;

    public Category() {
    }

    public Category(String catId, String title, Integer noOfWorkers) {
        this.catId = catId;
        this.title = title;
        this.noOfWorkers = noOfWorkers;
    }


    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getNoOfWorkers() {
        return noOfWorkers;
    }

    public void setNoOfWorkers(Integer noOfWorkers) {
        this.noOfWorkers = noOfWorkers;
    }
}
