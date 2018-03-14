package com.chem2cs.model;


public class User {
    private int id;
    private String name;
    private String password;
    private String salt;
    private String headUrl;

    public User(String name) {
        this.name = name;
    }
    public User(){}
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public String getName() {
        return name;
    }

    public String getInfo(){
        return "this is"+ name;
    }
}
