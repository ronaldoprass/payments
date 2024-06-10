package com.rprass.payment.application.controller;

import com.rprass.payment.application.dto.AccountDTO;
import com.rprass.payment.application.dto.AccountPayableDTO;
import com.rprass.payment.application.dto.AccountSummaryDTO;
import com.rprass.payment.domain.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/account/v1")
@Tag(name = "Account", description = "Endpoints for Managing Accounts")
public class AccountController {

    @Autowired
    private AccountService service;

    @PostMapping(value = "/",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Adds a new Account", description = "Adds a new Account",
            tags = {"Account"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = AccountDTO.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = {@Content}),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = {@Content})
            }
    )
    public AccountDTO create(@Valid @RequestBody AccountDTO dto) {
        return service.create(dto);
    }

    @GetMapping(value = "/", produces = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Finds all Accounts", description = "Finds all Accounts",
            tags = {"Account"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = AccountDTO.class))
                                    )
                            }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = {@Content}),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = {@Content})
            }
    )
    public ResponseEntity<PagedModel<EntityModel<AccountDTO>>> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                   @RequestParam(value = "size", defaultValue = "12") Integer size,
                                                                   @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "status"));
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Finds a Account", description = "Finds a Account",
            tags = {"Account"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = AccountDTO.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = {@Content}),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = {@Content}),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = {@Content})
            }
    )
    public AccountDTO findById(@Valid @PathVariable(value = "id") Long id) {
        return service.findById(id);
    }

    @PutMapping(value = "/",
            produces = {MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE},
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update a Account", description = "Update a Account",
            tags = {"Account"},
            responses = {
                    @ApiResponse(description = "Updated", responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = AccountDTO.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = {@Content}),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = {@Content})
            }
    )
    public AccountDTO update(@Valid @RequestBody AccountDTO dto) {
        return service.update(dto);
    }

    @PutMapping(value = "/{id}/{status}")
    @Operation(summary = "Update account status", description = "Update account status",
            tags = {"Account"},
            responses = {
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = {@Content}),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = {@Content})
            }
    )
    public AccountDTO updateByStatus(@Valid @PathVariable(value = "id") Long id, @PathVariable(value = "status") String status) {
        service.updateByStatus(id, status);
        return  service.updateByStatus(id, status);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete a Account", description = "Delete a Account",
            tags = {"Account"},
            responses = {
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = {@Content}),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = {@Content})
            }
    )
    public ResponseEntity<?> delete(@Valid @PathVariable(value = "id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/findByDueDateAndDescription", produces = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Finds accounts payable by dueDate and description", description = "Finds accounts payable by dueDate and description",
            tags = {"Account"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = AccountDTO.class))
                                    )
                            }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = {@Content}),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = {@Content})
            }
    )
    public ResponseEntity<PagedModel<EntityModel<AccountDTO>>> findByDueDateAndDescription(@Valid @RequestBody AccountPayableDTO accountPayableDTO,
                                                                                           @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                                           @RequestParam(value = "size", defaultValue = "12") Integer size,
                                                                                           @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "status"));
        return ResponseEntity.ok(service.findByDueDateAndDescription(pageable, accountPayableDTO.getDueDate(), accountPayableDTO.getDescription()));
    }

    @GetMapping(value = "/findTotalValueByPeriod", produces = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Finds total value by period", description = "Finds total value by period",
            tags = {"Account"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = AccountDTO.class))
                                    )
                            }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = {@Content}),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = {@Content}),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = {@Content}),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = {@Content})
            }
    )
    public ResponseEntity<PagedModel<EntityModel<AccountSummaryDTO>>> findTotalValueByPeriod(@Valid @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                                                 @RequestParam(value = "size", defaultValue = "12") Integer size,
                                                                                                 @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "period"));
        return ResponseEntity.ok(service.findTotalValueByPeriod(pageable));
    }
}
