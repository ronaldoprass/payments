package com.rprass.payment.domain.service;

import com.rprass.payment.application.controller.AccountController;
import com.rprass.payment.application.dto.AccountDTO;
import com.rprass.payment.application.dto.AccountSummaryDTO;
import com.rprass.payment.common.exceptions.RequiredObjectIsNullException;
import com.rprass.payment.common.exceptions.ResourceNotFoundException;
import com.rprass.payment.domain.type.Status;
import com.rprass.payment.infrastructure.converter.AccountConverter;
import com.rprass.payment.infrastructure.converter.AccountSummaryConverter;
import com.rprass.payment.infrastructure.persistence.repository.JpaAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class AccountService {

    private Logger logger = Logger.getLogger(AccountService.class.getName());

    @Autowired
    private JpaAccountRepository repository;

    @Autowired
    private PagedResourcesAssembler<AccountDTO> assembler;

    @Autowired
    private PagedResourcesAssembler<AccountSummaryDTO> accountSummaryAssembler;


    public AccountDTO create(AccountDTO accountDTO) {
        if (accountDTO == null) throw new RequiredObjectIsNullException();
        logger.info("Creating one account");

        var model = AccountConverter.toDomainModel(accountDTO);
        var dto =  AccountConverter.toDTO(repository.save(model));
        return dto.add(linkTo(methodOn(AccountController.class).findById(dto.getKey())).withSelfRel());
    }

    public AccountDTO findById(Long id) {
        logger.info("Finding one person");
        var model = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        var dto =  AccountConverter.toDTO(model);
        return dto.add(linkTo(methodOn(AccountController.class).findById(id)).withSelfRel());
    }

    public PagedModel<EntityModel<AccountDTO>> findAll(Pageable pageable) {
        logger.info("Finding all accounts");
        Page<AccountDTO> accountDTOPage = repository.findAll(pageable).map(AccountConverter::toDTO);
        accountDTOPage.forEach(p -> p.add(linkTo(methodOn(AccountController.class).findById(p.getKey())).withSelfRel()));
        Link link = linkTo(methodOn(AccountController.class)
                .findAll(pageable.getPageNumber(),
                        pageable.getPageSize(),
                        "asc")).withSelfRel();
        return assembler.toModel(accountDTOPage, link);
    }

    public PagedModel<EntityModel<AccountDTO>> findByDueDateAndDescription(Pageable pageable, Date dueDate, String description) {
        logger.info("Finding all accounts");
        Page<AccountDTO> accountDTOPage = repository.findByDueDateAndDescription(pageable, dueDate, description).map(AccountConverter::toDTO);
        accountDTOPage.forEach(p -> p.add(linkTo(methodOn(AccountController.class).findById(p.getKey())).withSelfRel()));
        Link link = linkTo(methodOn(AccountController.class)
                .findAll(pageable.getPageNumber(),
                        pageable.getPageSize(),
                        "asc")).withSelfRel();
        return assembler.toModel(accountDTOPage, link);
    }

    public PagedModel<EntityModel<AccountSummaryDTO>> findTotalValueByPeriod(Pageable pageable) {
        logger.info("Finding total payments by period");
        Page<AccountSummaryDTO> accountSummaryDTOPage = repository.findTotalValueByPeriod(pageable).map(AccountSummaryConverter::toDTO);

        accountSummaryDTOPage.forEach(summary -> {
            Link selfLink = linkTo(methodOn(AccountController.class)
                    .findTotalValueByPeriod(pageable.getPageNumber(), pageable.getPageSize(), "asc"))
                    .withSelfRel();
            Link allSummariesLink = linkTo(methodOn(AccountController.class)
                    .findTotalValueByPeriod(pageable.getPageNumber(), pageable.getPageSize(), "asc"))
                    .withRel("allSummaries");

            summary.add(selfLink);
            summary.add(allSummariesLink);
        });

        if (accountSummaryDTOPage.hasPrevious()) {
            Link previousPageLink = linkTo(methodOn(AccountController.class)
                    .findTotalValueByPeriod(pageable.previousOrFirst().getPageNumber(), pageable.getPageSize(), "asc"))
                    .withRel("previousPage");

        }

        if (accountSummaryDTOPage.hasNext()) {
            Link nextPageLink = linkTo(methodOn(AccountController.class)
                    .findTotalValueByPeriod(pageable.next().getPageNumber(), pageable.getPageSize(), "asc"))
                    .withRel("nextPage");

        }

        Link link = linkTo(methodOn(AccountController.class)
                .findTotalValueByPeriod(pageable.getPageNumber(), pageable.getPageSize(), "asc"))
                .withSelfRel();

        return accountSummaryAssembler.toModel(accountSummaryDTOPage, link);
    }


    public AccountDTO update(AccountDTO accountDTO) {
        if (accountDTO == null) throw new RequiredObjectIsNullException();
        logger.info("Updating one account");

        var model = AccountConverter.toDomainModel(accountDTO);
        var dto =  AccountConverter.toDTO(repository.update(model));
        return dto.add(linkTo(methodOn(AccountController.class).findById(dto.getKey())).withSelfRel());
    }

    public AccountDTO updateByStatus(Long id, String status) {
        if (id == null || status == null) throw new RequiredObjectIsNullException();
        logger.info("Updating status: " + status);

        Status statusEnum;
        try {
            statusEnum = Status.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status value: " + status);
        }

        repository.updateByStatus(id, statusEnum);
        var updatedEntity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        var dto = AccountConverter.toDTO(updatedEntity);
        return dto.add(linkTo(methodOn(AccountController.class).findById(dto.getKey())).withSelfRel());
    }

    public void delete(Long id) {
        logger.info("Deleting one account");
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        repository.deleteById(entity.getId());
    }
}
