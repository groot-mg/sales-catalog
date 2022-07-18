package com.generoso.shopping.api.controller;

import com.generoso.shopping.api.converter.ItemV1DataConverter;
import com.generoso.shopping.lib.service.ItemService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemV1Controller.class)
class ItemV1ControllerTest {

    @MockBean
    private ItemService service;

    @SpyBean
    private ItemV1DataConverter converter;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenARequestIsSentToGetAllShouldEvaluatesPageableParameter() throws Exception {
        // Arrange
        int page = 5;
        int pageSize = 10;
        when(service.findAll(any(PageRequest.class))).thenReturn(Page.empty());

        // Act
        mockMvc.perform(get("/v1/items")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(pageSize))
                        .param("sort", "id,desc")   // <-- no space after comma!
                        .param("sort", "name,asc"))
                .andExpect(status().isOk());

        // Assert
        ArgumentCaptor<Pageable> pageableCaptor =
                ArgumentCaptor.forClass(Pageable.class);
        verify(service).findAll(pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();

        assertThat(pageable.getPageNumber()).isEqualTo(page);
        assertThat(pageable.getPageSize()).isEqualTo(pageSize);
        assertThat(pageable.getSort().getOrderFor("id").getDirection()).isEqualTo(Sort.Direction.DESC);
        assertThat(pageable.getSort().getOrderFor("name").getDirection()).isEqualTo(Sort.Direction.ASC);
    }
}
