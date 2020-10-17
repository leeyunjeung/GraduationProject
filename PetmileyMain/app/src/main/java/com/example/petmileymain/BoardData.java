package com.example.petmileymain;

import android.graphics.Bitmap;

public class BoardData {
    private String email;
    private String id;
    private String note_title;
    private String note_memo;
    private String promote_local;
    private String promote_type;
    private Bitmap picture;
    private String nickname;
    private String img;
    private String userimg;
    private String review_categorize;
    private String adoption; //수정 추가
    private String file_name;

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getPost_id() {
        return id;
    }
    public String getUser_nickname() {
        return nickname;
    }
    public String getUserimg() {
        return userimg;
    }

    public void setUserimg(String userimg) {
        this.userimg = userimg;
    }
    public String getEmail() {
        return email;
    }
    public String getPost_note_title() {
        return note_title;
    }
    public String getPost_note_memo() {
        return note_memo;
    }
    public String getPromote_local() {
        return promote_local;
    }
    public String getPromote_type() {
        return promote_type;
    }
    public Bitmap getPicture() {
        return picture;
    }
    public String getImg() {
        return img;
    }
    public String getAdoption() {
        return adoption;
    } //수정 추가
    public String getReview_categorize() {
        return review_categorize;
    }


    public void setAdoption(String adoption) {
        this.adoption = adoption;
    } //수정 추가

    public void setEmail(String email) {
        this.email = email;
    }
    public void setUserNickname(String nickname) {
        this.nickname = nickname;
    }
    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }
    public void setPost_id(String id) {
        this.id = id;
    }
    public void setPost_note_title(String note_title) {
        this.note_title = note_title;
    }
    public void setImg(String img) {
        this.img = img;
    }
    public void setPost_note_memo(String note_memo) {
        this.note_memo = note_memo;
    }
    public void setPromote_local(String promote_local) {
        this.promote_local = promote_local;
    }
    public void setPromote_type(String promote_type) {
        this.promote_type = promote_type;
    }
    public void setReview_categorize(String review_categorize) {
        this.review_categorize = review_categorize;
    }
}
