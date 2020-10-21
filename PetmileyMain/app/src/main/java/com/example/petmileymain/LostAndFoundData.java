package com.example.petmileymain;


import android.graphics.Bitmap;

public class LostAndFoundData {

    private String email;
    private String lostandfound_id;
    private String sex;
    private String tnr;
    private String color;
    private Bitmap picture;
    private String m_f;
    private String age;
    private String kg;
    private String missing_date;
    private String place;
    private String feature;
    private String etc;
    private String type;
    private String lostandfound_img;
    private String file_name;

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getLostandfound_id() {
        return lostandfound_id;
    }
    public String getSex() {
        return sex;
    }
    public String getEmail() {
        return email;
    }
    public String getTnr() {
        return tnr;
    }
    public String getColor() {
        return color;
    }
    public String getM_f() {
        return m_f;
    }
    public Bitmap getPicture() {
        return picture;
    }
    public String getLostandfound_img() {
        return lostandfound_img;
    }
    public String getAge() {
        return age;
    }
    public String getKg() {
        return kg;
    }
    public String getMissing_date() {
        return missing_date;
    }
    public String getPlace() {
        return place;
    }
    public String getFeature() {
        return feature;
    }
    public String getEtc() {
        return etc;
    }
    public String getType() {
        return type;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }
    public void setLostandfound_id(String lostandfound_id) {
        this.lostandfound_id = lostandfound_id;
    }
    public void setTnr(String tnr) {
        this.tnr = tnr;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public void setM_f(String m_f) {
        this.m_f = m_f;
    }
    public void setLostandfound_img(String img) {
        this.lostandfound_img = img;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public void setKg(String kg) {
        this.kg = kg;
    }
    public void setMissing_date(String missing_date) {
        this.missing_date = missing_date;
    }
    public void setPlace(String place) {
        this.place = place;
    }
    public void setFeature(String feature) {
        this.feature = feature;
    }
    public void setEtc(String etc) {
        this.etc = etc;
    }
    public void setType(String type) {
        this.type = type;
    }
}
