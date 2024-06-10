package com.rprass.payment.infrastructure.persistence.repository;

import com.rprass.payment.common.exceptions.ResourceNotFoundException;
import com.rprass.payment.domain.model.Account;
import com.rprass.payment.domain.model.AccountSummary;
import com.rprass.payment.domain.repository.AccountRepository;
import com.rprass.payment.domain.type.Status;
import com.rprass.payment.infrastructure.converter.AccountConverter;
import com.rprass.payment.infrastructure.persistence.entity.AccountEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Repository
public class JpaAccountRepository implements AccountRepository {

    private Logger logger = Logger.getLogger(JpaAccountRepository.class.getName());

    @Autowired
    private SpringDataAccountRepository springDataAccountRepository;

    @Override
    public Page<Account> findAll(Pageable pageable) {
        logger.info("Fetching all accounts");
        Page<AccountEntity> accountEntities = springDataAccountRepository.findAll(pageable);
        Page<Account> accounts = accountEntities.map(AccountConverter::toDomainModel);
        logger.info("Found " + accounts.getTotalElements() + " accounts");
        return accounts;
    }

    @Override
    public Page<Account> findByDueDateAndDescription(Pageable pageable, Date dueDate, String description) {
        logger.info("Finding account by dueDate and description: " + dueDate);
        Page<AccountEntity> accountEntities = springDataAccountRepository.findByDueDateAndDescription(pageable, dueDate, description);
        Page<Account> accounts = accountEntities.map(AccountConverter::toDomainModel);
        logger.info("Found " + accounts.getTotalElements() + " accounts");
        return accounts;
    }

    @Override
    public Optional<Account> findById(Long id) {
        logger.info("Finding account with ID: " + id);
        Optional<Account> account = springDataAccountRepository.findById(id)
                .map(accountEntity -> {
                    Account acc = AccountConverter.toDomainModel(accountEntity);
                    logger.info("Account found: " + acc);
                    return acc;
                });

        if (account.isEmpty()) {
            logger.warning("No account found with ID: " + id);
            throw new ResourceNotFoundException("No records found for this ID");
        }

        return account;
    }

    @Override
    public Account save(Account account) {
        logger.info("Saving account: " + account);
        AccountEntity savedEntity = springDataAccountRepository.save(AccountConverter.toEntity(account));
        Account savedAccount = AccountConverter.toDomainModel(savedEntity);
        logger.info("Account saved with ID: " + savedAccount.getId());
        return savedAccount;
    }

    @Override
    public Account update(Account account) {
        logger.info("Updating account: " + account);
        AccountEntity accountEntity = AccountConverter.toEntity(account);
        AccountEntity updatedEntity = springDataAccountRepository.save(accountEntity);
        Account updatedAccount = AccountConverter.toDomainModel(updatedEntity);
        logger.info("Account updated with ID: " + updatedAccount.getId());
        return updatedAccount;
    }

    @Override
    public Account updateByStatus(Long id, Status status) {
        logger.info("Updating updateByStatus: " + status);

        AccountEntity entity = springDataAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID" + id));

        springDataAccountRepository.updateByStatus(entity.getId(), status);
        AccountEntity updateByStatus = springDataAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID" + id));
        Account updatedAccount = AccountConverter.toDomainModel(updateByStatus);

        logger.info("Account updated with ID: " + updatedAccount.getId());
        return updatedAccount;
    }

    @Override
    public void deleteById(Long id) {
        logger.info("Deleting account with ID: " + id);
        AccountEntity account = springDataAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID" + id));
        springDataAccountRepository.delete(account);
    }

    @Override
    public Page<AccountSummary> findTotalValueByPeriod(Pageable pageable) {
        logger.info("Finding total value by period");
        int limit = pageable.getPageSize();
        int offset = (int) pageable.getOffset();
        List<Object[]> totalValueByPeriod = springDataAccountRepository.findTotalValueByPeriod(limit, offset);
        long total = springDataAccountRepository.count();
        List<AccountSummary> accountSummaries = totalValueByPeriod.stream()
                .map(result -> new AccountSummary(result[0].toString(), (BigDecimal) result[1]))
                .collect(Collectors.toList());
        return new PageImpl<>(accountSummaries, pageable, total);
    }

}
