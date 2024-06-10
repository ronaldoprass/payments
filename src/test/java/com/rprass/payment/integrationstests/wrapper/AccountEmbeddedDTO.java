package com.rprass.payment.integrationstests.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rprass.payment.application.dto.AccountDTO;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class AccountEmbeddedDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("accountDTOList")
    private List<AccountDTO> accounts;

    public AccountEmbeddedDTO() {
    }

    public List<AccountDTO> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountDTO> accounts) {
        this.accounts = accounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountEmbeddedDTO that)) return false;
        return Objects.equals(accounts, that.accounts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accounts);
    }
}
