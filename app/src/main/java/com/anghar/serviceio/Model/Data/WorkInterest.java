package com.anghar.serviceio.Model.Data;

public class WorkInterest {

    String interestId;
    String workId;
    String userId;
    String workerId;
    Work work;
    Worker worker;
    String proposal;
    String amount;
    String fromDate, toDate;
    Long ts;
    String status = "SENT";

    public WorkInterest() {
    }

    public WorkInterest(String interestId, String workId, String userId, String workerId, Work work, Worker worker, String proposal, String amount, String fromDate, String toDate, Long ts) {
        this.interestId = interestId;
        this.workId = workId;
        this.userId = userId;
        this.workerId = workerId;
        this.work = work;
        this.worker = worker;
        this.proposal = proposal;
        this.amount = amount;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.ts = ts;
    }

    public String getInterestId() {
        return interestId;
    }

    public void setInterestId(String interestId) {
        this.interestId = interestId;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
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

    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public String getProposal() {
        return proposal;
    }

    public void setProposal(String proposal) {
        this.proposal = proposal;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
