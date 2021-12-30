package com.spring.crud.api.controller;

import com.spring.crud.api.converter.Expand;
import com.spring.crud.api.dto.OrderItemsV1Dto;
import com.spring.crud.api.utilities.PageOptions;
import com.spring.crud.api.converter.OrderItemsV1DataConverter;
import com.spring.crud.api.dto.OrderV1Dto;
import com.spring.crud.lib.model.Order;
import com.spring.crud.lib.model.OrderItems;
import com.spring.crud.lib.service.OrderItemsService;
import com.spring.crud.lib.service.OrderService;
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
 * The {@link OrderItems} controller
 *
 * @author Mauricio Generoso
 */
@Api(value = "Controller to manage Orders items")
@RestController
@RequestMapping(path = "/api/v1/orders/{orderId}/order-items", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderItemV1Controller {

    private OrderService orderService;
    private OrderItemsService service;
    private OrderItemsV1DataConverter converter;

    @Autowired
    public OrderItemV1Controller(OrderService orderService, OrderItemsService service, OrderItemsV1DataConverter converter) {
        this.orderService = orderService;
        this.service = service;
        this.converter = converter;
    }

    @GetMapping
    @ApiOperation(value = "Find all orders items to a specific order", response = Page.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully retrieved list")})
    public ResponseEntity<Page<OrderItemsV1Dto>> findAll(@Valid PageOptions pageOptions,
                                                         @ApiParam(value = "Order id", required = true)
                                                         @PathVariable(name = "orderId") UUID orderId,
                                                         @ApiParam(value = "The expand option") Expand expand) {
        Pageable pageable = PageRequest.of(pageOptions.getPageNumber(), pageOptions.getPageSize());
        Page<OrderItems> orders = service.findAll(orderId, pageable);

        List<OrderItemsV1Dto> orderV1Dtos = orders.getContent()
                .stream()
                .map(order -> converter.convertToDto(order, expand))
                .collect(Collectors.toList());

        return ok(new PageImpl<>(orderV1Dtos, pageable, orderV1Dtos.size()));
    }

    @GetMapping(path = "/{id}")
    @ApiOperation(value = "Find a specific order item by id", response = OrderV1Dto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved order items"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    public ResponseEntity<OrderItemsV1Dto> findById(
            @ApiParam(value = "Order id", required = true) @PathVariable(name = "orderId") UUID orderId,
            @ApiParam(value = "Order item id", required = true) @PathVariable(name = "id") UUID id,
            @ApiParam(value = "The expand option") Expand expand) {
        OrderItems orderItem = service.findById(orderId, id);
        OrderItemsV1Dto dto = converter.convertToDto(orderItem, expand);
        return ok(dto);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Includes a new order item inside existing item", response = OrderV1Dto.class)
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Successfully included a order item inside item")})
    public ResponseEntity<OrderItemsV1Dto> save(
            @ApiParam(value = "Order id", required = true) @PathVariable(name = "orderId") UUID orderId,
            @ApiParam(value = "Order item dto to include", required = true) @RequestBody @Valid OrderItemsV1Dto dto) {
        Order order = orderService.findById(orderId);
        OrderItems orderItem = new OrderItems();
        converter.convertToEntity(orderItem, dto);
        service.save(order, orderItem);
        OrderItemsV1Dto dtoToResponse = converter.convertToDto(orderItem, new Expand("itemExpanded"));
        return new ResponseEntity<>(dtoToResponse, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    @ApiOperation(value = "Update an order item")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully update an order item"),
            @ApiResponse(code = 404, message = "The resource you were trying to update is not found")
    })
    public ResponseEntity<Void> udpate(
            @ApiParam(value = "Order Id", required = true) @PathVariable(name = "orderId") UUID orderId,
            @ApiParam(value = "Order item id to delete", required = true) @PathVariable(name = "id") UUID id,
            @ApiParam(value = "Order dto to update", required = true) @RequestBody @Valid OrderItemsV1Dto dto) {
        Order order = orderService.findById(orderId);
        OrderItems orderItem = service.findById(orderId, id);
        converter.convertToEntity(orderItem, dto);
        service.save(order, orderItem);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    @ApiOperation(value = "Delete an order item by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted order item"),
            @ApiResponse(code = 404, message = "The resource you were trying deleted is not found")
    })
    public ResponseEntity<Void> delete(
            @ApiParam(value = "Order Id", required = true) @PathVariable(name = "orderId") UUID orderId,
            @ApiParam(value = "Order item id to delete", required = true) @PathVariable(name = "id") UUID id) {
        Order order = orderService.findById(orderId);
        OrderItems orderItem = service.findById(orderId, id);
        orderItem.setOrder(order);
        service.delete(orderItem);
        return status(HttpStatus.NO_CONTENT).build();
    }
}
