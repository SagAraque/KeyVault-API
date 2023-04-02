package com.keyvault.entities;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Passwords implements Serializable {
    @Serial
    private static final long serialVersionUID = 6529685098267757695L;
    private Integer idP;
    private String emailP;
    private String passP;
    private String url;
    private Items itemsByIdIp;

    public Integer getIdP() {
        return idP;
    }

    public void setIdP(Integer idP) {
        this.idP = idP;
    }

    public String getEmailP() {
        return emailP;
    }

    public void setEmailP(String emailP) {
        this.emailP = emailP;
    }

    public String getPassP() {
        return passP;
    }

    public void setPassP(String passP) {
        this.passP = passP;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Passwords passwords = (Passwords) o;
        return Objects.equals(idP, passwords.idP)  && Objects.equals(emailP, passwords.emailP) && Objects.equals(passP, passwords.passP) && Objects.equals(url, passwords.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idP, emailP, passP, url);
    }

    public Items getItemsByIdIp() {
        return itemsByIdIp;
    }

    public void setItemsByIdIp(Items itemsByIdIp) {
        this.itemsByIdIp = itemsByIdIp;
    }
}
