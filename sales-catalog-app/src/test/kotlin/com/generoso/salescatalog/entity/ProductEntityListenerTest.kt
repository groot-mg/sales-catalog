package com.generoso.salescatalog.entity

import com.generoso.salescatalog.auth.UserInfo
import com.generoso.salescatalog.auth.UserRole
import com.generoso.salescatalog.exception.ForbiddenDatabaseException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class ProductEntityListenerTest {


    @Mock
    private lateinit var userInfo: UserInfo

    @InjectMocks
    private lateinit var productEntityListener: ProductEntityListener<Product>

    @Test
    fun whenTheBeforeSaveIsCalled_shouldSetAllExpectedFields() {
        // Arrange
        val userId = UUID.randomUUID()
        `when`(userInfo.getUserId()).thenReturn(userId)
        `when`(userInfo.getRole()).thenReturn(UserRole.SALES)
        val entity = Product()

        // Act
        productEntityListener.beforeSave(entity)

        // Assert
        verify(userInfo).getUserId()
        verify(userInfo).getRole()
        assertEquals(userId, entity.salesUserId)
        assertNotNull(entity.created)
        assertEquals(false, entity.isDeleted)
    }

    @Test
    fun whenBeforeSaveIsCalledForANonSalesUser_shouldThrowsForbiddenDatabaseException() {
        // Arrange
        `when`(userInfo.getRole()).thenReturn(UserRole.CLIENT)
        `when`(userInfo.getUserId()).thenReturn(UUID.fromString("786a939e-48ce-4481-b713-998a1756f135"))
        `when`(userInfo.getUsername()).thenReturn("username")
        val entity = Product()

        // Act & Assert
        val exception = assertThrows<ForbiddenDatabaseException> {
            productEntityListener.beforeSave(entity)
        }

        verify(userInfo).getRole()
        assertEquals("User [786a939e-48ce-4481-b713-998a1756f135 | username] is not allowed to create products", exception.message)
    }
}