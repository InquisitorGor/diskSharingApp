package ru.ayubdzhanov.disksharingapp.controllers;

public class ControllerSupport {
    private Long id;
    private String name;
    private String realName;

    public ControllerSupport(Long id, String name, String realName) {
        this.id = id;
        this.name = name;
        this.realName = realName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
