package com.example.admin.Class;

public class AllUsers {
    String address,imageUrl,nationalIdCardNo,phone,uid,username,isVerified;

    public AllUsers() {
    }

    public AllUsers(String address, String imageUrl, String nationalIdCardNo, String phone, String uid, String username, String isVerified) {
        this.address = address;
        this.imageUrl = imageUrl;
        this.nationalIdCardNo = nationalIdCardNo;
        this.phone = phone;
        this.uid = uid;
        this.username = username;
        this.isVerified = isVerified;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }
}
