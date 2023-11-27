package com.example.projectmove.Utilis.Class;

public class Notifications {
    String timeStamp,nId,message,postId,uName,uDp,userId,uPhone;

    public Notifications() {
    }

    public Notifications(String timeStamp, String nId, String message, String postId, String uName, String uDp, String userId, String uPhone) {
        this.timeStamp = timeStamp;
        this.nId = nId;
        this.message = message;
        this.postId = postId;
        this.uName = uName;
        this.uDp = uDp;
        this.userId = userId;
        this.uPhone = uPhone;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getnId() {
        return nId;
    }

    public void setnId(String nId) {
        this.nId = nId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuDp() {
        return uDp;
    }

    public void setuDp(String uDp) {
        this.uDp = uDp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getuPhone() {
        return uPhone;
    }

    public void setuPhone(String uPhone) {
        this.uPhone = uPhone;
    }
}
