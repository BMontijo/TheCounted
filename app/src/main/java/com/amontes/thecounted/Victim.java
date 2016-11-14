package com.amontes.thecounted;

import java.io.Serializable;

class Victim implements Serializable{

    private String name;
    private String age;
    private String sex;
    private String race;
    private String month;
    private String day;
    private String year;
    private String address;
    private String city;
    private String state;
    private String cause;
    private String dept;
    private String armed;

    // Constructor.
    Victim(String name, String age, String sex, String race, String month, String day, String year, String address, String city, String state, String cause, String dept, String armed) {

        this.name = name;
        this.age = age;
        this.sex = sex;
        this.race = race;
        this.month = month;
        this.day = day;
        this.year = year;
        this.address = address;
        this.city = city;
        this.state = state;
        this.cause = cause;
        this.dept = dept;
        this.armed = armed;

    }

    // Setters.
    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public void setArmed(String armed) {
        this.armed = armed;
    }

    // Getters.
    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getSex() {
        return sex;
    }

    public String getRace() {
        return race;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    String getYear() {
        return year;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCause() {
        return cause;
    }

    public String getDept() {
        return dept;
    }

    public String getArmed() {
        return armed;
    }
}
