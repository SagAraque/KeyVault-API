package com.keyvault.database.models;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

public class Items implements Serializable {
    @Serial
    private static final long serialVersionUID = 6529685098267757693L;
    private Integer idI;
    private Integer idUi;
    private String name;
    private String observations;
    private Timestamp modification;
    private boolean fav;
    private Users usersByIdUi;
    private Notes notesByIdI;
    private Passwords passwordsByIdI;

    public Integer getIdI() {
        return idI;
    }

    public void setIdI(Integer idI) {
        this.idI = idI;
    }

    public Integer getIdUi() {
        return idUi;
    }

    public void setIdUi(Integer idUi) {
        this.idUi = idUi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Timestamp getModification() {
        return modification;
    }

    public void setModification(Timestamp modification) {
        this.modification = modification;
    }

    public boolean getFav() {
        return fav;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Items items = (Items) o;
        return fav == items.fav && Objects.equals(idI, items.idI) && Objects.equals(idUi, items.idUi) && Objects.equals(name, items.name) && Objects.equals(observations, items.observations) && Objects.equals(modification, items.modification);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idI, idUi, name, observations, modification, fav);
    }

    public Users getUsersByIdUi() {
        return usersByIdUi;
    }

    public void setUsersByIdUi(Users usersByIdUi) {
        this.usersByIdUi = usersByIdUi;
    }

    public Notes getNotesByIdI() {
        return notesByIdI;
    }

    public void setNotesByIdI(Notes notesByIdI) {
        this.notesByIdI = notesByIdI;
    }

    public Passwords getPasswordsByIdI() {
        return passwordsByIdI;
    }

    public void setPasswordsByIdI(Passwords passwordsByIdI) {
        this.passwordsByIdI = passwordsByIdI;
    }
}
