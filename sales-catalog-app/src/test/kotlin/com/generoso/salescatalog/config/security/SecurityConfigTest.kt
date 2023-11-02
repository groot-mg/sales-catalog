package com.generoso.salescatalog.config.security

import com.generoso.salescatalog.auth.UserInfo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SecurityConfigTest {

    @Test
    fun whenCallingAuthenticationThreadLocal_shouldReturnAUserInfoInstance() {
        val authenticationThreadLocal = SecurityConfig(SecurityEntryPoint()).authenticationThreadLocal()
        assertThat(authenticationThreadLocal.get()).isInstanceOf(UserInfo::class.java)
    }
}