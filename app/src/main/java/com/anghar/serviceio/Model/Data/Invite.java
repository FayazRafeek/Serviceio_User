package com.anghar.serviceio.Model.Data;

public class Invite {

    String inviteId;
    String userId, workerId;
    String workId;
    String userName, userPhone, workerName, category;
    Work work;
    Worker worker;
    String status;
    Long postedDate;

    public Invite() {
    }

    public Invite(String inviteId, String userId, String workerId, String workId, String userName, String userPhone, String workerName, String category, Work work, Worker worker, String status, Long postedDate) {
        this.inviteId = inviteId;
        this.userId = userId;
        this.workerId = workerId;
        this.workId = workId;
        this.userName = userName;
        this.userPhone = userPhone;
        this.workerName = workerName;
        this.category = category;
        this.work = work;
        this.worker = worker;
        this.status = status;
        this.postedDate = postedDate;
    }


    public String getInviteId() {
        return inviteId;
    }

    public void setInviteId(String inviteId) {
        this.inviteId = inviteId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Work getWork() {
        return work;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public void setWork(Work work) {
        this.work = work;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Long postedDate) {
        this.postedDate = postedDate;
    }
}
