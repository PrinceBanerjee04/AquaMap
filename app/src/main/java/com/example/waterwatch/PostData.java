package com.example.waterwatch;

public class PostData {
    String profile;
    String username;
    String date;
    String time;
    String titleP;
    String desP;
    String picP;
    String likecount;
    String commentcount;
    String sharecount;
    PostData(String profile,
             String username,
             String date,
             String time,
             String titleP,
             String desP,
             String picP,
             String likecount,
             String commentcount,
             String sharecount){
        this.username=username;
        this.date=date;
        this.time=time;
        this.profile=profile;
        this.titleP=titleP;
        this.desP=desP;
        this.picP=picP;
        this.likecount=likecount;
        this.commentcount=commentcount;
        this.sharecount=sharecount;

    }

    public String getCommentcount() {
        return commentcount;
    }

    public String getDate() {
        return date;
    }

    public String getDesP() {
        return desP;
    }

    public String getLikecount() {
        return likecount;
    }

    public String getPicP() {
        return picP;
    }

    public String getProfile() {
        return profile;
    }

    public String getSharecount() {
        return sharecount;
    }

    public String getTime() {
        return time;
    }

    public String getTitleP() {
        return titleP;
    }

    public String getUsername() {
        return username;
    }

    public void setCommentcount(String commentcount) {
        this.commentcount = commentcount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDesP(String desP) {
        this.desP = desP;
    }

    public void setLikecount(String likecount) {
        this.likecount = likecount;
    }

    public void setPicP(String picP) {
        this.picP = picP;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setSharecount(String sharecount) {
        this.sharecount = sharecount;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTitleP(String titleP) {
        this.titleP = titleP;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
