package com.rprass.payment.integrationstests.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.Objects;

@XmlRootElement
public class WrapperAccountDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("_embedded")
    private AccountEmbeddedDTO embedded;

    public WrapperAccountDTO() {
    }

    public AccountEmbeddedDTO getEmbedded() {
        return embedded;
    }

    public void setEmbedded(AccountEmbeddedDTO embedded) {
        this.embedded = embedded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WrapperAccountDTO that)) return false;
        return Objects.equals(embedded, that.embedded);
    }

    @Override
    public int hashCode() {
        return Objects.hash(embedded);
    }
}
