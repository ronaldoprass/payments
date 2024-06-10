package com.rprass.payment.infrastructure.persistence.repository;

import com.rprass.payment.common.exceptions.ResourceNotFoundException;
import com.rprass.payment.domain.model.Account;
import com.rprass.payment.domain.model.AccountSummary;
import com.rprass.payment.domain.type.Status;
import com.rprass.payment.infrastructure.persistence.entity.AccountEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JpaAccountRepositoryTest {

    @InjectMocks
    private JpaAccountRepository jpaAccountRepository;

    @Mock
    private SpringDataAccountRepository springDataAccountRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        List<AccountEntity> accountEntities = Arrays.asList(new AccountEntity(), new AccountEntity());
        Page<AccountEntity> accountEntityPage = new PageImpl<>(accountEntities, pageable, accountEntities.size());

        when(springDataAccountRepository.findAll(pageable)).thenReturn(accountEntityPage);

        Page<Account> result = jpaAccountRepository.findAll(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
    }

    @Test
    void testFindByDueDateAndDescription() {
        Pageable pageable = PageRequest.of(0, 10);
        Date dueDate = new Date();
        String description = "test";

        List<AccountEntity> accountEntities = Arrays.asList(new AccountEntity(), new AccountEntity());
        Page<AccountEntity> accountEntityPage = new PageImpl<>(accountEntities, pageable, accountEntities.size());

        when(springDataAccountRepository.findByDueDateAndDescription(pageable, dueDate, description)).thenReturn(accountEntityPage);

        Page<Account> result = jpaAccountRepository.findByDueDateAndDescription(pageable, dueDate, description);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
    }

    @Test
    void testFindById() {
        Long id = 1L;
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(id);

        when(springDataAccountRepository.findById(id)).thenReturn(Optional.of(accountEntity));

        Optional<Account> result = jpaAccountRepository.findById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
    }

    @Test
    void testFindById_NotFound() {
        Long id = 1L;

        when(springDataAccountRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> jpaAccountRepository.findById(id));
    }

    @Test
    void testSave() {
        Account account = Account.builder().id(1L).build();
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(1L);

        when(springDataAccountRepository.save(any(AccountEntity.class))).thenReturn(accountEntity);

        Account result = jpaAccountRepository.save(account);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testUpdate() {
        Account account = Account.builder().id(1L).build();
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(1L);

        when(springDataAccountRepository.save(any(AccountEntity.class))).thenReturn(accountEntity);

        Account result = jpaAccountRepository.update(account);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testUpdateByStatus() {
        Long id = 1L;
        Status status = Status.PAGO;
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(id);

        when(springDataAccountRepository.findById(id)).thenReturn(Optional.of(accountEntity));
        doNothing().when(springDataAccountRepository).updateByStatus(id, status);
        when(springDataAccountRepository.findById(id)).thenReturn(Optional.of(accountEntity));

        Account result = jpaAccountRepository.updateByStatus(id, status);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void testDeleteById() {
        Long id = 1L;
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(id);

        when(springDataAccountRepository.findById(id)).thenReturn(Optional.of(accountEntity));
        doNothing().when(springDataAccountRepository).delete(accountEntity);

        jpaAccountRepository.deleteById(id);

        verify(springDataAccountRepository, times(1)).delete(accountEntity);
    }

    @Test
    void testFindTotalValueByPeriod() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Object[]> data = new ArrayList<>();
        data.add(new Object[]{"2023-01", BigDecimal.valueOf(1000.00)});

        when(springDataAccountRepository.findTotalValueByPeriod(10, 0)).thenReturn(data);
        when(springDataAccountRepository.count()).thenReturn(1L);

        Page<AccountSummary> result = jpaAccountRepository.findTotalValueByPeriod(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }
}
