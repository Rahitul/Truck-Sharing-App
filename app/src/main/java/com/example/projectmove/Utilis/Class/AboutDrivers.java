package com.example.projectmove.Utilis.Class;

public class AboutDrivers {

      String cTime,comment,uid,dp,name;

      public AboutDrivers() {
      }

      public AboutDrivers(String cTime, String comment, String uid, String dp, String name) {
            this.cTime = cTime;
            this.comment = comment;
            this.uid = uid;
            this.dp = dp;
            this.name = name;
      }

      public String getcTime() {
            return cTime;
      }

      public void setcTime(String cTime) {
            this.cTime = cTime;
      }

      public String getComment() {
            return comment;
      }

      public void setComment(String comment) {
            this.comment = comment;
      }

      public String getUid() {
            return uid;
      }

      public void setUid(String uid) {
            this.uid = uid;
      }

      public String getDp() {
            return dp;
      }

      public void setDp(String dp) {
            this.dp = dp;
      }

      public String getName() {
            return name;
      }

      public void setName(String name) {
            this.name = name;
      }
}
