package com.anghar.serviceio.Model.Data;

public class BasicResponse<T> {

    String status; //Loading, Success, Error
    Throwable error;
    T data;


    public BasicResponse() {
    }


    public BasicResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public void setData(T t){ this.data = t;}

    public T getData() {return data;}
}
