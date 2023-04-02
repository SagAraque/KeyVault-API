package com.keyvault.entities;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

public class Tokens implements Serializable {
    @Serial
    private static final long serialVersionUID = 6529685098267757692L;
    private Integer idT;
    private Integer idTu;
    private String value;
    private Timestamp date;
    private byte state = 1;
    private Users usersByIdTu;

    public Integer getIdT() {
        return idT;
    }

    public void setIdT(Integer idT) {
        this.idT = idT;
    }

    public Integer getIdTu() {
        return idTu;
    }

    public void setIdTu(Integer idTu) {
        this.idTu = idTu;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tokens tokens = (Tokens) o;
        return state == tokens.state && Objects.equals(idT, tokens.idT) && Objects.equals(idTu, tokens.idTu) && Objects.equals(value, tokens.value) && Objects.equals(date, tokens.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idT, idTu, value, date, state);
    }

    public Users getUsersByIdTu() {
        return usersByIdTu;
    }

    public void setUsersByIdTu(Users usersByIdTu) {
        this.usersByIdTu = usersByIdTu;
    }
}
