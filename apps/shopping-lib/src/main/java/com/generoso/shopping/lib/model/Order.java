package com.generoso.shopping.lib.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Order entity
 *
 * @author Mauricio Generoso
 */
@Getter
@Setter
@Entity
@Table(name = "client_order")
public class Order extends BasicEntity {

    @Column(nullable = false)
    private boolean open = true;

    @Column
    private LocalDateTime closedAtDateTime;

    @Column(nullable = false)
    private int discount = 0;

    @Transient
    private double totalPreview;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE})
    private List<OrderItems> orderItems;
}
