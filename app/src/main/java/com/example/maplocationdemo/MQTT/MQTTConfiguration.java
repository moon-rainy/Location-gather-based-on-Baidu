package com.example.maplocationdemo.MQTT;

public class MQTTConfiguration {
    private String TGA;
    private String SERVER_HOST;
    private String SERVER_PORT;
    private String USERNAME;
    private String PASSWORD;

    public String getTGA() {
        return TGA;
    }

    public void setTGA(String TGA) {
        this.TGA = TGA;
    }

    public String getSERVER_HOST() {
        return SERVER_HOST;
    }

    public void setSERVER_HOST(String SERVER_HOST) {
        this.SERVER_HOST = SERVER_HOST;
    }

    public String getSERVER_PORT() {
        return SERVER_PORT;
    }

    public void setSERVER_PORT(String SERVER_PORT) {
        this.SERVER_PORT = SERVER_PORT;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }
}
