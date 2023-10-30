package com.generoso.salescatalog.config

import com.generoso.salescatalog.auth.UserInfo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SecurityConfigTest {

    @Test
    fun whenCallingAuthenticationThreadLocal_shouldReturnAUserInfoInstance() {
        val authenticationThreadLocal = SecurityConfig().authenticationThreadLocal()
        assertThat(authenticationThreadLocal.get()).isInstanceOf(UserInfo::class.java)
    }
}