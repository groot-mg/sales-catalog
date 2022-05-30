package com.spring.crud.api.controller;

import com.spring.crud.api.converter.Expand;
import com.spring.crud.api.converter.OrderV1DataConverter;
import com.spring.crud.api.dto.OrderDiscountV1Dto;
import com.spring.crud.api.dto.OrderV1Dto;
import com.spring.crud.api.utilities.PageOptions;
import com.spring.crud.lib.model.Order;
import com.spring.crud.lib.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

/**
 * The {@link Order} controller
 *
 * @author Mauricio Generoso
 */
@Tag(name = "OrderV1Controller", description = "Controller to manage Orders")
@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(path = "/api/v1/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderV1Controller {

    private final OrderService service;
    private final OrderV1DataConverter converter;

    @GetMapping
    @Operation(description = "Find all orders")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved list", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))
    })})
    public ResponseEntity<Page<OrderV1Dto>> findAll(@Valid PageOptions pageOptions,
                                                    @Parameter(description = "The expand option") Expand expand) {
        Pageable pageable = PageRequest.of(pageOptions.getPageNumber(), pageOptions.getPageSize());
        Page<Order> orders = service.findAll(pageable);

        List<OrderV1Dto> orderV1Dtos = orders.getContent()
                .stream()
                .map(order -> converter.convertToDto(order, expand))
                .collect(Collectors.toList());

        return ok(new PageImpl<>(orderV1Dtos, pageable, orderV1Dtos.size()));
    }

    @GetMapping(path = "/{id}")
    @Operation(description = "Find a specific order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved order", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = OrderV1Dto.class))
            }),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    public ResponseEntity<OrderV1Dto> findById(
            @Parameter(description = "Order id", required = true) @PathVariable(name = "id") UUID id,
            @Parameter(description = "The expand option") Expand expand) {
        Order order = service.customFindById(id);
        OrderV1Dto dto = converter.convertToDto(order, expand);
        return ok(dto);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Save a new Order")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Successfully saved new order", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = OrderV1Dto.class))
    })})
    public ResponseEntity<OrderV1Dto> save(
            @Parameter(description = "Order dto to save", required = true) @RequestBody @Valid OrderV1Dto dto) {
        Order order = new Order();
        converter.convertToEntity(order, dto);
        service.save(order);
        OrderV1Dto dtoToResponse = converter.convertToDto(order, new Expand("orderItemsExpanded"));
        return new ResponseEntity<>(dtoToResponse, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}/discount")
    @Operation(description = "Apply a discount to an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully applied discount"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to update is not found")
    })
    public ResponseEntity<Void> applyDiscount(
            @Parameter(description = "Order id", required = true) @PathVariable(name = "id") UUID id,
            @Parameter(description = "Order dto to update", required = true) @RequestBody @Valid OrderDiscountV1Dto dto) {
        Order order = service.findById(id);
        service.applyDiscount(order, dto.getDiscount());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "/{id}/close")
    @Operation(description = "Close an order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully close order"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to close is not found")
    })
    public ResponseEntity<Void> close(
            @Parameter(description = "Order id", required = true) @PathVariable(name = "id") UUID id) {
        Order order = service.findById(id);
        service.close(order);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    @Operation(description = "Delete an order by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted order"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying deleted is not found")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Order Id to delete", required = true) @PathVariable(name = "id") UUID id) {
        Order order = service.findById(id);
        service.delete(order);
        return status(HttpStatus.NO_CONTENT).build();
    }
}