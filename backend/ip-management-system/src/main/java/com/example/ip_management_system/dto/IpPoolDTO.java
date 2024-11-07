package com.example.ip_management_system.dto;

public class IpPoolDTO {
    private String ipAddress;
    private String description;
    private int portNum;

    public IpPoolDTO(String ipAddress, String description, int portNum) {
        this.ipAddress = ipAddress;
        this.description = description;
        this.portNum = portNum;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPortNum() {
        return portNum;
    }

    public void setPortNum(int portNum) {
        this.portNum = portNum;
    }

}
