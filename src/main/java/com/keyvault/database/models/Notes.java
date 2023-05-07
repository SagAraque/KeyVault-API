package com.keyvault.database.models;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Notes implements Serializable {
    @Serial
    private static final long serialVersionUID = 6529685098267757694L;
    private Integer idN;
    private String content;
    private Items itemsByIdIn;

    public Integer getIdN() {
        return idN;
    }

    public void setIdN(Integer idN) {
        this.idN = idN;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notes notes = (Notes) o;
        return Objects.equals(idN, notes.idN) && Objects.equals(content, notes.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idN, content);
    }

    public Items getItemsByIdIn() {
        return itemsByIdIn;
    }

    public void setItemsByIdIn(Items itemsByIdIn) {
        this.itemsByIdIn = itemsByIdIn;
    }

}
