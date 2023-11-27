package com.example.projectmove.Utilis.Class;

public class History {

    String timestamp,driveruid,driverPostId,didPostName,pTime,didPostDp,pDName,pDLocation,pDDestination,pDStartingTime,pDStartingDate,customerName,customerPhone,customerDp,pDExpectedPrice;

    public History() {
    }

    public History(String timestamp, String driveruid, String driverPostId, String didPostName, String pTime, String didPostDp, String pDName, String pDLocation, String pDDestination, String pDStartingTime, String pDStartingDate, String customerName, String customerPhone, String customerDp, String pDExpectedPrice) {
        this.timestamp = timestamp;
        this.driveruid = driveruid;
        this.driverPostId = driverPostId;
        this.didPostName = didPostName;
        this.pTime = pTime;
        this.didPostDp = didPostDp;
        this.pDName = pDName;
        this.pDLocation = pDLocation;
        this.pDDestination = pDDestination;
        this.pDStartingTime = pDStartingTime;
        this.pDStartingDate = pDStartingDate;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerDp = customerDp;
        this.pDExpectedPrice = pDExpectedPrice;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDriveruid() {
        return driveruid;
    }

    public void setDriveruid(String driveruid) {
        this.driveruid = driveruid;
    }

    public String getDriverPostId() {
        return driverPostId;
    }

    public void setDriverPostId(String driverPostId) {
        this.driverPostId = driverPostId;
    }

    public String getDidPostName() {
        return didPostName;
    }

    public void setDidPostName(String didPostName) {
        this.didPostName = didPostName;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public String getDidPostDp() {
        return didPostDp;
    }

    public void setDidPostDp(String didPostDp) {
        this.didPostDp = didPostDp;
    }

    public String getpDName() {
        return pDName;
    }

    public void setpDName(String pDName) {
        this.pDName = pDName;
    }

    public String getpDLocation() {
        return pDLocation;
    }

    public void setpDLocation(String pDLocation) {
        this.pDLocation = pDLocation;
    }

    public String getpDDestination() {
        return pDDestination;
    }

    public void setpDDestination(String pDDestination) {
        this.pDDestination = pDDestination;
    }

    public String getpDStartingTime() {
        return pDStartingTime;
    }

    public void setpDStartingTime(String pDStartingTime) {
        this.pDStartingTime = pDStartingTime;
    }

    public String getpDStartingDate() {
        return pDStartingDate;
    }

    public void setpDStartingDate(String pDStartingDate) {
        this.pDStartingDate = pDStartingDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerDp() {
        return customerDp;
    }

    public void setCustomerDp(String customerDp) {
        this.customerDp = customerDp;
    }

    public String getpDExpectedPrice() {
        return pDExpectedPrice;
    }

    public void setpDExpectedPrice(String pDExpectedPrice) {
        this.pDExpectedPrice = pDExpectedPrice;
    }
}

