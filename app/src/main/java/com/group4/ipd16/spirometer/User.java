package com.group4.ipd16.spirometer;

public class User {
    private int id;
    private String first_name;
    private String last_name;
    private String doctor_email;
    private int age;
    private int height;
    private int weight;
    private String gender;
    private String ethnicity;


    //constructors
    public User() {

    }

    public User(int id, String first_name, String last_name, String doctor_email, int age, int height, int weight, String gender, String ethnicity) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.doctor_email = doctor_email;
        this.age = age;
        this.height = height;
        this.weight= weight;
        this.gender = gender;
        this.ethnicity = ethnicity;
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
    public void setAge(int age) {
        this.age = age;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
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
    public int getAge() {
        return age;
    }
    public int getHeight() {
        return height;
    }
    public int getWeight() {
        return weight;
    }
    public String getGender() {
        return gender;
    }
    public String getEthnicity() {
        return ethnicity;
    }
}
