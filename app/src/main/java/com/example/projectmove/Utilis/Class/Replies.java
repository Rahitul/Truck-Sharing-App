package com.example.projectmove.Utilis.Class;

public class Replies {
    String comment,rId,timestamp,uDp,uName,uPhone,uid,postId,commentSub,cid;

    public Replies() {
    }


    public Replies(String comment, String rId, String timestamp, String uDp, String uName, String uPhone, String uid, String postId, String commentSub, String cid) {
        this.comment = comment;
        this.rId = rId;
        this.timestamp = timestamp;
        this.uDp = uDp;
        this.uName = uName;
        this.uPhone = uPhone;
        this.uid = uid;
        this.postId = postId;
        this.commentSub = commentSub;
        this.cid = cid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getrId() {
        return rId;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
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

    public String getuPhone() {
        return uPhone;
    }

    public void setuPhone(String uPhone) {
        this.uPhone = uPhone;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getCommentSub() {
        return commentSub;
    }

    public void setCommentSub(String commentSub) {
        this.commentSub = commentSub;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
