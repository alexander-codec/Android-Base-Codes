package com.itobuz.android.easybmicalculator;

/**
 * Created by itobuz-01 on 26/10/16.
 */

public class DataProvider {

    private int id;
    private String age;
    private String sex;
    private double weight;
    private String weightunit;
    private double height;
    private double heightinch;
    private String heightunit;
    private float result;
    private String date;
    private int status;

    public DataProvider(){
        // Empty Constractor
    }


    public DataProvider(int id, String date, String age, Float result){
        this.id = id;
        this.date = date;
        this.age = age;
        this.result = result;
    }

    public DataProvider(int id, String age, String sex, double weight, String weightunit, double height, double heightinch, String heightunit, float result, String date, int status){
        this.id = id;
        this.age = age;
        this.sex = sex;
        this.weight = weight;
        this.weightunit = weightunit;
        this.height = height;
        this.heightunit =heightunit;
        this.heightinch =heightinch;
        this.result =result;
        this.date = date;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getWeightunit() {
        return weightunit;
    }

    public void setWeightunit(String weightunit) {
        this.weightunit = weightunit;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getHeightinch() {
        return heightinch;
    }

    public void setHeightinch(double heightinch) {
        this.heightinch = heightinch;
    }

    public String getHeightunit() {
        return heightunit;
    }

    public void setHeightunit(String heightunit) {
        this.heightunit = heightunit;
    }

    public float getResult() {
        return result;
    }

    public void setResult(float result) {
        this.result = result;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}