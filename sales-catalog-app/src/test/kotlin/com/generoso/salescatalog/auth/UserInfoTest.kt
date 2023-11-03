package com.generoso.salescatalog.auth

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.util.*

@ExtendWith(MockitoExtension::class)
class UserInfoTest {

    private lateinit var userInfo: UserInfo

    @Mock
    private lateinit var jwt: Jwt

    @Mock(strictness = Mock.Strictness.LENIENT)
    private lateinit var jwtAuthenticationToken: JwtAuthenticationToken

    @Mock
    private lateinit var securityContext: SecurityContext

    @BeforeEach
    fun setUp() {
        userInfo = UserInfo()

        `when`(jwtAuthenticationToken.principal).thenReturn(jwt)
        `when`(securityContext.authentication).thenReturn(jwtAuthenticationToken)
        SecurityContextHolder.setContext(securityContext)
    }

    @Test
    fun whenGetUserIdIsCalled_shouldReturnItFromClaims() {
        // Arrange
        val uuid = UUID.randomUUID().toString()
        `when`(jwt.claims).thenReturn(mapOf("sub" to uuid))

        // Act
        val userId = userInfo.getUserId()

        // Assert
        assertEquals(UUID.fromString(uuid), userId)
    }

    @Test
    fun whenGetUsernameIsCalled_shouldReturnItFromClaims() {
        // Arrange
        val usernameTest = "test"
        `when`(jwt.claims).thenReturn(mapOf("username" to usernameTest))

        // Act
        val username = userInfo.getUsername()

        // Assert
        assertEquals(usernameTest, username)
    }

    @Test
    fun whenIsSalesUserIsCalledForAClientUser_shouldReturnFalse() {
        // Arrange
        val realmAccess = mapOf("roles" to listOf(UserRole.CLIENT.role))
        `when`(jwt.getClaim<Map<String, List<String>>>("realm_access")).thenReturn(realmAccess)

        // Act & Assert
        assertFalse(userInfo.isSalesUser())
    }

    @Test
    fun whenIsSalesUserIsCalledForASalesUser_shouldReturnTrue() {
        // Arrange
        val realmAccess = mapOf("roles" to listOf(UserRole.SALES.role))
        `when`(jwt.getClaim<Map<String, List<String>>>("realm_access")).thenReturn(realmAccess)

        // Act & Assert
        assertTrue(userInfo.isSalesUser())
    }

    @Test
    fun whenIsClientIsCalledForAClientUser_shouldReturnTrue() {
        // Arrange
        val realmAccess = mapOf("roles" to listOf(UserRole.CLIENT.role))
        `when`(jwt.getClaim<Map<String, List<String>>>("realm_access")).thenReturn(realmAccess)

        // Act & Assert
        assertTrue(userInfo.isClient())
    }

    @Test
    fun whenIsClientIsCalledForASalesUser_shouldReturnFalse() {
        // Arrange
        val realmAccess = mapOf("roles" to listOf(UserRole.SALES.role))
        `when`(jwt.getClaim<Map<String, List<String>>>("realm_access")).thenReturn(realmAccess)

        // Act & Assert
        assertFalse(userInfo.isClient())
    }

    @Test
    fun whenUserIsUnauthenticatedGettingRole_shouldReturnUnknown() {
        // Arrange
        `when`(securityContext.authentication).thenReturn(
            AnonymousAuthenticationToken("auth", TestingAuthenticationToken(null, null), listOf(SimpleGrantedAuthority("none")))
        )

        // Act & Assert
        assertThat(userInfo.getRole()).isEqualTo(UserRole.UNKNOWN)
    }

    @Test
    fun whenGetRoleIsCalledForClient_shouldReturnItFromClaims() {
        // Arrange
        val realmAccess = mapOf("roles" to listOf(UserRole.CLIENT.role))
        `when`(jwt.getClaim<Map<String, List<String>>>("realm_access")).thenReturn(realmAccess)

        // Act
        val role = userInfo.getRole()

        // Assert
        assertEquals(UserRole.CLIENT, role)
    }

    @Test
    fun whenGetRoleIsCalledForSalesUser_shouldReturnItFromClaims() {
        // Arrange
        val realmAccess = mapOf("roles" to listOf(UserRole.SALES.role))
        `when`(jwt.getClaim<Map<String, List<String>>>("realm_access")).thenReturn(realmAccess)

        // Act
        val role = userInfo.getRole()

        // Assert
        assertEquals(UserRole.SALES, role)
    }

    @Test
    fun whenGetRoleIsCalledForUserWithoutRoles_shouldReturnItFromClaims() {
        // Arrange
        val realmAccess = mapOf("roles" to emptyList<String>())
        `when`(jwt.getClaim<Map<String, List<String>>>("realm_access")).thenReturn(realmAccess)

        // Act
        val role = userInfo.getRole()

        // Assert
        assertEquals(UserRole.UNKNOWN, role)
    }

    @Test
    fun whenGetRoleIsCalledAndRealmAccessIsNull_shouldReturnUnknown() {
        // Arrange
        `when`(jwt.getClaim<Map<String, List<String>>>("realm_access")).thenReturn(null)

        // Act
        val role = userInfo.getRole()

        // Assert
        assertEquals(UserRole.UNKNOWN, role)
    }
}
