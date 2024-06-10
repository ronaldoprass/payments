package com.rprass.payment.infrastructure.persistence.repository;

import com.rprass.payment.domain.type.Status;
import com.rprass.payment.infrastructure.persistence.entity.AccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface SpringDataAccountRepository extends JpaRepository <AccountEntity, Long>{

    @Query(value = "SELECT DATE_TRUNC('month', a.data_vencimento) AS periodo, SUM(a.valor) AS totalValor " +
            "FROM conta a " +
            "GROUP BY DATE_TRUNC('month', a.data_vencimento) " +
            "ORDER BY DATE_TRUNC('month', a.data_vencimento) " +
            "LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Object[]> findTotalValueByPeriod(@Param("limit") int limit, @Param("offset") int offset);

    @Query("SELECT a FROM AccountEntity a WHERE a.dueDate = :dueDate AND LOWER(a.description) LIKE LOWER(CONCAT('%', :description, '%')) AND a.status = 'PENDENTE'")
    Page<AccountEntity> findByDueDateAndDescription(Pageable pageable, @Param("dueDate") Date dueDate, @Param("description") String description);

    @Transactional
    @Modifying
    @Query("UPDATE AccountEntity a SET a.status = :status WHERE a.id = :id")
    void updateByStatus(@Param("id") Long id, @Param("status") Status status);
}
