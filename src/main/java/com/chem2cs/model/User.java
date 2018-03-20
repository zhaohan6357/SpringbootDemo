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

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        //System.out.println("使用setId方法注入id");
        this.id = id;
        // System.out.println("id注入成功");
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        //System.out.println("使用setPassword方法注入password");
        this.password = password;
        // System.out.println("ps注入成功");
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        //System.out.println("使用setSalt方法注入Salt");
        this.salt = salt;
        // System.out.println("salt注入成功");
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        //System.out.println("使用setUrl方法注入headUrl");
        this.headUrl = headUrl;
        //System.out.println("url注入成功");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        //System.out.println("使用setName方法注入name,值为"+name);
        this.name = name;
        // System.out.println("name注入成功");
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", headUrl='" + headUrl + '\'' +
                '}';
    }
}
