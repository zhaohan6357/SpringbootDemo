package com.chem2cs.model;


public class user {
    String name;

    public user(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getInfo(){
        return "this is"+ name;
    }
}
