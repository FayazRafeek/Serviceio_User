package com.anghar.serviceio.Model.Data;

public class Work {

    String workId;
    String category;
    String complaint, otherCat;
    String userId;
    String username,phone,email;
    String address;
    Boolean isUrgent;
    Long postedDate;

    public Work() {
    }


    public Work(String workId, String category, String complaint, String userId, String username, String phone,String email, String address, Boolean isUrgent, Long postedDate) {
        this.workId = workId;
        this.category = category;
        this.complaint = complaint;
        this.userId = userId;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.isUrgent = isUrgent;
        this.postedDate = postedDate;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Long postedDate) {
        this.postedDate = postedDate;
    }

    public String getOtherCat() {
        return otherCat;
    }

    public void setOtherCat(String otherCat) {
        this.otherCat = otherCat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
