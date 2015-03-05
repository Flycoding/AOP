package com.flyingh.vo;

import com.flyingh.annotation.Log;

@Log
public class User {
    private int id;
    private String name;
    private int age;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Log
    public String sayHello(String name) {
        System.out.println("say hello to " + name);
        return "Hello," + name;
    }
}