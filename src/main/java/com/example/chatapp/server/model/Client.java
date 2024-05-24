package com.example.chatapp.server.model;

public class Client {
    private int id;
    private String ip;
    private String name;

    public Client(int id, String ip, String name) {
        this.id = id;
        this.ip = ip;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public String getName() {
        return name;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
