package com.generoso.salescatalog.entity

import jakarta.persistence.*
import org.hibernate.annotations.Where
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "products")
@Where(clause = "is_deleted = false")
@EntityListeners(ProductEntityListener::class)
class Product @JvmOverloads constructor(
    @Id
    @Column(name = "product_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    val productId: UUID? = null,

    @Column(length = 100, nullable = false)
    var name: String? = null,

    @Column(length = 256)
    var description: String? = null,

    @Column(nullable = false, precision = 10, scale = 2)
    var price: BigDecimal? = null,

    @Column(nullable = false)
    var quantity: Long = 0,

    @Column(name = "is_reserved", nullable = false)
    var isReserved: Boolean = false,

    @Column(name = "is_sold", nullable = false)
    var isSold: Boolean = false,

    @Column(name = "sales_user_id", nullable = false)
    var salesUserId: UUID? = null,

    created: LocalDateTime? = null, // Added for constructor
    lastUpdate: LocalDateTime? = null, // Added for constructor
    isDeleted: Boolean = false // Added for constructor
) : BaseEntity<UUID>() {

    init {
        this.created = created
        this.lastUpdate = lastUpdate
        this.isDeleted = isDeleted
    }

    override fun getId(): UUID? {
        return productId
    }

    override fun setOwner(ownerId: UUID) {
        salesUserId = ownerId
    }
}
