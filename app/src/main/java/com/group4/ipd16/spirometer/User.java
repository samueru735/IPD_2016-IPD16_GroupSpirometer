package com.group4.ipd16.spirometer;

public class User {
    private int id;
    private String first_name;
    private String last_name;
    private String doctor_email;

    //constructor
    public User(int id, String first_name, String last_name, String doctor_email) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.doctor_email = doctor_email;
    }

    //set methods
    public void setId(int id) {
        this.id = id;
    }
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
    public void setDoctor_email(String doctor_email) {
        this.doctor_email = doctor_email;
    }

    //get methods
    public int getId() {
        return id;
    }
    public String getFirst_name() {
        return first_name;
    }
    public String getLast_name() {
        return last_name;
    }
    public String getDoctor_email() {
        return doctor_email;
    }
}
