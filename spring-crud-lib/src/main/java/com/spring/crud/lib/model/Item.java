package com.spring.crud.lib.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Item entity.
 *
 * @author Mauricio Generoso
 */
@Getter
@Setter
@Entity
@Table(name = "item")
public class Item extends BasicEntity {

    @Column(length = 100, nullable = false)
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private TypeItem type;

    @Column
    private double price;

    @Column
    private boolean active = true;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<OrderItems> orderItems;
}