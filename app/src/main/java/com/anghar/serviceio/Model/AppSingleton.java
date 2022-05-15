package com.anghar.serviceio.Model;

import com.anghar.serviceio.Model.Data.Category;
import com.anghar.serviceio.Model.Data.Invite;
import com.anghar.serviceio.Model.Data.Work;
import com.anghar.serviceio.Model.Data.WorkInterest;
import com.anghar.serviceio.Model.Data.Worker;

public class AppSingleton {

    public static AppSingleton INSTANCE;

    public static AppSingleton getINSTANCE() {
        if(INSTANCE == null) INSTANCE = new AppSingleton();
        return INSTANCE;
    }


    String userType = "USER";

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }

    Category selectedCategory;

    public Category getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(Category selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    Worker selectedWorker;

    public Worker getSelectedWorker() {
        return selectedWorker;
    }

    public void setSelectedWorker(Worker selectedWorker) {
        this.selectedWorker = selectedWorker;
    }

    Work selectedWork;

    public Work getSelectedWork() {
        return selectedWork;
    }

    public void setSelectedWork(Work selectedWork) {
        this.selectedWork = selectedWork;
    }

    WorkInterest selectedProposal;

    public WorkInterest getSelectedProposal() {
        return selectedProposal;
    }

    public void setSelectedProposal(WorkInterest selectedProposal) {
        this.selectedProposal = selectedProposal;
    }

    Invite selectedInvite;

    public Invite getSelectedInvite() {
        return selectedInvite;
    }

    public void setSelectedInvite(Invite selectedInvite) {
        this.selectedInvite = selectedInvite;
    }
}
