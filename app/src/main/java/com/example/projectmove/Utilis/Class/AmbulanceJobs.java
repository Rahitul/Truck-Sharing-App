package com.example.projectmove.Utilis.Class;

public class AmbulanceJobs {

    String pid,uid,isAccepted,uPhone,pVehicleModel,pPrice,pLocation,pDestination,pName,userName,pTime,pDp,pComments,vehicleNumber;

    public AmbulanceJobs() {
    }

    public AmbulanceJobs(String pid, String uid, String isAccepted, String uPhone, String pVehicleModel, String pPrice, String pLocation, String pDestination, String pName, String userName, String pTime, String pDp, String pComments, String vehicleNumber) {
        this.pid = pid;
        this.uid = uid;
        this.isAccepted = isAccepted;
        this.uPhone = uPhone;
        this.pVehicleModel = pVehicleModel;
        this.pPrice = pPrice;
        this.pLocation = pLocation;
        this.pDestination = pDestination;
        this.pName = pName;
        this.userName = userName;
        this.pTime = pTime;
        this.pDp = pDp;
        this.pComments = pComments;
        this.vehicleNumber = vehicleNumber;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(String isAccepted) {
        this.isAccepted = isAccepted;
    }

    public String getuPhone() {
        return uPhone;
    }

    public void setuPhone(String uPhone) {
        this.uPhone = uPhone;
    }

    public String getpVehicleModel() {
        return pVehicleModel;
    }

    public void setpVehicleModel(String pVehicleModel) {
        this.pVehicleModel = pVehicleModel;
    }

    public String getpPrice() {
        return pPrice;
    }

    public void setpPrice(String pPrice) {
        this.pPrice = pPrice;
    }

    public String getpLocation() {
        return pLocation;
    }

    public void setpLocation(String pLocation) {
        this.pLocation = pLocation;
    }

    public String getpDestination() {
        return pDestination;
    }

    public void setpDestination(String pDestination) {
        this.pDestination = pDestination;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public String getpDp() {
        return pDp;
    }

    public void setpDp(String pDp) {
        this.pDp = pDp;
    }

    public String getpComments() {
        return pComments;
    }

    public void setpComments(String pComments) {
        this.pComments = pComments;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }
}
