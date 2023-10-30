package com.generoso.salescatalog.auth

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class UserRoleTest {

    companion object {
        @JvmStatic
        fun enumConvertTest(): Array<Arguments> = sequenceOf(
            Arguments.of("api-client", UserRole.CLIENT),
            Arguments.of("api-sales", UserRole.SALES)
        ).toList().toTypedArray()

    }

    @ParameterizedTest
    @MethodSource("enumConvertTest")
    fun whenStringContainsApiClient_returnsTheCorrectEnum(role: String, expectedEnum: UserRole) {
        val output = UserRole.fromString(role)
        assertThat(output).isEqualTo(expectedEnum)
    }

    @Test
    fun whenStringIsUnknownShouldReturnUnknownUserRole() {
        assertThat(UserRole.fromString("any")).isEqualTo(UserRole.UNKNOWN)
    }
}