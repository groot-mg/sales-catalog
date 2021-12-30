package com.spring.crud.app.example.api.controllers;

import com.spring.crud.app.example.api.converters.Expand;
import com.spring.crud.app.example.api.converters.OrderV1DataConverter;
import com.spring.crud.app.example.api.dtos.OrderDiscountV1Dto;
import com.spring.crud.app.example.api.dtos.OrderV1Dto;
import com.spring.crud.app.example.api.utilities.PageOptions;
import com.spring.crud.app.example.models.Order;
import com.spring.crud.app.example.services.OrderService;
import io.swagger.annotations.*;
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
@Api(value = "Controller to manage Orders")
@RestController
@RequestMapping(path = "/api/v1/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderV1Controller {

    private OrderService service;
    private OrderV1DataConverter converter;

    @Autowired
    public OrderV1Controller(OrderService service, OrderV1DataConverter converter) {
        this.service = service;
        this.converter = converter;
    }

    @GetMapping
    @ApiOperation(value = "Find all orders", response = Page.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully retrieved list")})
    public ResponseEntity<Page<OrderV1Dto>> findAll(@Valid PageOptions pageOptions,
                                                    @ApiParam(value = "The expand option") Expand expand) {
        Pageable pageable = PageRequest.of(pageOptions.getPageNumber(), pageOptions.getPageSize());
        Page<Order> orders = service.findAll(pageable);

        List<OrderV1Dto> orderV1Dtos = orders.getContent()
                .stream()
                .map(order -> converter.convertToDto(order, expand))
                .collect(Collectors.toList());

        return ok(new PageImpl<>(orderV1Dtos, pageable, orderV1Dtos.size()));
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "Find a specific order by id", response = OrderV1Dto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved order"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    public ResponseEntity<OrderV1Dto> findById(
            @ApiParam(value = "Order id", required = true) @PathVariable(name = "id") UUID id,
            @ApiParam(value = "The expand option") Expand expand) {
        Order order = service.customFindById(id);
        OrderV1Dto dto = converter.convertToDto(order, expand);
        return ok(dto);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Save a new Order", response = OrderV1Dto.class)
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Successfully saved new order")})
    public ResponseEntity<OrderV1Dto> save(
            @ApiParam(value = "Order dto to save", required = true) @RequestBody @Valid OrderV1Dto dto) {
        Order order = new Order();
        converter.convertToEntity(order, dto);
        service.save(order);
        OrderV1Dto dtoToResponse = converter.convertToDto(order, new Expand("orderItemsExpanded"));
        return new ResponseEntity<>(dtoToResponse, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}/discount")
    @ApiOperation(value = "Apply a discount to an order")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully applied discount"),
            @ApiResponse(code = 404, message = "The resource you were trying to update is not found")
    })
    public ResponseEntity<Void> applyDiscount(
            @ApiParam(value = "Order id", required = true) @PathVariable(name = "id") UUID id,
            @ApiParam(value = "Order dto to update", required = true) @RequestBody @Valid OrderDiscountV1Dto dto) {
        Order order = service.findById(id);
        service.applyDiscount(order, dto.getDiscount());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "/{id}/close")
    @ApiOperation(value = "Close an order")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully close order"),
            @ApiResponse(code = 404, message = "The resource you were trying to close is not found")
    })
    public ResponseEntity<Void> close(
            @ApiParam(value = "Order id", required = true) @PathVariable(name = "id") UUID id) {
        Order order = service.findById(id);
        service.close(order);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    @ApiOperation(value = "Delete an order by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted order"),
            @ApiResponse(code = 404, message = "The resource you were trying deleted is not found")
    })
    public ResponseEntity<Void> delete(
            @ApiParam(value = "Order Id to delete", required = true) @PathVariable(name = "id") UUID id) {
        Order order = service.findById(id);
        service.delete(order);
        return status(HttpStatus.NO_CONTENT).build();
    }
}