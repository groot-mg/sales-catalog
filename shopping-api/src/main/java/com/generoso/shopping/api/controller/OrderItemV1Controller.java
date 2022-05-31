package com.generoso.shopping.api.controller;

import com.generoso.shopping.api.converter.Expand;
import com.generoso.shopping.api.converter.OrderItemsV1DataConverter;
import com.generoso.shopping.api.utilities.PageOptions;
import com.generoso.shopping.api.dto.OrderItemsV1Dto;
import com.generoso.shopping.lib.model.Order;
import com.generoso.shopping.lib.model.OrderItems;
import com.generoso.shopping.lib.service.OrderItemsService;
import com.generoso.shopping.lib.service.OrderService;
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
 * The {@link OrderItems} controller
 *
 * @author Mauricio Generoso
 */
@Tag(name = "OrderItemV1Controller", description = "Controller to manage Orders items")
@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(path = "/api/v1/orders/{orderId}/order-items", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderItemV1Controller {

    private final OrderService orderService;
    private final OrderItemsService service;
    private final OrderItemsV1DataConverter converter;

    @GetMapping
    @Operation(description = "Find all orders items to a specific order")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved list", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))
    })})
    public ResponseEntity<Page<OrderItemsV1Dto>> findAll(@Valid PageOptions pageOptions,
                                                         @Parameter(description = "Order id", required = true)
                                                         @PathVariable(name = "orderId") UUID orderId,
                                                         @Parameter(description = "The expand option") Expand expand) {
        Pageable pageable = PageRequest.of(pageOptions.getPageNumber(), pageOptions.getPageSize());
        Page<OrderItems> orders = service.findAll(orderId, pageable);

        List<OrderItemsV1Dto> orderV1Dtos = orders.getContent()
                .stream()
                .map(order -> converter.convertToDto(order, expand))
                .collect(Collectors.toList());

        return ok(new PageImpl<>(orderV1Dtos, pageable, orderV1Dtos.size()));
    }

    @GetMapping(path = "/{id}")
    @Operation(description = "Find a specific order item by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved order items", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = OrderItemsV1Dto.class))
            }),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    public ResponseEntity<OrderItemsV1Dto> findById(
            @Parameter(description = "Order id", required = true) @PathVariable(name = "orderId") UUID orderId,
            @Parameter(description = "Order item id", required = true) @PathVariable(name = "id") UUID id,
            @Parameter(description = "The expand option") Expand expand) {
        OrderItems orderItem = service.findById(orderId, id);
        OrderItemsV1Dto dto = converter.convertToDto(orderItem, expand);
        return ok(dto);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Includes a new order item inside existing item")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Successfully included a order item inside item", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = OrderItemsV1Dto.class))
    })})
    public ResponseEntity<OrderItemsV1Dto> save(
            @Parameter(description = "Order id", required = true) @PathVariable(name = "orderId") UUID orderId,
            @Parameter(description = "Order item dto to include", required = true) @RequestBody @Valid OrderItemsV1Dto dto) {
        Order order = orderService.findById(orderId);
        OrderItems orderItem = new OrderItems();
        converter.convertToEntity(orderItem, dto);
        service.save(order, orderItem);
        OrderItemsV1Dto dtoToResponse = converter.convertToDto(orderItem, new Expand("itemExpanded"));
        return new ResponseEntity<>(dtoToResponse, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    @Operation(description = "Update an order item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully update an order item"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to update is not found")
    })
    public ResponseEntity<Void> udpate(
            @Parameter(description = "Order Id", required = true) @PathVariable(name = "orderId") UUID orderId,
            @Parameter(description = "Order item id to delete", required = true) @PathVariable(name = "id") UUID id,
            @Parameter(description = "Order dto to update", required = true) @RequestBody @Valid OrderItemsV1Dto dto) {
        Order order = orderService.findById(orderId);
        OrderItems orderItem = service.findById(orderId, id);
        converter.convertToEntity(orderItem, dto);
        service.save(order, orderItem);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    @Operation(description = "Delete an order item by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted order item"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying deleted is not found")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Order Id", required = true) @PathVariable(name = "orderId") UUID orderId,
            @Parameter(description = "Order item id to delete", required = true) @PathVariable(name = "id") UUID id) {
        Order order = orderService.findById(orderId);
        OrderItems orderItem = service.findById(orderId, id);
        orderItem.setOrder(order);
        service.delete(orderItem);
        return status(HttpStatus.NO_CONTENT).build();
    }
}
