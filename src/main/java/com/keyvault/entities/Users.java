package com.keyvault.entities;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;


public class Users implements Serializable {
    @Serial
    private static final long serialVersionUID = 6529685098267757690L;
    private Integer idU;
    private String emailU;
    private String passU;
    private byte stateU = 1;
    private boolean has2fa;
    public Integer getIdU() {
        return idU;
    }

    public void setIdU(Integer idU) {
        this.idU = idU;
    }

    public String getEmailU() {
        return emailU;
    }

    public void setEmailU(String emailU) {
        this.emailU = emailU;
    }

    public String getPassU() {
        return passU;
    }

    public void setPassU(String passU) {
        this.passU = passU;
    }

    public byte getStateU() {
        return stateU;
    }

    public void setStateU(byte stateU) {
        this.stateU = stateU;
    }

    public Boolean getHas2fa() {return has2fa;}

    public void setHas2fa(Boolean has2fa) {this.has2fa = has2fa;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return stateU == users.stateU && Objects.equals(idU, users.idU) && Objects.equals(emailU, users.emailU) && Objects.equals(passU, users.passU);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idU, emailU, passU, stateU);
    }
}
