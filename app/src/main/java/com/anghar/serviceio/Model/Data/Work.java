package com.anghar.serviceio.Model.Data;

public class Work {

    String workId;
    String category;
    String complaint;
    String userId;
    String address;
    Boolean isUrgent;

    public Work() {
    }

    public Work(String workId, String category, String complaint, String userId, String address, Boolean isUrgent) {
        this.workId = workId;
        this.category = category;
        this.complaint = complaint;
        this.userId = userId;
        this.address = address;
        this.isUrgent = isUrgent;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getUrgent() {
        return isUrgent;
    }

    public void setUrgent(Boolean urgent) {
        isUrgent = urgent;
    }
}
