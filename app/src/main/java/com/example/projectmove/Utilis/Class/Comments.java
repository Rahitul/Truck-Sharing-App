package com.example.projectmove.Utilis.Class;

public class Comments {
    String cId,comment,timestamp,uid,uPhone,uDp,uName,commentSub,vehicleModel,postId,pReplies;

    public Comments() {

    }

    public Comments(String cId, String comment, String timestamp, String uid, String uPhone, String uDp, String uName, String commentSub, String vehicleModel, String postId, String pReplies) {
        this.cId = cId;
        this.comment = comment;
        this.timestamp = timestamp;
        this.uid = uid;
        this.uPhone = uPhone;
        this.uDp = uDp;
        this.uName = uName;
        this.commentSub = commentSub;
        this.vehicleModel = vehicleModel;
        this.postId = postId;
        this.pReplies = pReplies;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getuPhone() {
        return uPhone;
    }

    public void setuPhone(String uPhone) {
        this.uPhone = uPhone;
    }

    public String getuDp() {
        return uDp;
    }

    public void setuDp(String uDp) {
        this.uDp = uDp;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getCommentSub() {
        return commentSub;
    }

    public void setCommentSub(String commentSub) {
        this.commentSub = commentSub;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getpReplies() {
        return pReplies;
    }

    public void setpReplies(String pReplies) {
        this.pReplies = pReplies;
    }
}
