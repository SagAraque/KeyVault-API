package com.keyvault.entities;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


public class Devices implements Serializable {
    @Serial
    private static final long serialVersionUID = 6529685098267757691L;

    private Integer idD;

    private Integer idUd;

    private String mac;

    private String ip;
    private String location;
    private String agent;
    private Date lastLogin;

    private byte stateD = 1;

    private Users user;

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public Integer getIdD() {
        return idD;
    }

    public void setIdD(Integer idD) {
        this.idD = idD;
    }

    public Integer getIdUd(Integer idU) {
        return idUd;
    }

    public void setIdUd(Integer idUd) {
        this.idUd = idUd;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public byte getStateD() {
        return stateD;
    }

    public void setStateD(byte stateD) {
        this.stateD = stateD;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Devices devices = (Devices) o;
        return stateD == devices.stateD && Objects.equals(idD, devices.idD) && Objects.equals(idUd, devices.idUd) && Objects.equals(mac, devices.mac) && Objects.equals(ip, devices.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idD, idUd, mac, ip, stateD);
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
