package com.generoso.salescatalog.entity

import com.generoso.salescatalog.auth.UserInfo
import com.generoso.salescatalog.auth.UserRole
import com.generoso.salescatalog.exception.ForbiddenDatabaseException
import jakarta.persistence.PrePersist
import java.time.Instant
import java.time.ZoneOffset
import java.util.*

class ProductEntityListener<T : BaseEntity<UUID>>(
    private val userInfo: UserInfo
) {

    @PrePersist
    fun beforeSave(entity: T) {
        val userId = userInfo.getUserId()
        if (UserRole.SALES != userInfo.getRole()) {
            throw ForbiddenDatabaseException("User [${userId} | ${userInfo.getUsername()}] is not allowed to create products")
        }

        if (entity.isNew()) {
            entity.setOwner(userId)
            entity.created = currentDateTime()
            entity.isDeleted = false
        } else if (entity.isDeleted) {
            entity.deletedAt = currentDateTime()
        }
    }

    private fun currentDateTime() = Instant.now().atOffset(ZoneOffset.UTC).toLocalDateTime()
}