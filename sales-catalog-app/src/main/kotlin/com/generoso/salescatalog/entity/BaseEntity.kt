package com.generoso.salescatalog.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.util.ProxyUtils
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(ProductEntityListener::class)
abstract class BaseEntity<T> {

    @Column(name = "created_at", nullable = false, updatable = false)
    var created: LocalDateTime? = null

    @Column(name = "last_update_at")
    var lastUpdate: LocalDateTime? = null

    @Column(name = "is_deleted", nullable = false)
    var isDeleted: Boolean? = null

    abstract fun getId(): T?
    abstract fun setOwner(ownerId: T)

    fun isNew(): Boolean = getId()?.let { false } ?: true

    override fun equals(other: Any?): Boolean {
        other ?: return false
        if (this === other) return true
        if (javaClass != ProxyUtils.getUserClass(other)) return false

        other as BaseEntity<*>
        return this.getId() == other.getId()
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString() = "Entity of type ${this.javaClass.name} with id: ${getId()}"
}