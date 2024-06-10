package com.rprass.payment.domain.repository;


import com.rprass.payment.domain.model.Account;
import com.rprass.payment.domain.model.AccountSummary;
import com.rprass.payment.domain.type.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.Optional;

public interface AccountRepository {
    Page<Account> findAll(Pageable pageable);
    Page<Account> findByDueDateAndDescription(Pageable pageable, Date dueDate, String description);
    Page<AccountSummary> findTotalValueByPeriod(Pageable pageable);
    Optional<Account> findById(Long id);
    Account save(Account account);
    Account update(Account account);
    Account updateByStatus(Long id, Status status);
    void deleteById(Long id);
}