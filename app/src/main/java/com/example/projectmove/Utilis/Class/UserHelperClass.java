package com.example.projectmove.Utilis.Class;

public class UserHelperClass {

    String imageUrl,username,address,nationalIdCardNo,phone,uid,isVerified;

    public UserHelperClass(){

    }

    public UserHelperClass(String imageUrl, String username, String address, String nationalIdCardNo, String phone, String uid, String isVerified) {
        this.imageUrl = imageUrl;
        this.username = username;
        this.address = address;
        this.nationalIdCardNo = nationalIdCardNo;
        this.phone = phone;
        this.uid = uid;
        this.isVerified = isVerified;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNationalIdCardNo() {
        return nationalIdCardNo;
    }

    public void setNationalIdCardNo(String nationalIdCardNo) {
        this.nationalIdCardNo = nationalIdCardNo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }
}



