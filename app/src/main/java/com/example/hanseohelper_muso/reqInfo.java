package com.example.hanseohelper_muso;

public class reqInfo {

    public String reqTheme;
    public String reqCategory;
    public String reqImage;
    public String reqPayMethod;
    public String reqAddress;
    public String reqGender;
    public int reqPay;
    public String reqMessage;
    public String reqWho;

    public reqInfo(){
    }

    public reqInfo(String reqTheme, String reqCategory, String reqImage, String reqPayMethod, String reqAddress, String reqGender,
                   int reqPay, String reqMessage, String reqWho){
        this.reqTheme = reqTheme;
        this.reqCategory = reqCategory;
        this.reqImage = reqImage;
        this.reqPayMethod = reqPayMethod;
        this.reqAddress = reqAddress;
        this.reqGender = reqGender;
        this.reqPay = reqPay;
        this.reqMessage = reqMessage;
        this.reqWho = reqWho;

    }

    public String getReqTheme() {
        return reqTheme;
    }

    public String getReqCategory() {
        return reqCategory;
    }

    public String getReqImage() {
        return reqImage;
    }

    public String getReqPayMethod() {
        return reqPayMethod;
    }

    public String getReqAddress() {
        return reqAddress;
    }

    public String getReqGender() {
        return reqGender;
    }

    public int getReqPay() {
        return reqPay;
    }

    public String getReqMessage() {
        return reqMessage;
    }

    public String getReqWho() {
        return reqWho;
    }

    public void setReqTheme(String reqTheme){
        this.reqTheme = reqTheme;
    }

    public void setReqCategory(String reqCategory){
        this.reqCategory = reqCategory;
    }

    public void setReqImage(String reqImage){
        this.reqImage = reqImage;
    }

    public void setReqPayMethod(String reqPayMethod){
        this.reqPayMethod = reqPayMethod;
    }

    public void setReqAddress(String reqAddress){
        this.reqAddress = reqAddress;
    }

    public void setReqGender(String reqGender){
        this.reqGender = reqGender;
    }

    public void setReqPay(int reqPay){
        this.reqPay = reqPay;
    }

    public void setReqMessage(String reqMessage){
        this.reqMessage = reqMessage;
    }

    public void setReqWho(String reqWho){
        this.reqWho = reqWho;
    }
}