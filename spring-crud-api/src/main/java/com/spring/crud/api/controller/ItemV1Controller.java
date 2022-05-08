package com.spring.crud.api.controller;

import com.spring.crud.api.converter.ItemV1DataConverter;
import com.spring.crud.api.dto.ItemV1Dto;
import com.spring.crud.api.utilities.PageOptions;
import com.spring.crud.lib.model.Item;
import com.spring.crud.lib.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
 * The {@link Item} controller
 *
 * @author Mauricio Generoso
 */
@Tag(name = "ItemV1Controller",  description = "Controller to manage Items")
@RestController
@RequestMapping(path = "/api/v1/items", produces = MediaType.APPLICATION_JSON_VALUE)
public class ItemV1Controller {

    private ItemService service;
    private ItemV1DataConverter converter;

    @Autowired
    public ItemV1Controller(ItemService service, ItemV1DataConverter converter) {
        this.service = service;
        this.converter = converter;
    }

    @GetMapping
    @Operation(description = "Find all items (products and services, active and inactive)")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully retrieved list")})
    public ResponseEntity<Page<ItemV1Dto>> findAll(@Valid PageOptions pageOptions) {
        Pageable pageable = PageRequest.of(pageOptions.getPageNumber(), pageOptions.getPageSize());
        Page<Item> items = service.findAll(pageable);

        List<ItemV1Dto> itemV1Dtos = items.getContent()
                .stream()
                .map(order -> converter.convertToDto(order))
                .collect(Collectors.toList());

        return ok(new PageImpl<>(itemV1Dtos, pageable, itemV1Dtos.size()));
    }

    @GetMapping(path = "/{id}")
    @Operation(description = "Find a specific item by id (products and services, active and inactive)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved item", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ItemV1Dto.class)) }),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    public ResponseEntity<ItemV1Dto> findById(
            @Parameter(description = "Item id", required = true) @PathVariable(name = "id") UUID id) {
        Item item = service.findById(id);
        ItemV1Dto dto = converter.convertToDto(item);
        return ok(dto);
    }

    @PostMapping
    @Operation(description = "Save a new Item (product or service)")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Successfully saved new item", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ItemV1Dto.class))
    })})
    public ResponseEntity<ItemV1Dto> save(
            @Parameter(description = "Item dto to save", required = true) @RequestBody @Valid ItemV1Dto dto) {
        Item entity = new Item();
        converter.convertToEntity(entity, dto);
        service.save(entity);
        return new ResponseEntity<>(converter.convertToDto(entity), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    @Operation(description = "Update an existing Item by id (product or service)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated item", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ItemV1Dto.class))
            }),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to update is not found")
    })
    public ResponseEntity<ItemV1Dto> update(
            @Parameter(description = "Item id", required = true) @PathVariable(name = "id") UUID id,
            @Parameter(description = "Item dto to update", required = true) @RequestBody @Valid ItemV1Dto dto) {
        Item item = service.findById(id);
        converter.convertToEntity(item, dto);
        service.save(item);
        return new ResponseEntity<>(converter.convertToDto(item), HttpStatus.OK);
    }

    @PutMapping(path = "/{id}/activate")
    @Operation(description = "Activate an item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully activate item"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying activate is not found")
    })
    public ResponseEntity<Void> activate(
            @Parameter(description = "Item id to active", required = true) @PathVariable(name = "id") UUID id) {
        Item item = service.findById(id);
        service.activate(item);
        return ok().build();
    }

    @PutMapping(path = "/{id}/deactivate")
    @Operation(description = "Deactivate an item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deactivate item"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying deactivate is not found")
    })
    public ResponseEntity<Void> deactivate(
            @Parameter(description = "Item id to inactive", required = true) @PathVariable(name = "id") UUID id) {
        Item item = service.findById(id);
        service.deactivate(item);
        return ok().build();
    }

    @DeleteMapping(path = "/{id}")
    @Operation(description = "Delete an item by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted item"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying deleted is not found")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Item Id to delete", required = true) @PathVariable(name = "id") UUID id) {
        service.deleteById(id);
        return status(HttpStatus.NO_CONTENT).build();
    }
}